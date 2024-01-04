package com.github.carlwilk32;

import static com.github.carlwilk32.sdl.core.api.blendmode.SDL_BlendMode.SDL_BLENDMODE_ADD;
import static com.github.carlwilk32.sdl.core.api.blendmode.SDL_BlendMode.SDL_BLENDMODE_NONE;
import static com.github.carlwilk32.sdl.core.api.render.SdlRender.*;
import static com.github.carlwilk32.sdl.core.api.scancode.SDL_Scancode.*;
import static com.github.carlwilk32.model.Owner.PLAYER;

import com.github.carlwilk32.sdl.core.api.rect.SDL_Rect;
import com.github.carlwilk32.sdl.core.api.render.SDL_Texture;
import com.github.carlwilk32.model.*;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Singleton
@RequiredArgsConstructor(onConstructor_ = {@Inject})
@Slf4j
public class StageView {

  private final CommonView commonView;
  private final DrawService draw;
  private final AudioService audio;
  private final InputService input;
  private final AppConfig conf;
  private final PhysicsService physics;
  private final TextService text;
  private final Provider<HighscoresView> highscores;
  private final Provider<App> app;

  @Named("PlayerBullet")
  private final SDL_Texture playerBulletSprite;

  @Named("EnemyBullet")
  private final SDL_Texture enemyBulletSprite;

  @Named("Enemy")
  private final SDL_Texture enemySprite;

  @Named("Player")
  private final SDL_Texture playerSprite;

  @Named("Explosion")
  private final SDL_Texture explosionTexture;

  private Deque<GameObject> fighters, bullets;
  private Deque<Explosion> explosions;
  private Deque<Debris> wreckages;
  private GameObject player;
  private int enemySpawnTimer;
  private int stageResetTimer;
  private int score;

  public void logic() {
    commonView.doBackground();
    doPlayer();
    doEnemies();
    doFighters();
    doBullets();
    doExplosions();
    doDebris();
    spawnEnemies();
    clipPlayer();
    if (player == null && --stageResetTimer <= 0) {
      highscores.get().addHighScore(this.score);
      highscores.get().init();
    }
  }

  private void doDebris() {
    for (var debris : wreckages) {
      debris.x += debris.dx;
      debris.y += debris.dy;
      debris.dy += 0.5;
      if (--debris.life <= 0) {
        wreckages.remove(debris);
      }
    }
  }

  public void init() {
    app.get().logic = this::logic;
    app.get().draw = this::draw;
    resetStage();
  }

  private void addDebris(GameObject obj) {
    var random = new Random();
    var w = obj.w / 2;
    var h = obj.h / 2;
    for (var y = 0; y <= h; y += h) {
      for (var x = 0; x <= w; x += w) {
        var rect = new SDL_Rect();
        rect.x = x;
        rect.y = y;
        rect.w = w;
        rect.h = h;
        var debris =
            Debris.builder()
                .life(conf.GAME_FPS * 2)
                .texture(obj.texture)
                .rect(rect)
                .x(obj.x + w)
                .y(obj.y + h)
                .dx(random.nextInt() % 5 - random.nextInt() % 5)
                .dy(-(5 + (random.nextInt() % 12)))
                .build();
        wreckages.add(debris);
      }
    }
  }

  private void doExplosions() {
    for (var explosion : explosions) {
      explosion.x += explosion.dx;
      explosion.y += explosion.dy;
      if (--explosion.alpha <= 0) {
        explosions.remove(explosion);
      }
    }
  }

  private void addExplosions(double x, double y, int num) {
    var random = new Random();
    for (var i = 0; i < num; i++) {
      var color =
          switch (random.nextInt() % 4) {
            case 0 -> new Color(255, 0, 0);
            case 1 -> new Color(255, 128, 0);
            case 2 -> new Color(255, 255, 0);
            default -> new Color(255, 255, 255);
          };
      var explosion =
          Explosion.builder()
              .x(x + (random.nextInt() % 32) - (random.nextInt() % 32))
              .y(y + (random.nextInt() % 32) - (random.nextInt() % 32))
              .dx(((random.nextInt() % 10) - (random.nextInt() % 10)) / 10.0)
              .dy(((random.nextInt() % 10) - (random.nextInt() % 10)) / 10.0)
              .alpha(random.nextInt() % conf.GAME_FPS * 3)
              .color(color)
              .build();
      explosions.add(explosion);
    }
  }

  private void doEnemies() {
    for (var enemy : fighters) {
      if (enemy != player && player != null && --enemy.reload <= 0) {
        audio.playSound(AudioService.Event.ENEMY_FIRE, AudioService.Channel.ENEMY_FIRE);
        fireEnemyBullet(enemy);
      }
    }
  }

  private void fireEnemyBullet(GameObject enemy) {
    var bullet =
        new GameObject(enemyBulletSprite)
            .toBuilder().health(1).owner(Owner.ENEMY).x(enemy.x).y(enemy.y).build();
    bullets.add(bullet);

    bullet.x += (enemy.w / 2.0) - (bullet.w / 2.0);
    bullet.y += (enemy.h / 2.0) - (bullet.h / 2.0);

    var dxDy =
        physics.calcSlope(
            player.x + (player.w / 2.0), player.y + (player.h / 2.0), enemy.x, enemy.y);
    bullet.dx = dxDy.first() * conf.ENEMY_BULLET_SPEED;
    bullet.dy = dxDy.second() * conf.ENEMY_BULLET_SPEED;

    enemy.reload = (new Random().nextInt(0, conf.GAME_FPS * 2));
  }

  private void clipPlayer() {
    if (player != null) {
      if (player.x < 0) player.x = 0;
      if (player.y < 0) player.y = 0;
      if (player.x > conf.WINDOW_WIDTH / 2.0) player.x = conf.WINDOW_WIDTH / 2.0;
      if (player.y > conf.WINDOW_HEIGHT - player.h) player.y = conf.WINDOW_HEIGHT - player.h;
    }
  }

  private void spawnEnemies() {
    var borderThreshold = 10; // pixels, allow some space from screen edges
    if (--enemySpawnTimer <= 0) {
      var enemy = new GameObject(enemySprite);
      enemy =
          enemy.toBuilder()
              .owner(Owner.ENEMY)
              .health(1)
              .reload(conf.GAME_FPS * (1 + (new Random().nextInt(0, 3))))
              .x(conf.WINDOW_WIDTH)
              .y(
                  new Random()
                      .nextInt(borderThreshold, conf.WINDOW_HEIGHT - enemy.h - borderThreshold))
              .dx(-(2 + new Random().nextInt(0, 4)))
              .build();
      fighters.add(enemy);

      enemySpawnTimer = (int) (30 + (Math.random() % conf.GAME_FPS));
    }
  }

  private void doFighters() {
    for (var e : fighters) {
      e.x += e.dx;
      e.y += e.dy;
      if (e != player && e.x < -e.w) {
        e.health = 0;
      }
      if (e.health == 0) {
        if (e == player) {
          player = null;
        }
        fighters.remove(e);
      }
    }
  }

  private void doBullets() {
    for (var b : bullets) {
      b.x += b.dx;
      b.y += b.dy;
      if (hit(b)
          || b.x < -b.w
          || b.y < -b.h
          || b.x > conf.WINDOW_WIDTH
          || b.y > conf.WINDOW_HEIGHT) {
        bullets.remove(b);
      }
    }
  }

  private boolean hit(GameObject entity) {
    for (var fighter : fighters) {
      if (entity.owner != fighter.owner && physics.collisionDetect(entity, fighter)) {
        entity.health = 0;
        fighter.health = 0;
        addExplosions(fighter.x, fighter.y, 32);
        addDebris(fighter);
        if (fighter != player) {
          this.score++;
          audio.playSound(AudioService.Event.ENEMY_DIE, AudioService.Channel.ANY);
        } else {
          audio.playSound(AudioService.Event.PLAYER_DIE, AudioService.Channel.PLAYER);
        }
        return true;
      }
    }
    return false;
  }

  private void doPlayer() {
    if (player != null) {
      player.dx = 0;
      player.dy = 0;

      if (player.reload > 0) player.reload--;

      if (input.keyboard[SDL_SCANCODE_UP]) player.dy = -conf.PLAYER_SPEED;
      if (input.keyboard[SDL_SCANCODE_DOWN]) player.dy = conf.PLAYER_SPEED;
      if (input.keyboard[SDL_SCANCODE_LEFT]) player.dx = -conf.PLAYER_SPEED;
      if (input.keyboard[SDL_SCANCODE_RIGHT]) player.dx = conf.PLAYER_SPEED;

      if (input.keyboard[SDL_SCANCODE_SPACE] && player.reload == 0) {
        audio.playSound(AudioService.Event.PLAYER_FIRE, AudioService.Channel.PLAYER);
        fireBullet();
      }
    }
  }

  private void fireBullet() {
    var bullet = new GameObject(playerBulletSprite);
    bullet =
        bullet.toBuilder()
            .health(1)
            .dx(conf.PLAYER_BULLET_SPEED)
            .owner(PLAYER)
            // maybe adjust to SDL_Rect, i.e. keep predefined sizes of every game object
            .x(1 + player.x + player.w / 2.0)
            .y(player.y + 2 * (player.h / 3.0) - (bullet.h / 2.0))
            .build();
    bullets.add(bullet);
    player.reload = 8;
  }

  public void draw() {
    commonView.drawBackground();
    drawFighters();
    drawDebris();
    drawExplosions();
    drawBullets();
    drawHUD();
  }

  private void drawHUD() {
    text.drawText(
        10, 10, Color.WHITE, TextAlignment.LEFT, String.format("SCORE: %03d", this.score));
  }

  private void drawDebris() {
    for (var debris : wreckages) {
      draw.blitRect(debris.texture, debris.rect, (int) debris.x, (int) debris.y);
    }
  }

  private void drawExplosions() {
    draw.setBlendMode(SDL_BLENDMODE_ADD);
    SDL_SetTextureBlendMode(explosionTexture, SDL_BLENDMODE_ADD);
    for (var explosion : explosions) {
      SDL_SetTextureColorMod(
          explosionTexture,
          (byte) explosion.color.r(),
          (byte) explosion.color.g(),
          (byte) explosion.color.b());
      SDL_SetTextureAlphaMod(explosionTexture, (byte) explosion.alpha);
      draw.blit(explosionTexture, (int) explosion.x, (int) explosion.y);
    }
    draw.setBlendMode(SDL_BLENDMODE_NONE);
  }

  private void drawBullets() {
    for (var b : bullets) {
      draw.blit(b.texture, (int) b.x, (int) b.y);
    }
  }

  private void drawFighters() {
    for (var it : fighters) draw.blit(it.texture, (int) it.x, (int) it.y);
  }

  public void resetStage() {
    this.fighters = new ConcurrentLinkedDeque<>();
    this.bullets = new ConcurrentLinkedDeque<>();
    this.explosions = new ConcurrentLinkedDeque<>();
    this.wreckages = new ConcurrentLinkedDeque<>();

    initPlayer();

    enemySpawnTimer = 0;
    stageResetTimer = conf.GAME_FPS * 3;
    score = 0;
  }

  private void initPlayer() {
    player =
        new GameObject(playerSprite)
            .toBuilder().health(1).owner(PLAYER).x(100).y(conf.WINDOW_HEIGHT / 2.0).build();
    fighters.add(player);
  }
}

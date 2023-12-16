package org.example;

import static io.github.libsdl4j.api.scancode.SDL_Scancode.*;
import static org.example.model.Owner.PLAYER;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import io.github.libsdl4j.api.rect.SDL_Rect;
import io.github.libsdl4j.api.render.SDL_Texture;
import java.util.Deque;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedDeque;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.model.GameObject;
import org.example.model.Owner;

@Singleton
@RequiredArgsConstructor(onConstructor_ = {@Inject})
@Slf4j
public class Stage {

  private final Draw draw;
  private final Input input;
  private final AppConfig conf;
  private final Physics physics;

  @Named("PlayerBullet")
  private final SDL_Texture playerBulletSprite;

  @Named("EnemyBullet")
  private final SDL_Texture enemyBulletSprite;

  @Named("Enemy")
  private final SDL_Texture enemySprite;

  @Named("Player")
  private final SDL_Texture playerSprite;

  @Named("Background")
  private final SDL_Texture background1;

  @Named("BackgroundSky")
  private final SDL_Texture background2;

  @Named("BackgroundMask")
  private final SDL_Texture background3;

  private Deque<GameObject> fighters, bullets;
  private GameObject player;
  private int enemySpawnTimer;
  private int stageResetTimer;
  private int backgroundX;
  private int skyX;
  private int maskX;

  public void logic() {
    doBackground();
    doSky();
    doMask();
    doPlayer();
    doEnemies();
    doFighters();
    doBullets();
    spawnEnemies();
    clipPlayer();
    if (player == null && --stageResetTimer <= 0) {
      resetStage();
    }
  }

  private void doSky() {
    if (--skyX < -conf.WINDOW_WIDTH) skyX = 0;
  }

  private void doMask() {
    maskX -= 2;
    if (--maskX < -conf.WINDOW_WIDTH) maskX = 0;
  }

  private void doBackground() {
    backgroundX -= 4;
    if (backgroundX < -conf.WINDOW_WIDTH) backgroundX = 0;
  }

  private void doEnemies() {
    for (var enemy : fighters) {
      if (enemy != player && player != null && --enemy.reload <= 0) {
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

      if (input.keyboard[SDL_SCANCODE_SPACE] && player.reload == 0) fireBullet();
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
    drawBackground(backgroundX, background1);
    drawBackground(maskX, background2);
    drawBackground(skyX, background3);
    drawFighters();
    drawBullets();
  }

  private void drawBackground(int xAnchor, SDL_Texture texture) {
    var sdlRect = new SDL_Rect();
    for (var x = xAnchor; x < conf.WINDOW_WIDTH; x += conf.WINDOW_WIDTH) {
      sdlRect.x = x;
      sdlRect.y = 0;
      sdlRect.w = conf.WINDOW_WIDTH;
      sdlRect.h = conf.WINDOW_HEIGHT;
      draw.renderBackground(texture, sdlRect);
    }
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

    initPlayer();

    enemySpawnTimer = 0;
    stageResetTimer = conf.GAME_FPS * 3;
  }

  private void initPlayer() {
    player =
        new GameObject(playerSprite)
            .toBuilder().health(1).owner(PLAYER).x(100).y(conf.WINDOW_HEIGHT / 2.0).build();
    fighters.add(player);
  }
}

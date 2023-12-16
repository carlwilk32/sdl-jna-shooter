package org.example;

import static io.github.libsdl4j.api.render.SdlRender.SDL_QueryTexture;
import static io.github.libsdl4j.api.scancode.SDL_Scancode.*;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.sun.jna.ptr.IntByReference;
import io.github.libsdl4j.api.render.SDL_Texture;
import java.util.Deque;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedDeque;
import lombok.RequiredArgsConstructor;
import org.example.model.GameObject;
import org.example.model.Owner;

@Singleton
@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class Stage {

  private final Draw draw;
  private final Input input;
  private final AppConfig conf;
  private final Physics physics;

  @Named("PlayerBullet")
  private final SDL_Texture playerBulletSprite;

  @Named("Enemy")
  private final SDL_Texture enemySprite;

  private Deque<GameObject> fighters, bullets;
  private GameObject player;
  private int enemySpawnTimer;

  public void logic() {
    doPlayer();
    doFighters();
    doBullets();
    spawnEnemies();
  }

  private void spawnEnemies() {
    if (--enemySpawnTimer <= 0) {
      var enemy = new GameObject(enemySprite);
      fighters.add(enemy);
      enemy.owner = Owner.ENEMY;
      enemy.health = 1;
      enemy.x = conf.WINDOW_WIDTH;
      enemy.y = new Random().nextInt(0, conf.WINDOW_HEIGHT);
      assignHeightAndWidth(enemy);
      enemy.dx = -(2 + new Random().nextInt(0, 4));

      enemySpawnTimer = (int) (30 + (Math.random() % conf.GAME_FPS));
    }
  }

  private void doFighters() {
    for (var e : fighters) {
      e.x += e.dx;
      e.y += e.dy;
      if (e != player && (e.x < -e.w || e.health == 0)) {
        fighters.remove(e);
      }
    }
  }

  private void doBullets() {
    for (var b : bullets) {
      b.x += b.dx;
      b.y += b.dy;
      if (hit(b) || b.x > conf.WINDOW_WIDTH) {
        bullets.remove(b);
      }
    }
  }

  private boolean hit(GameObject entity) {
    for (var fighter : fighters) {
      if (entity.owner != fighter.owner
              && physics.collisionDetect(entity, fighter)) {
        entity.health = 0;
        fighter.health = 0;
        return true;
      }
    }
    return false;
  }

  private void doPlayer() {
    if (player.reload > 0) player.reload--;

    if (input.keyboard[SDL_SCANCODE_UP]) player.dy = -conf.PLAYER_SPEED;
    if (input.keyboard[SDL_SCANCODE_DOWN]) player.dy = conf.PLAYER_SPEED;
    if (input.keyboard[SDL_SCANCODE_LEFT]) player.dx = -conf.PLAYER_SPEED;
    if (input.keyboard[SDL_SCANCODE_RIGHT]) player.dx = conf.PLAYER_SPEED;

    if (input.keyboard[SDL_SCANCODE_SPACE] && player.reload == 0) fireBullet();
  }

  private void fireBullet() {
    var bullet = new GameObject(playerBulletSprite);
    bullets.add(bullet);

    bullet.owner = Owner.PLAYER;
    bullet.x = player.x + player.w / 2;
    bullet.y = player.y;
    bullet.dx = conf.PLAYER_BULLET_SPEED;
    bullet.health = 1;
    assignHeightAndWidth(bullet);

    bullet.y += (player.h / 2) - (bullet.h / 2);

    player.reload = 8;
  }

  private void assignHeightAndWidth(GameObject assignee) {
    var wRef = new IntByReference();
    var hRef = new IntByReference();

    SDL_QueryTexture(assignee.texture, null, null, wRef, hRef);

    assignee.w = wRef.getValue();
    assignee.h = hRef.getValue();
  }

  public void draw() {
    drawFighters();
    drawBullets();
  }

  private void drawBullets() {
    for (var b : bullets) {
      draw.blit(b.texture, b.x, b.y);
    }
  }

  private void drawFighters() {
    for (var it : fighters) draw.blit(it.texture, it.x, it.y);
  }

  public void initStage() {
    this.fighters = new ConcurrentLinkedDeque<>();
    this.bullets = new ConcurrentLinkedDeque<>();

    initPlayer();
    enemySpawnTimer = 0;
  }

  private void initPlayer() {
    player = new GameObject(draw.loadTexture("gfx/f_16_2.png"));
    fighters.add(player);

    player.health = 1;
    player.x = 100;
    player.y = 250;
    player.owner = Owner.PLAYER;
    assignHeightAndWidth(player);
  }
}

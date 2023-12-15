package org.example;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.github.libsdl4j.api.timer.SdlTimer;
import lombok.RequiredArgsConstructor;
import org.example.model.CommonGameEntity;

@Singleton
@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class App {

  private final Draw draw;
  private final AppConfig appConfig;
  private final Input input;

  public void start() {
    draw.initGraphics();

    var player = new CommonGameEntity(draw.loadTexture("gfx/f_16_2.png"));
    player.x = 100;
    player.y = 250;
    player.health = 1;
    var bullet = new CommonGameEntity(draw.loadTexture("gfx/bullet.png"));

    while (true) {
      draw.prepareScene();
      input.doInput();

      player.x += player.dx;
      player.y += player.dy;

      if (input.up) player.y -= appConfig.PLAYER_SPEED;
      if (input.down) player.y += appConfig.PLAYER_SPEED;
      if (input.left) player.x -= appConfig.PLAYER_SPEED;
      if (input.right) player.x += appConfig.PLAYER_SPEED;

      if (input.fire && bullet.health == 0) {
        bullet.x = player.x;
        bullet.y = player.y;
        bullet.dx = appConfig.PLAYER_BULLET_SPEED;
        bullet.dy = 0;
        bullet.health = 1;
      }

      bullet.x += bullet.dx;
      bullet.y += bullet.dy;

      if (bullet.x > appConfig.WINDOW_WIDTH) bullet.health = 0;

      draw.blit(player.texture, player.x, player.y);

      if (bullet.health > 0) draw.blit(bullet.texture, bullet.x, bullet.y);

      draw.presentScene();
      SdlTimer.SDL_Delay(16);
    }
  }
}

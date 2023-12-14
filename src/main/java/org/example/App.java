package org.example;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.github.libsdl4j.api.timer.SdlTimer;
import lombok.RequiredArgsConstructor;
import org.example.model.Player;

@Singleton
@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class App {

  private final Draw draw;
  private final Input input;

  public void start() {
    draw.initGraphics();

    var playerTexture = draw.loadTexture("gfx/player.png");
    var player = new Player(100, 100, playerTexture);
    while (true) {
      draw.prepareScene();
      input.doInput();

      if (input.up) player.y -= 4;
      if (input.down) player.y += 4;
      if (input.left) player.x -= 4;
      if (input.right) player.x += 4;

      draw.blit(player.texture, player.x, player.y);
      draw.presentScene();
      SdlTimer.SDL_Delay(16);
    }
  }
}

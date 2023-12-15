package org.example;

import static io.github.libsdl4j.api.timer.SdlTimer.*;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Singleton
@RequiredArgsConstructor(onConstructor_ = {@Inject})
@Slf4j
public class App {

  private final Draw draw;
  private final Stage stage;
  private final Input input;
  private final AppConfig conf;

  public void start() {
    draw.initGraphics();
    stage.initStage();

    // ~16.6667 ms per frame
    final double desiredFrameDuration = 1000.0 / conf.GAME_FPS;

    while (true) {
      var start = SDL_GetPerformanceCounter();

      draw.prepareScene();
      input.doInput();
      stage.logic();
      stage.draw();
      draw.presentScene();

      // cap frames to 60 FPS
      var end = SDL_GetPerformanceCounter();
      var elapsedMs = (end - start) / (double) SDL_GetPerformanceFrequency();
      SDL_Delay((int) Math.floor(desiredFrameDuration - elapsedMs));
    }
  }
}

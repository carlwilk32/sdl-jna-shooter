package org.example;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.github.libsdl4j.api.timer.SdlTimer;
import lombok.RequiredArgsConstructor;

@Singleton
@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class App {

  private final Draw draw;
  private final Stage stage;
  private final Input input;

  public void start() {
    draw.initGraphics();
    stage.initStage();

    while (true) {
      draw.prepareScene();
      input.doInput();
      stage.logic();
      stage.draw();
      draw.presentScene();
      SdlTimer.SDL_Delay(16);
    }
  }
}

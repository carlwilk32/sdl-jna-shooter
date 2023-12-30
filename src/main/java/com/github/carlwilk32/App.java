package com.github.carlwilk32;

import static com.github.carlwilk32.api.timer.SdlTimer.*;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Singleton
@RequiredArgsConstructor(onConstructor_ = {@Inject})
@Slf4j
public class App {

  private final DrawService drawService;
  private final AudioService audioService;
  private final TitleView homeView;
  private final InputService input;
  private final AppConfig conf;

  public Runnable logic;
  public Runnable draw;

  public void start() {
    drawService.initGraphics();
    audioService.initAudio();
    homeView.init();

    // ~16.6667 ms per frame
    final double desiredFrameDuration = 1000.0 / conf.GAME_FPS;

    while (true) {
      var start = SDL_GetPerformanceCounter();

      drawService.prepareScene();
      input.doInput();
      this.logic.run();
      this.draw.run();
      drawService.presentScene();

      // cap frames to 60 FPS
      // TODO: maybe introduce independent timing for physics
      // https://thenumb.at/cpp-course/sdl2/08/08.html
      var end = SDL_GetPerformanceCounter();
      var elapsedMs = (end - start) / (double) SDL_GetPerformanceFrequency();
      //      log.info("FPS: {}", 1 / (desiredFrameDuration - elapsedMs) * 1000);
      SDL_Delay((int) Math.floor(desiredFrameDuration - elapsedMs));
    }
  }
}

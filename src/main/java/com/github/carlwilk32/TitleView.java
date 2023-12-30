package com.github.carlwilk32;

import static com.github.carlwilk32.api.scancode.SDL_Scancode.SDL_SCANCODE_SPACE;

import com.github.carlwilk32.model.Color;
import com.github.carlwilk32.model.TextAlignment;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Singleton
@RequiredArgsConstructor(onConstructor_ = {@Inject})
@Slf4j
public class TitleView {

  private final CommonView commonView;
  private final Provider<HighscoresView> highscoresView;
  private final InputService input;
  private final StageView stageView;
  private final Provider<App> app;
  private final TextService textService;
  private final AppConfig conf;

  private int reveal;
  private int timeout;

  public void init() {
    app.get().logic = this::logic;
    app.get().draw = this::draw;
    timeout = conf.GAME_FPS * 5;
  }

  private void logic() {
    commonView.doBackground();
    if (reveal < conf.WINDOW_HEIGHT) {
      reveal++;
    }
    if (--timeout <= 0) {
      highscoresView.get().init();
    }
    if (input.keyboard[SDL_SCANCODE_SPACE]) {
      stageView.init();
    }
  }

  private void draw() {
    commonView.drawBackground();
    drawLogo();

    if (timeout % 40 < 20) {
      textService.drawText(
          conf.WINDOW_WIDTH / 2, 600, Color.WHITE, TextAlignment.CENTER, "PRESS FIRE TO PLAY!");
    }
  }

  private void drawLogo() {
    textService.drawText(
        conf.WINDOW_WIDTH / 2, 150 , Color.YELLOW, TextAlignment.CENTER, "nno v1");
  }
}

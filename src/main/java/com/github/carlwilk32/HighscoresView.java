package com.github.carlwilk32;

import static com.github.carlwilk32.TextService.GLYPH_HEIGHT;
import static com.github.carlwilk32.TextService.GLYPH_WIDTH;
import static com.github.carlwilk32.sdl.core.api.render.SdlRender.SDL_RenderFillRect;
import static com.github.carlwilk32.sdl.core.api.render.SdlRender.SDL_SetRenderDrawColor;
import static com.github.carlwilk32.sdl.core.api.scancode.SDL_Scancode.*;

import com.github.carlwilk32.sdl.core.api.rect.SDL_Rect;
import com.github.carlwilk32.sdl.core.api.render.SDL_Renderer;
import com.github.carlwilk32.model.Color;
import com.github.carlwilk32.model.Highscore;
import com.github.carlwilk32.model.TextAlignment;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.util.*;
import lombok.RequiredArgsConstructor;

@Singleton
@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class HighscoresView {

  private static final int NUM_HIGHSCORES = 9;
  private static final int NAME_LENGTH = 15;

  private final CommonView commonView;
  private final TitleView homeView;
  private final InputService input;
  private final StageView stage;
  private final App app;
  private final TextService textService;
  private final AppConfig conf;
  private final TextPlaceholders texts;
  private final SDL_Renderer renderer;

  private final List<Highscore> scoresDescOrdered = new LinkedList<>();
  private Integer newHighscoreIdx;
  private int cursorBlink;
  private int timeout;

  private StringBuilder sb = new StringBuilder();

  private void logic() {
    commonView.doBackground();
    if (newHighscoreIdx != null) {
      doNameInput();
    } else {
      if (--timeout <= 0) homeView.init();
      if (input.keyboard[SDL_SCANCODE_SPACE]) {
        stage.init();
      }
    }
    if (++cursorBlink >= conf.GAME_FPS) {
      cursorBlink = 0;
    }
  }

  private void doNameInput() {
    if (input.inputChar != null && sb.length() < NAME_LENGTH) {
      if (input.inputChar >= ' ' && input.inputChar <= '~') {
        sb.append(input.inputChar);
      }
    }

    if (!sb.isEmpty() && input.keyboard[SDL_SCANCODE_BACKSPACE]) {
      sb.deleteCharAt(sb.length() - 1);
      input.keyboard[SDL_SCANCODE_BACKSPACE] = false;
    }

    if (input.keyboard[SDL_SCANCODE_RETURN]) {
      if (sb.isEmpty()) {
        sb.append(texts.HIGHSCORES_ANONYMOUS);
      }
      scoresDescOrdered.get(newHighscoreIdx).name = sb.toString();
      sb = new StringBuilder();
      this.newHighscoreIdx = null;
    }
  }

  private void draw() {
    commonView.drawBackground();
    if (newHighscoreIdx != null) drawNameInput();
    else {
      drawHighscores();
    }
  }

  private void drawNameInput() {
    var center = conf.WINDOW_WIDTH / 2;
    var name = sb.toString();
    textService.drawText(
        center, 70, Color.WHITE, TextAlignment.CENTER, texts.HIGHSCORES_INPUT_TITLE);
    textService.drawText(
        center, 120, Color.WHITE, TextAlignment.CENTER, texts.HIGHSCORES_INPUT_NAME);
    textService.drawText(center, 250, Color.WHITE, TextAlignment.CENTER, name);

    if (cursorBlink < conf.GAME_FPS / 2) {
      var sdlRect = new SDL_Rect();
      sdlRect.x = (center + (name.length() * GLYPH_WIDTH) / 2) + 5;
      sdlRect.y = 250;
      sdlRect.w = GLYPH_WIDTH;
      sdlRect.h = GLYPH_HEIGHT;

      SDL_SetRenderDrawColor(renderer, (byte) 0, (byte) 255, (byte) 0, (byte) 255);
      SDL_RenderFillRect(renderer, sdlRect);
    }

    textService.drawText(
        center, 625, Color.WHITE, TextAlignment.CENTER, texts.HIGHSCORES_INPUT_CONFIRM);
  }

  private void drawHighscores() {
    var center = conf.WINDOW_WIDTH / 2;
    textService.drawText(center, 50, Color.WHITE, TextAlignment.CENTER, texts.HIGHSCORES_TITLE);
    var verticalOffset = 150;
    for (var i = 0; i < scoresDescOrdered.size(); i++) {
      var highscore = scoresDescOrdered.get(i);
      textService.drawText(
          center,
          verticalOffset,
          highscore.color,
          TextAlignment.CENTER,
          String.format("#%d %-" + NAME_LENGTH + "s %03d", i + 1, highscore.name, highscore.score));
      verticalOffset += 50;
    }
    textService.drawText(
        center,
        conf.WINDOW_HEIGHT - 50 - GLYPH_HEIGHT,
        Color.WHITE,
        TextAlignment.CENTER,
        texts.HIGHSCORES_FOOTER);
  }

  public void init() {
    app.logic = this::logic;
    app.draw = this::draw;

    timeout = conf.GAME_FPS * 5;
  }

  public void addHighScore(int newScore) {
    if (newScore == 0) return;

    for (var score : scoresDescOrdered) {
      score.color = Color.WHITE;
    }

    var maybeHighscore = new Highscore(newScore);
    maybeHighscore.color = Color.YELLOW;

    if (scoresDescOrdered.isEmpty()) {
      scoresDescOrdered.add(maybeHighscore);
      this.newHighscoreIdx = 0;
    } else if (scoresDescOrdered.size() <= NUM_HIGHSCORES) {
      var leftIdx = 0;
      var rightIdx = scoresDescOrdered.size();
      while (leftIdx < rightIdx) {
        var midIdx = (rightIdx + leftIdx) / 2;
        if (newScore <= scoresDescOrdered.get(midIdx).getScore()) leftIdx = midIdx + 1;
        else rightIdx = midIdx;
      }
      scoresDescOrdered.add(leftIdx, maybeHighscore);
      this.newHighscoreIdx = leftIdx;

      if (scoresDescOrdered.size() > NUM_HIGHSCORES) scoresDescOrdered.remove(NUM_HIGHSCORES);
    }
  }
}

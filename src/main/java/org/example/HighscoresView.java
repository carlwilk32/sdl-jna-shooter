package org.example;

import static io.github.libsdl4j.api.render.SdlRender.SDL_RenderFillRect;
import static io.github.libsdl4j.api.render.SdlRender.SDL_SetRenderDrawColor;
import static io.github.libsdl4j.api.scancode.SDL_Scancode.*;
import static org.example.TextService.GLYPH_HEIGHT;
import static org.example.TextService.GLYPH_WIDTH;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.github.libsdl4j.api.rect.SDL_Rect;
import io.github.libsdl4j.api.render.SDL_Renderer;
import java.util.*;
import lombok.RequiredArgsConstructor;
import org.example.model.Color;
import org.example.model.Highscore;
import org.example.model.TextAlignment;

@Singleton
@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class HighscoresView {

  public static final String TITLE_TEXT = "== HIGHSCORES ==";
  public static final String FOOTER_TEXT = "PRESS FIRE TO PLAY!";
  private static final int NUM_HIGHSCORES = 9;
  private static final int NAME_LENGTH = 15;

  private final InputService input;
  private final StageView stage;
  private final App app;
  private final TextService text;
  private final AppConfig conf;
  private final SDL_Renderer renderer;

  private final List<Highscore> scoresDescOrdered = new LinkedList<>();
  private Integer newHighscoreIdx;
  private int cursorBlink;

  private StringBuilder sb = new StringBuilder();

  private void logic() {
    if (newHighscoreIdx != null) {
      doNameInput();
    } else if (input.keyboard[SDL_SCANCODE_SPACE]) {
      stage.init();
    }
    if (++cursorBlink >= conf.GAME_FPS) {
      cursorBlink = 0;
    }
  }

  private void doNameInput() {
    if (input.inputChar != null && sb.length() < NAME_LENGTH) {
      if (input.inputChar >= '!' && input.inputChar <= '~') {
        sb.append(input.inputChar);
      }
    }

    if (!sb.isEmpty() && input.keyboard[SDL_SCANCODE_BACKSPACE]) {
      sb.deleteCharAt(sb.length() - 1);
      input.keyboard[SDL_SCANCODE_BACKSPACE] = false;
    }

    if (input.keyboard[SDL_SCANCODE_RETURN]) {
      if (sb.isEmpty()) {
        sb.append("Anonymous");
      }
      scoresDescOrdered.get(newHighscoreIdx).name = sb.toString();
      sb = new StringBuilder();
      this.newHighscoreIdx = null;
    }
  }

  private void draw() {
    if (newHighscoreIdx != null) drawNameInput();
    else drawHighscores();
  }

  private void drawNameInput() {
    var center = conf.WINDOW_WIDTH / 2;
    var name = sb.toString();
    text.drawText(center, 70, Color.WHITE, TextAlignment.CENTER, "YAY! IT'S A HIGHSCORE!");
    text.drawText(center, 120, Color.WHITE, TextAlignment.CENTER, "ENTER YOUR NAME:");
    text.drawText(center, 250, Color.WHITE, TextAlignment.CENTER, name);

    if (cursorBlink < conf.GAME_FPS / 2) {
      var sdlRect = new SDL_Rect();
      sdlRect.x = (center + (name.length() * GLYPH_WIDTH) / 2) + 5;
      sdlRect.y = 250;
      sdlRect.w = GLYPH_WIDTH;
      sdlRect.h = GLYPH_HEIGHT;

      SDL_SetRenderDrawColor(renderer, (byte) 0, (byte) 255, (byte) 0, (byte) 255);
      SDL_RenderFillRect(renderer, sdlRect);
    }

    text.drawText(center, 625, Color.WHITE, TextAlignment.CENTER, "PRESS RETURN TO CONFIRM");
  }

  private void drawHighscores() {
    var center = conf.WINDOW_WIDTH / 2;
    text.drawText(center, 50, Color.WHITE, TextAlignment.CENTER, TITLE_TEXT);
    var verticalOffset = 150;
    for (var i = 0; i < scoresDescOrdered.size(); i++) {
      var highscore = scoresDescOrdered.get(i);
      text.drawText(
          center,
          verticalOffset,
          highscore.color,
          TextAlignment.CENTER,
          String.format("#%d %-" + NAME_LENGTH + "s %03d", i + 1, highscore.name, highscore.score));
      verticalOffset += 50;
    }
    text.drawText(
        center,
        conf.WINDOW_HEIGHT - 50 - GLYPH_HEIGHT,
        Color.WHITE,
        TextAlignment.CENTER,
        FOOTER_TEXT);
  }

  public void init() {
    app.logic = this::logic;
    app.draw = this::draw;
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

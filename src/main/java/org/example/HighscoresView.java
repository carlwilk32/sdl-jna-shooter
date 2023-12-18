package org.example;

import static io.github.libsdl4j.api.scancode.SDL_Scancode.SDL_SCANCODE_SPACE;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.util.*;

import lombok.RequiredArgsConstructor;
import org.example.model.Highscore;
import org.example.model.TextAlignment;

@Singleton
@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class HighscoresView {

  public static final String TITLE_TEXT = "== HIGHSCORES ==";
  public static final String FOOTER_TEXT = "PRESS FIRE TO PLAY!";
  private static final int NUM_HIGHSCORES = 9;
  private final InputService input;
  private final StageView stage;
  private final App app;
  private final TextService text;
  private final AppConfig conf;

  private final List<Highscore> scores = new ArrayList<>();

  private void logic() {
    if (input.keyboard[SDL_SCANCODE_SPACE]) {
      stage.init();
    }
  }

  private void draw() {
    drawHighscores();
  }

  private void drawHighscores() {
    scores.sort(Comparator.comparingInt(Highscore::score).reversed());
    if (scores.size() > NUM_HIGHSCORES) scores.remove(NUM_HIGHSCORES);

    var center = conf.WINDOW_WIDTH / 2;
    text.drawText(center, 50, 255, 255, 255, TextAlignment.CENTER, TITLE_TEXT);
    var verticalOffset = 150;
    for (var i = 0; i < scores.size(); i++) {
      text.drawText(
          center,
          verticalOffset,
          255,
          255,
          255,
          TextAlignment.CENTER,
          String.format("#%d ............. %03d", i + 1, scores.get(i).score()));
      verticalOffset += 50;
    }
    text.drawText(
        center,
        conf.WINDOW_HEIGHT - 50 - TextService.GLYPH_HEIGHT,
        255,
        255,
        255,
        TextAlignment.CENTER,
        FOOTER_TEXT);
  }

  public void init() {
    app.logic = this::logic;
    app.draw = this::draw;
  }

  public void addHighScore(int score) {
    if (score == 0) return;
    scores.add(new Highscore("plauyer", score));
  }
}

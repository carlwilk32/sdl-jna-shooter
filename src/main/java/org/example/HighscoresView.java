package org.example;

import static io.github.libsdl4j.api.scancode.SDL_Scancode.SDL_SCANCODE_SPACE;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.time.Instant;
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

  private final List<Highscore> scores = new LinkedList<>();

  private Highscore newHighscore;

  private void logic() {
    //    if (newHighscore != null) {
    //      doNameInput();
    //    } else
    if (input.keyboard[SDL_SCANCODE_SPACE]) {
      stage.init();
    }
  }

  private void draw() {
    drawHighscores();
  }

  private void drawHighscores() {
    var center = conf.WINDOW_WIDTH / 2;
    text.drawText(center, 50, 255, 255, 255, TextAlignment.CENTER, TITLE_TEXT);
    var verticalOffset = 150;
    for (var i = 0; i < scores.size(); i++) {
      var r = 255;
      var g = 255;
      var b = 255;
      var highscore = scores.get(i);
      if (newHighscore != null && newHighscore.equals(highscore)) {
        b = 0;
      }
      text.drawText(
          center,
          verticalOffset,
          r,
          g,
          b,
          TextAlignment.CENTER,
          String.format("#%d ............. %03d", i + 1, highscore.getScore()));
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

  public void addHighScore(int newScore) {
    var maybeHighscore = new Highscore(newScore, Instant.now().getEpochSecond());
    if (scores.size() < NUM_HIGHSCORES) {
      scores.add(maybeHighscore);
      scores.sort(Comparator.comparingInt(Highscore::getScore).reversed());
      this.newHighscore = maybeHighscore;
    } else {
      scores.sort(Comparator.comparingInt(Highscore::getScore).reversed());
      var rightIdx = scores.size() - 1;
      if (newScore > scores.get(rightIdx).getScore()) {
        var leftIdx = 0;
        while (leftIdx < rightIdx) {
          var midIdx = (rightIdx + leftIdx) / 2;
          if (newScore <= scores.get(midIdx).getScore()) leftIdx = midIdx + 1;
          else rightIdx = midIdx;
        }
        scores.add(rightIdx, maybeHighscore);
        scores.remove(NUM_HIGHSCORES);
        this.newHighscore = maybeHighscore;
      }
    }
  }
}

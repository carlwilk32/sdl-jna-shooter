package org.example;

import static io.github.libsdl4j.api.scancode.SDL_Scancode.SDL_SCANCODE_SPACE;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.util.*;
import lombok.RequiredArgsConstructor;
import org.example.model.Highscore;

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

  private final PriorityQueue<Highscore> scores =
      new PriorityQueue<>(Comparator.comparingInt(Highscore::score));

  private void logic() {
    if (input.keyboard[SDL_SCANCODE_SPACE]) {
      stage.init();
    }
  }

  private void draw() {
    drawHighscores();
  }

  private void drawHighscores() {
    {
      text.drawText(alignToCenter(TITLE_TEXT), 50, 255, 255, 255, TITLE_TEXT);
      var verticalOffset = 150;
      var num = 1;
      var scoresArray = scores.toArray(new Highscore[0]);
      for (var i = scores.size() - 1; i >= 0; i--) {
        var scoreString = String.format("#%d ............. %03d", num, scoresArray[i].score());
        text.drawText(alignToCenter(scoreString), verticalOffset, 255, 255, 255, scoreString);
        verticalOffset += 50;
        num++;
      }
    }
    text.drawText(
        alignToCenter(FOOTER_TEXT),
        conf.WINDOW_HEIGHT - 50 - TextService.GLYPH_HEIGHT,
        255,
        255,
        255,
        FOOTER_TEXT);
  }

  private int alignToCenter(String s) {
    return conf.WINDOW_WIDTH / 2 - (s.length() * TextService.GLYPH_WIDTH) / 2;
  }

  public void init() {
    app.logic = this::logic;
    app.draw = this::draw;
  }

  public void addHighScore(int score) {
    if (score == 0) return;
    scores.add(new Highscore("plauyer", score));
    if (scores.size() > NUM_HIGHSCORES) {
      scores.poll();
    }
  }
}

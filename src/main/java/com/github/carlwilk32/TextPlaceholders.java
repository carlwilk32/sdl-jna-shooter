package com.github.carlwilk32;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import lombok.RequiredArgsConstructor;

@Singleton
@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class TextPlaceholders {

  @Named("highscores.table.title")
  public final String HIGHSCORES_TITLE;

  @Named("highscores.table.footer")
  public final String HIGHSCORES_FOOTER;

  @Named("highscores.table.anonymous")
  public final String HIGHSCORES_ANONYMOUS;

  @Named("highscores.input.title")
  public final String HIGHSCORES_INPUT_TITLE;

  @Named("highscores.input.name")
  public final String HIGHSCORES_INPUT_NAME;

  @Named("highscores.input.confirm")
  public final String HIGHSCORES_INPUT_CONFIRM;
}

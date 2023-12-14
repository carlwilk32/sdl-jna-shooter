package org.example;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import lombok.RequiredArgsConstructor;

@Singleton
@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class AppConfig {

  @Named("app.window.width")
  public final int WINDOW_WIDTH;

  @Named("app.window.height")
  public final int WINDOW_HEIGHT;

}

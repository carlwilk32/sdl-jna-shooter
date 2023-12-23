package com.github.carlwilk32.di;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.Properties;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AppModule extends AbstractModule {
  @Override
  public void configure() {
    try {
      Properties properties = new Properties();
      ClassLoader loader = Thread.currentThread().getContextClassLoader();
      properties.load(
          new InputStreamReader(
              Objects.requireNonNull(loader.getResourceAsStream("app.properties"))));
      Names.bindProperties(binder(), properties);
    } catch (IOException ex) {
      log.error("Unable to load configuration file", ex);
      throw new RuntimeException(ex);
    }
  }
}

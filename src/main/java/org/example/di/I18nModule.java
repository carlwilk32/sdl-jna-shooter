package org.example.di;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.Properties;

@Slf4j
public class I18nModule extends AbstractModule {
  @Override
  public void configure() {
    try {
      Properties properties = new Properties();
      ClassLoader loader = Thread.currentThread().getContextClassLoader();
      properties.load(
          new InputStreamReader(
              Objects.requireNonNull(loader.getResourceAsStream("en_US.properties"))));
      Names.bindProperties(binder(), properties);
    } catch (IOException ex) {
      log.error("Unable to load configuration file", ex);
      throw new RuntimeException(ex);
    }
  }
}

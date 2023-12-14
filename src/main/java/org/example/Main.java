package org.example;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class Main {
  public static void main(String[] args) {
//    NativeLibrary.addSearchPath("SDL2", "/usr/local/lib/");
    Injector injector = Guice.createInjector(new SdlModule(), new AppModule());
    App app = injector.getInstance(App.class);
    app.start();
  }
}

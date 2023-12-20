package org.example;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.util.Modules;
import org.example.di.AppModule;
import org.example.di.I18nModule;
import org.example.di.SdlModule;
import org.example.di.StageModule;

public class Main {
  public static void main(String[] args) {
    //    NativeLibrary.addSearchPath("SDL2", "/usr/local/lib/");
    Injector injector = Guice.createInjector(
            Modules.disableCircularProxiesModule(),
            new SdlModule(),
            new AppModule(),
            new StageModule(),
            new I18nModule());
    App app = injector.getInstance(App.class);
    app.start();
  }
}

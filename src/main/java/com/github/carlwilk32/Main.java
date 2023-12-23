package com.github.carlwilk32;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.util.Modules;
import com.github.carlwilk32.di.AppModule;
import com.github.carlwilk32.di.I18nModule;
import com.github.carlwilk32.di.SdlModule;
import com.github.carlwilk32.di.StageModule;

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

package org.example;

import static io.github.libsdl4j.api.error.SdlError.SDL_GetError;
import static io.github.libsdl4j.api.render.SdlRender.SDL_CreateRenderer;
import static io.github.libsdl4j.api.video.SdlVideo.SDL_CreateWindow;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import io.github.libsdl4j.api.event.SDL_Event;
import io.github.libsdl4j.api.render.SDL_Renderer;
import io.github.libsdl4j.api.render.SDL_RendererFlags;
import io.github.libsdl4j.api.video.SDL_Window;
import io.github.libsdl4j.api.video.SdlVideoConst;
import org.example.jna.SDL_imageLibrary;

public class SdlModule extends AbstractModule {

  @Provides
  static SDL_Event providesSdlEvent() {
    return new SDL_Event();
  }

  @Provides
  static SDL_imageLibrary providesSdlImageLibrary() {
    return SDL_imageLibrary.INSTANCE;
  }

  //  @Provides
  //  static SDL2 providesSdlLibrary() {
  //    return Native.load("SDL2", SDL2.class);
  //  }

  @Provides
  static SDL_Renderer provideSdlRenderer(SDL_Window sdlWindow) {
    var rendererFlags = SDL_RendererFlags.SDL_RENDERER_ACCELERATED;
    return SDL_CreateRenderer(sdlWindow, -1, rendererFlags);
  }

  @Provides
  static SDL_Window provideSdlWindow(AppConfig appConfig) {
    var windowFlags = 0;
    var window =
        SDL_CreateWindow(
            "SDL Java Shooter",
            SdlVideoConst.SDL_WINDOWPOS_UNDEFINED(),
            SdlVideoConst.SDL_WINDOWPOS_UNDEFINED(),
            appConfig.WINDOW_WIDTH,
            appConfig.WINDOW_HEIGHT,
            windowFlags);
    if (window == null) {
      throw new IllegalStateException("Unable to create SDL window: " + SDL_GetError());
    }
    return window;
  }
}

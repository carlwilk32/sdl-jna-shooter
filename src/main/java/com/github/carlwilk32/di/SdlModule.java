package com.github.carlwilk32.di;

import static com.github.carlwilk32.api.error.SdlError.SDL_GetError;
import static com.github.carlwilk32.api.render.SdlRender.SDL_CreateRenderer;
import static com.github.carlwilk32.api.video.SdlVideo.SDL_CreateWindow;

import com.github.carlwilk32.AppConfig;
import com.github.carlwilk32.api.render.SDL_Renderer;
import com.github.carlwilk32.api.render.SDL_RendererFlags;
import com.github.carlwilk32.api.video.SDL_Window;
import com.github.carlwilk32.api.video.SdlVideoConst;
import com.github.carlwilk32.jna.SDL_imageLibrary;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

public class SdlModule extends AbstractModule {

  @Provides
  @Singleton
  static SDL_imageLibrary providesSdlImageLibrary() {
    return SDL_imageLibrary.INSTANCE;
  }

  @Provides
  @Singleton
  static SDL_Renderer provideSdlRenderer(SDL_Window sdlWindow) {
    var rendererFlags = SDL_RendererFlags.SDL_RENDERER_ACCELERATED;
    // TODO: maybe pass with vsync to cap FPS
    //    var rendererFlags = SDL_RendererFlags.SDL_RENDERER_ACCELERATED |
    // SDL_RendererFlags.SDL_RENDERER_PRESENTVSYNC;
    return SDL_CreateRenderer(sdlWindow, -1, rendererFlags);
  }

  @Provides
  @Singleton
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

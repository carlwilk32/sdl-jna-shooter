package org.example;

import static io.github.libsdl4j.api.Sdl.SDL_Init;
import static io.github.libsdl4j.api.Sdl.SDL_Quit;
import static io.github.libsdl4j.api.error.SdlError.SDL_GetError;
import static io.github.libsdl4j.api.hints.SdlHints.SDL_SetHint;
import static io.github.libsdl4j.api.mouse.SdlMouse.SDL_ShowCursor;
import static io.github.libsdl4j.api.render.SdlRender.SDL_CreateRenderer;
import static io.github.libsdl4j.api.video.SdlVideo.SDL_CreateWindow;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import io.github.libsdl4j.api.SdlSubSystemConst;
import io.github.libsdl4j.api.event.SDL_Event;
import io.github.libsdl4j.api.event.SdlEventsConst;
import io.github.libsdl4j.api.hints.SdlHintsConst;
import io.github.libsdl4j.api.render.SDL_Renderer;
import io.github.libsdl4j.api.render.SDL_RendererFlags;
import io.github.libsdl4j.api.video.SDL_Window;
import io.github.libsdl4j.api.video.SdlVideoConst;

public class SdlModule extends AbstractModule {

  // Initialize SDL
  public SdlModule() {
    //    var result = SDL_Init(SDL_INIT_EVERYTHING);
    var sdlFlags = SdlSubSystemConst.SDL_INIT_VIDEO;
    var result = SDL_Init(sdlFlags);
    if (result != 0) {
      throw new IllegalStateException(
          "Unable to initialize SDL library (Error code " + result + "): " + SDL_GetError());
    }
    SDL_SetHint(SdlHintsConst.SDL_HINT_RENDER_SCALE_QUALITY, "linear");
    SDL_ShowCursor(SdlEventsConst.SDL_DISABLE);
  }

  @Provides
  static SDL_Event providesSdlEvent() {
    return new SDL_Event();
  }

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

  private void cleanup() {
    //            SDL_DestroyRenderer(app.renderer);
    //            SDL_DestroyWindow(app.window);
    SDL_Quit();
  }
}

package org.example;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.github.libsdl4j.api.render.SDL_Renderer;
import lombok.RequiredArgsConstructor;

import static io.github.libsdl4j.api.render.SdlRender.*;

@Singleton
@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class Draw {

  private final SDL_Renderer renderer;

  public void prepareScene() {
    SDL_SetRenderDrawColor(renderer, (byte) 96, (byte) 128, (byte) 255, (byte) 255);
    SDL_RenderClear(renderer);
  }

  public void presentScene() {
    SDL_RenderPresent(renderer);
  }
}

package org.example;

import static io.github.libsdl4j.api.Sdl.SDL_Init;
import static io.github.libsdl4j.api.blendmode.SDL_BlendMode.SDL_BLENDMODE_ADD;
import static io.github.libsdl4j.api.error.SdlError.SDL_GetError;
import static io.github.libsdl4j.api.hints.SdlHints.SDL_SetHint;
import static io.github.libsdl4j.api.mouse.SdlMouse.SDL_ShowCursor;
import static io.github.libsdl4j.api.render.SdlRender.*;
import static org.example.jna.SDL_imageLibrary.IMG_InitFlags.IMG_INIT_JPG;
import static org.example.jna.SDL_imageLibrary.IMG_InitFlags.IMG_INIT_PNG;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.sun.jna.ptr.IntByReference;
import io.github.libsdl4j.api.SdlSubSystemConst;
import io.github.libsdl4j.api.blendmode.SDL_BlendMode;
import io.github.libsdl4j.api.event.SdlEventsConst;
import io.github.libsdl4j.api.hints.SdlHintsConst;
import io.github.libsdl4j.api.rect.SDL_Rect;
import io.github.libsdl4j.api.render.SDL_Renderer;
import io.github.libsdl4j.api.render.SDL_Texture;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.jna.SDL_imageLibrary;
import org.intellij.lang.annotations.MagicConstant;

@Singleton
@RequiredArgsConstructor(onConstructor_ = {@Inject})
@Slf4j
public class Draw {

  private final SDL_Renderer renderer;
  private final SDL_imageLibrary sdlImage;

  public void initGraphics() {
    initSDL();
    initSdlImage();
  }

  private void initSdlImage() {
    sdlImage.IMG_Init(IMG_INIT_JPG | IMG_INIT_PNG);
  }

  private void initSDL() {
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

  public void prepareScene() {
    SDL_SetRenderDrawColor(renderer, (byte) 96, (byte) 128, (byte) 255, (byte) 255);
    SDL_RenderClear(renderer);
  }

  public void presentScene() {
    SDL_RenderPresent(renderer);
  }

  public SDL_Texture loadTexture(String fileName) {
    log.info("Loading {}", fileName);
    try {
      var resourceAsStream =
          Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
      var imageFile = Utils.stream2file(resourceAsStream, "image", ".png");
      var fullPath = imageFile.toURI().getPath();
      var texture = sdlImage.IMG_LoadTexture(renderer, fullPath);
      if (texture == null) {
        log.error("Failed to load texture {}.", fileName);
        throw new RuntimeException();
      }
      return texture;
    } catch (IOException e) {
      log.error("Unable to save texture stream to memory.", e);
      throw new RuntimeException(e);
    }
  }

  public void blit(SDL_Texture texture, int x, int y) {
    var dest = new SDL_Rect();
    dest.x = x;
    dest.y = y;
    var w = new IntByReference(dest.w);
    var h = new IntByReference(dest.h);
    var loaded = SDL_QueryTexture(texture, null, null, w, h);
    if (loaded != 0) {
      log.error("Unable to query texture with SDL error = {}", SDL_GetError());
      throw new RuntimeException();
    }
    dest.w = w.getValue();
    dest.h = h.getValue();
    var copied = SDL_RenderCopy(renderer, texture, null, dest);
    if (copied != 0) {
      log.error("Unable to copy texture with SDL error = {}", SDL_GetError());
      throw new RuntimeException();
    }
  }

  public void cleanup() {
    //    SdlImage.IMG_Quit();
    //    SDL_DestroyRenderer(renderer);
    //    SDL_DestroyWindow(window);
    //    SDL_Quit();
  }

  public void renderBackground(SDL_Texture background, SDL_Rect sdlRect) {
    SDL_RenderCopy(renderer, background, null, sdlRect);
  }

  public void setBlenMode(@MagicConstant(valuesFromClass = SDL_BlendMode.class) int blendMode) {
    SDL_SetRenderDrawBlendMode(renderer, blendMode);
  }
}

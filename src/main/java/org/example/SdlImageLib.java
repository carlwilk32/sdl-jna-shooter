package org.example;

import com.sun.jna.Library;
import io.github.libsdl4j.api.render.SDL_Renderer;
import io.github.libsdl4j.api.render.SDL_Texture;

public interface SdlImageLib extends Library {
  int IMG_INIT_JPG = 0x00000001;
  int IMG_INIT_PNG = 0x00000002;
  int IMG_INIT_TIF = 0x00000004;
  int IMG_INIT_WEBP = 0x00000008;
  int IMG_INIT_JXL = 0x00000010;
  int IMG_INIT_AVIF = 0x00000020;

  int IMG_Init(int flags);

  void IMG_quit();

  SDL_Texture IMG_LoadTexture(SDL_Renderer renderer, String file);
}

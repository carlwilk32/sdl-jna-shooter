package org.example.model;

import io.github.libsdl4j.api.render.SDL_Texture;

public interface Entity {
  int getX();

  int getY();

  SDL_Texture getTexture();
}

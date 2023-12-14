package org.example.model;

import io.github.libsdl4j.api.render.SDL_Texture;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Player implements Entity {
  public int x;
  public int y;
  public SDL_Texture texture;
}

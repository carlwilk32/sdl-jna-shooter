package org.example.model;

import io.github.libsdl4j.api.render.SDL_Texture;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;


@AllArgsConstructor
@RequiredArgsConstructor
public class CommonGameEntity {
  public int x;
  public int y;
  public int w;
  public int h;
  public int dx;
  public int dy;
  public int health;
  public int reload;
  public final SDL_Texture texture;
}

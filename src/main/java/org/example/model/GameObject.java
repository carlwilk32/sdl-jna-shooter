package org.example.model;

import io.github.libsdl4j.api.render.SDL_Texture;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;


@AllArgsConstructor
@RequiredArgsConstructor
public class GameObject {
  public int x;
  public int y;
  public int w;
  public int h;
  public int dx;
  public int dy;
  public int health;
  public int reload;
  public Owner owner;
  public final SDL_Texture texture;
}

package com.github.carlwilk32.model;

import static com.github.carlwilk32.sdl.core.api.render.SdlRender.SDL_QueryTexture;

import com.github.carlwilk32.sdl.core.api.render.SDL_Texture;
import com.sun.jna.ptr.IntByReference;
import lombok.AllArgsConstructor;
import lombok.Builder;

@AllArgsConstructor
@Builder(toBuilder = true, builderMethodName = "")
public class GameObject {
  public final SDL_Texture texture;
  public double x;
  public double y;
  public int w;
  public int h;
  public double dx;
  public double dy;
  public int health;
  public int reload;
  public Owner owner;

  public GameObject(SDL_Texture texture) {
    this.texture = texture;

    var wRef = new IntByReference();
    var hRef = new IntByReference();
    SDL_QueryTexture(texture, null, null, wRef, hRef);

    this.w = wRef.getValue();
    this.h = hRef.getValue();
  }
}

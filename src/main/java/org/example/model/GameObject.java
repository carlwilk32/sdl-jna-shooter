package org.example.model;

import com.sun.jna.ptr.IntByReference;
import io.github.libsdl4j.api.render.SDL_Texture;
import lombok.AllArgsConstructor;
import lombok.Builder;

import static io.github.libsdl4j.api.render.SdlRender.SDL_QueryTexture;

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

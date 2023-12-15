package org.example.jna;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;
import java.util.Arrays;
import java.util.List;

/**
 * Animated image support<br>
 * Currently only animated GIFs are supported.<br>
 * <i>native declaration : /usr/local/include/SDL_image.h:1646</i><br>
 */
public class IMG_Animation extends Structure {
  public int w;
  public int h;
  public int count;

  /**
   * C type : SDL_Surface**
   */
  public PointerByReference frames;

  /**
   * C type : int*
   */
  public IntByReference delays;

  public IMG_Animation() {
    super();
  }

  /**
   * @param frames C type : SDL_Surface**<br>
   * @param delays C type : int*
   */
  public IMG_Animation(int w, int h, int count, PointerByReference frames, IntByReference delays) {
    super();
    this.w = w;
    this.h = h;
    this.count = count;
    this.frames = frames;
    this.delays = delays;
  }

  public IMG_Animation(Pointer peer) {
    super(peer);
  }

  protected List<String> getFieldOrder() {
    return Arrays.asList("w", "h", "count", "frames", "delays");
  }

  public static class ByReference extends IMG_Animation implements Structure.ByReference {}

  public static class ByValue extends IMG_Animation implements Structure.ByValue {}
}

package org.example;

import static io.github.libsdl4j.api.render.SdlRender.SDL_SetTextureColorMod;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import io.github.libsdl4j.api.rect.SDL_Rect;
import io.github.libsdl4j.api.render.SDL_Texture;
import lombok.RequiredArgsConstructor;

@Singleton
@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class Text {

  private static final int GLYPH_HEIGHT = 26;
  private static final int GLYPH_WIDTH = 13;
  private final Draw draw;

  @Named("MainFont")
  private final SDL_Texture fontTexture;

  public void drawText(int x, int y, int r, int g, int b, String text) {
    var rect = new SDL_Rect();
    rect.w = GLYPH_WIDTH;
    rect.h = GLYPH_HEIGHT;
    rect.y = 0;

    SDL_SetTextureColorMod(fontTexture, (byte) r, (byte) g, (byte) b);

    for (var i = 0; i < text.length(); i++) {
      var c = text.charAt(i);
      if (c >= ' ' && c <= '~') {
        rect.x = (c - ' ') * GLYPH_WIDTH;
        draw.blitRect(fontTexture, rect, x, y);
        x += GLYPH_WIDTH;
      }
    }
  }
}

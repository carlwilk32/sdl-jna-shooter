package com.github.carlwilk32;

import static com.github.carlwilk32.sdl.core.api.render.SdlRender.SDL_SetTextureColorMod;

import com.github.carlwilk32.sdl.core.api.rect.SDL_Rect;
import com.github.carlwilk32.sdl.core.api.render.SDL_Texture;
import com.github.carlwilk32.model.Color;
import com.github.carlwilk32.model.TextAlignment;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import lombok.RequiredArgsConstructor;

@Singleton
@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class TextService {

  public static final int GLYPH_HEIGHT = 26;
  public static final int GLYPH_WIDTH = 13;
  private final DrawService draw;

  @Named("MainFont")
  private final SDL_Texture fontTexture;

  public void drawText(int x, int y, Color color, TextAlignment align, String text) {
    var rect = new SDL_Rect();
    rect.w = GLYPH_WIDTH;
    rect.h = GLYPH_HEIGHT;
    rect.y = 0;

    x =
        switch (align) {
          case CENTER -> x - text.length() * GLYPH_WIDTH / 2;
          case RIGHT -> x - text.length() * GLYPH_WIDTH;
          case LEFT -> x;
        };

    SDL_SetTextureColorMod(fontTexture, (byte) color.r(), (byte) color.g(), (byte) color.b());

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

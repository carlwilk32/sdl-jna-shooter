package com.github.carlwilk32;

import com.github.carlwilk32.sdl.core.api.rect.SDL_Rect;
import com.github.carlwilk32.sdl.core.api.render.SDL_Texture;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Singleton
@RequiredArgsConstructor(onConstructor_ = {@Inject})
@Slf4j
public class CommonView {

  @Named("Background")
  private final SDL_Texture background1;

  @Named("BackgroundSky")
  private final SDL_Texture background2;

  @Named("BackgroundMask")
  private final SDL_Texture background3;

  private final DrawService drawService;
  private final AppConfig conf;
  private int backgroundX;
  private int skyX;
  private int maskX;

  private void drawBackground(int xAnchor, SDL_Texture texture) {
    var sdlRect = new SDL_Rect();
    for (var x = xAnchor; x < conf.WINDOW_WIDTH; x += conf.WINDOW_WIDTH) {
      sdlRect.x = x;
      sdlRect.y = 0;
      sdlRect.w = conf.WINDOW_WIDTH;
      sdlRect.h = conf.WINDOW_HEIGHT;
      drawService.renderBackground(texture, sdlRect);
    }
  }

  private void drawBackgroundBase() {
    drawBackground(backgroundX, background1);
  }

  private void drawSky() {
    drawBackground(skyX, background2);
  }

  private void drawMask() {
    drawBackground(maskX, background3);
  }

  private void doBackgroundBase() {
    backgroundX -= 4;
    if (backgroundX < -conf.WINDOW_WIDTH) backgroundX = 0;
  }

  public void doBackground() {
    doBackgroundBase();
    doSky();
    doMask();
  }

  public void drawBackground() {
    drawBackgroundBase();
    drawSky();
    drawMask();
  }

  private void doSky() {
    if (--skyX < -conf.WINDOW_WIDTH) skyX = 0;
  }

  private void doMask() {
    maskX -= 2;
    if (--maskX < -conf.WINDOW_WIDTH) maskX = 0;
  }
}

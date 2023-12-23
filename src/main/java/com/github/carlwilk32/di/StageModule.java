package com.github.carlwilk32.di;

import com.github.carlwilk32.DrawService;
import com.github.carlwilk32.api.render.SDL_Texture;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StageModule extends AbstractModule {

  private static final String PLAYER_BULLET_SPRITE = "gfx/Bullet3.png";
  private static final String ENEMY_SPRITE = "gfx/su27.png";
  private static final String PLAYER_SPRITE = "gfx/f_16_2.png";
  private static final String ENEMY_BULLET_SPRITE = "gfx/BulletC6.png";
  private static final String BACKGROUND_BACK = "gfx/1.png";
  private static final String BACKGROUND_SKY = "gfx/2.png";
  private static final String BACKGROUND_MASK = "gfx/3.png";
  private static final String EXPLOSION_MASK = "gfx/explosion.png";
  private static final String MAIN_FONT = "fonts/inconsolata-ultra-bold-27-1235px-width-13px-glyph.png";

  @Provides
  @Singleton
  @Named("PlayerBullet")
  static SDL_Texture provideBulletTexture(DrawService draw) {
    return draw.loadTexture(PLAYER_BULLET_SPRITE);
  }

  @Provides
  @Singleton
  @Named("Enemy")
  static SDL_Texture provideEnemyTexture(DrawService draw) {
    return draw.loadTexture(ENEMY_SPRITE);
  }

  @Provides
  @Singleton
  @Named("Player")
  static SDL_Texture providePlayerTexture(DrawService draw) {
    return draw.loadTexture(PLAYER_SPRITE);
  }

  @Provides
  @Singleton
  @Named("EnemyBullet")
  static SDL_Texture provideEnemyBullet(DrawService draw) {
    return draw.loadTexture(ENEMY_BULLET_SPRITE);
  }

  @Provides
  @Singleton
  @Named("Background")
  static SDL_Texture provideBackground(DrawService draw) {
    return draw.loadTexture(BACKGROUND_BACK);
  }

  @Provides
  @Singleton
  @Named("BackgroundSky")
  static SDL_Texture provideBackgroundSky(DrawService draw) {
    return draw.loadTexture(BACKGROUND_SKY);
  }

  @Provides
  @Singleton
  @Named("BackgroundMask")
  static SDL_Texture provideBackgroundMask(DrawService draw) {
    return draw.loadTexture(BACKGROUND_MASK);
  }

  @Provides
  @Singleton
  @Named("Explosion")
  static SDL_Texture provideExplosionTexture(DrawService draw) {
    return draw.loadTexture(EXPLOSION_MASK);
  }

  @Provides
  @Singleton
  @Named("MainFont")
  static SDL_Texture provideMainFont(DrawService draw) {
    return draw.loadTexture(MAIN_FONT);
  }
}

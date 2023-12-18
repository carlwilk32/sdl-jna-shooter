package org.example.di;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import io.github.libsdl4j.api.render.SDL_Texture;
import lombok.extern.slf4j.Slf4j;
import org.example.Draw;

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
  static SDL_Texture provideBulletTexture(Draw draw) {
    return draw.loadTexture(PLAYER_BULLET_SPRITE);
  }

  @Provides
  @Singleton
  @Named("Enemy")
  static SDL_Texture provideEnemyTexture(Draw draw) {
    return draw.loadTexture(ENEMY_SPRITE);
  }

  @Provides
  @Singleton
  @Named("Player")
  static SDL_Texture providePlayerTexture(Draw draw) {
    return draw.loadTexture(PLAYER_SPRITE);
  }

  @Provides
  @Singleton
  @Named("EnemyBullet")
  static SDL_Texture provideEnemyBullet(Draw draw) {
    return draw.loadTexture(ENEMY_BULLET_SPRITE);
  }

  @Provides
  @Singleton
  @Named("Background")
  static SDL_Texture provideBackground(Draw draw) {
    return draw.loadTexture(BACKGROUND_BACK);
  }

  @Provides
  @Singleton
  @Named("BackgroundSky")
  static SDL_Texture provideBackgroundSky(Draw draw) {
    return draw.loadTexture(BACKGROUND_SKY);
  }

  @Provides
  @Singleton
  @Named("BackgroundMask")
  static SDL_Texture provideBackgroundMask(Draw draw) {
    return draw.loadTexture(BACKGROUND_MASK);
  }

  @Provides
  @Singleton
  @Named("Explosion")
  static SDL_Texture provideExplosionTexture(Draw draw) {
    return draw.loadTexture(EXPLOSION_MASK);
  }

  @Provides
  @Singleton
  @Named("MainFont")
  static SDL_Texture provideMainFont(Draw draw) {
    return draw.loadTexture(MAIN_FONT);
  }
}

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

  public static final String PLAYER_BULLET_SPRITE = "gfx/bullet.png";
  public static final String ENEMY_SPRITE = "gfx/su27.png";

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
}

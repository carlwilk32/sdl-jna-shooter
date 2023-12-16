package org.example;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import lombok.RequiredArgsConstructor;

@Singleton
@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class AppConfig {

  @Named("app.window.width")
  public final int WINDOW_WIDTH;

  @Named("app.window.height")
  public final int WINDOW_HEIGHT;

  @Named("gameplay.speed.player")
  public final int PLAYER_SPEED;

  @Named("gameplay.speed.playerBullet")
  public final int PLAYER_BULLET_SPEED;

  @Named("gameplay.speed.enemyBullet")
  public final int ENEMY_BULLET_SPEED;

  @Named("gameplay.FPS")
  public final int GAME_FPS;
}

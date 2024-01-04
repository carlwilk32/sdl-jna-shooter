package com.github.carlwilk32;

import com.github.carlwilk32.sdl.mixer.Mix_Chunk;
import com.github.carlwilk32.sdl.mixer.SDL_mixerLibrary;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.sun.jna.ptr.PointerByReference;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

import static com.github.carlwilk32.sdl.core.api.error.SdlError.SDL_GetError;

@Singleton
@RequiredArgsConstructor(onConstructor_ = {@Inject})
@Slf4j
public class AudioService {

  private static final int MAX_SND_CHANNELS = 8;
  private final SDL_mixerLibrary sdlMixerLibrary;
  private Map<Source, Mix_Chunk> sounds;
  private PointerByReference music;

  public void initAudio() {
    var openAudio =
        sdlMixerLibrary.Mix_OpenAudio(44100, SDL_mixerLibrary.MIX_DEFAULT_FORMAT, 2, 1024);
    if (openAudio != 0) {
      throw new IllegalStateException(
          "Unable to initialize SDL library (Error code " + openAudio + "): " + SDL_GetError());
    }

    sdlMixerLibrary.Mix_AllocateChannels(MAX_SND_CHANNELS);
    sounds = initSounds();
    music = initMusic("audio/music/607942__bloodpixelhero__retro-arcade-music-3.ogg");
  }

  public void playSound(Source source, int channel) {
    sdlMixerLibrary.Mix_PlayChannel(channel, sounds.get(source), 0);
  }

  private Map<Source, Mix_Chunk> initSounds() {
    var sounds = new HashMap<Source, Mix_Chunk>();
    sounds.put(Source.ENEMY_DIE, loadSound("audio/enemy_plane_explosion_.mp3"));
    sounds.put(Source.PLAYER_DIE, loadSound("audio/player_plane_explosion_smooth.mp3"));
    return sounds;
  }

  private PointerByReference initMusic(String filename) {
    log.info("Loading {}", filename);
    if (this.music != null) {
      sdlMixerLibrary.Mix_HaltMusic();
      sdlMixerLibrary.Mix_FreeMusic(music);
      music = null;
    }
    return sdlMixerLibrary.Mix_LoadMUS(Utils.getFullPath(filename));
  }

  public void playMusic(boolean loop) {
    // -1 for infinite play
    sdlMixerLibrary.Mix_PlayMusic(music, loop ? -1 : 0);
  }

  // TODO generify
  private Mix_Chunk loadSound(String fileName) {
    log.info("Loading {}", fileName);
    var fullPath = Utils.getFullPath(fileName);
    var sound = sdlMixerLibrary.Mix_LoadWAV(fullPath);
    if (sound == null) {
      log.error("Failed to load sound {}.", fileName);
      throw new RuntimeException();
    }
    return sound;
  }

  public void cleanUp() {
    sdlMixerLibrary.Mix_Quit();
  }

  public enum Source {
    PLAYER_DIE,
    ENEMY_DIE,
  }
}

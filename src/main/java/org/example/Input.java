package org.example;

import static io.github.libsdl4j.api.event.SdlEvents.SDL_PollEvent;
import static io.github.libsdl4j.api.scancode.SDL_Scancode.*;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.github.libsdl4j.api.event.SDL_Event;
import io.github.libsdl4j.api.event.SDL_EventType;
import io.github.libsdl4j.api.event.events.SDL_KeyboardEvent;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Singleton
public class Input {

  public final boolean[] keyboard = new boolean[350];
  private final SDL_Event event;

  @Inject
  public Input(SDL_Event event) {
    this.event = event;
  }

  public void doInput() {
    while (SDL_PollEvent(event) != 0) {
      switch (event.type) {
        case SDL_EventType.SDL_QUIT -> System.exit(0);
        case SDL_EventType.SDL_KEYDOWN -> doKeyDown(event.key);
        case SDL_EventType.SDL_KEYUP -> doKeyUp(event.key);
      }
    }
  }

  private void doKeyUp(SDL_KeyboardEvent key) {
    if (key.repeat == 0 && key.keysym.scancode < keyboard.length)
      this.keyboard[key.keysym.scancode] = false;
  }

  private void doKeyDown(SDL_KeyboardEvent key) {
    if (key.repeat == 0 && key.keysym.scancode < keyboard.length)
      this.keyboard[key.keysym.scancode] = true;
  }
}

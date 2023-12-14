package org.example;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.github.libsdl4j.api.event.SDL_Event;
import io.github.libsdl4j.api.event.SDL_EventType;
import io.github.libsdl4j.api.event.events.SDL_KeyboardEvent;
import io.github.libsdl4j.api.scancode.SDL_Scancode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static io.github.libsdl4j.api.event.SdlEvents.SDL_PollEvent;

@Slf4j
@Singleton
@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class Input {

  private final SDL_Event event;
  public boolean up;
  public boolean down;
  public boolean left;
  public boolean right;

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
    if (key.repeat == 0) {
      if (key.keysym.scancode == SDL_Scancode.SDL_SCANCODE_UP) {
        this.up = false;
      }

      if (key.keysym.scancode == SDL_Scancode.SDL_SCANCODE_DOWN) {
        this.down = false;
      }

      if (key.keysym.scancode == SDL_Scancode.SDL_SCANCODE_LEFT) {
        this.left = false;
      }

      if (key.keysym.scancode == SDL_Scancode.SDL_SCANCODE_RIGHT) {
        this.right = false;
      }
    }
  }

  private void doKeyDown(SDL_KeyboardEvent key) {
    if (key.repeat == 0) {
      if (key.keysym.scancode == SDL_Scancode.SDL_SCANCODE_UP) {
        this.up = true;
      }

      if (key.keysym.scancode == SDL_Scancode.SDL_SCANCODE_DOWN) {
        this.down = true;
      }

      if (key.keysym.scancode == SDL_Scancode.SDL_SCANCODE_LEFT) {
        this.left = true;
      }

      if (key.keysym.scancode == SDL_Scancode.SDL_SCANCODE_RIGHT) {
        this.right = true;
      }
    }
  }
}

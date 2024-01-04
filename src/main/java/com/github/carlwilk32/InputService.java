package com.github.carlwilk32;

import static com.github.carlwilk32.sdl.core.api.event.SdlEvents.SDL_PollEvent;

import com.github.carlwilk32.sdl.core.api.event.SDL_Event;
import com.github.carlwilk32.sdl.core.api.event.SDL_EventType;
import com.github.carlwilk32.sdl.core.api.event.events.SDL_KeyboardEvent;
import com.google.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Singleton
public class InputService {

  public final boolean[] keyboard = new boolean[350];
  public Character inputChar;

  public boolean doInput() {
    var event = new SDL_Event();
    inputChar = null;
    var shouldRun = true;
    while (SDL_PollEvent(event) != 0 && shouldRun) {
      switch (event.type) {
        case SDL_EventType.SDL_QUIT -> shouldRun = false;
        case SDL_EventType.SDL_KEYDOWN -> doKeyDown(event.key);
        case SDL_EventType.SDL_KEYUP -> doKeyUp(event.key);
        case SDL_EventType.SDL_TEXTINPUT -> inputChar = (char) event.text.text[0];
      }
    }
    return shouldRun;
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

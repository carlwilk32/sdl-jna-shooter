package com.github.carlwilk32;

import static com.github.carlwilk32.api.event.SdlEvents.SDL_PollEvent;

import com.github.carlwilk32.api.event.SDL_Event;
import com.github.carlwilk32.api.event.SDL_EventType;
import com.github.carlwilk32.api.event.events.SDL_KeyboardEvent;
import com.google.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Singleton
public class InputService {

  public final boolean[] keyboard = new boolean[350];
  public Character inputChar;

  public void doInput() {
    var event = new SDL_Event();
    inputChar = null;
    while (SDL_PollEvent(event) != 0) {
      switch (event.type) {
        case SDL_EventType.SDL_QUIT -> System.exit(0);
        case SDL_EventType.SDL_KEYDOWN -> doKeyDown(event.key);
        case SDL_EventType.SDL_KEYUP -> doKeyUp(event.key);
        case SDL_EventType.SDL_TEXTINPUT -> inputChar = (char) event.text.text[0];
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

package org.example;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.github.libsdl4j.api.event.SDL_Event;
import io.github.libsdl4j.api.event.SDL_EventType;
import lombok.RequiredArgsConstructor;

import static io.github.libsdl4j.api.event.SdlEvents.SDL_PollEvent;
import static java.lang.System.exit;

@Singleton
@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class Input {

  private final SDL_Event event;

  public void doInput() {
    while (SDL_PollEvent(event) != 0) {
      switch (event.type) {
        case SDL_EventType.SDL_QUIT -> exit(0);
      }
    }
  }
}

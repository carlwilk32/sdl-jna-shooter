package org.example.model;

import io.github.libsdl4j.api.rect.SDL_Rect;
import io.github.libsdl4j.api.render.SDL_Texture;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Debris {
        public double x;
        public double y;
        public double dx;
        public double dy;
        public SDL_Rect rect;
        public SDL_Texture texture;
        public int life;
}

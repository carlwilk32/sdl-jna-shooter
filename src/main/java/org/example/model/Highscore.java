package org.example.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class Highscore {
  public final int score;
  public Color color;
  public String name;
}

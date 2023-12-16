package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Explosion {
  public double x;
  public double y;
  public double dx;
  public double dy;
  public int alpha;
  public Color color;
}

package org.example.model;

public record Color(int r, int g, int b) {

  public static final Color WHITE = new Color(255, 255, 255);
  public static final Color YELLOW = new Color(255, 255, 0);
  public static final Color GREEN = new Color(0, 255, 0);
}

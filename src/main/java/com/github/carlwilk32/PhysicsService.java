package com.github.carlwilk32;

import com.github.carlwilk32.model.GameObject;
import com.github.carlwilk32.model.Pair;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import lombok.RequiredArgsConstructor;

@Singleton
@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class PhysicsService {

  public boolean collisionDetect(GameObject obj1, GameObject obj2) {
    var hit = Math.max(obj1.x, obj2.x) < Math.min(obj1.x + obj1.w, obj2.x + obj2.w);
    hit &= Math.max(obj1.y, obj2.y) < Math.min(obj1.y + obj1.h, obj2.y + obj2.h);
    return hit;
  }

  public Pair<Double, Double> calcSlope(double x1, double y1, double x2, double y2) {
    var steps = Math.max(Math.abs(x1 - x2), Math.abs(y1 - y2));

    if (steps == 0) return new Pair<>(0.0, 0.0);

    var dx = (x1 - x2) / steps;
    var dy = (y1 - y2) / steps;
    return new Pair<>(dx, dy);
  }
}

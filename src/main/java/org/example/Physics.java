package org.example;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import lombok.RequiredArgsConstructor;
import org.example.model.GameObject;

@Singleton
@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class Physics {

  public boolean collisionDetect(GameObject obj1, GameObject obj2) {
    var hit = Math.max(obj1.x, obj2.x) < Math.min(obj1.x + obj1.w, obj2.x + obj2.w);
    hit &= Math.max(obj1.y, obj2.y) < Math.min(obj1.y + obj1.h, obj2.y + obj2.h);
    return hit;
  }
}

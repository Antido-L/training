package cn.antido.cases.guava.ratelimter;

import com.google.common.util.concurrent.RateLimiter;

import java.util.HashMap;
import java.util.Map;

@FunctionalInterface
interface SendOperation<T> {
  T send();
}

public abstract class Client {

  public static final Map<Class, RateLimiter> limiterMap = new HashMap<>();

  abstract boolean init();

  abstract void flush();

  <T> T send(SendOperation<T> operation) {
    RateLimiter rateLimiter = limiterMap.get(this.getClass());
    if (rateLimiter != null) {
      rateLimiter.acquire();
    }
    return operation.send();
  }
}

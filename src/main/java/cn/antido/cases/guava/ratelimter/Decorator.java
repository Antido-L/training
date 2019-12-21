package cn.antido.cases.guava.ratelimter;

import com.google.common.util.concurrent.RateLimiter;

public class Decorator extends Client {

  private Client client;

  public Decorator(Class clientClass) throws IllegalAccessException, InstantiationException {
    this.client = (Client) clientClass.newInstance();
  }

  @Override
  public boolean init() {
    limiterMap.putIfAbsent(client.getClass(), RateLimiter.create(1));
    return client.init();
  }

  @Override
  public void flush() {
    client.flush();
  }


}

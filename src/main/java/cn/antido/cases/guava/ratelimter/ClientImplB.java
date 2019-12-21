package cn.antido.cases.guava.ratelimter;

public class ClientImplB extends Client {

  @Override
  public boolean init() {
    return true;
  }

  @Override
  public void flush() {
    for (int i = 0; i < 10; i++) {
      send(() -> {
        System.out.println("clent B");
        return null;
      });
    }
  }
}

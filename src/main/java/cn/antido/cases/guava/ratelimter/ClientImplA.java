package cn.antido.cases.guava.ratelimter;

public class ClientImplA extends Client {
  @Override
  public boolean init() {
    //System.out.println(this.getClass().getName() + "is init");
    return true;
  }

  @Override
  public void flush() {
    for (int i = 0; i < 10; i++) {
      send(() -> {
        System.out.println("clent A");
        return null;
      });
    }
  }
}

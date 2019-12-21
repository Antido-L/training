package cn.antido.cases.guava.ratelimter;

public class Main {
  public static void main(String[] args) throws Exception {
    Decorator clientA = new Decorator(ClientImplA.class);
    Decorator clientB = new Decorator(ClientImplB.class);
    //ClientImplA clientA = new ClientImplA();
    //ClientImplB clientB = new ClientImplB();
    clientA.init();
    clientB.init();
    new Thread(() -> clientA.flush()).start();
    new Thread(() -> clientA.flush()).start();
    new Thread(() -> clientB.flush()).start();
    Thread.sleep(100000L);
  }


}

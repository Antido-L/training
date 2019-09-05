package cn.antido.cases.java8;


/**
 * 这种方式我理解为可以将方法变成一个参数进行传递。
 * 将一个方法的实现抽象出来，通过函数式接口进行描述。
 * 之前是创建一个匿名内部类传递一个对象，然后进行调用。
 */
@FunctionalInterface
interface Operation {
  String operate(String v1, String v2);

  static void println(String str) {
    System.out.println(str);
  }
}


public class FuncInterface {
  public static void main(String[] args) {
    doSomething(((v1, v2) -> {
      if (v1 == null || v2 == null) {
        return null;
      }
      return v1 + ", " + v2 + "!";
    }));
  }

  public static void doSomething(Operation operation) {
    System.out.println("before");
    String head = "hello";
    String tail = "world";
    String result = operation.operate(head, tail);
    Operation.println(result);
    System.out.println("after");
  }
}

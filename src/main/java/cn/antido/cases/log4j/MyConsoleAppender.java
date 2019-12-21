package cn.antido.cases.log4j;

import org.apache.log4j.ConsoleAppender;

public class MyConsoleAppender extends ConsoleAppender {
  {
    System.out.println("in MyConsoleAppender");
  }

}

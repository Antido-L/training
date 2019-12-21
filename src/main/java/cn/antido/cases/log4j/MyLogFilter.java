package cn.antido.cases.log4j;

import org.apache.log4j.spi.Filter;
import org.apache.log4j.spi.LoggingEvent;

import java.util.List;

public class MyLogFilter extends Filter {



  @Override
  public int decide(LoggingEvent event) {
    System.out.println(event.getLocationInformation().getClassName());
    System.out.println(event.getLocationInformation().getMethodName());
    System.out.println(event.getMDC("a"));
    System.out.println(event.getLogger());
    System.out.println(event.getRenderedMessage());

    return 0;
  }
}

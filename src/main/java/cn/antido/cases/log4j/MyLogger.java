package cn.antido.cases.log4j;

import lombok.extern.slf4j.Slf4j;
import org.apache.log4j.LogManager;
import org.slf4j.MDC;
import org.slf4j.MarkerFactory;

@Slf4j
public class MyLogger {

  //private static final Logger log = LoggerFactory.getLogger(MyLogger.class);

  public static void main(String[] args) {
    org.apache.log4j.Logger logger = LogManager.getLogger(MyLogger.class.getName());

    MDC.put("a","b");
    log.info(MarkerFactory.getMarker("name"),"main");
    fun();
  }


  public static void fun() {
    log.info(MarkerFactory.getMarker("name"),"fun");
  }
}

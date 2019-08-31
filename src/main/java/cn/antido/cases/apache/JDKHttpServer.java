package cn.antido.cases.apache;



import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;

/**
 * JDK HTTP SERVER
 * 不怎么用
 * https://docs.oracle.com/javase/9/docs/api/com/sun/net/httpserver
 */
public class JDKHttpServer {
  public static void main(String[] args) throws IOException {
    InetSocketAddress addr = new InetSocketAddress(30000);
    HttpServer server = HttpServer.create(addr, 0);

    server.createContext("/", new HttpHandler() {
      @Override
      public void handle(HttpExchange exchange) throws IOException {
        Headers responseHeaders = exchange.getResponseHeaders();
        responseHeaders.set("Content-Type", "text/plain");
        exchange.sendResponseHeaders(200, 0);
        OutputStream responseBody = exchange.getResponseBody();
        Headers requestHeaders = exchange.getRequestHeaders();
        Set<String> keySet = requestHeaders.keySet();
        Iterator<String> iter = keySet.iterator();
        while (iter.hasNext()) {
          String key = iter.next();
          List values = requestHeaders.get(key);
          System.out.println(values);
        }

        responseBody.write("JDK http server".getBytes());
        responseBody.flush();
        responseBody.close();
      }
    });
    server.setExecutor(Executors.newCachedThreadPool());
    server.start();
  }
}

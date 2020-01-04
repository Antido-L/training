package cn.antido.cases.jetty;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Jetty
 */
@Slf4j
public class JettyServer {
  private static final int PORT = 60001;

  public static void main(String[] args) throws Exception {
    Server server = new Server(PORT);
    ServletHandler servletHandler = new ServletHandler();
    servletHandler.addServletWithMapping(HelloServlet.class, "/");
    server.setHandler(servletHandler);
    server.start();
  }

  public static class HelloServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
      try (PrintWriter writer = resp.getWriter()) {
        writer.println("hello world");
      } catch (Exception e) {
        log.error("write response failed. status:", e);
      }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
      try (PrintWriter writer = resp.getWriter()) {
        writer.println("hello world");
      } catch (Exception e) {
        log.error("write response failed. status:", e);
      }
    }
  }
}

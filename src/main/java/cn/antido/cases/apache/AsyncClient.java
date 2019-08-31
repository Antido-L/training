package cn.antido.cases.apache;

import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.nio.reactor.ConnectingIOReactor;
import org.apache.http.nio.reactor.IOReactorException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * 异步的 Apache http client
 * TODO: 有待补充
 */
public class AsyncClient {

  public static CloseableHttpAsyncClient httpAsyncClient;

  public static void main(String[] args) throws IOException {
    init();
    for (int i = 0; i < 10; i++) {
      doPost("http://10.42.72.26:8106/sa");
    }
  }

  private static void doPost(String uri) throws UnsupportedEncodingException {
    HttpPost httpPost = new HttpPost(uri);
    httpPost.setEntity(new StringEntity("hello"));
    httpAsyncClient.execute(httpPost, new RequestCallBack());
  }

  private static void init() throws IOReactorException {
    RequestConfig requestConfig = RequestConfig.custom().
        setConnectTimeout(10 * 1000).  // 连接超时,连接建立时间,三次握手完成时间
        setSocketTimeout(30 * 1000). // 请求超时,数据传输过程中数据包之间间隔的最大时间
        build();

    ConnectingIOReactor ioReactor = new DefaultConnectingIOReactor();
    PoolingNHttpClientConnectionManager connManager =
        new PoolingNHttpClientConnectionManager(ioReactor);
    connManager.setMaxTotal(10); // 连接池大小 10
    connManager.setDefaultMaxPerRoute(10); // 每个 host 最多 10 个连接

    httpAsyncClient = HttpAsyncClients.custom().setConnectionManager(connManager)
        .setDefaultRequestConfig(requestConfig).build();
    httpAsyncClient.start();
  }

  static class RequestCallBack implements FutureCallback<HttpResponse> {
    @Override
    public void completed(HttpResponse response) {
      System.out.println(response);
    }

    @Override
    public void failed(Exception e) {

    }

    @Override
    public void cancelled() {

    }
  }
}

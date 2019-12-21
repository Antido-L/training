package cn.antido.cases.apache;

import org.apache.http.HttpRequest;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Apache Http client
 * http://hc.apache.org
 */
public class Client {
  public static void main(String[] args) throws IOException {
    // 同步的一般就用这种就行
    CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
    String uri = "http://10.42.2.239:8106/sa";
    HttpRequestBase request = doPost(uri);
    for (int i = 0; i < 10; i++) {
      new Thread(() -> {
        // 这个 response 是需要关闭，如果不设置超时时间，response 会保持 socket 链接等数据
        try (CloseableHttpResponse response = closeableHttpClient.execute(request)) {
          if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            // EntityUtils 用于处理各种 Entity
            // EntityUtils.consume(entity) 这个也可以用来关闭 response
            String result = EntityUtils.toString(response.getEntity());
            System.out.println(result);
          } else {
            // do something
          }
        } catch (IOException e) {
          // do something
          e.printStackTrace();
        }
      }).start();
    }
    // 这也是 Apache client 的一种关闭方法，具体实现不明，Apache client 的 close 真的令人头大
    request.releaseConnection();
    // 这个 client 不用了需要关闭
    closeableHttpClient.close();
  }

  private static RequestConfig requestConfig(HttpRequest request) {
    // 设置各种 Header
    request.addHeader("User-Agent", "Apache closeable http client");
    request.addHeader("Accept-Charset", StandardCharsets.UTF_8.toString());

    // 设置各种请求配置
    return RequestConfig.custom().setMaxRedirects(3) // 最大重定向次数
        .setConnectTimeout(1000 * 10) // 连接超时
        .setSocketTimeout(1000 * 30).build();
  }

  private static HttpRequestBase doPost(String uri) {
    HttpPost post = new HttpPost(uri);
    post.setConfig(requestConfig(post));

    // 有各种 Entity
    StringEntity stringEntity = new StringEntity("Post contents", StandardCharsets.UTF_8);
    post.setEntity(stringEntity);
    return post;
  }

  private static HttpRequestBase doGet(String uri) {
    HttpGet get = new HttpGet(uri);
    get.setConfig(requestConfig(get));
    return get;
  }

  public static HttpRequestBase postForm(String uri) throws IOException {
    HttpPost post = new HttpPost(uri);
    post.setConfig(requestConfig(post));
    List<NameValuePair> list = new ArrayList<>();
    list.add(new BasicNameValuePair("name", "antido"));
    list.add(new BasicNameValuePair("age", "18"));
    post.setEntity(new UrlEncodedFormEntity(list, "UTF-8"));
    return post;
  }

  public static HttpRequestBase postJSON(String uri) throws IOException {
    HttpPost post = new HttpPost(uri);
    post.setConfig(requestConfig(post));
    List<NameValuePair> list = new ArrayList<>();
    list.add(new BasicNameValuePair("name", "antido"));
    list.add(new BasicNameValuePair("age", "18"));
    post.setEntity(new UrlEncodedFormEntity(list, "UTF-8"));
    String body = "{\"lib\":{\"$lib\":\"Java\",\"$lib_method\":\"code\",\"$lib_version\":\"3.1.10\",\"$lib_detail\":\"cn.antidio.LogAgentData##main##LogAgentData.java##23\"},\"distinct_id\":\"antido\",\"time\":1566360065770,\"type\":\"track\",\"event\":\"checkIp\",\"properties\":{\"$lib\":\"Java\",\"$ip\":\"127.0.0.1\",\"$is_login_id\":true,\"$lib_version\":\"3.1.10\"}}";
    post.setEntity(new StringEntity(body, "UTF-8"));
    post.setHeader("content-type", "application/json");
    return post;
  }
}

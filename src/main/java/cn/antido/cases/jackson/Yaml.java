package cn.antido.cases.jackson;


import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * jackson 的 yaml 解析功能
 */
public class Yaml {
  private static YAMLFactory factory = new YAMLFactory();

  public static void main(String[] args) throws Exception {
    ObjectMapper mapper = new ObjectMapper(factory);
    Map<String, String> map = new HashMap<>();
    map.put("name", "antido");
    map.put("age", "18");
    System.out.println();

    Config config = new Config();
    config.setName("Antido");
    // 将对象转换为 yaml 字符串
    String mapStr = mapper.writeValueAsString(map);
    String configStr = mapper.writeValueAsString(config);
    System.out.println(mapStr);
    System.out.println(configStr);

    File file = new File("/Users/antido/tmp/easy.conf");
    System.out.println(file.createNewFile());
    PrintWriter writer = new PrintWriter(new FileOutputStream(file));
    writer.print(configStr);
    writer.close();

    // 从文件中读取 yaml 并转换为指定对象
    try (YAMLGenerator generator = factory.createGenerator(new FileOutputStream("/Users/antido/tmp/easy.conf"),
        JsonEncoding.UTF8)) {
      generator.useDefaultPrettyPrinter();
      Config result = new Config();
      generator.writeObject(config);
      System.out.println(result);
    }
  }

  @Getter
  @Setter
  @ToString
  static class Config implements Serializable {
    String name = "config";
    Integer age = 1;
    Job job = new Job("earth", "sensors");
    List<String> books = Arrays.asList("a", "b", "c");


    @Getter
    @Setter
    static class Job implements Serializable {
      String address;
      String name;

      public Job(String address, String name) {
        this.address = address;
        this.name = name;
      }
    }
  }

}

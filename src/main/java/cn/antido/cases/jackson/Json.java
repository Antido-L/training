package cn.antido.cases.jackson;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Jackson json 相关的简单示例
 * TODO: 待补充
 */
public class Json {
  public static void main(String[] args) throws IOException {
    simple();
    tree();
  }

  private static void tree() throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();
    String jsonStr = "{\"name\":\"Antido\", \"age\":21, \"pack\":[\"book\", \"phone\"]}";
    JsonNode jsonNode = objectMapper.readTree(jsonStr);
    System.out.println(jsonNode.get("name").asText());
    System.out.println(jsonNode.get("pack").get(1).asText());

    ArrayNode arrayNode = objectMapper.createArrayNode();
    arrayNode.add(1).add(2).add(3).add(4);
    ((ObjectNode)jsonNode).replace("array",arrayNode);
    ((ObjectNode)jsonNode).put("address","beijing");
    System.out.println(jsonNode.toString());
    // get 方法拿不到时返回 null, path 方法拿不到时返回 MissingNode
    System.out.println(jsonNode.get("notExist"));
    System.out.println(jsonNode.path("notExist").isMissingNode());

    Iterator<JsonNode> elements = jsonNode.elements();
    while (elements.hasNext()) {
      System.out.println(elements.next());
    }

  }

  private static void simple() throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();
    String jsonStr = "{\"name\":\"Antido\", \"age\":21, \"pack\":[\"book\", \"phone\"]}";
    // 将字符串转为 Bean
    People people = objectMapper.readValue(jsonStr, People.class);
    System.out.println(people);
    // 该方法可直接读文件
    //People people = objectMapper.readValue(new File("/tmp/people"), People.class);
    people.setAge(18);
    // 将 Bean 转成 JSON
    String peopleStr = objectMapper.writeValueAsString(people);
    System.out.println(peopleStr);
    // 直接写文件
    //objectMapper.writeValue(new File("/tmp/people"), people);

    HashMap<String, Object> map = new HashMap<>();
    map.put("list", Collections.singletonList(123));
    map.put("null",null);
    map.put("people",people);
    System.out.println(objectMapper.writeValueAsString(map));
  }



  @Setter
  @Getter
  @ToString
  static class People {
    String name;
    Integer age;
    List<String> pack;
  }
}

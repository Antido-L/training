package cn.antido.cases.java8;

import java.util.HashMap;
import java.util.Map;

/**
 * Map 新特性
 */
public class MapCase {
  public static void main(String[] args) {
    Map<Integer, String> map = new HashMap<>();
    for (int i = 0; i < 10; i++) {
      map.put(i, "value-" + i);
    }

    /**
     * foreach
     * 遍历
     */
    map.forEach((key, value) -> System.out.println(key + ":" + value));

    /**
     * getOrDefault
     * 获取 key 的值, 如果 key 不存在则用 defaultValue
     */
    System.out.println("3: " + map.getOrDefault(3, "value-"));

    /**
     * putIfAbsent
     * 根据 key 匹配, 如果匹配不到则增加 key-value, 返回 null, 如果匹配到, 如果 oldValue 不等于 null 则不进行 value 覆盖，返回 oldValue
     */
    System.out.println(map.putIfAbsent(3, "value-x")); // value-3
    System.out.println(map.putIfAbsent(100, "value-100")); // null

    /**
     * remove
     * 根据 key 匹配，如果 value 也相同则删除
     */
    map.remove(1, "value-1");


    /**
     * replace
     * 根据 key 匹配, 如果 value 相同则使用 newValue 覆盖返回 true，否则返回 false
     */
    map.replace(11, null, "value-11");

    /**
     * replaceAll
     * 遍历并重新赋值 调用此方法时重写 BiFunction 的 Object apply(Object o, Object o2)方法，
     * 其中 o 为 key，o2 为 value ，根据重写方法逻辑进行重新赋值。
     */
    map.replaceAll((key, value) -> {
      if (key == 2) {
        return value + "add222";
      }
      return value;
    });

    /**
     * compute
     * 根据 key 做匹配，返回做存储的 value 。
     * 如果匹配到做 value 替换，匹配不到则新增，返回值如果为 null 则删除该节点，否则即为要存储的 value
     */
    map.compute(4, (key, value) -> null); // 删除 key == 4 的
    map.compute(10, (key, value) -> { // 将 key == 10 全变大写
      System.out.println(value);
      return value == null ? "NULL" : value.toUpperCase();
    });

    /**
     * 功能大部分与 compute 相同，不同之处在于入参为 oldValue、value，
     * 调用 merge 时根据两个 value 进行逻辑处理并返回 value。
     */
    map.merge(3, "newValue", (value, newValue) -> newValue.compareTo(value) < 0 ? value : newValue);

    /**
     * 根据 key 做匹配 Node（匹配不到则新建）
     * 匹配到了返回 value
     */
    map.computeIfAbsent(14, key -> "value-" + key);

    /**
     * 根据 key 做匹配，如果匹配不上则返回 null ,匹配上根据返回值覆盖。
     * 可以根据 key 和 oldValue 进行逻辑处理，返回值如果为 null 则删除该节点
     */
    map.computeIfPresent(10, (key, value) -> "new-10");

    /**
     * 做下比较
     * compute：根据 key 做匹配，key、value 为参数，匹配到做 value 替换，匹配不到新增 value 返回值为 null 则删除该节点。
     * merge：oldValue、newValue 作为为参数，其它功能于 compute 类似
     * computeIfAbsent：根据 key 匹配，参数为 key , 存在且 value 不为 null，不做修改，为 null 用返回值作为 value，不存在则新增
     * computeIfPresent：key、value 作为参数，匹配到原来的值为 null 不做操作，否则返回值作为新的 value 覆盖，匹配不到不做操作；返回值为 null 删除该节点
     */
  }
}

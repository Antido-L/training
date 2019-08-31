package cn.antido.cases;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Google Guava
 */
public class Guava {
  public static void main(String[] args) {
    multiSet();
    multiMap();
    unmodifiable();
  }

  /**
   * unmodifiable 和  ImmutableList 能够使集合变得不可修改
   */
  private static void unmodifiable() {
    List<Integer> list = new ArrayList<Integer>();
    list.add(1);
    list.add(2);
    list = Collections.unmodifiableList(list);
    try {
      list.add(3);
    } catch (UnsupportedOperationException e) {
      System.out.println("list 不能更改了");
    }
    // [1, 2]
    System.out.println(list);


    List<Integer> googleList = new ArrayList<Integer>();
    googleList.add(1);
    googleList.add(2);
    googleList = ImmutableList.copyOf(googleList);
    try {
      googleList.add(3);
    } catch (UnsupportedOperationException e) {
      System.out.println("googleList 也不能更改了");
    }
    // [1, 2]
    System.out.println(googleList);
  }

  /**
   * Multiset 是无序的，但是可以重复的集合
   */
  private static void multiSet() {
    HashMultiset<String> multiset = HashMultiset.create();
    multiset.add("foo");
    multiset.add("foo");
    multiset.add("foo");
    multiset.add("bar");
    multiset.add("bar");
    multiset.add("zoo",5);
    // [bar x 2, foo x 3, zoo x 5]
    System.out.println(multiset);
    // 3
    System.out.println(multiset.count("foo"));
  }

  /**
   * MultiMap 中一个 key 可以对应多个 value
   */
  private static void multiMap() {
    HashMultimap<String, String> multimap = HashMultimap.create();
    multimap.put("a", "123");
    multimap.put("a", "456");
    multimap.put("b", "789");
    // {a=[456, 123], b=[789]}
    System.out.println(multimap);
    Set<String> set = multimap.get("a");
    // [456, 123]
    System.out.println(set);
  }
}

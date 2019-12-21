package cn.antido.cases.re;



import dk.brics.automaton.Automaton;
import dk.brics.automaton.RegExp;

import java.util.ArrayList;
import java.util.Random;


public class Test {
  public static void main(String[] args) {
    String[] randomStringList = getRandomStringList("AABBCCDDEEFFGGHHIIGGKKabcdefghijk0123456789!?,.()~[]+-*/", 10000);
    final int times = 100;

    String re1 = "^((?!^distinct_id$|^original_id$|^time$|^properties$|^id$|^first_id$|^second_id$|^users$|^events$|^event$|^user_id$|^date$|^datetime$)[a-zA-Z_$][a-zA-Z\\d_$]{0,99})$";

    ArrayList<String> regexList = new ArrayList<>();
    //regexList.add(".*a\\d.*");
    //regexList.add(".*A.*(12|CD|0)");
    regexList.add("(c123|\\dG\\d+|012|K*aA|A?c?C\\d+)(.*)j?\\(?\\+*\\~(i|j|c|d|\\[)(.*)\\d(.*)");

    doMatch(randomStringList, times, regexList);
  }

  private static void doMatch(String[] randomStringList, int times, ArrayList<String> regexList) {
    for(String re : regexList) {
      jdkMatch(re, times, randomStringList);
      googleMatch(re, times, randomStringList);
      bricsMatch(re, times, randomStringList);
      florianingerlMatch(re, times, randomStringList);
    }
  }

  private static void florianingerlMatch(String re, int times, String[] randomStringList) {
    int count = 0;
    long time = System.currentTimeMillis();
    com.florianingerl.util.regex.Pattern pattern = com.florianingerl.util.regex.Pattern.compile(re);
    for (int j = 0; j < times; j++) {
      for (String s : randomStringList) {
        if (pattern.matcher(s).matches()) {
          count++;
        }
      }
    }
    System.out.println(re + " " + count + " florianingerl match time: " + (System.currentTimeMillis() - time));
  }

  private static void bricsMatch(String re, int times, String[] randomStringList) {
    int count = 0;
    long time = System.currentTimeMillis();
    RegExp regExp = new RegExp(re);
    Automaton automaton = regExp.toAutomaton();
    for (int j = 0; j < times; j++) {
      for (String s : randomStringList) {
        if (automaton.run(s)) {
          count++;
        }
      }
    }
    System.out.println(re + " " + count + " brics match time: " + (System.currentTimeMillis() - time));
  }


  public static String[] getRandomStringList(String str, int size) {
    String[] strings = new String[size];
    Random random = new Random();
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < size; i++) {
      for (int j = 0; j < str.length(); j++) {
        int number = random.nextInt(str.length());
        sb.append(str.charAt(number));
      }
      strings[i] = sb.toString();
      sb.delete(0, sb.length());
    }
    return strings;
  }


  private static void  jdkMatch(String re, int times, String[] randomStringList) {
    int count = 0;
    java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(re);
    long time = System.currentTimeMillis();
    for (int j = 0; j < times; j++) {
      for (String s : randomStringList) {
        if (pattern.matcher(s).matches()) {
          count++;
        }
      }
    }
    System.out.println(re + " " + count + " jdk match time: " + (System.currentTimeMillis() - time));
  }

  private static void googleMatch(String re, int times, String[] randomStringList) {
    int count = 0;
    com.google.re2j.Pattern pattern = com.google.re2j.Pattern.compile(re);
    long time = System.currentTimeMillis();
    for (int j = 0; j < times; j++) {
      for (String s : randomStringList) {
        if(pattern.matcher(s).matches()) {
          count++;
        }
      }
    }
    System.out.println(re + " " + count + " google match time: " + (System.currentTimeMillis() - time));
  }
}

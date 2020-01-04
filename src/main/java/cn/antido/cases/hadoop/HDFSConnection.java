package cn.antido.cases.hadoop;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.IOException;

public class HDFSConnection {
  public static void main(String[] args) throws IOException {
    listFiles("/sa/lcb/wordcount/output");
  }

  public static void listFiles(String path) throws IOException {
    Configuration conf = new Configuration();
    conf.set("fs.defaultFS", "hdfs://debugboxcreate408x3.sa:8020/sa/lcb/wordcount/output");
    try (FileSystem fs = FileSystem.newInstance(conf)) {
      final FileStatus[] fileStatuses = fs.listStatus(new Path(path));
      for (FileStatus fileStatus : fileStatuses) {
        System.out.println(fileStatus);
        System.out.println(fileStatus.getPath());
      }
    }
  }
}

package cn.antido.cases.hadoop;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;
import java.util.Properties;
import java.util.StringTokenizer;

/**
 * https://hadoop.apache.org/docs/r2.8.5/hadoop-mapreduce-client/hadoop-mapreduce-client-core/MapReduceTutorial.html
 */
public class WordCount1 {

  public static class TokenizerMapper extends Mapper<Object, Text, Text, IntWritable> {

    private final static IntWritable one = new IntWritable(1);
    private Text word = new Text();

    public void map(Object key, Text value, Context context
    ) throws IOException, InterruptedException {
      StringTokenizer itr = new StringTokenizer(value.toString());
      while (itr.hasMoreTokens()) {
        word.set(itr.nextToken());
        context.write(word, one);
      }
    }
  }


  public static class IntSumReducer extends Reducer<Text, IntWritable, Text, IntWritable> {
    private IntWritable result = new IntWritable();

    public void reduce(Text key, Iterable<IntWritable> values,
        Context context
    ) throws IOException, InterruptedException {
      int sum = 0;
      for (IntWritable val : values) {
        sum += val.get();
      }
      result.set(sum);
      context.write(key, result);
    }
  }


  private static final String HDFS_ADDRESS = "hdfs://debugboxcreate408x3.sa:8020";
  private static final String INPUT_ADDRESS = "/sa/lcb/wordcount/input";
  private static final String OUTPUT_ADDRESS = "/sa/lcb/wordcount/output";

  public static void main(String[] args) throws Exception {
    // hadoop 在访问 hdfs的 时候会依次进行权限认证
    // 1、读取 HADOOP_USER_NAME 系统环境变量
    // 2、读取 HADOOP_USER_NAME 这个 java 环境变量
    // 3、从 com.sun.security.auth.NTUserPrincipal 或者 com.sun.security.auth.UnixPrincipal 的实例获取 username
    // 4、抛出异常 LoginException("Can’t find user name")
    Properties properties = System.getProperties();
    properties.setProperty("HADOOP_USER_NAME", "sa_cluster");


    Configuration conf = new Configuration();
    conf.set("fs.defaultFS", HDFS_ADDRESS);
    Job job = Job.getInstance(conf, "word_count");
    job.setJarByClass(WordCount1.class);
    job.setMapperClass(TokenizerMapper.class);
    job.setCombinerClass(IntSumReducer.class);
    job.setReducerClass(IntSumReducer.class);
    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(IntWritable.class);
    FileInputFormat.addInputPath(job, new Path(INPUT_ADDRESS));
    FileOutputFormat.setOutputPath(job, new Path(OUTPUT_ADDRESS));
    job.waitForCompletion(true);

    try (FileSystem fileSystem = FileSystem.newInstance(conf)) {
      FileStatus[] fileStatuses = fileSystem.listStatus(new Path(OUTPUT_ADDRESS));
      for (FileStatus fs : fileStatuses) {
        if (fs.getLen() > 0) {
          FSDataInputStream fr = fileSystem.open(fs.getPath());
          IOUtils.copyBytes(fr, System.out, 1024, true);
        }
      }
    }
  }
}
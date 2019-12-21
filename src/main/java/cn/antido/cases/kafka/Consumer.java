package cn.antido.cases.kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Properties;

/**
 * Kafka Consumer
 */
public class Consumer {
  private static final String BOOTSTRAP_LIST = "debugbox234:9092";
  private static final String GROUP_ID = "antido";
  private static final String TOPIC_NAME = "sa_log_topic";
  private static final long START_OFFSET = 0L;

  private static KafkaConsumer<String, String> kafkaConsumer;

  private static Properties initConfig() {
    Properties properties = new Properties();
    properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_LIST);
    properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
    properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
    properties.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID);
    properties.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, 10000);
    // 消费完成后是否自动提交已经消费的 offset
    properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
    properties.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 1000);
    // 如果没有初始化 offset 从何处消费
    properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
    return properties;
  }

  private static void init() {
    Properties properties = initConfig();
    kafkaConsumer = new KafkaConsumer<>(properties);
    kafkaConsumer.subscribe(Collections.singleton(TOPIC_NAME));
    ArrayList<TopicPartition> list = new ArrayList<>();
    list.add(new TopicPartition(TOPIC_NAME,0));
    list.add(new TopicPartition(TOPIC_NAME,1));

    //kafkaConsumer.seekToBeginning(list);

    // 手工指定 TopicPartition
    // kafkaConsumer.assign(Collections.singleton(new TopicPartition("topic", 1)));

    // 手工指定起始位置
    // kafkaConsumer.seek(new TopicPartition(TOPIC_NAME,0),START_OFFSET);

    // 监控 Rebalance
    /*kafkaConsumer.subscribe(Collections.singleton(TOPIC_NAME), new ConsumerRebalanceListener() {
      @Override
      public void onPartitionsRevoked(Collection<TopicPartition> partitions) {

      }

      @Override
      public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
        // 处理 Rebalance 之后的 partition
      }
    });*/
  }

  public static void main(String[] args) throws InterruptedException {
    init();
    //AutoCommitOffset();
    for (int i = 0; i < 5; i++) {
      new Thread(() -> {
        while (true) {
          ConsumerRecords<String, String> records = poll();

          // Iterator<ConsumerRecord<String, String>> iterator = records.iterator();
          // Set<TopicPartition> partitions = records.partitions();
          // Iterable<ConsumerRecord<String, String>> topic = records.records(TOPIC_NAME);
          // List<ConsumerRecord<String, String>> partitionRecords = records.records(new TopicPartition(TOPIC_NAME, 0));

          for (ConsumerRecord<String, String> record : records) {
            System.out.println("offset: " + record.offset() + " value: " + record.value());

            // 同步提交每条记录的消费

            commit(record);
          }
          // 异步提交当前 consumer 已消费 offset
          // kafkaConsumer.commitAsync();
        }
      }).start();
    }
    Thread.sleep(10000);
    //kafkaConsumer.close();
  }

  private static void AutoCommitOffset() {
    while (true) {
      ConsumerRecords<String, String> records = kafkaConsumer.poll(100);
      for (ConsumerRecord<String, String> record : records) {
        System.out.println("offset: " + record.offset() + " value: " + record.value());
      }
      if (records.isEmpty()) {
        kafkaConsumer.close();
        break;
      }
    }
  }

  private synchronized static ConsumerRecords<String, String> poll() {
    ConsumerRecords<String, String> records = kafkaConsumer.poll(100);
    return records;
  }

  private synchronized static void commit(ConsumerRecord<String, String> record) {
    kafkaConsumer.commitSync(Collections.singletonMap(
        new TopicPartition(record.topic(), record.partition()),
        new OffsetAndMetadata(record.offset())));
  }

}

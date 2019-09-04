package cn.antido.cases.kafka;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Kafka Producer
 */
public class Producer {
  private static final Logger logger = LoggerFactory.getLogger(Producer.class);

  private static final String TOPIC = "sa_log_topic";
  private static final int PARTITION_NUM = 2;
  // 需要配置下 hosts 细节未知
  private static final String BROKER_LIST = "10.42.2.239:9092";
  private static KafkaProducer<String, String> kafkaProducer;

  private static void init() {
    Properties configs = initConfig();
    kafkaProducer = new KafkaProducer<>(configs);
  }

  private static Properties initConfig() {
    Properties properties = new Properties();
    properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BROKER_LIST);
    properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
    properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
    return properties;
  }

  public static void main(String[] args) throws ExecutionException, InterruptedException, TimeoutException {
    init();
    // Future 存放每个 Partition 中最后一条数据的发送状态
    Future<RecordMetadata>[] partitionRecord = new Future[PARTITION_NUM];
    ProducerRecord<String, String> record;
    for (int i = 0; i < 10; i++) {
      int partition = i % PARTITION_NUM;
      /**
       * partition 和 Key 都是可选的。
       * 当不传入 partition 时，默认使用 key 哈希进行分区。
       * 当 key == null 时，每隔 topic.metadata.refresh.interval.ms 随机选择一次分区。
       */
      record = new ProducerRecord<>(TOPIC, partition, "key", "value : " + i);
      // 异步发用回调
      partitionRecord[partition] = kafkaProducer.send(record, new SendCallBack());
      // 同步发用 Future
      // Future<RecordMetadata> send = kafkaProducer.send(record);
    }
    for (Future<RecordMetadata> future : partitionRecord) {
      // 该方法会阻塞线程，等待返回，超时则抛出异常
      RecordMetadata recordMetadata = future.get(10, TimeUnit.SECONDS);
      System.out.println("last: " + recordMetadata.offset());
    }
    kafkaProducer.close();
  }

  /**
   * 发送异步发送后的回调
   */
  static class SendCallBack implements Callback {

    @Override
    public void onCompletion(RecordMetadata recordMetadata, Exception e) {
      if (e != null) {
        logger.error("发送失败！", e);
        return;
      }
      System.out.println("partition: " + recordMetadata.partition());
      System.out.println("timestamp: " + recordMetadata.timestamp());
      System.out.println("offset: " + recordMetadata.offset());
    }
  }

}

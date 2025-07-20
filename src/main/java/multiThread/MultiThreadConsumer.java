package multiThread;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultiThreadConsumer {
    public static void main(String[] args) {
        Properties properties = new Properties();

        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:9092"); //broker kafka
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG,"alex-group"); //identificador del consumer group
        properties.setProperty(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG,"true"); //commit auto
        properties.setProperty(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG,"1000"); //tiempo de commit
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.StringDeserializer");
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.StringDeserializer");

        ExecutorService executorService = Executors.newFixedThreadPool(5);

        for (int i = 0; i < 5; i++) {
            ThreadConsumer consumer = new ThreadConsumer(new KafkaConsumer<>(properties));
            executorService.execute(consumer);
        }
        while (!executorService.isTerminated());
    }
}

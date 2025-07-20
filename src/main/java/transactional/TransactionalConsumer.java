package transactional;

import consumers.Consumers;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

public class TransactionalConsumer {
    private static final Logger log = LoggerFactory.getLogger(TransactionalConsumer.class);
    public static void main(String[] args) {
        Properties properties = new Properties();

        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:9092"); //broker kafka
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG,"alex-group"); //identificador del consumer group
        properties.setProperty(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG,"true"); //commit auto
        properties.setProperty(ConsumerConfig.ISOLATION_LEVEL_CONFIG,"read_committed"); //solo traemos los msj a los que se le hizo commit
        properties.setProperty(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG,"1000"); //tiempo de commit
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.StringDeserializer");
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.StringDeserializer");

        try (KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties)) {
            consumer.subscribe(Arrays.asList("alex-topic"));
            while (true) {
                //trae los mensajes que haya en el tiempo establecido
                ConsumerRecords<String, String> consumerRecords = consumer.poll(Duration.ofMillis(100));
                for (ConsumerRecord<String, String> consumerRecord : consumerRecords) {
                    log.info("Offset = ´{}, Partition = {}, Key = {}, Value = {} ",
                            consumerRecord.offset(), consumerRecord.partition(), consumerRecord.key(), consumerRecord.value());
                }
            }
        }
    }
}

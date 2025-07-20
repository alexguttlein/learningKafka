package producers;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Properties;


public class Producers {

    public static final Logger log = LoggerFactory.getLogger(Producers.class);
    public static void main(String[] args) {

        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092"); //broker de conexion de kafka
        properties.put(ProducerConfig.ACKS_CONFIG, "1"); //acknowledge de que los nodos recibieron el mensaje
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.StringSerializer");
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.StringSerializer");

        //parametros extra para mejorar performance
        properties.put(ProducerConfig.LINGER_MS_CONFIG, "20");
        properties.put(ProducerConfig.BATCH_SIZE_CONFIG,"1000");
        properties.put(ProducerConfig.BUFFER_MEMORY_CONFIG,"20000");

        long startTime = System.currentTimeMillis();

        try (Producer<String, String> producer = new KafkaProducer<>(properties);) {

            for (int i = 0; i < 1000000; i++) {
                //producer asincrono (mejor performance)
                producer.send(new ProducerRecord<String, String>("alex-topic", String.valueOf(i), "alex-value"));

                //producer sincrono (menor performance pero se envia en orden)
//                producer.send(new ProducerRecord<String, String>("alex-topic", String.valueOf(i), "alex-value")).get();
            }
            producer.flush();
//        } catch (Exception e) {
//            log.error("Message producer interrupted ", e);
        }
        log.info("Processing time = {} ms", (System.currentTimeMillis() - startTime));
    }
}

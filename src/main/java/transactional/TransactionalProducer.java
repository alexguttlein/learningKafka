package transactional;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import producers.Producers;

import java.util.Properties;

public class TransactionalProducer {
    public static final Logger log = LoggerFactory.getLogger(TransactionalProducer.class);
    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092"); //broker de conexion de kafka
        properties.put(ProducerConfig.ACKS_CONFIG, "all"); //en transacciones debe ser all
        properties.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG, "alex-producer-id"); //en transacciones debe ser all
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.StringSerializer");
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.StringSerializer");

        //parametros extra para mejorar performance
        properties.put(ProducerConfig.LINGER_MS_CONFIG, "20");
        properties.put(ProducerConfig.BATCH_SIZE_CONFIG,"1000");
        properties.put(ProducerConfig.BUFFER_MEMORY_CONFIG,"20000");

        long startTime = System.currentTimeMillis();

        try (Producer<String, String> producer = new KafkaProducer<>(properties);) {
            try {
                producer.initTransactions();
                producer.beginTransaction();
                for (int i = 0; i < 100000; i++) {
                    producer.send(new ProducerRecord<String, String>("alex-topic", String.valueOf(i), "alex-value"));

                    //se fuerza un error para probar comportamiento
//                    if (i == 50000) {
//                        throw new Exception("Unexpected exception");
//                    }
                }
                producer.commitTransaction();
                producer.flush();
            } catch (Exception e) {
                log.error("Error: ", e);
                producer.abortTransaction();
            }
        }
        log.info("Processing time = {} ms", (System.currentTimeMillis() - startTime));
    }
}

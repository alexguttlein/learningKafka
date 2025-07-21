package callbacks;

import org.apache.kafka.clients.producer.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import producers.Producers;

import java.util.Properties;

/*
 * Los callbacks se usan cuando se necesita realizar una accion cuando el mensaje fue entregado
 */

public class CallbackProducer {
    public static final Logger log = LoggerFactory.getLogger(Producers.class);
    public static void main(String[] args) {
        Properties properties = getProperties();

        long startTime = System.currentTimeMillis();

        try (Producer<String, String> producer = new KafkaProducer<>(properties);) {

            for (int i = 0; i < 10000; i++) {
                //sendCallbackWithAnonymousClass(producer, i);
                sendCallbackWithLambda(producer, i);
            }
            producer.flush();
        }
        log.info("Processing time = {} ms", (System.currentTimeMillis() - startTime));
    }

    private static void sendCallbackWithLambda(Producer<String, String> producer, int i) {
        producer.send(new ProducerRecord<String, String>("alex-topic", String.valueOf(i), "alex-value"),
                (recordMetadata, exception) -> {
                    if (exception != null) {
                        log.info("There was an error {} ", exception.getMessage());
                    }
                    log.info("Offset = {}, Partition = {}, Topic {}",
                            recordMetadata.offset(), recordMetadata.partition(), recordMetadata.topic());
                });
    }

    private static void sendCallbackWithAnonymousClass(Producer<String, String> producer, int i) {
        producer.send(new ProducerRecord<String, String>("alex-topic", String.valueOf(i), "alex-value"), new Callback() {
            @Override
            public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                if (e != null) {
                    log.info("There was an error {} ", e.getMessage());
                }
                log.info("Offset = {}, Partition = {}, Topic {}",
                        recordMetadata.offset(), recordMetadata.partition(), recordMetadata.topic());
            }
        });
    }

    private static Properties getProperties() {
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092"); //broker de conexion de kafka
        properties.put(ProducerConfig.ACKS_CONFIG, "all"); //acknowledge de que los nodos recibieron el mensaje
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.StringSerializer");
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.StringSerializer");

        //parametros extra para mejorar performance
        properties.put(ProducerConfig.LINGER_MS_CONFIG, "20");
        properties.put(ProducerConfig.BATCH_SIZE_CONFIG,"1000");
        properties.put(ProducerConfig.BUFFER_MEMORY_CONFIG,"20000");

        return properties;
    }
}

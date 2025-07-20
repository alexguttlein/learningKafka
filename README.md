Comandos utilizados (se deben ejecutar en la carpeta donde se tiene descomprimido kafka):

Iniciar Zookeeper:
bin/zookeeper-server-start.sh config/zookeeper.properties

Iniciar Kafka:
bin/kafka-server-start.sh config/server.properties

Correr docker de kafka (opcion si se usa docker):
docker run -p 9092:9092 apache/kafka:4.0.0

Crear un nuevo topic:
bin/kafka-topics.sh --bootstrap-server localhost:9092 --create --topic alex-topic -partitions 5 --replication-factor 1

Listar topics creados:
bin/kafka-topics.sh --bootstrap-server localhost:9092 --list

Crear Producer
bin/kafka-console-producer.sh --topic alex-topic --bootstrap-server localhost:9092

Crear Consumer
bin/kafka-console-consumer.sh --topic alex-topic --from-beginning --bootstrap-server localhost:9092 --property print.key=true --property key.separator="-"

Modificar Topic
bin/kafka-topics.sh --bootstrap-server localhost:9092 --alter --topic alex-topic\ --partitions 5
Comandos utilizados (se deben ejecutar en la carpeta donde se tiene descomprimido kafka):

Iniciar Zookeeper:
bin/zookeeper-server-start.sh config/zookeeper.properties

Iniciar Kafka:
bin/kafka-server-start.sh config/server.properties

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

Eliminar Topic
bin/kafka-topics.sh --bootstrap-server localhost:9092 --delete --topic alex-topic


*** DOCKER ***
Levantar docker
docker-compose up -d

Bajar docker
docker-compose down

Crear topic
docker exec kafka kafka-topics --create --topic mi-topic --bootstrap-server localhost:9092 --partitions 1 --replication-factor 1

Listar topics
docker exec kafka kafka-topics --list --bootstrap-server localhost:9092

Modificar topic
docker exec kafka kafka-topics --alter --topic alex-topic --partitions 5 --bootstrap-server localhost:9092

Eliminar topic
docker exec kafka kafka-topics --delete --topic mi-topic --bootstrap-server localhost:9092

Producir mensaje
docker exec -it kafka kafka-console-producer --topic mi-topic --bootstrap-server localhost:9092

Consumir mensaje
docker exec -it kafka kafka-console-consumer --topic mi-topic --bootstrap-server localhost:9092 --from-beginning


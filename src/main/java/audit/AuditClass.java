package audit;

import org.apache.kafka.clients.producer.KafkaProducer;

import java.util.Properties;

abstract class AuditClass {
   private Properties properties = new Properties();
   private Properties errorProperties = new Properties();
   private KafkaProducer kafkaProducer;
   private KafkaProducer errorKafkaProducer;

    protected AuditClass() {
        properties.put("bootstrap.servers", "localhost:9092");
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer", "org.springframework.kafka.support.serializer.JsonSerializer");
        kafkaProducer = new KafkaProducer<>(properties);

        errorProperties.put("bootstrap.servers", "localhost:9092");
        errorProperties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        errorProperties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        errorKafkaProducer = new KafkaProducer<>(errorProperties);
    }

    protected KafkaProducer getErrorKafkaProducer() {
        return errorKafkaProducer;
    }

    protected KafkaProducer getKafkaProducer() {
        return kafkaProducer;
    }
}

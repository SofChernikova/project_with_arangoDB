package audit;

import com.arangodb.internal.audit.Audit;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class VertexAudit extends AuditClass implements Audit {
    private static final Logger logger = LoggerFactory.getLogger(VertexAudit.class);

    private final String topicName;
    private final String errorsTopicName;
    private final KafkaProducer kafkaProducer;
    private final KafkaProducer errorKafkaProducer;
    private List<Object> internalStorage = new ArrayList<>();
    private ProducerRecord producerRecord;
    private ProducerRecord errorProducerRecord;

    public VertexAudit(String topicName, String errorsTopicName) {
        super();
        this.topicName = topicName;
        this.kafkaProducer = super.getKafkaProducer();
        this.errorKafkaProducer = super.getErrorKafkaProducer();
        this.errorsTopicName = errorsTopicName;
    }


    @Override
    public <T> void insert(T t) {
        try {
            producerRecord = new ProducerRecord<>(topicName, Integer.toString(t.hashCode()), t);
            kafkaProducer.send(producerRecord);
        } catch (Exception e) {
            errors(e);
            logger.info("Message: " + e.getMessage() + "cause: " + e.getCause());
        } finally {
            internalStorage.add(t);
            logger.info(t.toString() + " added to the internal storage");
        }
    }

    @Override
    public <T> void replace(T t) {
        try {
            producerRecord = new ProducerRecord<>(topicName, Integer.toString(t.hashCode()), t);
            kafkaProducer.send(producerRecord);
        } catch (Exception e) {
            errors(e);
            logger.info("Message: " + e.getMessage() + "cause: " + e.getCause());
        } finally {
            internalStorage.add(t);
            logger.info(t.toString() + " added to the internal storage");
        }
    }

    @Override
    public <T> void update(T t) {
        try {
            producerRecord = new ProducerRecord<>(topicName, Integer.toString(t.hashCode()), t);
            kafkaProducer.send(producerRecord);
        } catch (Exception e) {
            errors(e);
            logger.info("Message: " + e.getMessage() + "cause: " + e.getCause());
        } finally {
            internalStorage.add(t);
            logger.info(t.toString() + " added to the internal storage");
        }
    }

    @Override
    public void errors(Exception e) {
        errorProducerRecord = new ProducerRecord<>(errorsTopicName, Integer.toString(e.hashCode()), e.getMessage());
        errorKafkaProducer.send(errorProducerRecord);
    }
}

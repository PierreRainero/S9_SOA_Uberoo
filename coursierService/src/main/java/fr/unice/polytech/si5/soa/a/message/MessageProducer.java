package fr.unice.polytech.si5.soa.a.message;

import fr.unice.polytech.si5.soa.a.communication.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;

/**
 * Class MessageProducer
 *
 * @author Joël CANCELA VAZ
 */
public class MessageProducer {

	@Autowired
	private KafkaTemplate<String, Message> kafkaTemplate;

	@Value(value = "${message.topic.name}")
	private String topicName;

	public MessageProducer() {
	}

	public void sendMessage(Message message) {
		kafkaTemplate.send(topicName, message);
	}
}

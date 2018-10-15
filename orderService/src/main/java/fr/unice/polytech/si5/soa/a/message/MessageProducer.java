package fr.unice.polytech.si5.soa.a.message;

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
	private KafkaTemplate<String, String> kafkaTemplate;

	@Value(value = "${message.topic.name}")
	private String topicName;

	public MessageProducer() {
		System.out.println(topicName);
	}

	public void sendMessage(String message) {
		kafkaTemplate.send(topicName, message);
	}
}

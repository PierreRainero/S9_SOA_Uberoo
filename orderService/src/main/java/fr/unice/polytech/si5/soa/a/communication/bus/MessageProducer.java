package fr.unice.polytech.si5.soa.a.communication.bus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;

import fr.unice.polytech.si5.soa.a.communication.bus.messages.Message;

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

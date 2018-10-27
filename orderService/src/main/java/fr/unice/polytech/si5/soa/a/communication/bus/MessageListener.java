package fr.unice.polytech.si5.soa.a.communication.bus;

import org.springframework.kafka.annotation.KafkaListener;

import java.util.concurrent.CountDownLatch;

/**
 * Class MessageListener
 *
 * @author Joël CANCELA VAZ
 */
public class MessageListener {

	private CountDownLatch latch = new CountDownLatch(3);

	@KafkaListener(topics = "${message.topic.name}", containerFactory = "topicKafkaListenerContainerFactory")
	public void listenGroupOrder(Message message) {
		System.out.println("Received Message in group 'order': " + message);
		//TODO màj base payment
		latch.countDown();
	}

	public CountDownLatch getLatch() {
		return latch;
	}
}

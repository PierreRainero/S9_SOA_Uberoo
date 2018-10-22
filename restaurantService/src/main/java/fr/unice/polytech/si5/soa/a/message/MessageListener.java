package fr.unice.polytech.si5.soa.a.message;

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
	public void listenGroupFoo(String message) {
		System.out.println("Received Message in group 'topic': " + message);
		latch.countDown();
	}

	public CountDownLatch getLatch() {
		return latch;
	}
}

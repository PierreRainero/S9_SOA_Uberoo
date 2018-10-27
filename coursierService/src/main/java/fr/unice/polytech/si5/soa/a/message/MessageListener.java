package fr.unice.polytech.si5.soa.a.message;

import fr.unice.polytech.si5.soa.a.communication.PaymentConfirmation;
import org.springframework.kafka.annotation.KafkaListener;

import java.util.concurrent.CountDownLatch;

/**
 * Class MessageListener
 *
 * @author Joël CANCELA VAZ
 */
public class MessageListener {

	private CountDownLatch latch = new CountDownLatch(3);

	@KafkaListener(topics = "${message.topic.name}", containerFactory = "paymentContainerFactory")
	public void listenNewPayment(PaymentConfirmation message) {
		System.out.println("Received new payment for coursier: " + message);
		//TODO: do something with the payment confirmation .i.e. update db
		latch.countDown();
	}

	public CountDownLatch getLatch() {
		return latch;
	}
}

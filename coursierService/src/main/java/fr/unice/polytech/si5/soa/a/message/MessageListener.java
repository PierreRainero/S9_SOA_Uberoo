package fr.unice.polytech.si5.soa.a.message;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.unice.polytech.si5.soa.a.communication.Message;
import fr.unice.polytech.si5.soa.a.communication.PaymentConfirmation;
import fr.unice.polytech.si5.soa.a.exceptions.CoursierDoesntGetPaidException;
import fr.unice.polytech.si5.soa.a.exceptions.UnknownDeliveryException;
import fr.unice.polytech.si5.soa.a.services.IDeliveryService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * Class MessageListener
 *
 * @author Joël CANCELA VAZ
 */
public class MessageListener {
	private static Logger logger = LogManager.getLogger(MessageListener.class);
	@Autowired
	private IDeliveryService deliveryService;

	private CountDownLatch latch = new CountDownLatch(3);

	@KafkaListener(topics = {"${coursier.topic.name}"}, containerFactory = "kafkaListenerContainerFactory")
	public void listenMessage(String message) {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		Message messageRead = null;
		try {
			messageRead = objectMapper.readValue(message, Message.class);
		} catch (IOException e) {
			logger.error("Malformed message received " + e.getMessage(), e);
		}
		if (messageRead == null || messageRead.getType() == null)
			return;
		logger.info("Received message of type " + messageRead.getType());
		switch (messageRead.getType()) {
			case "PAYMENT_CONFIRMATION":
				try {
					PaymentConfirmation paymentConfirmation = objectMapper.readValue(message, PaymentConfirmation.class);
					listenPaymentConfirmation(paymentConfirmation);
				} catch (IOException e) {
					logger.error("Malformed PaymentConfirmation " + e.getMessage(), e);
				}
				break;
			default:
				break;
		}
		latch.countDown();
	}

	public void listenPaymentConfirmation(PaymentConfirmation message) {
		System.out.println("Received new payment for coursier: " + message.getId());
		try {
			deliveryService.receiveNewPayment(message);
		} catch (UnknownDeliveryException | CoursierDoesntGetPaidException e) {
			logger.error("Problem while treating payment confirmation " + e.getMessage());
		}
	}

	public CountDownLatch getLatch() {
		return latch;
	}
}

package fr.unice.polytech.si5.soa.a.communication.bus;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.unice.polytech.si5.soa.a.communication.MealDTO;
import fr.unice.polytech.si5.soa.a.communication.bus.messages.Message;
import fr.unice.polytech.si5.soa.a.communication.bus.messages.NewMeal;
import fr.unice.polytech.si5.soa.a.communication.bus.messages.NewRestaurant;
import fr.unice.polytech.si5.soa.a.communication.bus.messages.PaymentConfirmation;
import fr.unice.polytech.si5.soa.a.entities.states.PaymentState;
import fr.unice.polytech.si5.soa.a.exceptions.UnknowPaymentException;
import fr.unice.polytech.si5.soa.a.exceptions.UnknowRestaurantException;
import fr.unice.polytech.si5.soa.a.services.IPaymentService;
import fr.unice.polytech.si5.soa.a.services.IRestaurantService;
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
	private CountDownLatch latch = new CountDownLatch(3);

	@Autowired
	private IPaymentService paymentService;

	@Autowired
	private IRestaurantService restaurantService;

	@KafkaListener(topics = {"${message.topic.name}"}, containerFactory = "kafkaListenerContainerFactory")
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
		logger.info("Received message of type "+messageRead.getType());
		switch (messageRead.getType()) {
			case "NEW_RESTAURANT":
				try {
					NewRestaurant newRestaurant = objectMapper.readValue(message, NewRestaurant.class);
					listenNewRestaurant(newRestaurant);
				} catch (IOException e) {
					logger.error("Malformed NewRestaurant " + e.getMessage(), e);
				}
				break;
			case "NEW_MEAL":
				try {
					NewMeal newMeal = objectMapper.readValue(message, NewMeal.class);
					listenNewMeal(newMeal);
				} catch (IOException e) {
					logger.error("Malformed NewMeal " + e.getMessage(), e);
				}
				break;
			default:
				break;
		}
		latch.countDown();
	}

	@KafkaListener(topics = {"${bank.topic.name}"}, containerFactory = "kafkaListenerContainerFactory")
	public void listenBankMessage(String message) {
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
		logger.info("Received message of type "+messageRead.getType());
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
		System.out.println("Received payment confirmation");

		PaymentState stateOfThePayment;
		if (message.isStatus()) {
			stateOfThePayment = PaymentState.ACCEPTED;
		} else {
			stateOfThePayment = PaymentState.REFUSED;
		}

		try {
			paymentService.updatePaymentStatus(message.getId(), stateOfThePayment);
		} catch (UnknowPaymentException e) {
			logger.error(e.getMessage(), e);
		}
	}

	public void listenNewRestaurant(NewRestaurant message) {
		System.out.println("Received newRestaurant");

		restaurantService.addRestaurant(message.createRestaurant());
	}

	public void listenNewMeal(NewMeal message) {
		System.out.println("Received new Meal");

		MealDTO meal = message.createMeal();
		try {
			restaurantService.addMeal(meal, message.getRestaurantName(), message.getRestaurantAddress());
		} catch (UnknowRestaurantException e) {
			logger.error(e.getMessage(), e);
		}
	}

	public CountDownLatch getLatch() {
		return latch;
	}
}

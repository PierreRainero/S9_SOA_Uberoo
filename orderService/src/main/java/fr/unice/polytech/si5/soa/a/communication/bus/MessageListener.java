package fr.unice.polytech.si5.soa.a.communication.bus;

import fr.unice.polytech.si5.soa.a.communication.MealDTO;
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

	@KafkaListener(topics = "${bank.topic.name}", containerFactory = "bankContainerFactory")
	public void listenPaymentConfirmation(PaymentConfirmation message) {
		System.out.println("Received payment confirmation");
		
		PaymentState stateOfThePayment;
		if(message.isStatus()) {
			stateOfThePayment = PaymentState.ACCEPTED;
		}else {
			stateOfThePayment = PaymentState.REFUSED;
		}
		
		try {
			paymentService.updatePaymentStatus(message.getId(), stateOfThePayment);
		} catch (UnknowPaymentException e) {
			logger.error(e.getMessage(), e);
		}
		
		latch.countDown();
	}

	@KafkaListener(topics = "${message.topic.name}", containerFactory = "restaurantContainerFactory")
	public void listenNewRestaurant(NewRestaurant message) {
		System.out.println("Received newRestaurant");
		
		restaurantService.addRestaurant(message.createRestaurant());
		
		latch.countDown();
	}

	@KafkaListener(topics = "${message.topic.name}", containerFactory = "mealContainerFactory")
	public void listenNewMeal(NewMeal message) {
		System.out.println("Received new Meal");
		
		MealDTO meal = message.createMeal();
		try {
			restaurantService.addMeal(meal, message.getRestaurantName(), message.getRestaurantAddress());
		} catch (UnknowRestaurantException e) {
			logger.error(e.getMessage(), e);
		}
		
		
		latch.countDown();
	}

	public CountDownLatch getLatch() {
		return latch;
	}
}

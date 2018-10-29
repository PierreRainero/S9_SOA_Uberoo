package fr.unice.polytech.si5.soa.a.communication.bus;

import fr.unice.polytech.si5.soa.a.communication.FeedbackDTO;
import fr.unice.polytech.si5.soa.a.communication.RestaurantOrderDTO;
import fr.unice.polytech.si5.soa.a.communication.bus.messages.NewFeedback;
import fr.unice.polytech.si5.soa.a.communication.bus.messages.NewOrder;
import fr.unice.polytech.si5.soa.a.communication.bus.messages.OrderDelivered;
import fr.unice.polytech.si5.soa.a.exceptions.UnknowMealException;
import fr.unice.polytech.si5.soa.a.exceptions.UnknowRestaurantException;
import fr.unice.polytech.si5.soa.a.services.IMealService;
import fr.unice.polytech.si5.soa.a.services.IOrderService;
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
	@Autowired
	private IOrderService orderService;

	@Autowired
	private IMealService mealService;

	@Autowired
	private IRestaurantService restaurantService;

	private CountDownLatch latch = new CountDownLatch(3);

	@KafkaListener(topics = "${message.topic.name}", containerFactory = "newOrderConcurrentKafkaListenerContainerFactory")
	public void listenNewOrder(NewOrder message) {
		System.out.println("A new order has arrived "+message.getAddress());
		RestaurantOrderDTO orderToAdd = new RestaurantOrderDTO();

		try {
			orderToAdd.setRestaurant(restaurantService.findRestaurant(message.getRestaurantName(), message.getRestaurantAddress()));
		} catch (UnknowRestaurantException e) {
			logger.error(e.getMessage(), e);
		}

		try {
			for (String foodName : message.getFood()) {
				orderToAdd.addMeal(mealService.findMealByName(foodName));
			}
		} catch (UnknowMealException e) {
			logger.error(e.getMessage(), e);
		}

		try {
			orderService.addOrder(orderToAdd);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		latch.countDown();
	}
	
	@KafkaListener(topics = "${message.topic.name}", containerFactory = "newFeedbackConcurrentKafkaListenerContainerFactory")
	public void listenNewFeedback(NewFeedback message) {
		System.out.println("A new feedback has arrived");

		FeedbackDTO feedback = message.createFeedback();
		try {
			mealService.addFeedback(feedback, message.getMealName(), message.getRestaurantName(), message.getRestaurantAddress());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		
		latch.countDown();
	}

	@KafkaListener(topics = "${message.topic.name}", containerFactory = "orderDeliveredConcurrentKafkaListenerContainerFactory")
	public void listenOrderDelivered(OrderDelivered orderDelivered) {
		System.out.println("An order has been delivered "+orderDelivered.getAddress());
		//TODO: update db order set delivered and create message process payment in topic coursier
		latch.countDown();
	}

	public CountDownLatch getLatch() {
		return latch;
	}
}

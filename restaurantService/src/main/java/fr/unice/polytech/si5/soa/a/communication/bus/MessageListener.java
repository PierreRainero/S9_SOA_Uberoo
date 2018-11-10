package fr.unice.polytech.si5.soa.a.communication.bus;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.unice.polytech.si5.soa.a.communication.FeedbackDTO;
import fr.unice.polytech.si5.soa.a.communication.RestaurantOrderDTO;
import fr.unice.polytech.si5.soa.a.communication.bus.messages.Message;
import fr.unice.polytech.si5.soa.a.communication.bus.messages.NewFeedback;
import fr.unice.polytech.si5.soa.a.communication.bus.messages.NewOrder;
import fr.unice.polytech.si5.soa.a.communication.bus.messages.OrderDelivered;
import fr.unice.polytech.si5.soa.a.exceptions.UnknowMealException;
import fr.unice.polytech.si5.soa.a.exceptions.UnknowOrderException;
import fr.unice.polytech.si5.soa.a.exceptions.UnknowRestaurantException;
import fr.unice.polytech.si5.soa.a.services.IMealService;
import fr.unice.polytech.si5.soa.a.services.IOrderService;
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
	private IOrderService orderService;

	@Autowired
	private IMealService mealService;

	private CountDownLatch latch = new CountDownLatch(3);

	@KafkaListener(topics = "${message.topic.name}", containerFactory = "kafkaListenerContainerFactory")
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
			case "NEW_ORDER":
				try {
					NewOrder newOrder = objectMapper.readValue(message, NewOrder.class);
					listenNewOrder(newOrder);
				} catch (IOException e) {
					logger.error("Malformed NewOrder " + e.getMessage(), e);
				}
				break;
			case "NEW_FEEDBACK":
				try {
					NewFeedback newFeedback = objectMapper.readValue(message, NewFeedback.class);
					listenNewFeedback(newFeedback);
				} catch (IOException e) {
					logger.error("Malformed NewFeedback " + e.getMessage(), e);
				}
				break;
			case "ORDER_DELIVERED":
				try {
					OrderDelivered orderDelivered = objectMapper.readValue(message, OrderDelivered.class);
					listenOrderDelivered(orderDelivered);
				} catch (IOException e) {
					logger.error("Malformed OrderDelivered " + e.getMessage(), e);
				}
				break;
			default:
				break;
		}
		latch.countDown();
	}

	public void listenNewOrder(NewOrder message) {
		System.out.println("A new order has arrived ");

		RestaurantOrderDTO orderDTO = message.createOrder();
		try {
			orderService.addOrder(orderDTO, message.getFood(), message.getRestaurantName(), message.getRestaurantAddress());
		} catch (UnknowRestaurantException | UnknowMealException e) {
			logger.error(e.getMessage(), e);
		}

	}

	public void listenNewFeedback(NewFeedback message) {
		System.out.println("A new feedback has arrived");

		FeedbackDTO feedback = message.createFeedback();
		try {
			mealService.addFeedback(feedback, message.getMealName(), message.getRestaurantName(), message.getRestaurantAddress());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

	}

	public void listenOrderDelivered(OrderDelivered orderDelivered) {
		System.out.println("An order has been delivered "+orderDelivered.toString());
		try {
			orderService.deliverOrder(orderDelivered.getRestaurantName(), orderDelivered.getRestaurantAddress(), orderDelivered.getDeliveryAddress(),
					orderDelivered.getFood(), orderDelivered.getDate(), orderDelivered.getAccount(), orderDelivered.getAmount());
		} catch (UnknowOrderException | UnknowRestaurantException | UnknowMealException e) {
			logger.error(e.getMessage(), e);
		}
	}

	public CountDownLatch getLatch() {
		return latch;
	}
}

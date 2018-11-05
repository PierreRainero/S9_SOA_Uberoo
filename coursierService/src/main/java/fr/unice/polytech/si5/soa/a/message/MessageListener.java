package fr.unice.polytech.si5.soa.a.message;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.unice.polytech.si5.soa.a.communication.DTO.DeliveryDTO;
import fr.unice.polytech.si5.soa.a.communication.message.Message;
import fr.unice.polytech.si5.soa.a.communication.message.NewOrder;
import fr.unice.polytech.si5.soa.a.communication.message.NewRestaurant;
import fr.unice.polytech.si5.soa.a.communication.message.PaymentConfirmation;
import fr.unice.polytech.si5.soa.a.exceptions.CoursierDoesntGetPaidException;
import fr.unice.polytech.si5.soa.a.exceptions.NoAvailableCoursierException;
import fr.unice.polytech.si5.soa.a.exceptions.UnknownDeliveryException;
import fr.unice.polytech.si5.soa.a.exceptions.UnknownRestaurantException;
import fr.unice.polytech.si5.soa.a.services.IDeliveryService;
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
    @Autowired
    private IDeliveryService deliveryService;
    
    @Autowired
    private IRestaurantService restaurantService;

    private CountDownLatch latch = new CountDownLatch(3);

    @KafkaListener(topics = {"${coursier.topic.name}"}, containerFactory =
            "kafkaListenerContainerFactory")
    public void listenMessageCoursier(String message) {
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


    @KafkaListener(topics = {"${message.topic.name}"}, containerFactory =
            "kafkaListenerContainerFactory")
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
                NewOrder newOrder = null;
                try {
                    newOrder = objectMapper.readValue(message, NewOrder.class);
                    listenNewOrder(newOrder);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case "NEW_RESTAURANT":
                NewRestaurant newRestaurant = null;
                try {
                	newRestaurant = objectMapper.readValue(message, NewRestaurant.class);
                	listenNewRestaurant(newRestaurant);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
        latch.countDown();
    }
    
    public void listenNewRestaurant(NewRestaurant newRestaurant){
    	System.out.println("Received new restaurant for coursier");
    	restaurantService.addRestaurant(newRestaurant.createRestaurant());
    }

    public void listenPaymentConfirmation(PaymentConfirmation message) {
        System.out.println("Received new payment for coursier: " + message.getId());
        try {
            deliveryService.receiveNewPayment(message);
        } catch (UnknownDeliveryException | CoursierDoesntGetPaidException e) {
            logger.error("Problem while treating payment confirmation " + e.getMessage());
        }
    }

    public void listenNewOrder(NewOrder message) {
        System.out.println("Received new order from restaurant : " + message.getRestaurantName());
        DeliveryDTO deliveryDTO = message.createDelivery();
        try {
			deliveryService.addDelivery(deliveryDTO);
		} catch (UnknownRestaurantException | NoAvailableCoursierException e) {
			logger.error("Problem while treating new restaurant message " + e.getMessage());
		}
    }

    public CountDownLatch getLatch() {
        return latch;
    }
}

package fr.unice.polytech.si5.soa.a.message;

import fr.unice.polytech.si5.soa.a.communication.NewOrder;
import fr.unice.polytech.si5.soa.a.communication.RestaurantOrderDTO;
import fr.unice.polytech.si5.soa.a.exceptions.UnknowMealException;
import fr.unice.polytech.si5.soa.a.services.IMealService;
import fr.unice.polytech.si5.soa.a.services.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;

import java.util.concurrent.CountDownLatch;

/**
 * Class MessageListener
 *
 * @author Joël CANCELA VAZ
 */
public class MessageListener {
    @Autowired
    private IOrderService orderService;

    @Autowired
    private IMealService mealService;

	private CountDownLatch latch = new CountDownLatch(3);

	@KafkaListener(topics = "${message.topic.name}", containerFactory = "topicKafkaListenerContainerFactory")
	public void listenGroupFoo(NewOrder message) {
        RestaurantOrderDTO orderToAdd = new RestaurantOrderDTO();
        try {
            for(String foodName : message.getFood()){
                orderToAdd.addMeal(mealService.findMealByName(foodName));
            }
        } catch (UnknowMealException e) {
            e.printStackTrace();
        }

        orderService.addOrder(orderToAdd);

		latch.countDown();
	}

	public CountDownLatch getLatch() {
		return latch;
	}
}

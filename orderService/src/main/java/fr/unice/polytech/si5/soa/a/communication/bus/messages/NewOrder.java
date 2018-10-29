package fr.unice.polytech.si5.soa.a.communication.bus.messages;

import fr.unice.polytech.si5.soa.a.communication.MealDTO;
import fr.unice.polytech.si5.soa.a.communication.OrderDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Class name	NewOrder
 * Date			06/10/2018
 * @author		PierreRainero
 */
@Data
@EqualsAndHashCode(callSuper=true)
@ToString()
public class NewOrder extends Message {
	public static String messageType = "NEW_ORDER";
	
	private String address;
	private String restaurantName;
	private String restaurantAddress;
	private List<String> food;
	private Date date;
	
	/**
	 * Default constructor
	 */
	public NewOrder() {
		// Default constructor for Jackson databinding
	}
	
	/**
	 * Normal constructor for an "NEW_ORDER" message
	 * @param order {@link OrderDTO} to use to construct the message
	 */
	public NewOrder(OrderDTO order) {
		super();
		
		food = new ArrayList<>();
		
		type = messageType;
		address = order.getDeliveryAddress();

		for(MealDTO meal : order.getMeals()) {
			food.add(meal.getName());
		}
		
		restaurantName = order.getRestaurant().getName();
		restaurantAddress = order.getRestaurant().getRestaurantAddress();
		date = order.getValidationDate();
	}
}

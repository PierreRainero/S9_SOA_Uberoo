package fr.unice.polytech.si5.soa.a.communication;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Class name	NewOrder
 * Date			06/10/2018
 * @author		PierreRainero
 */
@Data
@EqualsAndHashCode(callSuper=true)
@ToString()
public class NewOrder extends Message {
	private String address;
	private String restaurantName;
	private String restaurantAddress;
	private List<String> food;
	
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
		
		type = "NEW_ORDER";
		address = order.getDeliveryAddress();

		for(MealDTO meal : order.getMeals()) {
			food.add(meal.getName());
		}
		
		restaurantName = order.getRestaurant().getName();
		restaurantAddress = order.getRestaurant().getRestaurantAddress();
	}
}

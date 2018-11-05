package fr.unice.polytech.si5.soa.a.communication.bus.messages;

import java.util.ArrayList;
import java.util.List;

import fr.unice.polytech.si5.soa.a.communication.MealDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Class name	NewMeal
 * Date			23/10/2018
 * @author 		PierreRainero
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString()
public class NewMeal extends Message {
	private String name;
	private String restaurantName;
	private String restaurantAddress;
	private double price;
	private List<String> tags = new ArrayList<>();
	
	public static String messageType = "NEW_MEAL";

	
	public NewMeal(MealDTO meal, String restaurantName, String restaurantAddress) {
		type = "NEW_MEAL";
		
		name = meal.getName();
		this.restaurantName = restaurantName;
		this.restaurantAddress = restaurantAddress;
		this.price = meal.getPrice();
		this.tags = meal.getTags();
	}
}

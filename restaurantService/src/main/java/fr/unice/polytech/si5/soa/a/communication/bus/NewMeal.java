package fr.unice.polytech.si5.soa.a.communication.bus;

import fr.unice.polytech.si5.soa.a.communication.MealDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Class name	NewMeal
 * Date			23/10/2018
 *
 * @author PierreRainero
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString()
public class NewMeal extends Message {
	private String name;
	private String restaurantName;
	private String restaurantAddress;
	public static String messageType = "NEW_MEAL";

	public NewMeal() {

	}

	public NewMeal(MealDTO meal, String restaurantName, String restaurantAddress) {
		type = messageType;

		name = meal.getName();
		this.restaurantName = restaurantName;
		this.restaurantAddress = restaurantAddress;
	}
}

package fr.unice.polytech.si5.soa.a.communication.bus;

import fr.unice.polytech.si5.soa.a.communication.MealDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Class NewMeal
 *
 * @author Joël CANCELA VAZ
 */
@Data
@EqualsAndHashCode(callSuper=true)
@ToString()
public class NewMeal extends Message {
	private String name;
	private String restaurantName;
	private String restaurantAddress;

	public NewMeal() {

	}

	public NewMeal(MealDTO meal, String restaurantName, String restaurantAddress) {
		type = "NEW_MEAL";

		name = meal.getName();
		this.restaurantName =  restaurantName;
		this.restaurantAddress = restaurantAddress;
	}
}


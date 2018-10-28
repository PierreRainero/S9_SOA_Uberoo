package fr.unice.polytech.si5.soa.a.communication.bus;

import java.util.ArrayList;
import java.util.List;

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
@EqualsAndHashCode(callSuper = true)
@ToString()
public class NewMeal extends Message {
	public static String messageType = "NEW_MEAL";
	private String name;
	private String restaurantName;
	private String restaurantAddress;
	private double price;
	private List<String> tags = new ArrayList<>();

	/**
	 * Default constructor
	 */
	public NewMeal() {
		// Default constructor for Jackson databinding
	}
	
	/**
	 * Generate a {@link Meal} data transfer object from a message of the bus
	 * @return meal representation for order service
	 */
	public MealDTO createMeal() {
		return new MealDTO(name, tags, null, price);
	}
}


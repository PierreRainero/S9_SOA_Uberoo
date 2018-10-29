package fr.unice.polytech.si5.soa.a.communication.bus.messages;

import fr.unice.polytech.si5.soa.a.communication.RestaurantDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Class NewRestaurant
 *
 * @author Joël CANCELA VAZ
 */
@Data
@EqualsAndHashCode(callSuper=true)
@ToString()
public class NewRestaurant extends Message {
	public static String messageType = "NEW_RESTAURANT";
	private String name;
	private String address;

	/**
	 * Default constructor
	 */
	public NewRestaurant() {
		// Default constructor for Jackson databinding
	}

	/**
	 * Generate a {@link Restaurant} data transfer object from a message of the bus
	 * @return restaurant representation for order service
	 */
	public RestaurantDTO createRestaurant() {
		return new RestaurantDTO(-1, name, address);
	}
}

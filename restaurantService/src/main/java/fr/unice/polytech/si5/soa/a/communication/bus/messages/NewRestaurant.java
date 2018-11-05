package fr.unice.polytech.si5.soa.a.communication.bus.messages;

import fr.unice.polytech.si5.soa.a.communication.RestaurantDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Class name	NewRestaurant
 * Date			23/10/2018
 *
 * @author PierreRainero
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString()
public class NewRestaurant extends Message {
	private String name;
	private String address;
	public static String messageType = "NEW_RESTAURANT";

	public NewRestaurant() {

	}

	public NewRestaurant(RestaurantDTO restaurant) {
		type = messageType;

		name = restaurant.getName();
		address = restaurant.getRestaurantAddress();
	}
}

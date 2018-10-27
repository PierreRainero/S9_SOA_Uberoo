package fr.unice.polytech.si5.soa.a.communication.bus;

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
	private String name;
	private String address;

	public NewRestaurant() {

	}

	public NewRestaurant(RestaurantDTO restaurant) {
		type = "NEW_RESTAURANT";

		name = restaurant.getName();
		address = restaurant.getRestaurantAddress();
	}
}

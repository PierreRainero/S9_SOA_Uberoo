package fr.unice.polytech.si5.soa.a.communication.message;

import fr.unice.polytech.si5.soa.a.communication.DTO.RestaurantDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper=true)
@ToString()
public class NewRestaurant extends Message {
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
		return new RestaurantDTO(name, address);
	}
}


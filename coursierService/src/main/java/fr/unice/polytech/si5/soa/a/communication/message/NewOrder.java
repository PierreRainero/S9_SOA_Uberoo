package fr.unice.polytech.si5.soa.a.communication.message;

import fr.unice.polytech.si5.soa.a.communication.DTO.DeliveryDTO;
import fr.unice.polytech.si5.soa.a.communication.DTO.RestaurantDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

/**
 * Class name	NewOrder
 * Date			06/10/2018
 *
 * @author PierreRainero
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString()
public class NewOrder extends Message {
	public String address;
	private Integer restaurantId;
	public List<String> food;
	public Integer id;
	public static String messageType = "NEW_ORDER";

	/**
	 * Default constructor
	 */
	public NewOrder() {
		type = messageType;
		// Default constructor for Jackson databinding
	}

	public DeliveryDTO createDelivery() {
		return new DeliveryDTO(id, address, false, new RestaurantDTO(restaurantId));
	}
}

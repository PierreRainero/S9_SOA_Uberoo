package fr.unice.polytech.si5.soa.a.communication.bus.messages;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

import fr.unice.polytech.si5.soa.a.communication.RestaurantOrderDTO;
import fr.unice.polytech.si5.soa.a.entities.OrderState;

/**
 * Class name	NewOrder
 * Date			06/10/2018
 * @author		PierreRainero
 */
@Data
@EqualsAndHashCode(callSuper=true)
public class NewOrder extends Message {
	public static String messageType = "NEW_ORDER";
	
	private String address;
	private String restaurantName;
	private String restaurantAddress;
	private List<String> food;
	private Date date;

	/**
	 * Default constructor
	 */
	public NewOrder() {
		// Default constructor for Jackson databinding
		
		type = messageType;
	}
	
	public RestaurantOrderDTO createOrder() {
		return new RestaurantOrderDTO(-1, null, OrderState.TO_PREPARE, null, date);
	}
}
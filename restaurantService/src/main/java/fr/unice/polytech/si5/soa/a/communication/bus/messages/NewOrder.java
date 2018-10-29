package fr.unice.polytech.si5.soa.a.communication.bus.messages;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * Class name	NewOrder
 * Date			06/10/2018
 * @author		PierreRainero
 */
@Data
@EqualsAndHashCode(callSuper=true)
public class NewOrder extends Message {
	private String address;
	private String restaurantName;
	private String restaurantAddress;
	private List<String> food;
	public static String messageType = "NEW_ORDER";

	/**
	 * Default constructor
	 */
	public NewOrder() {
		// Default constructor for Jackson databinding
		
		type = messageType;
	}
}
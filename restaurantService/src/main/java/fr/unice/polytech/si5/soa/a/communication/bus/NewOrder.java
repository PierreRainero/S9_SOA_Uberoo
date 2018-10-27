package fr.unice.polytech.si5.soa.a.communication.bus;

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
		type = messageType;
		// Default constructor for Jackson databinding
	}

	@Override
	public String toString() {
		return "NewOrder{" +
				"address='" + address + '\'' +
				", restaurantName='" + restaurantName + '\'' +
				", restaurantAddress='" + restaurantAddress + '\'' +
				", food=" + food +
				", type='" + type + '\'' +
				'}';
	}
}
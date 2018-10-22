package fr.unice.polytech.si5.soa.a.communication.bus;

import java.util.List;

import fr.unice.polytech.si5.soa.a.entities.OrderState;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Class name	NewOrder
 * Date			06/10/2018
 * @author		PierreRainero
 */
@Data
@EqualsAndHashCode(callSuper=true)
@ToString()
public class NewOrder extends Message {
	private String address;
	private String restaurantName;
	private String restaurantAddress;
	private List<String> food;

	/**
	 * Default constructor
	 */
	public NewOrder() {
		// Default constructor for Jackson databinding
	}
}
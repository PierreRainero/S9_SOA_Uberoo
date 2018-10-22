package fr.unice.polytech.si5.soa.a.communication;

import java.util.List;

import fr.unice.polytech.si5.soa.a.entities.Meal;
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
	public String address;
	public List<Meal> meals;
	
	/**
	 * Default constructor
	 */
	public NewOrder() {
		// Default constructor for Jackson databinding
	}
	
	public RestaurantOrderDTO createRestaurantOrder() {
		return new RestaurantOrderDTO(-1, meals, OrderState.TO_PREPARE);
	}
}

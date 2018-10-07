package fr.unice.polytech.si5.soa.a.restaurantservice.communication;

import java.util.List;

import fr.unice.polytech.si5.soa.a.restaurantservice.model.OrderToPrepare;
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
	public List<String> food;
	
	/**
	 * Default constructor
	 */
	public NewOrder() {
		// Default constructor for Jackson databinding
	}
	
	public OrderToPrepare getOrder() {
		OrderToPrepare orderToPrepare = new OrderToPrepare();
		
		for(String mealName : food) {
			orderToPrepare.addMeal(mealName);
		}
		
		return orderToPrepare;
	}
}

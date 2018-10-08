package fr.unice.polytech.si5.soa.a.communication;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import fr.unice.polytech.si5.soa.a.entities.OrderState;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Class name	DeliveryDTO
 * Date			08/10/2018
 * @author		PierreRainero
 */
@Data
@EqualsAndHashCode()
@ToString()
public class RestaurantOrderDTO implements Serializable {
	/**
	 * Generated UID version 
	 */
	private static final long serialVersionUID = 2917537927445063668L;

	private int id;
	private List<String> meals = new ArrayList<>();
	private OrderState state = OrderState.TO_PREPARE;
	
	public RestaurantOrderDTO() {
		// Default constructor for Jackson databinding
	}
	
	public RestaurantOrderDTO(int id, List<String> meals, OrderState state) {
		this.id = id;
		this.meals = meals;
		this.state = state;
	}
}

package fr.unice.polytech.si5.soa.a.communication;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import fr.unice.polytech.si5.soa.a.entities.OrderState;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Class name	RestaurantOrderDTO
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
	private List<MealDTO> meals = new ArrayList<>();
	private OrderState state = OrderState.TO_PREPARE;
	private RestaurantDTO restaurant;
	
	public RestaurantOrderDTO() {
		// Default constructor for Jackson databinding
	}
	
	public RestaurantOrderDTO(int id, List<MealDTO> meals, OrderState state, RestaurantDTO restaurant) {
		this.id = id;
		this.meals = meals;
		this.state = state;
		this.restaurant = restaurant;
	}

	public void addMeal(MealDTO meal){
        meals.add(meal);
    }

    public void removeMeal(MealDTO meal){
        meals.remove(meal);
    }
}

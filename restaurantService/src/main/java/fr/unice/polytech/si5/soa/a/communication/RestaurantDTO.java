package fr.unice.polytech.si5.soa.a.communication;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode()
@ToString()
public class RestaurantDTO {
	private int id;
	private String name;
	private String restaurantAddress;
	private List<MealDTO> meals = new ArrayList<>();
	
	public RestaurantDTO() {
		
	}
	
	public RestaurantDTO(int id, String name, String restaurantAddress, List<MealDTO> meals) {
		this.id = id;
		this.name = name;
		this.restaurantAddress = restaurantAddress;
		this.meals = meals;
	}
}

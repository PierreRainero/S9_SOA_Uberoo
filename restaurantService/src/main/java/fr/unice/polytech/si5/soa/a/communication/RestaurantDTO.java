package fr.unice.polytech.si5.soa.a.communication;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Class name	RestaurantDTO
 * Date			22/10/2018
 * @author		PierreRainero
 */
@Data
@EqualsAndHashCode()
@ToString()
public class RestaurantDTO {
	private int id;
	private String name;
	private String restaurantAddress;
	
	public RestaurantDTO() {
		
	}
	
	public RestaurantDTO(int id, String name, String restaurantAddress) {
		this.id = id;
		this.name = name;
		this.restaurantAddress = restaurantAddress;
	}
}

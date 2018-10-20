package fr.unice.polytech.si5.soa.a.communication;

import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 
 * Class name	RestaurantDTO
 * Date			20/10/2018
 * @author		PierreRainero
 *
 */
@Data
@EqualsAndHashCode()
@ToString()
public class RestaurantDTO implements Serializable {
	/**
	 * Generated UID version
	 */
	private static final long serialVersionUID = -4364601420434565401L;
	
	private int id;
	private String name;
	private String restaurantAddress;

	/**
	 * Default constructor
	 */
	public RestaurantDTO() {
		// Default constructor for Jackson databinding
	}
	
	/**
	 * Normal constructor 
	 * @param id unique identifier to find a restaurant
	 * @param name restaurant's name
	 * @param restaurantAddress restaurant's address
	 */
	public RestaurantDTO(int id, String name, String restaurantAddress) {
		this.id = id;
		this.name = name;
		this.restaurantAddress = restaurantAddress;
	}
}

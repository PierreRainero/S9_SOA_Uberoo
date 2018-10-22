package fr.unice.polytech.si5.soa.a.communication;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Class name	MealDTO
 * Date			29/09/2018
 * @author		PierreRainero
 */
@Data
@EqualsAndHashCode()
@ToString()
public class MealDTO implements Serializable {
	/**
	 * Generated UID version
	 */
	private static final long serialVersionUID = -8640334793101869876L;
	
	private String name;
	private List<String> tags = new ArrayList<>();
	private RestaurantDTO restaurant;
	private double price;
	
	/**
	 * Default constructor
	 */
	public MealDTO() {
		// Default constructor for Jackson databinding
	}
	
	/**
	 * Normal constructor 
	 * @param name meal's name
	 * @param tags list of tag to find a meal
	 * @param restaurant restaurant associed to a meal
	 */
	public MealDTO(String name, List<String> tags, RestaurantDTO restaurant, double price) {
		this.name = name;
		this.tags = tags;
		this.restaurant = restaurant;
		this.price = price;
	}
}

package fr.unice.polytech.si5.soa.a.entities;

import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.NONE;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import fr.unice.polytech.si5.soa.a.communication.MealDTO;
import fr.unice.polytech.si5.soa.a.communication.RestaurantDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.ToString;

/**
 * Class name	Restaurant
 * Date			20/10/2018
 * @author		PierreRainero
 */
@Entity
@Data
@Table(name = "`RESTAURANT`")
@EqualsAndHashCode(exclude={"id", "meals"})
@ToString()
public class Restaurant implements Serializable {
	/**
	 * Generated UID version
	 */
	private static final long serialVersionUID = 5974879958140413751L;
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id")
	@Setter(NONE)
	private int id;
	
	@Column(name = "name", nullable = false)
	private String name;
	
	@Column(name = "restaurantAddress", nullable = false)
	private String restaurantAddress;
	
	@Setter(NONE)
	@OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY, mappedBy = "restaurant")
	@ToString.Exclude
	private List<Meal> meals = new ArrayList<>();
	
	/**
	 * Default constructor
	 */
	public Restaurant() {
		// Default constructor for JPA
	}
	
	/**
	 * Generate a Data Transfer Object from a business object
	 * @return DTO for a {@link Restaurant}
	 */
	public RestaurantDTO toDTO() {
		return new RestaurantDTO(id, name, restaurantAddress);
	}
	
	/**
	 * Add a meal to the restaurant
	 * @param mealToAdd meal to add
	 */
	public void addMeal(Meal mealToAdd) {
		meals.add(mealToAdd);
	}
	
	/**
	 * Remove a meal to the restaurant
	 * @param mealToRemove meal to remove
	 */
	public void removeMeal(Meal mealToRemove) {
		meals.remove(mealToRemove);
	}
}

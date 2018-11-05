package fr.unice.polytech.si5.soa.a.entities;

import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.NONE;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import fr.unice.polytech.si5.soa.a.communication.RestaurantDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.ToString;

/**
 * Class name	Restaurant
 * Date			23/10/2018
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
	private static final long serialVersionUID = -5271132190076336472L;

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
	
	@Setter(NONE)
	@OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY, mappedBy = "restaurant")
	@ToString.Exclude
	private List<RestaurantOrder> orders = new ArrayList<>();
	
	public Restaurant() {
		
	}
	
	public Restaurant(RestaurantDTO datas) {
		name = datas.getName();
		restaurantAddress = datas.getRestaurantAddress();
	}
	
	public RestaurantDTO toDTO() {
		return new RestaurantDTO(id, name, restaurantAddress);
	}
	
	public void addMeal(Meal meal) {
		meals.add(meal);
	}
	
	public void removeMeal(Meal meal) {
		meals.remove(meal);
	}
}

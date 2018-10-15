package fr.unice.polytech.si5.soa.a.entities;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import fr.unice.polytech.si5.soa.a.communication.RestaurantOrderDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Class name	RestaurantOrder
 * Date			08/10/2018
 * @author		PierreRainero
 */
@Entity
@Data
@Table(name = "`RESTAURANTORDER`")
@EqualsAndHashCode(exclude={"id"})
@ToString()
public class RestaurantOrder implements Serializable {
	/**
	 * Generated UID version 
	 */
	private static final long serialVersionUID = 6675013421945330865L;
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id")
	private int id;
	
	@ElementCollection(fetch = FetchType.EAGER)
	private List<String> meals = new ArrayList<>();
	
	@Enumerated(EnumType.STRING)
	private OrderState state = OrderState.TO_PREPARE;
	
	public RestaurantOrder() {
		// Default constructor for JPA
	}
	
	public RestaurantOrder(RestaurantOrderDTO data) {
		this.meals = data.getMeals();
		this.state = data.getState();
	}
	
	public RestaurantOrderDTO toDTO() {
		return new RestaurantOrderDTO(id, meals, state);
	}



}

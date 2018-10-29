package fr.unice.polytech.si5.soa.a.communication;

import java.io.Serializable;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import fr.unice.polytech.si5.soa.a.entities.states.OrderState;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Class name	CommandDTO
 * Date			30/09/2018
 * @author		PierreRainero
 */
@Data
@EqualsAndHashCode()
@ToString()
public class OrderDTO implements Serializable {
	/**
	 * Generated UID version
	 */
	private static final long serialVersionUID = 5044196469861617600L;
	
	private int id;
	private List<MealDTO> meals = new ArrayList<>();
	private UserDTO transmitter;
	private String deliveryAddress;
	private Duration eta;
	private OrderState state;
	private RestaurantDTO restaurant;
	private double price;
	private Date validationDate;
	
	/**
	 * Default constructor
	 */
	public OrderDTO() {
		// Default constructor for Jackson databinding
	}
	
	/**
	 * Normal constructor 
	 * @param id identifier of the order
	 * @param meals list of meals (DTO)
	 * @param transmitter user concerned by the order
	 * @param deliveryAddress address to deliver the order
	 * @param eta estimate time before arrival to the deliveryAddress
	 * @param state status of the order (accepted or not)
	 * @param restaurant restaurant associated to the order
	 * @param price price of the order
	 */
	public OrderDTO(int id, List<MealDTO> meals, UserDTO transmitter, String deliveryAddress, Duration eta, OrderState state, RestaurantDTO restaurant, double price, Date validationDate) {
		this.id = id;
		this.meals = meals;
		this.transmitter = transmitter;
		this.deliveryAddress = deliveryAddress;
		this.eta = eta;
		this.state = state;
		this.restaurant = restaurant;
		this.price = price;
		this.validationDate = validationDate;
	}
}

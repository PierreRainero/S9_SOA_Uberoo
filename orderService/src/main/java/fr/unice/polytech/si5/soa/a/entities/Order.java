package fr.unice.polytech.si5.soa.a.entities;

import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.NONE;

import java.io.Serializable;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.lang.NonNull;

import fr.unice.polytech.si5.soa.a.dto.OrderDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.ToString;

/**
 * Class name	Order
 * Date			01/10/2018
 * @author		PierreRainero
 */
@Entity
@Data
@Table(name = "`ORDER`")
@EqualsAndHashCode(exclude={"id"})
@ToString()
public class Order implements Serializable {
	/**
	 * Generated UID version
	 */
	private static final long serialVersionUID = 6853129339978021134L;
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id")
	@Setter(NONE)
	private int id;
	
	@ManyToMany(fetch = FetchType.EAGER)
	@Setter(NONE)
	private List<Meal> meals = new ArrayList<>();
	
	@ManyToOne(cascade = CascadeType.MERGE)
	@NonNull
	private User transmitter;
	
	@Column(name = "deliveryAddress", nullable = false)
	private String deliveryAddress;
	
	@Column(name = "eta")
	private Duration eta;
	
	@Enumerated(EnumType.STRING)
	private OrderState state = OrderState.WAITING;
	
	/**
	 * Default constructor
	 */
	public Order() {
		// Default constructor for JPA
	}
	
	/**
	 * Normal construtor using Data Transfert Object
	 * @param orderDatas DTO for {@link Order}
	 */
	public Order(OrderDTO orderDatas) {
		this.deliveryAddress = orderDatas.getDeliveryAddress();
		this.eta = orderDatas.getEta();
		this.state = orderDatas.getState();
	}
	/**
	 * Generate a Data Transfer Object from a business object
	 * @return DTO for a {@link Order}
	 */
	public OrderDTO toDTO() {
		return new OrderDTO(id, meals.stream().map(meal -> meal.toDTO()).collect(Collectors.toList()), transmitter.toDTO(), deliveryAddress, eta, state);
	}
	
	/**
	 * Add a meal to the order list
	 * @param meal meal to add
	 */
	public void addMeal(Meal meal) {
		meals.add(meal);
	}
	
	/**
	 * Remove a meal of the order list
	 * @param meal meal to remove
	 */
	public void removeMeal(Meal meal) {
		meals.remove(meal);
	}
	
	/**
	 * Calculate the Estimated Time of Arrival
	 * MOCKED
	 */
	public void calculateEta() {
		eta = Duration.ofMinutes(30);
	}
}

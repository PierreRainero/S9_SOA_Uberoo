package fr.unice.polytech.si5.soa.a.entities;

import fr.unice.polytech.si5.soa.a.communication.OrderDTO;
import fr.unice.polytech.si5.soa.a.entities.states.OrderState;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.ToString;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.NONE;

/**
 * Class name	Order
 * Date			01/10/2018
 * @author		PierreRainero
 */
@Entity
@Data
@Table(name = "`UBEROOORDER`")
@EqualsAndHashCode(exclude={"id", "payments"})
@ToString()
public class UberooOrder implements Serializable {
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

	@ManyToOne(cascade = CascadeType.MERGE)
	@NonNull
	private Restaurant restaurant;

	@Enumerated(EnumType.STRING)
	private OrderState state = OrderState.WAITING;
	
	@Setter(NONE)
	@OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY, mappedBy = "order")
	@ToString.Exclude
	private List<Payment> payments = new ArrayList<>();

	/**
	 * Default constructor
	 */
	public UberooOrder() {
		// Default constructor for JPA
	}

	/**
	 * Normal construtor using Data Transfert Object
	 * @param orderDatas DTO for {@link UberooOrder}
	 */
	public UberooOrder(OrderDTO orderDatas) {
		this.deliveryAddress = orderDatas.getDeliveryAddress();
		this.eta = orderDatas.getEta();
		this.state = orderDatas.getState();
	}

	/**
	 * Generate a Data Transfer Object from a business object
	 * @return DTO for a {@link UberooOrder}
	 */
	public OrderDTO toDTO() {
		return new OrderDTO(
				id,
				meals.stream().map(meal -> meal.toDTO()).collect(Collectors.toList()),
				transmitter.toDTO(),
				deliveryAddress,
				eta,
				state,
				restaurant.toDTO(),
				getPrice());
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
	 * MOCKED return always 30min
	 */
	public void calculateEta() {
		eta = Duration.ofMinutes(30);
	}
	
	/**
	 * Calculated the price of the order
	 * @return sum of every meals' price
	 */
	public double getPrice() {
		double price = 0.;
		for(Meal tmp : meals) {
			price += tmp.getPrice();
		}
		
		return price;
	}
}

package fr.unice.polytech.si5.soa.a.entities;

import fr.unice.polytech.si5.soa.a.communication.MealDTO;
import fr.unice.polytech.si5.soa.a.communication.OrderDTO;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.NONE;

/**
 * Class OrderToPrepare
 *
 * @author Joël CANCELA VAZ
 */
@Entity
@Data
@Table(name = "`ORDERTOPREPARE`")
@EqualsAndHashCode(exclude = {"id"})
@ToString()
public class OrderToPrepare implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = -4603988534527511525L;

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id")
	@Setter(NONE)
	private int id;

	@ElementCollection(fetch = FetchType.EAGER)
	private List<String> meals = new ArrayList<>();

	@Column(name = "isDone")
	@Setter
	@Getter
	private boolean isDone;

	public OrderToPrepare() {
		// Default constructor for JPA
	}

	public OrderToPrepare(OrderDTO orderToAdd) {
		for (MealDTO meal : orderToAdd.getMeals()) {
			meals.add(meal.getName());
		}
	}

	public void addMeal(String meal) {
		meals.add(meal);
	}

	public void removeMeal(String meal) {
		meals.remove(meal);
	}

	public OrderDTO toDTO() {
		OrderDTO orderDTO = new OrderDTO();
		List<MealDTO> mealDTOS = new ArrayList<>();
		for(String meal : meals){
			MealDTO mealDTO = new MealDTO();
			mealDTO.setName(meal);
			mealDTOS.add(mealDTO);
		}
		orderDTO.setMeals(mealDTOS);
		return orderDTO;
	}

}

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
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.sun.istack.internal.NotNull;

import fr.unice.polytech.si5.soa.a.communication.CommandDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.ToString;

/**
 * Class name	Command
 * Date			29/09/2018
 * @author		PierreRainero
 */
@Entity
@Data
@Table(name = "`COMMAND`")
@EqualsAndHashCode(exclude={"id"})
@ToString()
public class Command implements Serializable {
	/**
	 * Generated UID version
	 */
	private static final long serialVersionUID = 6853129339978021134L;
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id")
	@Setter(NONE)
	private int id;
	
	@ManyToMany
	@Setter(NONE)
	private List<Meal> meals = new ArrayList<>();
	
	@ManyToOne(cascade = CascadeType.MERGE)
	@NotNull
	private User transmitter;
	
	@Column(name = "deliveryAddress", nullable = false)
	private String deliveryAddress;

	/**
	 * Default constructor
	 */
	public Command() {
		// Default constructor for JPA
	}
	
	/**
	 * Normal construtor using Data Transfert Object
	 * @param commandDatas DTO for {@link Command}
	 */
	public Command(CommandDTO commandDatas) {
		this.deliveryAddress = commandDatas.getDeliveryAddress();
	}
	/**
	 * Generate a Data Transfer Object from a business object
	 * @return DTO for a {@link Command}
	 */
	public CommandDTO toDTO() {
		return new CommandDTO(meals.stream().map(command -> command.toDTO()).collect(Collectors.toList()), transmitter.toDTO(), deliveryAddress);
	}
	
	/**
	 * Add a meal to the command list
	 * @param meal meal to add
	 */
	public void addMeal(Meal meal) {
		meals.add(meal);
	}
	
	/**
	 * Remove a meal of the command list
	 * @param meal meal to remove
	 */
	public void removeMeal(Meal meal) {
		meals.remove(meal);
	}
}

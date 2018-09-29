package fr.unice.polytech.si5.soa.a.communication;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode()
@ToString()
public class CommandDTO implements Serializable {
	/**
	 * Generated UID version
	 */
	private static final long serialVersionUID = 5044196469861617600L;
	
	private List<MealDTO> meals = new ArrayList<>();
	
	/**
	 * Normal constructor 
	 * @param meals list of meals (DTO)
	 */
	public CommandDTO(List<MealDTO> meals) {
		this.meals = meals;
	}
}

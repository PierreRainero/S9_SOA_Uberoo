package fr.unice.polytech.si5.soa.a.communication;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode()
@ToString()
public class MealDTO implements Serializable {
	/**
	 * Generated UID version
	 */
	private static final long serialVersionUID = -8640334793101869876L;
	
	private String name;
	
	/**
	 * Normal constructor 
	 * @param name meal's name
	 */
	public MealDTO(String name) {
		this.name = name;
	}
}
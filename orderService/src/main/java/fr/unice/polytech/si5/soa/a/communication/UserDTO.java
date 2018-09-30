package fr.unice.polytech.si5.soa.a.communication;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Class name	UserDTO
 * Date			30/09/2018
 * @author		PierreRainero
 */
@Data
@EqualsAndHashCode()
@ToString()
public class UserDTO implements Serializable {
	/**
	 * Generated UID version
	 */
	private static final long serialVersionUID = -8701182960466208380L;

	private int id;
	private String lastName;
	private String firstName;
	
	/**
	 * Default constructor
	 */
	public UserDTO() {
		// Default constructor for Jackson databinding
	}
	
	/**
	 * Normal constructor 
	 * @param lastName last name of the user
	 * @param firstName first name if the user
	 */
	public UserDTO(int id, String lastName, String firstName) {
		this.id = id;
		this.lastName = lastName;
		this.firstName = firstName;
	}
}

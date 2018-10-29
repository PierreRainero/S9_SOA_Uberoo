package fr.unice.polytech.si5.soa.a.communication.bus.messages;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Class name	Message
 * Date			06/10/2018
 * @author		PierreRainero
 */
@Data
@EqualsAndHashCode()
@ToString()
public class Message {
	protected String type;
	
	/**
	 * Default constructor
	 */
	public Message() {
		// Default constructor for Jackson databinding
	}
}

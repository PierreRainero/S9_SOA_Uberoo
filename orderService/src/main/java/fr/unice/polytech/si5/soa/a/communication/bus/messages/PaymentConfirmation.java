package fr.unice.polytech.si5.soa.a.communication.bus.messages;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Class PaymentConfirmation
 *
 * @author Joël CANCELA VAZ
 */
@Data
@EqualsAndHashCode(callSuper=true)
@ToString()
public class PaymentConfirmation extends Message {
	private int id;
	private boolean status;

	/**
	 * Default constructor
	 */
	public PaymentConfirmation() {

		// Default constructor for Jackson databinding
	}
}

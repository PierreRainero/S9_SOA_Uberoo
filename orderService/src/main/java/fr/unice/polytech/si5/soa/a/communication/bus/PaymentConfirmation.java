package fr.unice.polytech.si5.soa.a.communication.bus;

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
	public static String messageType = "PAYMENT_CONFIRMATION";
	private int id;
	private boolean status;

	public PaymentConfirmation() {
		type=messageType;
	}
}

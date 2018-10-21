package fr.unice.polytech.si5.soa.a.communication.bus;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Class name	ProcessPayment
 * Date			21/10/2018
 * @author		PierreRainero
 */
@Data
@EqualsAndHashCode(callSuper=true)
@ToString()
public class ProcessPayment extends Message {
	private String account;
	private float amont;

	/**
	 * Default constructor
	 */
	public ProcessPayment() {
		// Default constructor for Jackson databinding
	}
	
	/**
	 * Normal constructor for an "PROCESS_PAYMENT" message
	 */
	public ProcessPayment(String accountToPay, float amont) {
		type = "PROCESS_PAYMENT";
		
		this.account = accountToPay;
		this.amont = amont;
	}
}

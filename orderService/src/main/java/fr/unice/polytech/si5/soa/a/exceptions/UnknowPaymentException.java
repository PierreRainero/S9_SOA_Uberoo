package fr.unice.polytech.si5.soa.a.exceptions;

import java.io.Serializable;

/**
 * Class name	UnknowPaymentException
 * Date			22/10/2018
 * @author		PierreRainero
 */
public class UnknowPaymentException extends Exception implements Serializable {
	/**
	 * Generated UID version
	 */
	private static final long serialVersionUID = -2368453312997532818L;
	
	public UnknowPaymentException(String message) {
		super(message);
	}
}

package fr.unice.polytech.si5.soa.a.exceptions;

import java.io.Serializable;

/**
 * Class name	UnknowDeliveryException
 * Date			08/10/2018
 * @author		PierreRainero
 */
public class UnknowDeliveryException extends Exception implements Serializable {
	/**
	 * Generated UID version
	 */
	private static final long serialVersionUID = -4424383533715188943L;

	public UnknowDeliveryException(String message) {
		super(message);
	}
}

package fr.unice.polytech.si5.soa.a.exceptions;

import java.io.Serializable;

/**
 * Class name	UnknowUserException
 * Date			30/09/2018
 * @author		PierreRainero
 */
public class UnknowUserException extends Exception implements Serializable {
	/**
	 * Generated UID version
	 */
	private static final long serialVersionUID = 8601371144848382820L;

	public UnknowUserException(String message) {
		super(message);
	}
}

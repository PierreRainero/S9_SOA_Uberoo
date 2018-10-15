package fr.unice.polytech.si5.soa.a.exceptions;

import java.io.Serializable;

/**
 * Class name	UnknowOrderException
 * Date			08/10/2018
 * @author		PierreRainero
 */
public class UnknowOrderException extends Exception implements Serializable {
	/**
	 * Generated UID version
	 */
	private static final long serialVersionUID = 4067256462638663125L;

	public UnknowOrderException(String message) {
		super(message);
	}
}

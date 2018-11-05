package fr.unice.polytech.si5.soa.a.exceptions;

import java.io.Serializable;

/**
 * Class name	UnknowMealException
 * Date			22/10/2018
 * @author		PierreRainero
 */
public class UnknowMealException extends Exception implements Serializable {
    /**
	 * Generated UID version
	 */
	private static final long serialVersionUID = 848310372374069762L;

	public UnknowMealException(String message) {
        super(message);
    }
}

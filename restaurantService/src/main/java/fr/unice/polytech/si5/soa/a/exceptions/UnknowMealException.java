package fr.unice.polytech.si5.soa.a.exceptions;

import java.io.Serializable;

public class UnknowMealException extends Exception implements Serializable {
    public UnknowMealException(String message) {
        super(message);
    }
}

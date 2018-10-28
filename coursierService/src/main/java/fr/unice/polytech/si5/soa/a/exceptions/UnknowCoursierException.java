package fr.unice.polytech.si5.soa.a.exceptions;

public class UnknowCoursierException extends Exception{

    public UnknowCoursierException(String s) {
        super("Can't find the coursier with id : "  + s);
    }
}

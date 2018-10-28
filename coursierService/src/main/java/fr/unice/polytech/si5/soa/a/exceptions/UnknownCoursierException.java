package fr.unice.polytech.si5.soa.a.exceptions;

public class UnknownCoursierException extends Exception{

    public UnknownCoursierException(String s) {
        super("Can't find the coursier with id : " + s);
    }
}

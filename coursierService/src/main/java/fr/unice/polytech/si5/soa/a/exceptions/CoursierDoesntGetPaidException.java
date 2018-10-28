package fr.unice.polytech.si5.soa.a.exceptions;

public class CoursierDoesntGetPaidException extends Throwable {
    public CoursierDoesntGetPaidException(int id) {
        super("The coursier has not been paid, concerned delivery : " + id);
    }
}

package fr.unice.polytech.si5.soa.a.exceptions;

public class EmptyDeliveriesCoursierException extends Throwable {
    public EmptyDeliveriesCoursierException(Integer idCoursier) {
        super("No deliveries has been done by the coursier of id : " + idCoursier);
    }
}

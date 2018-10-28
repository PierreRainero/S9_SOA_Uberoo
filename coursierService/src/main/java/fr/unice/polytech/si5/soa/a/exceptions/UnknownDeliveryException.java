package fr.unice.polytech.si5.soa.a.exceptions;

public class UnknownDeliveryException extends Exception {

    public UnknownDeliveryException(Integer message) {
        super("Can't find delivery with id = " + message);
    }
}

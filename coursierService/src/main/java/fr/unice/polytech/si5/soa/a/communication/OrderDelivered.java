package fr.unice.polytech.si5.soa.a.communication;

import java.util.Date;

/**
 * Class OrderDelivered
 *
 * @author Joël CANCELA VAZ
 */
public class OrderDelivered extends Message {
    public String address;
    private Date date;
    private Integer deliveryId;

    public OrderDelivered() {
        type = "ORDER_DELIVERED";
    }

    public OrderDelivered(String address, int deliveryId){
        this.date = new Date();
        this.address = address;
        this.deliveryId = deliveryId;
    }
}

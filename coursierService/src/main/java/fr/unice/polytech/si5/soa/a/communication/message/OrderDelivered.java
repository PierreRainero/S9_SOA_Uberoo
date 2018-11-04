package fr.unice.polytech.si5.soa.a.communication.message;

import java.util.Date;

/**
 * Class OrderDelivered
 *
 * @author Joël CANCELA VAZ
 */
public class OrderDelivered extends Message {
    public String address;
    public static String messageType = "ORDER_DELIVERED";
    private Date date;
    private Integer deliveryId;
    private String accountNumber;

    public OrderDelivered addAddress(String address) {
        this.address = address;
        return this;
    }

    public OrderDelivered addDate(Date date) {
        this.date = date;
        return this;
    }

    public OrderDelivered addDeliveryId(Integer deliveryId) {
        this.deliveryId = deliveryId;
        return this;
    }

    public OrderDelivered addAccount(String accountNumber) {
        this.accountNumber = accountNumber;
        return this;
    }

    public OrderDelivered() {
        type = messageType;
    }
}

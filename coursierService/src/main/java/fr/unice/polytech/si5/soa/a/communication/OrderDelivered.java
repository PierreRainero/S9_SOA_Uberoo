package fr.unice.polytech.si5.soa.a.communication;

import java.util.Date;

/**
 * Class OrderDelivered
 *
 * @author Joël CANCELA VAZ
 */
public class OrderDelivered extends Message {
    public String address;

    public String getAddress() {
        return address;
    }

    public OrderDelivered addAddress(String address) {
        this.address = address;
        return this;
    }

    public Date getDate() {
        return date;
    }

    public OrderDelivered addDate(Date date) {
        this.date = date;
        return this;
    }

    public Integer getDeliveryId() {
        return deliveryId;
    }

    public OrderDelivered addDeliveryId(Integer deliveryId) {
        this.deliveryId = deliveryId;
        return this;
    }

    private Date date;
    private Integer deliveryId;
    private String accountNumber;
    public OrderDelivered() {
        type = "ORDER_DELIVERED";
    }

    public OrderDelivered(String address, int deliveryId){
        this.date = new Date();
        this.address = address;
        this.deliveryId = deliveryId;
    }

    public OrderDelivered addAccount(String accountNumber) {
        this.accountNumber = accountNumber;
        return this;
    }
}

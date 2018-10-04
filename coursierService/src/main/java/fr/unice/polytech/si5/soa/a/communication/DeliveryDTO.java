package fr.unice.polytech.si5.soa.a.communication;

public class DeliveryDTO {

    private Boolean toBeDelivered;
    private OrderDTO order;

    public DeliveryDTO(){

    }

    public DeliveryDTO(Boolean toBeDelivered, OrderDTO order){
        this.toBeDelivered = toBeDelivered;
        this.order = order;
    }

}

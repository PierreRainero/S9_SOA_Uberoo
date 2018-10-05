package fr.unice.polytech.si5.soa.a.dto;

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

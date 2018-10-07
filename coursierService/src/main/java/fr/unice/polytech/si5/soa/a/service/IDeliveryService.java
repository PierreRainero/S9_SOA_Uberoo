package fr.unice.polytech.si5.soa.a.service;

import fr.unice.polytech.si5.soa.a.entities.Delivery;

import java.util.List;

/**
 * Service of the delivery
 *  @author Alexis Deslandes
 */
public interface IDeliveryService {

    List<Delivery> findTobeDeliveredDeliveries();

    void updateDeliveryToDelivered(Long idDelivery) throws Exception;

	void createDelivery(Delivery delivery);
}
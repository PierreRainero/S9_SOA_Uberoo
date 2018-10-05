package fr.unice.polytech.si5.soa.a.service;

import fr.unice.polytech.si5.soa.a.entities.Delivery;

import java.util.List;

public interface IDeliveryService {

    List<Delivery> findTobeDeliveredDeliveries();

}
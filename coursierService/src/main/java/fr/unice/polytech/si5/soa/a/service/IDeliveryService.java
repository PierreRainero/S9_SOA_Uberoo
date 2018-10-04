package fr.unice.polytech.si5.soa.a.service;

import fr.unice.polytech.si5.soa.a.communication.DeliveryDTO;

import java.util.List;

public interface IDeliveryService {

    List<DeliveryDTO> findTobeDeliveredDeliveries();

}
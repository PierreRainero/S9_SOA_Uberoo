package fr.unice.polytech.si5.soa.a.services;

import java.util.List;

import fr.unice.polytech.si5.soa.a.communication.DeliveryDTO;
import fr.unice.polytech.si5.soa.a.exceptions.UnknowDeliveryException;

/**
 * Class name	IDeliveryService
 * Date			08/10/2018
 * @author		PierreRainero
 */
public interface IDeliveryService {
	DeliveryDTO addDelivery(DeliveryDTO deliveryToAdd);
	DeliveryDTO updateDelivery(DeliveryDTO deliveryToUpdate) throws UnknowDeliveryException;
	List<DeliveryDTO> getDeliveriesToDo();
}

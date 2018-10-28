package fr.unice.polytech.si5.soa.a.services;

import java.util.List;

import fr.unice.polytech.si5.soa.a.communication.DeliveryDTO;
import fr.unice.polytech.si5.soa.a.communication.PaymentConfirmation;
import fr.unice.polytech.si5.soa.a.entities.Delivery;
import fr.unice.polytech.si5.soa.a.exceptions.CoursierDoesntGetPaidException;
import fr.unice.polytech.si5.soa.a.exceptions.UnknownCoursierException;
import fr.unice.polytech.si5.soa.a.exceptions.UnknownDeliveryException;

/**
 * Class name	IDeliveryService
 * Date			08/10/2018
 * @author		PierreRainero
 */
public interface IDeliveryService {
	DeliveryDTO addDelivery(DeliveryDTO deliveryToAdd);
	DeliveryDTO updateDelivery(DeliveryDTO deliveryToUpdate) throws UnknownCoursierException, UnknownDeliveryException;
	List<DeliveryDTO> getDeliveriesToDo();
    List<DeliveryDTO> getDeliveriesToDo(Double latitude, Double longitude);

    Delivery receiveNewPayment(PaymentConfirmation message) throws UnknownDeliveryException, CoursierDoesntGetPaidException;
    DeliveryDTO assignDelivery(Integer deliveryId, Integer coursierId) throws UnknownDeliveryException, UnknownCoursierException;
}

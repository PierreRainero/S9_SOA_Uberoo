package fr.unice.polytech.si5.soa.a.services;

import java.util.List;

import fr.unice.polytech.si5.soa.a.communication.DTO.CancelDataDTO;
import fr.unice.polytech.si5.soa.a.communication.message.CourseCancelMessage;
import fr.unice.polytech.si5.soa.a.communication.DTO.DeliveryDTO;
import fr.unice.polytech.si5.soa.a.communication.message.PaymentConfirmation;
import fr.unice.polytech.si5.soa.a.entities.Delivery;
import fr.unice.polytech.si5.soa.a.exceptions.CoursierDoesntGetPaidException;
import fr.unice.polytech.si5.soa.a.exceptions.NoAvailableCoursierException;
import fr.unice.polytech.si5.soa.a.exceptions.UnknownCoursierException;
import fr.unice.polytech.si5.soa.a.exceptions.UnknownDeliveryException;
import fr.unice.polytech.si5.soa.a.exceptions.UnknownRestaurantException;

/**
 * Class name	IDeliveryService
 * Date			08/10/2018
 * @author		PierreRainero
 */
public interface IDeliveryService {
	DeliveryDTO addDelivery(DeliveryDTO deliveryToAdd) throws UnknownRestaurantException, NoAvailableCoursierException;
	DeliveryDTO updateDelivery(DeliveryDTO deliveryToUpdate) throws UnknownCoursierException, UnknownDeliveryException;
	List<DeliveryDTO> getDeliveriesToDo();
    List<DeliveryDTO> getDeliveriesToDo(Double latitude, Double longitude);
    Delivery receiveNewPayment(PaymentConfirmation message) throws UnknownDeliveryException, CoursierDoesntGetPaidException;
    DeliveryDTO assignDelivery(Integer deliveryId, Integer coursierId) throws UnknownDeliveryException, UnknownCoursierException;

    DeliveryDTO replaceOrder(CancelDataDTO courseCancelMessage) throws UnknownDeliveryException, UnknownCoursierException;
}

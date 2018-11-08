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
    /**
     * Add the following delivery to the system.
     * @param deliveryToAdd The delivery to add.
     * @return The added delivery.
     * @throws UnknownRestaurantException
     * @throws NoAvailableCoursierException
     */
	DeliveryDTO addDelivery(DeliveryDTO deliveryToAdd) throws UnknownRestaurantException, NoAvailableCoursierException;

    /**
     * Update the following delivery to the system.
     * @param deliveryToUpdate The delivery to update on the system.
     * @return The updated delivery.
     * @throws UnknownCoursierException
     * @throws UnknownDeliveryException
     */
	DeliveryDTO updateDelivery(DeliveryDTO deliveryToUpdate) throws UnknownCoursierException, UnknownDeliveryException;

    /**
     * Get all the deliveries which are undone.
     * @return The deliveries which satisfy this condition.
     */
	List<DeliveryDTO> getDeliveriesToDo();

    /**
     * Get the deliveries that are nearly
     * the same position as parameters.
     * @param latitude Latitude interest.
     * @param longitude Longitude interest.
     * @return
     */
    List<DeliveryDTO> getDeliveriesToDo(Double latitude, Double longitude);

    /**
     * Receive new payment for one particular delivery.
     * @param message Message containing the confirmation.
     * @return Concerned delivery.
     * @throws UnknownDeliveryException
     * @throws CoursierDoesntGetPaidException
     */
    Delivery receiveNewPayment(PaymentConfirmation message) throws UnknownDeliveryException, CoursierDoesntGetPaidException;

    /**
     * Assign a coursier to a specific delivery.
     * @param deliveryId The delivery id.
     * @param coursierId The coursier id that will be assigned to the delivery.
     * @return The delivery which has been updated.
     * @throws UnknownDeliveryException
     * @throws UnknownCoursierException
     */
    DeliveryDTO assignDelivery(Integer deliveryId, Integer coursierId) throws UnknownDeliveryException, UnknownCoursierException;

    /**
     * Cancel the current delivery, remove from the coursier.
     * Send a message to the other services to take consideration of this replacement.
     * @param courseCancelMessage Explains why the delivery is canceled.
     * @return The modified delivery.
     * @throws UnknownDeliveryException
     * @throws UnknownCoursierException
     */
    DeliveryDTO replaceOrder(CancelDataDTO courseCancelMessage) throws UnknownDeliveryException, UnknownCoursierException;
}

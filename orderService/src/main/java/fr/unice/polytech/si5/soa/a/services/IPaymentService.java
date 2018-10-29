package fr.unice.polytech.si5.soa.a.services;

import fr.unice.polytech.si5.soa.a.communication.PaymentDTO;
import fr.unice.polytech.si5.soa.a.entities.states.PaymentState;
import fr.unice.polytech.si5.soa.a.exceptions.UnknowOrderException;
import fr.unice.polytech.si5.soa.a.exceptions.UnknowPaymentException;

/**
 * Class name	IPaymentService
 * Date			22/10/2018
 * @author		PierreRainero
 *
 */
public interface IPaymentService {
	/**
	 * Add a new payment to treat
	 * @param paymentToAdd payment to create
	 * @param orderAssociated order associated to the payment
	 * @return saved payment
	 * @throws UnknowOrderException if the order doesn't exist
	 */
	PaymentDTO addPayment(PaymentDTO paymentToAdd, int orderIdAssociated) throws UnknowOrderException ;
	
	/**
	 * Search a payment in the system using his id
	 * @param idToSearch id to use
	 * @return found payment
	 * @throws UnknowPaymentException if the payment doesn't exist
	 */
	PaymentDTO findPaymentById(int idToSearch) throws UnknowPaymentException;
	
	/**
	 * Update a payment in the system
	 * @param paymentUpdated payment to use to update the payment in the system
	 * @return the updated payment
	 * @throws UnknowPaymentException if the payment doesn't exist
	 */
	PaymentDTO updatePayment(PaymentDTO paymentUpdated) throws UnknowPaymentException;
	
	/**
	 * Update a payment in the system
	 * @param paymentId id of the payment to update
	 * @param paymentState new state of the payment
	 * @return the updated payment
	 * @throws UnknowPaymentException if the payment doesn't exist
	 */
	PaymentDTO updatePaymentStatus(int paymentId, PaymentState paymentState) throws UnknowPaymentException;
}

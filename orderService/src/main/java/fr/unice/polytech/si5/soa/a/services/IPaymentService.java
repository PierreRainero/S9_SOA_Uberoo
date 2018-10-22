package fr.unice.polytech.si5.soa.a.services;

import fr.unice.polytech.si5.soa.a.communication.OrderDTO;
import fr.unice.polytech.si5.soa.a.communication.PaymentDTO;
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
	 * @param orderAssociated order associated to the payement
	 * @return saved payment
	 * @throws UnknowOrderException if the order doesn't exist
	 */
	PaymentDTO addPayment(PaymentDTO paymentToAdd, OrderDTO orderAssociated) throws UnknowOrderException ;
	
	/**
	 * Search a payment in the system using his id
	 * @param idToSearch id to use
	 * @return found payment
	 * @throws UnknowPaymentException if the payment doesn't exist
	 */
	PaymentDTO findPaymentById(int idToSearch) throws UnknowPaymentException;
}

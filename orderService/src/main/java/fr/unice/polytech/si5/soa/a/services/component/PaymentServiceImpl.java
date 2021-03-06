package fr.unice.polytech.si5.soa.a.services.component;

import fr.unice.polytech.si5.soa.a.communication.PaymentDTO;
import fr.unice.polytech.si5.soa.a.communication.bus.MessageProducer;
import fr.unice.polytech.si5.soa.a.communication.bus.messages.ProcessPayment;
import fr.unice.polytech.si5.soa.a.dao.IOrderTakerDao;
import fr.unice.polytech.si5.soa.a.dao.IPaymentDao;
import fr.unice.polytech.si5.soa.a.entities.Payment;
import fr.unice.polytech.si5.soa.a.entities.UberooOrder;
import fr.unice.polytech.si5.soa.a.entities.states.PaymentState;
import fr.unice.polytech.si5.soa.a.exceptions.UnknowOrderException;
import fr.unice.polytech.si5.soa.a.exceptions.UnknowPaymentException;
import fr.unice.polytech.si5.soa.a.services.IPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Class name	PaymentServiceImpl
 * @author 		PierreRainero
 * @see 		IPaymentService
 * Date			22/10/2018
 **/
@Primary
@Service("PaymentService")
public class PaymentServiceImpl implements IPaymentService {
	@Autowired
	private IPaymentDao paymentDao;
	
	@Autowired
	private IOrderTakerDao orderDao;
	
	@Autowired
	private MessageProducer producer;
	
	@Override
	/**
	 * {@inheritDoc}
	 */
	public PaymentDTO addPayment(PaymentDTO paymentToAdd, int orderIdAssociated) throws UnknowOrderException {		
		Optional<UberooOrder> orderWrapped = orderDao.findOrderById(orderIdAssociated);
		if (!orderWrapped.isPresent()) {
			throw new UnknowOrderException("Can't find order with id = " + orderIdAssociated);
		}
		
		Payment payment = new Payment(paymentToAdd);
		payment.setOrder(orderWrapped.get());
		
		PaymentDTO result = paymentDao.addPayment(payment).toDTO();
		
		ProcessPayment message = new ProcessPayment(result);
		producer.sendMessage(message);
		
		return result;
	}

	@Override
	/**
	 * {@inheritDoc}
	 */
	public PaymentDTO findPaymentById(int idToSearch) throws UnknowPaymentException {
		return checkAndFindPayment(idToSearch).toDTO();
	}

	@Override
	/**
	 * {@inheritDoc}
	 */
	public PaymentDTO updatePayment(PaymentDTO paymentUpdated) throws UnknowPaymentException {
		Payment payment = checkAndFindPayment(paymentUpdated.getId());
		payment.setState(paymentUpdated.getState());
		
		return paymentDao.updatePayment(payment).toDTO();
	}

	@Override
	/**
	 * {@inheritDoc}
	 */
	public PaymentDTO updatePaymentStatus(int paymentId, PaymentState paymentState) throws UnknowPaymentException {
		Payment payment = checkAndFindPayment(paymentId);
		payment.setState(paymentState);
		
		return paymentDao.updatePayment(payment).toDTO();
	}
	
	private Payment checkAndFindPayment(int id) throws UnknowPaymentException {
		Optional<Payment> paymentWrapped = paymentDao.findPaymentById(id);
		
		if(!paymentWrapped.isPresent()) {
			throw new UnknowPaymentException("Can't find payment with id = "+id);
		}
		
		return paymentWrapped.get();
	}
}

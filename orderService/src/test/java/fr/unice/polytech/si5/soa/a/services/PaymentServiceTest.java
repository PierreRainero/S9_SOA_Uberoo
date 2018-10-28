package fr.unice.polytech.si5.soa.a.services;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import fr.unice.polytech.si5.soa.a.communication.PaymentDTO;
import fr.unice.polytech.si5.soa.a.communication.bus.Message;
import fr.unice.polytech.si5.soa.a.communication.bus.MessageProducer;
import fr.unice.polytech.si5.soa.a.configuration.TestConfiguration;
import fr.unice.polytech.si5.soa.a.dao.IOrderTakerDao;
import fr.unice.polytech.si5.soa.a.dao.IPaymentDao;
import fr.unice.polytech.si5.soa.a.entities.Payment;
import fr.unice.polytech.si5.soa.a.entities.Restaurant;
import fr.unice.polytech.si5.soa.a.entities.UberooOrder;
import fr.unice.polytech.si5.soa.a.entities.User;
import fr.unice.polytech.si5.soa.a.entities.states.PaymentState;
import fr.unice.polytech.si5.soa.a.exceptions.UnknowOrderException;
import fr.unice.polytech.si5.soa.a.exceptions.UnknowPaymentException;
import fr.unice.polytech.si5.soa.a.services.component.PaymentServiceImpl;

/**
 * Class name	PaymentServiceTest
 * Date			22/10/2018
 * @author 		PierreRainero
 *
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfiguration.class})
public class PaymentServiceTest {
	@Autowired
	@Mock
	private MessageProducer messageProducerMock;
	
	@Autowired
	@Qualifier("mock")
	@Mock
	private IPaymentDao paymentDaoMock;

	@Autowired
	@Qualifier("mock")
	@Mock
	private IOrderTakerDao orderDaoMock;

	@Autowired
	@InjectMocks
	private PaymentServiceImpl paymentService;

	private Payment payment;
	private UberooOrder order;

	@BeforeEach
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		Mockito.reset(paymentDaoMock);
		Mockito.reset(orderDaoMock);

		order = new UberooOrder();
		order.setRestaurant(new Restaurant());
		order.setDeliveryAddress("930 Route des Colles, 06410 Biot");
		order.setTransmitter(new User());

		payment = new Payment();
		payment.setAccount("FR89 3704 0044 0532 0130 00");
		payment.setAmount(45.25);
		payment.setOrder(order);
	}

	@Test
	public void addANewPayment() throws Exception {
		when(orderDaoMock.findOrderById(anyInt())).thenReturn(Optional.of(order));
		when(paymentDaoMock.addPayment(any(Payment.class))).thenReturn(payment);
		MessageProducer spy = Mockito.spy(messageProducerMock);
		doNothing().when(spy).sendMessage(any(Message.class));

		PaymentDTO paymentDTO = paymentService.addPayment(payment.toDTO(), order.getId());

		assertNotNull(paymentDTO);
		assertTrue(payment.getAmount() == paymentDTO.getAmount());
	}

	@Test
	public void addNewPaymentWithUnknowOrder() {
		when(orderDaoMock.findOrderById(anyInt())).thenReturn(Optional.empty());
		when(paymentDaoMock.addPayment(any(Payment.class))).thenReturn(payment);

		assertThrows(UnknowOrderException.class, () -> {
			paymentService.addPayment(payment.toDTO(), -1);
		});
	}

	@Test
	public void findExistingPayment() throws Exception {
		when(paymentDaoMock.findPaymentById(anyInt())).thenReturn(Optional.of(payment));

		PaymentDTO paymentDTO = paymentService.findPaymentById(-1);

		assertNotNull(paymentDTO);
		assertTrue(payment.getAmount() == paymentDTO.getAmount());
	}

	@Test
	public void findNonExistingPayment() {
		when(paymentDaoMock.findPaymentById(anyInt())).thenReturn(Optional.empty());
		assertThrows(UnknowPaymentException.class, () -> {
			paymentService.findPaymentById(-1);
		});

	}
	
	@Test
	public void updateAPayment() throws Exception {
		when(paymentDaoMock.findPaymentById(anyInt())).thenReturn(Optional.of(payment));
		when(paymentDaoMock.updatePayment(any(Payment.class))).thenReturn(payment);
		payment.setState(PaymentState.REFUSED);
		
		PaymentDTO paymentDTO = paymentService.updatePayment(payment.toDTO());
		assertNotNull(paymentDTO);
		assertTrue(payment.getAmount() == paymentDTO.getAmount());
		assertEquals(PaymentState.REFUSED, paymentDTO.getState());
	}
	
	@Test
	public void updateANonExistingPayment() {
		when(paymentDaoMock.findPaymentById(anyInt())).thenReturn(Optional.empty());
		payment.setState(PaymentState.REFUSED);
		
		assertThrows(UnknowPaymentException.class, () -> {
			paymentService.updatePayment(payment.toDTO());
		});
	}
	
	@Test
	public void updatePaymentWithIdAndState() throws Exception {
		when(paymentDaoMock.findPaymentById(anyInt())).thenReturn(Optional.of(payment));
		when(paymentDaoMock.updatePayment(any(Payment.class))).thenReturn(payment);
		
		PaymentDTO paymentDTO = paymentService.updatePaymentStatus(payment.getId(), PaymentState.REFUSED);
		assertNotNull(paymentDTO);
		assertTrue(payment.getAmount() == paymentDTO.getAmount());
		assertEquals(PaymentState.REFUSED, paymentDTO.getState());
	}
}

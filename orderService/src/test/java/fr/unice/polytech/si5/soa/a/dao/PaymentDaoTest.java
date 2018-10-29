package fr.unice.polytech.si5.soa.a.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.exception.SQLGrammarException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import fr.unice.polytech.si5.soa.a.configuration.TestConfiguration;
import fr.unice.polytech.si5.soa.a.entities.Payment;
import fr.unice.polytech.si5.soa.a.entities.states.PaymentState;

/**
 * Class name	PaymentDaoTest
 * Date			22/10/2018
 * @author 		PierreRainero
 *
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { TestConfiguration.class })
public class PaymentDaoTest {
	@Autowired
	private SessionFactory sessionFactory;
	
	@Autowired
	private IPaymentDao paymentDao;
	
	private Payment alreadySavedPayment;
	private Payment newPayment;
	
	@BeforeEach
	public void setUp() throws Exception {
		alreadySavedPayment = new Payment();
		alreadySavedPayment.setAccount("FR89 3704 0044 0532 0130 00");
		alreadySavedPayment.setAmount(45.25);
		
		newPayment = new Payment();
		newPayment.setAccount("FR89 3704 0044 0532 0130 00");
		newPayment.setAmount(22);
		
		Session session = sessionFactory.openSession();
		try {
			session.save(alreadySavedPayment);
			session.beginTransaction().commit();
		} catch (SQLGrammarException e) {
			session.getTransaction().rollback();
		} finally {
			session.close();
		}
	}
	
	@AfterEach
	public void cleanUp() throws Exception {
		Session session = sessionFactory.openSession();
	    Transaction transaction = null;
	    try {
	    	transaction = session.beginTransaction();
	    	
	    	if(newPayment.getId() != 0) {
	    		session.delete(newPayment);
	    	}
	    	
			session.delete(alreadySavedPayment);
			
			session.flush();
			transaction.commit();
			
			newPayment = null;
			alreadySavedPayment = null;
		} catch (SQLGrammarException e) {
			session.getTransaction().rollback();
		} finally {
			session.close();
		}
	}
	
	@Test
	public void addANewPayment() {
		Payment payment = paymentDao.addPayment(newPayment);
		
		assertNotNull(payment);
		assertNotEquals(0, payment.getId());
		assertTrue(newPayment.getAmount() == payment.getAmount());
	}
	
	@Test
	public void searchExistingPaymentById() {
		Optional<Payment> result = paymentDao.findPaymentById(alreadySavedPayment.getId());
		assertTrue(result.isPresent());
		
		Payment payment = result.get();
		assertTrue(alreadySavedPayment.getAmount() == payment.getAmount());
	}
	
	@Test
	public void searchNonExistingPaymentById() {
		Optional<Payment> result = paymentDao.findPaymentById(newPayment.getId());
		assertFalse(result.isPresent());
	}
	
	@Test
	public void updateAPayment() {
		assertEquals(PaymentState.SENT, alreadySavedPayment.getState());
		
		Session session = sessionFactory.openSession();
		session.evict(alreadySavedPayment);
		alreadySavedPayment.setState(PaymentState.REFUSED);
	
		Payment payment = paymentDao.updatePayment(alreadySavedPayment);
		assertEquals(PaymentState.REFUSED, payment.getState());
	}
}

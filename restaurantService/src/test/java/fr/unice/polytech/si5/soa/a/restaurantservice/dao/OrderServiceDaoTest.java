package fr.unice.polytech.si5.soa.a.restaurantservice.dao;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

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

import fr.unice.polytech.si5.soa.a.restaurantservice.configuration.TestConfiguration;
import fr.unice.polytech.si5.soa.a.restaurantservice.model.OrderToPrepare;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { TestConfiguration.class })
public class OrderServiceDaoTest {
	@Autowired
	private SessionFactory sessionFactory;
	
	@Autowired
	private IOrderDao orderDao;
	
	private OrderToPrepare otp;
	
	@BeforeEach
	public void setUp() throws Exception {
		otp = new OrderToPrepare();
		
		Session session = sessionFactory.openSession();
		try {
			session.save(otp);
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
	    	
			session.delete(otp);
			
			session.flush();
			transaction.commit();

			otp = null;
		} catch (SQLGrammarException e) {
			session.getTransaction().rollback();
		} finally {
			session.close();
		}
	}
	
	@Test
	public void getAllOrders () {
		List<OrderToPrepare> result = orderDao.getOrders();
		
		assertNotNull(result);
		assertEquals(1, result.size());
	}
}

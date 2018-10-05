package fr.unice.polytech.si5.soa.a.dao;

import static org.junit.Assert.assertEquals;
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

import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import fr.unice.polytech.si5.soa.a.configuration.TestConfiguration;
import fr.unice.polytech.si5.soa.a.entities.Order;
import fr.unice.polytech.si5.soa.a.entities.Meal;
import fr.unice.polytech.si5.soa.a.entities.User;

/**
 * Class name	OrderTakerDaoTest
 * Date			29/09/2018
 * @author		PierreRainero
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { TestConfiguration.class })
public class OrderTakerDaoTest {
	@Autowired
	private SessionFactory sessionFactory;
	
	@Autowired
	private IOrderTakerDao orderDao;
	
	private Meal ramen;
	private Order bobOrder;
	private User bob;
	
	@BeforeEach
	public void setUp() throws Exception {
		ramen = new Meal();
		ramen.setName("Ramen soup");
		
		bob = new User();
		bob.setFirstName("Bob");
		bob.setLastName("Harington");
		
		bobOrder = new Order();
		bobOrder.addMeal(ramen);
		bobOrder.setDeliveryAddress("930 Route des Colles, 06410 Biot");
		bobOrder.setTransmitter(bob);
		
		Session session = sessionFactory.openSession();
		try {
			session.save(ramen);
			session.save(bob);
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
	    	
	    	if(bobOrder.getId() != 0) {
	    		session.delete(bobOrder);
	    	}
	    	
			session.delete(ramen);
			session.delete(bob);
			
			session.flush();
			transaction.commit();
			
			bobOrder = null;
			bob = null;
			ramen = null;
		} catch (SQLGrammarException e) {
			session.getTransaction().rollback();
		} finally {
			session.close();
		}
	}
	
	@Test
	public void addANewOrder() {
		Order order = orderDao.addOrder(bobOrder);
		
		assertNotNull(order);
		assertNotEquals(0, order.getId());
		assertEquals(1, order.getMeals().size());
	}
	
	@Test
	public void findAnOrderUsingHisId() {
		Session session = sessionFactory.openSession();
		try {
			session.save(bobOrder);
			session.beginTransaction().commit();
		} catch (SQLGrammarException e) {
			session.getTransaction().rollback();
		} finally {
			session.close();
		}
		
		Optional<Order> order = orderDao.findOrderById(bobOrder.getId());
		assertTrue(order.isPresent());
		assertEquals(bobOrder.getId(), order.get().getId());
	}
	
	@Test
	public void updateAnOrder() {
		Session session = sessionFactory.openSession();
		try {
			session.save(bobOrder);
			session.beginTransaction().commit();
		} catch (SQLGrammarException e) {
			session.getTransaction().rollback();
		} finally {
			session.close();
		}
		
		String oldAddress = bobOrder.getDeliveryAddress();
		String newAddress = "221B Baker Street";
		assertNotEquals(oldAddress, newAddress);
		
		session = sessionFactory.openSession();
		session.evict(bobOrder);
		
		bobOrder.setDeliveryAddress(newAddress);
		Order order = orderDao.updateOrder(bobOrder);
		assertNotEquals(oldAddress, order.getDeliveryAddress());
		assertEquals(newAddress, order.getDeliveryAddress());
	}
}

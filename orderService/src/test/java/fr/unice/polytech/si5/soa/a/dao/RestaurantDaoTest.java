package fr.unice.polytech.si5.soa.a.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

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
import fr.unice.polytech.si5.soa.a.entities.Restaurant;

/**
 * Class name	RestaurantDaoTest
 * Date			20/10/2018
 * @author 		PierreRainero
 *
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { TestConfiguration.class })
public class RestaurantDaoTest {
	@Autowired
	private SessionFactory sessionFactory;
	
	@Autowired
	private IRestaurantDao restaurantDao;
	
	private Restaurant asianRestaurant;
	private Restaurant italianRestaurant;
	
	@BeforeEach
	public void setUp() throws Exception {
		asianRestaurant = new Restaurant();
		asianRestaurant.setName("Lion d'or");
		asianRestaurant.setRestaurantAddress("22 rue des nems");
		
		italianRestaurant = new Restaurant();
		italianRestaurant.setName("Chez pietro");
		italianRestaurant.setRestaurantAddress("57 avenue des pizzas");
		
		Session session = sessionFactory.openSession();
		try {
			session.save(asianRestaurant);
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
	    	
	    	if(italianRestaurant.getId() != 0) {
	    		session.delete(italianRestaurant);
	    	}
	    	
			session.delete(asianRestaurant);
			
			session.flush();
			transaction.commit();
			
			italianRestaurant = null;
			asianRestaurant = null;
		} catch (SQLGrammarException e) {
			session.getTransaction().rollback();
		} finally {
			session.close();
		}
	}
	
	@Test
	public void addANewRestaurant() {
		Restaurant restaurant = restaurantDao.addRestaurant(italianRestaurant);
		
		assertNotNull(restaurant);
		assertNotEquals(0, restaurant.getId());
		assertEquals(italianRestaurant.getName(), restaurant.getName());
	}
}

package fr.unice.polytech.si5.soa.a.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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
import fr.unice.polytech.si5.soa.a.entities.Restaurant;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfiguration.class})
public class RestaurantDaoTest {
	@Autowired
    private SessionFactory sessionFactory;
	
	@Autowired
	private IRestaurantDao restaurantDao;
	
	private Restaurant asianRestaurant;
	private Restaurant frenchRestaurant;
	
	@BeforeEach
	public void setup() throws Exception {
		asianRestaurant = new Restaurant();
    	asianRestaurant.setName("RizRiz");
    	asianRestaurant.setRestaurantAddress("47 avenue des bols");
    	
    	frenchRestaurant = new Restaurant();
    	frenchRestaurant.setName("La bonne baguette");
    	frenchRestaurant.setRestaurantAddress("14 rue du camenbert");
    	
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

            if(frenchRestaurant.getId()!=0){
                session.delete(frenchRestaurant);
            }

        	session.delete(asianRestaurant);

            session.flush();
            transaction.commit();

            asianRestaurant = null;
            frenchRestaurant = null;
        } catch (SQLGrammarException e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
	}
	
	@Test
	public void addRestaurant() {
        Restaurant restaurant = restaurantDao.addRestaurant(frenchRestaurant);

        assertNotNull(restaurant);
        assertNotEquals(0, restaurant.getId());
        assertEquals(frenchRestaurant.getName(), restaurant.getName());
    }
	
	@Test
	public void findRestaurantById() {
		Optional<Restaurant> restaurant = restaurantDao.findRestaurantById(asianRestaurant.getId());
		
		assertTrue(restaurant.isPresent());
		assertEquals(asianRestaurant.getName(), restaurant.get().getName());
	}
	
	@Test
	public void findRestaurantUsingNameAndAddress() {
		Optional<Restaurant> restaurant = restaurantDao.findRestaurant(asianRestaurant.getName(), asianRestaurant.getRestaurantAddress());
		
		assertTrue(restaurant.isPresent());
		assertEquals(asianRestaurant.getId(), restaurant.get().getId());
	}
}

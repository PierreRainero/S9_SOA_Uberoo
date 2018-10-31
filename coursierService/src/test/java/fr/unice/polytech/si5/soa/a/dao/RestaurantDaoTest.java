package fr.unice.polytech.si5.soa.a.dao;

import fr.unice.polytech.si5.soa.a.configuration.TestConfiguration;
import fr.unice.polytech.si5.soa.a.entities.Restaurant;
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

import java.util.Optional;

import static org.junit.Assert.assertEquals;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfiguration.class)
public class RestaurantDaoTest {

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private IRestaurantDao restaurantDao;

    private Restaurant restaurant;

    @BeforeEach
    public void setUp() {
        restaurant = new Restaurant();
        restaurant.setLatitude(0.2);
        restaurant.setLongitude(0.7);
        Session session = sessionFactory.openSession();
        try {
            session.save(restaurant);
            session.beginTransaction().commit();
        } catch (SQLGrammarException e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
    }

    @AfterEach
    public void cleanUp() {
        Session session = sessionFactory.openSession();
        Transaction transaction;
        try {
            transaction = session.beginTransaction();
            session.delete(restaurant);
            session.flush();
            transaction.commit();
            restaurant = null;
        } catch (SQLGrammarException e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
    }

    @Test
    public void findRestaurantById() {
        assertEquals(restaurant, restaurantDao.findRestaurantById(restaurant.getId()).get());
        assertEquals(Optional.empty(), restaurantDao.findRestaurantById(-6));
    }

}

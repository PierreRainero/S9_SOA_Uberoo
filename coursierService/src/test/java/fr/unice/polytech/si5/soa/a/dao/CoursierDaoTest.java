package fr.unice.polytech.si5.soa.a.dao;

import fr.unice.polytech.si5.soa.a.configuration.TestConfiguration;
import fr.unice.polytech.si5.soa.a.entities.Coursier;
import fr.unice.polytech.si5.soa.a.entities.Delivery;
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

import static org.junit.Assert.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfiguration.class})
public class CoursierDaoTest {
    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private ICoursierDao coursierDao;

    private Coursier coursier;

    private Delivery delivery;
    private Delivery delivery2;
    private Restaurant restaurant;

    @BeforeEach
    public void setUp() {
        this.coursier = new Coursier();
        coursier.setName("Jean");
        coursier.setAccountNumber("FR XXX XXXX");
        coursier.setId(9);

        restaurant = new Restaurant();
        restaurant.setLatitude(0.);
        restaurant.setLongitude(0.);

        delivery = new Delivery();
        delivery.setCoursier(coursier);
        delivery.setRestaurant(restaurant);

        delivery2 = new Delivery();
        delivery2.setCoursier(coursier);
        delivery2.setRestaurant(restaurant);

        coursier.addDelivery(delivery);

        Session session = sessionFactory.openSession();
        try {
            session.save(coursier);
            session.save(restaurant);
            session.save(delivery);
            //session.save(delivery2);
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
            session.delete(delivery);
            session.delete(restaurant);
            session.delete(coursier);
            session.delete(delivery2);
            session.flush();
            transaction.commit();
        } catch (SQLGrammarException e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
    }

    @Test
    public void findCoursierById() {
        assertEquals(Optional.empty(), coursierDao.findCoursierById(-2));
        assertEquals(coursier, coursierDao.findCoursierById(coursier.getId()).get());
    }

    @Test
    public void updateCoursier() {
        Optional<Coursier> coursierWrapped = coursierDao.findCoursierById(coursier.getId());
        if (coursierWrapped.isPresent()) {
            Coursier coursierByDao = coursierWrapped.get();
            assertTrue(coursierByDao.getDeliveries().size() == 1);
            assertTrue(coursier.getDeliveries().size() == 1);
            assertEquals(coursierByDao.getDeliveries().get(0), coursierByDao.getDeliveries().get(0));
            coursierByDao.addDelivery(delivery2);
            coursierByDao = coursierDao.updateCoursier(coursierByDao);
            assertFalse(coursierByDao.getDeliveries().size() == 1);
        }
    }
}

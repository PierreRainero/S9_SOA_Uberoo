package fr.unice.polytech.si5.soa.a.dao;

import fr.unice.polytech.si5.soa.a.configuration.TestConfiguration;
import fr.unice.polytech.si5.soa.a.entities.Feedback;
import fr.unice.polytech.si5.soa.a.entities.Meal;
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfiguration.class})
class FeedbackDaoTest {

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private IFeedbackDao feedbackDao;

    private Restaurant resto;
    private Meal pizzaFrommageMeal;
    private Meal pizzaBaconMeal;
    private Feedback oldFeedback;
    private Feedback newFeedback;
    

    @BeforeEach
    public void setup() throws Exception {
        resto = new Restaurant();
        resto.setName("Mister Pizza");
        resto.setRestaurantAddress("Espace st Philippe");

        pizzaFrommageMeal = new Meal();
        pizzaFrommageMeal.setName("Pizza frommage");
        pizzaFrommageMeal.setPrice(8);
        pizzaFrommageMeal.setRestaurant(resto);
        
        pizzaBaconMeal = new Meal();
        pizzaBaconMeal.setName("Pizza Bacon");
        pizzaBaconMeal.setPrice(9);
        pizzaBaconMeal.setRestaurant(resto);
        
        oldFeedback = new Feedback();
        oldFeedback.setAuthor("John");
        oldFeedback.setContent("Bof");
        oldFeedback.setMeal(pizzaFrommageMeal);

        newFeedback = new Feedback();
        newFeedback.setAuthor("Jack");
        newFeedback.setContent("Dégueu");
        newFeedback.setMeal(pizzaFrommageMeal);

        Session session = sessionFactory.openSession();
        try {
            session.save(resto);
            session.save(pizzaFrommageMeal);
            session.save(pizzaBaconMeal);
            session.save(oldFeedback);
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

            if(newFeedback.getId()!=0){
                session.delete(newFeedback);
            }
            
            session.delete(pizzaBaconMeal);
            session.delete(oldFeedback);
        	session.delete(resto);
            session.delete(pizzaFrommageMeal);

            session.flush();
            transaction.commit();

            resto = null;
            pizzaFrommageMeal = null;
            pizzaBaconMeal = null;
            newFeedback = null;
            oldFeedback = null;
        } catch (SQLGrammarException e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
	}

    @Test
    public void addFeedback() {
        Feedback f = feedbackDao.addFeedback(newFeedback);

        assertNotNull(f);
        assertNotEquals(0, f.getId());
        assertEquals(f.getContent(), newFeedback.getContent());
    }
    
    @Test
    public void listFeedbackByMeal() {
    	List<Feedback> result = feedbackDao.findFeedBackByMeal(pizzaFrommageMeal);
    	assertEquals(1, result.size());
    	
    	Session session = sessionFactory.openSession();
        try {
            session.save(newFeedback);
            session.beginTransaction().commit();
        } catch (SQLGrammarException e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        result = feedbackDao.findFeedBackByMeal(pizzaFrommageMeal);
    	assertEquals(2, result.size());
        
    	result = feedbackDao.findFeedBackByMeal(pizzaBaconMeal);
    	assertEquals(0, result.size());
    }
}
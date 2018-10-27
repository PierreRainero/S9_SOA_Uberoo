package fr.unice.polytech.si5.soa.a.dao;

import fr.unice.polytech.si5.soa.a.configuration.TestConfiguration;
import fr.unice.polytech.si5.soa.a.entities.Feedback;
import fr.unice.polytech.si5.soa.a.entities.Meal;
import fr.unice.polytech.si5.soa.a.entities.Restaurant;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.exception.SQLGrammarException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfiguration.class})
class FeedbackDaoTest {

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private IFeedbackDao mealDao;

    private Restaurant resto;
    private Meal pizzaMeal;
    private Feedback feedback;

    @BeforeEach
    public void setup() throws Exception {
        resto = new Restaurant();
        resto.setName("Mister Pizza");
        resto.setRestaurantAddress("Espace st Philippe");

        pizzaMeal = new Meal();
        pizzaMeal.setName("Pizza");
        pizzaMeal.setPrice(8);
        pizzaMeal.setRestaurant(resto);

        feedback = new Feedback();
        feedback.setContent("Dégueu");
        feedback.setMeal(pizzaMeal);
        feedback.setRestaurant(resto);

        Session session = sessionFactory.openSession();
        try {
            session.save(resto);
            session.save(pizzaMeal);
            session.beginTransaction().commit();
        } catch (SQLGrammarException e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }

    }

    @Test
    void addFeedback() {
        Feedback f = mealDao.addFeedback(feedback);

        assertNotNull(f);
        assertNotEquals(0, f.getId());
        assertEquals(f.getContent(), feedback.getContent());
    }
}
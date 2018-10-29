package fr.unice.polytech.si5.soa.a.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
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
import fr.unice.polytech.si5.soa.a.entities.Ingredient;
import fr.unice.polytech.si5.soa.a.entities.Meal;
import fr.unice.polytech.si5.soa.a.entities.Restaurant;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfiguration.class})
public class MealDaoTest {
	@Autowired
    private SessionFactory sessionFactory;
	
	@Autowired
	private IMealDao mealDao;
	
	private Restaurant asianRestaurant;
	private Ingredient pork;
	private Meal ramen;
	private Meal sushis;
	
	@BeforeEach
	public void setup() throws Exception {
		asianRestaurant = new Restaurant();
    	asianRestaurant.setName("RizRiz");
    	asianRestaurant.setRestaurantAddress("47 avenue des bols");
    	
		ramen = new Meal();
        ramen.setName("Ramen soup");
        ramen.setPrice(10);
        ramen.setRestaurant(asianRestaurant);
        
        sushis = new Meal();
        sushis.setName("Sushis");
        sushis.setPrice(12);
        sushis.setRestaurant(asianRestaurant);
        
        pork = new Ingredient();
        pork.setName("Porc");
        
        Session session = sessionFactory.openSession();
        try {
        	session.save(asianRestaurant);
            session.save(ramen);
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

            if(sushis.getId()!=0){
                session.delete(sushis);
            }
            
            if(pork.getId()!=0) {
            	session.delete(pork);
            }

        	session.delete(asianRestaurant);
            session.delete(ramen);

            session.flush();
            transaction.commit();

            asianRestaurant = null;
            sushis = null;
            ramen = null;
            pork = null;
        } catch (SQLGrammarException e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
	}
	
	@Test
	public void addMeal() {
        Meal meal = mealDao.addMeal(sushis);

        assertNotNull(meal);
        assertNotEquals(0, meal.getId());
        assertEquals(sushis.getName(), meal.getName());
    }
	
	@Test
	public void findMealById() {
		Optional<Meal> meal = mealDao.findMealById(ramen.getId());
		
		assertTrue(meal.isPresent());
		assertEquals(ramen.getName(), meal.get().getName());
	}
	
	@Test
	public void findMealByName() {
		Optional<Meal> meal = mealDao.findMealByName(ramen.getName());
		
		assertTrue(meal.isPresent());
		assertEquals(ramen.getName(), meal.get().getName());
	}
	
	@Test
	public void addIngredient() {
        Ingredient ingredient = mealDao.addIngredient(pork);

        assertNotNull(ingredient);
        assertNotEquals(0, ingredient.getId());
        assertEquals(pork.getName(), ingredient.getName());
    }
	
	@Test
	public void findIngredientByName() {
		Session session = sessionFactory.openSession();
        try {
            session.save(pork);
            session.beginTransaction().commit();
        } catch (SQLGrammarException e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        Optional<Ingredient> ingredient = mealDao.findIngredientByName(pork.getName());
		
		assertTrue(ingredient.isPresent());
		assertEquals(pork.getName(), ingredient.get().getName());
    }
	
	@Test
	public void findExistingMealByNameForRestaurant() {
		Optional<Meal> result = mealDao.findMealByNameForRestaurant(ramen.getName(), asianRestaurant);
		assertTrue(result.isPresent());
	}
	
	@Test
	public void findNonExistingMealByNameForRestaurant() {
		Optional<Meal> result = mealDao.findMealByNameForRestaurant(sushis.getName(), asianRestaurant);
		assertFalse(result.isPresent());
	}
}

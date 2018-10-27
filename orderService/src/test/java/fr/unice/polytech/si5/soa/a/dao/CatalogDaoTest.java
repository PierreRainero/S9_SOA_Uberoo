package fr.unice.polytech.si5.soa.a.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;
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
import fr.unice.polytech.si5.soa.a.entities.Meal;
import fr.unice.polytech.si5.soa.a.entities.Restaurant;

/**
 * Class name	CatalogDaoTest
 * Date			01/10/2018
 * @author		PierreRainero
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { TestConfiguration.class })
public class CatalogDaoTest {
	private static final String ASIAN_CATEGORY = "Asian";
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Autowired
	private ICatalogDao catalogDao;
	
	private Restaurant dragonRestaurant;
	private Restaurant lionRestaurant;
	
	private Meal ramenFromDragon;
	private Meal ramenFromLion;
	private Meal sushis;
	
	@BeforeEach
	public void setUp() throws Exception {
		dragonRestaurant = new Restaurant();
		dragonRestaurant.setName("Dragon rouge");
		dragonRestaurant.setRestaurantAddress("26 rue des nems");
		
		lionRestaurant = new Restaurant();
		lionRestaurant.setName("Lion d'or");
		lionRestaurant.setRestaurantAddress("22 rue des nems");
		
		ramenFromLion = new Meal();
		ramenFromLion.setName("Ramen soup");
		ramenFromLion.addTag(ASIAN_CATEGORY);
		ramenFromLion.setRestaurant(lionRestaurant);
		ramenFromLion.setPrice(11.5);
		
		ramenFromDragon = new Meal();
		ramenFromDragon.setName("Ramen soup");
		ramenFromDragon.addTag(ASIAN_CATEGORY);
		ramenFromDragon.setRestaurant(dragonRestaurant);
		ramenFromDragon.setPrice(10.99);
		
		sushis = new Meal();
		sushis.setName("Sushis");
		sushis.addTag(ASIAN_CATEGORY);
		sushis.setPrice(7.5);
		
		Session session = sessionFactory.openSession();
		try {
			session.save(dragonRestaurant);
			session.save(lionRestaurant);
			session.save(ramenFromLion);
			session.save(sushis);
			session.beginTransaction().commit();
		} catch (SQLGrammarException e) {
			session.getTransaction().rollback();
		} finally {
			session.close();
		}
	}
	
	@AfterEach
	public void cleanUp() throws Exception {
		List<Meal> result = catalogDao.listMeals();
		
		Session session = sessionFactory.openSession();
	    Transaction transaction = null;
	    try {
	    	transaction = session.beginTransaction();
	    	
	    	if(ramenFromDragon.getId() != 0) {
	    		session.delete(ramenFromDragon);
	    	}
	    	
	    	
	    	session.delete(dragonRestaurant);
			session.delete(lionRestaurant);
	    	
			session.delete(ramenFromLion);
			session.delete(sushis);
			
			session.flush();
			transaction.commit();

			dragonRestaurant = null;
			lionRestaurant = null;
			ramenFromDragon = null;
			ramenFromLion = null;
			sushis = null;
		} catch (SQLGrammarException e) {
			session.getTransaction().rollback();
		} finally {
			session.close();
		}
	}
	
	@Test
	public void findExistingMealByName() {
		Optional<Meal> mealWrapped = catalogDao.findMealByName(ramenFromLion.getName());
		
		assertTrue(mealWrapped.isPresent());
		
		Meal meal = mealWrapped.get();
		assertEquals(ramenFromLion.getName(), meal.getName());
	}
	
	@Test
	public void dontFindNonExistingMealByName() {
		Optional<Meal> mealWrapped = catalogDao.findMealByName("superfétatoire");
		
		assertFalse(mealWrapped.isPresent());
	}
	
	@Test
	public void findAMealsByTag() {
		List<Meal> result = catalogDao.findMealsByTag(ASIAN_CATEGORY);
		
		assertFalse(result.isEmpty());
		assertEquals(2, result.size());
	}
	
	@Test
	public void dontFindAnyMealByTag() {
		List<Meal> result = catalogDao.findMealsByTag("superfétatoire");
		assertTrue(result.isEmpty());
	}
	
	@Test
	public void findMealsFromDifferentRestaurants() {
		Session session = sessionFactory.openSession();
		try {
			session.save(ramenFromDragon);
			session.beginTransaction().commit();
		} catch (SQLGrammarException e) {
			session.getTransaction().rollback();
		} finally {
			session.close();
		}
		
		List<Meal> result = catalogDao.findMealsByTag(ASIAN_CATEGORY);
		assertFalse(result.isEmpty());
		assertEquals(3, result.size());
		assertTrue(result.contains(ramenFromLion));
		assertTrue(result.contains(ramenFromDragon));
	}
	
	@Test
	public void findMealsForARestaurant() {
		List<Meal> result = catalogDao.findMealsByRestaurant(lionRestaurant);
		assertEquals(1, result.size());
		
		sushis.setRestaurant(lionRestaurant);
		Session session = sessionFactory.openSession();
		try {
			session.merge(sushis);
			session.beginTransaction().commit();
		} catch (SQLGrammarException e) {
			session.getTransaction().rollback();
		} finally {
			session.close();
		}
		result = catalogDao.findMealsByRestaurant(lionRestaurant);
		assertEquals(2, result.size());
		
		result = catalogDao.findMealsByRestaurant(dragonRestaurant);
		assertEquals(0, result.size());
	}
	
	@Test
	public void listEveryMeal() {
		ramenFromDragon.removeTag(ASIAN_CATEGORY);
		ramenFromDragon.addTag("Something else");
		Session session = sessionFactory.openSession();
		try {
			session.save(ramenFromDragon);
			session.beginTransaction().commit();
		} catch (SQLGrammarException e) {
			session.getTransaction().rollback();
		} finally {
			session.close();
		}
		
		List<Meal> result = catalogDao.listMeals();
		assertEquals(3, result.size());
	}
}

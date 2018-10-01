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
	
	private Meal ramen;
	private Meal sushis;
	
	@BeforeEach
	public void setUp() throws Exception {
		ramen = new Meal();
		ramen.setName("Ramen soup");
		ramen.addTag(ASIAN_CATEGORY);
		
		sushis = new Meal();
		sushis.setName("Sushis");
		sushis.addTag(ASIAN_CATEGORY);
		
		Session session = sessionFactory.openSession();
		try {
			session.save(ramen);
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
		Session session = sessionFactory.openSession();
	    Transaction transaction = null;
	    try {
	    	transaction = session.beginTransaction();
	    	
			session.delete(ramen);
			session.delete(sushis);
			
			session.flush();
			transaction.commit();

			ramen = null;
			sushis = null;
		} catch (SQLGrammarException e) {
			session.getTransaction().rollback();
		} finally {
			session.close();
		}
	}
	
	@Test
	public void findExistingMealByName() {
		Optional<Meal> mealWrapped = catalogDao.findMealByName(ramen.getName());
		
		assertTrue(mealWrapped.isPresent());
		
		Meal meal = mealWrapped.get();
		assertEquals(ramen.getName(), meal.getName());
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
}

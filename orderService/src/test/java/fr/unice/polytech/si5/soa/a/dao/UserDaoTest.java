package fr.unice.polytech.si5.soa.a.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
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

import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import fr.unice.polytech.si5.soa.a.configuration.TestConfiguration;
import fr.unice.polytech.si5.soa.a.entities.User;

/**
 * Class name	OrderTakerDaoTest
 * Date			29/09/2018
 * @author		PierreRainero
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { TestConfiguration.class })
public class UserDaoTest {
	@Autowired
	private SessionFactory sessionFactory;
	
	@Autowired
	private IUserDao userDao;
	
	private User bob;
	
	@BeforeEach
	public void setUp() throws Exception {
		
		bob = new User();
		bob.setFirstName("Bob");
		bob.setLastName("Harington");
		
		Session session = sessionFactory.openSession();
		try {
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
	    	
			session.delete(bob);
			
			session.flush();
			transaction.commit();

			bob = null;
		} catch (SQLGrammarException e) {
			session.getTransaction().rollback();
		} finally {
			session.close();
		}
	}
	
	@Test
	public void findExistingUserById() {
		Optional<User> userWrapped = userDao.findUserById(bob.getId());
		
		assertTrue(userWrapped.isPresent());
		
		User user = userWrapped.get();
		assertEquals(bob.getFirstName(), user.getFirstName());
	}
	
	@Test
	public void dontFindNonExistingUserById() {
		Optional<User> userWrapped = userDao.findUserById(-1);
		
		assertFalse(userWrapped.isPresent());
	}
}

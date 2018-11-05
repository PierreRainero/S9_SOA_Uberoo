package fr.unice.polytech.si5.soa.a.dao.component;

import java.util.Optional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.exception.SQLGrammarException;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import fr.unice.polytech.si5.soa.a.dao.IUserDao;
import fr.unice.polytech.si5.soa.a.entities.Meal;
import fr.unice.polytech.si5.soa.a.entities.User;

/**
 * Class name	UserDaoImpl
 * @see 		IUserDao
 * Date			30/09/2018
 * @author		PierreRainero
 **/
@Primary
@Repository
@Transactional
public class UserDaoImpl implements IUserDao {
	private static Logger logger = LogManager.getLogger(OrderTakerDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	/**
	 * {@inheritDoc}
	 */
	public Optional<User> findUserById(int userId) {
		Session session = sessionFactory.getCurrentSession();

		Optional<User> result = Optional.empty();
		try {
			User user = (User) session.get(User.class, userId);

			if(user!=null){
				result = Optional.of(user);
			}
		} catch (SQLGrammarException e) {
			logger.error("Cannot execute query : findUserById", e);
		}

		return result;
	}

	@Override
	/**
	 * {@inheritDoc}
	 */
	public User addUser(User userToAdd) {
		Session session = sessionFactory.getCurrentSession();

		try {
			session.save(userToAdd);
		} catch (SQLGrammarException e) {
			session.getTransaction().rollback();
			logger.error("Cannot execute query : addUser", e);
		}

		return userToAdd;
	}

	@Override
	public Optional<User> findUserByName(String userFirstName, String userLastName) {
		Session session = sessionFactory.getCurrentSession();
		CriteriaBuilder builder = session.getCriteriaBuilder();
		CriteriaQuery<User> criteria = builder.createQuery(User.class);
		Root<User> root =  criteria.from(User.class);
		criteria.select(root).where(
				builder.and(
						builder.equal(root.get("firstName"), userFirstName),
						builder.equal(root.get("lastName"), userLastName)
						));
		Query<User> query = session.createQuery(criteria);
		
		try {
			return Optional.of(query.getSingleResult());
		}catch(Exception e) {
			return Optional.empty();
		}
	}

}

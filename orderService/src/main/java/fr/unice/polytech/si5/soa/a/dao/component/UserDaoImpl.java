package fr.unice.polytech.si5.soa.a.dao.component;

import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.exception.SQLGrammarException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import fr.unice.polytech.si5.soa.a.dao.IUserDao;
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

}

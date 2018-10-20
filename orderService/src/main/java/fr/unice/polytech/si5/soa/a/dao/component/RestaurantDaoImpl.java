package fr.unice.polytech.si5.soa.a.dao.component;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.exception.SQLGrammarException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import fr.unice.polytech.si5.soa.a.dao.IRestaurantDao;
import fr.unice.polytech.si5.soa.a.entities.Restaurant;

/**
 * Class name	RestaurantDaoImpl
 * @see 		IRestaurantDao
 * Date			20/10/2018
 * @author		PierreRainero
**/
@Primary
@Repository
@Transactional
public class RestaurantDaoImpl implements IRestaurantDao{
	private static Logger logger = LogManager.getLogger(RestaurantDaoImpl.class);
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	/**
     * {@inheritDoc}
     */
	public Restaurant addRestaurant(Restaurant restaurantToAdd) {
		Session session = sessionFactory.getCurrentSession();

		try {
			session.save(restaurantToAdd);
		} catch (SQLGrammarException e) {
			session.getTransaction().rollback();
			logger.error("Cannot execute query : addRestaurant", e);
		}

		return restaurantToAdd;
	}

}

package fr.unice.polytech.si5.soa.a.dao.component;

import java.util.List;
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

import fr.unice.polytech.si5.soa.a.dao.IRestaurantDao;
import fr.unice.polytech.si5.soa.a.entities.Feedback;
import fr.unice.polytech.si5.soa.a.entities.Meal;
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
	
	@Override
	/**
     * {@inheritDoc}
     */
	public Meal addMeal(Meal mealToAdd) {
		Session session = sessionFactory.getCurrentSession();

		try {
			session.save(mealToAdd);
		} catch (SQLGrammarException e) {
			session.getTransaction().rollback();
			logger.error("Cannot execute query : addMeal", e);
		}

		return mealToAdd;
	}

	@Override
	/**
     * {@inheritDoc}
     */
	public List<Restaurant> findRestaurantByName(String name) {
		Session session = sessionFactory.getCurrentSession();
		CriteriaBuilder builder = session.getCriteriaBuilder();
		CriteriaQuery<Restaurant> criteria = builder.createQuery(Restaurant.class);
		Root<Restaurant> root =  criteria.from(Restaurant.class);
		
		criteria.select(root).where(builder.like(builder.upper(root.get("name")), "%"+name.toUpperCase()+"%"));
		Query<Restaurant> query = session.createQuery(criteria);
		
		return query.getResultList();
	}

	@Override
	/**
     * {@inheritDoc}
     */
	public Optional<Restaurant> findRestaurantById(int id) {
		Session session = sessionFactory.getCurrentSession();

		Optional<Restaurant> result = Optional.empty();
		try {
			Restaurant restaurant = (Restaurant) session.get(Restaurant.class, id);

			if(restaurant!=null){
				result = Optional.of(restaurant);
			}
		} catch (SQLGrammarException e) {
			logger.error("Cannot execute query : findRestaurantById", e);
		}

		return result;
	}

	@Override
	/**
     * {@inheritDoc}
     */
	public List<Restaurant> listRestaurants() {
		Session session = sessionFactory.getCurrentSession();
		CriteriaBuilder builder = session.getCriteriaBuilder();
		CriteriaQuery<Restaurant> criteria = builder.createQuery(Restaurant.class);
		Root<Restaurant> root =  criteria.from(Restaurant.class);
		
		criteria.select(root);
		Query<Restaurant> query = session.createQuery(criteria);
		
		return query.getResultList();
	}

	@Override
	/**
     * {@inheritDoc}
     */
	public Optional<Restaurant> findRestaurantByNameAndAddress(String name, String address) {
		Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Restaurant> criteria = builder.createQuery(Restaurant.class);
        Root<Restaurant> root =  criteria.from(Restaurant.class);
        criteria.select(root).where(
        		builder.and(
        				builder.equal(root.get("name"), name), 
        				builder.equal(root.get("restaurantAddress"), address)
        				));
        Query<Restaurant> query = session.createQuery(criteria);

        try {
            return Optional.of(query.getSingleResult());
        }catch(Exception e) {
            return Optional.empty();
        }
	}

	@Override
	/**
     * {@inheritDoc}
     */
	public Feedback addFeedback(Feedback feedbackToAdd) {
		Session session = sessionFactory.getCurrentSession();

		try {
			session.save(feedbackToAdd);
		} catch (SQLGrammarException e) {
			session.getTransaction().rollback();
			logger.error("Cannot execute query : addFeedback", e);
		}

		return feedbackToAdd;
	}

	@Override
	/**
     * {@inheritDoc}
     */
	public Optional<Meal> findMealById(int id) {
		Session session = sessionFactory.getCurrentSession();

		Optional<Meal> result = Optional.empty();
		try {
			Meal meal = (Meal) session.get(Meal.class, id);

			if(meal!=null){
				result = Optional.of(meal);
			}
		} catch (SQLGrammarException e) {
			logger.error("Cannot execute query : findRestaurantById", e);
		}

		return result;
	}
}

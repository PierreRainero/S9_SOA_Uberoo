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

import fr.unice.polytech.si5.soa.a.dao.ICatalogDao;
import fr.unice.polytech.si5.soa.a.entities.Feedback;
import fr.unice.polytech.si5.soa.a.entities.Meal;
import fr.unice.polytech.si5.soa.a.entities.Restaurant;

/**
 * Class name	CatalogImpl
 * @see 		ICatalogDao
 * Date			01/10/2018
 * @author		PierreRainero
 * 
 * @version		1.2
 * Date			29/10/2018
 **/
@Primary
@Repository
@Transactional
public class CatalogDaoImpl implements ICatalogDao {
	private static Logger logger = LogManager.getLogger(CatalogDaoImpl.class);
	
	@Autowired
	private SessionFactory sessionFactory;

	@Override
	/**
     * {@inheritDoc}
     */
	public Optional<Meal> findMealByName(String name) {
		Session session = sessionFactory.getCurrentSession();
		CriteriaBuilder builder = session.getCriteriaBuilder();
		CriteriaQuery<Meal> criteria = builder.createQuery(Meal.class);
		Root<Meal> root =  criteria.from(Meal.class);
		criteria.select(root).where(builder.equal(root.get("name"), name));
		Query<Meal> query = session.createQuery(criteria);
		
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
	public List<Meal> findMealsByTag(String tag) {
		Session session = sessionFactory.getCurrentSession();
		CriteriaBuilder builder = session.getCriteriaBuilder();
		CriteriaQuery<Meal> criteria = builder.createQuery(Meal.class);
		Root<Meal> root =  criteria.from(Meal.class);
		
		criteria.select(root).where(builder.isMember(tag, root.get("tags")));
		Query<Meal> query = session.createQuery(criteria);
		
		return query.getResultList();
	}

	@Override
	/**
     * {@inheritDoc}
     */
	public List<Meal> findMealsByRestaurant(Restaurant restaurant) {
		Session session = sessionFactory.getCurrentSession();
		CriteriaBuilder builder = session.getCriteriaBuilder();
		CriteriaQuery<Meal> criteria = builder.createQuery(Meal.class);
		Root<Meal> root =  criteria.from(Meal.class);
		
		criteria.select(root).where(builder.equal(root.get("restaurant"), restaurant));
		Query<Meal> query = session.createQuery(criteria);
		
		return query.getResultList();
	}

	@Override
	/**
     * {@inheritDoc}
     */
	public List<Meal> listMeals() {
		Session session = sessionFactory.getCurrentSession();
		CriteriaBuilder builder = session.getCriteriaBuilder();
		CriteriaQuery<Meal> criteria = builder.createQuery(Meal.class);
		Root<Meal> root =  criteria.from(Meal.class);
		
		criteria.select(root);
		Query<Meal> query = session.createQuery(criteria);
		
		return query.getResultList();
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

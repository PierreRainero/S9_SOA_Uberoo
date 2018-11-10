package fr.unice.polytech.si5.soa.a.dao.component;

import java.util.Date;
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

import fr.unice.polytech.si5.soa.a.dao.IOrderDao;
import fr.unice.polytech.si5.soa.a.entities.Meal;
import fr.unice.polytech.si5.soa.a.entities.OrderState;
import fr.unice.polytech.si5.soa.a.entities.Restaurant;
import fr.unice.polytech.si5.soa.a.entities.RestaurantOrder;

/**
 * Class name	OrderDaoImpl
 * @see			IOrderDao
 * Date			08/10/2018
 * @author		PierreRainero
 * 
 * @version		1.1
 * Date			23/10/2018
 */
@Primary
@Repository
@Transactional
public class OrderDaoImpl implements IOrderDao {
	private static Logger logger = LogManager.getLogger(OrderDaoImpl.class);
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	public RestaurantOrder addOrder(RestaurantOrder orderToAdd) {
		Session session = sessionFactory.getCurrentSession();

		try {
			session.save(orderToAdd);
		} catch (SQLGrammarException e) {
			session.getTransaction().rollback();
			logger.error("Cannot execute query : addOrder", e);
		}

		return orderToAdd;
	}

	@Override
	public RestaurantOrder updateOrder(RestaurantOrder orderToUpdate) {
		Session session = sessionFactory.getCurrentSession();

		RestaurantOrder result = null;
		try {
            result = (RestaurantOrder) session.merge(orderToUpdate);
		} catch (SQLGrammarException e) {
			session.getTransaction().rollback();
			logger.error("Cannot execute query : updateOrder", e);
		}

		return result;
	}

	@Override
	public Optional<RestaurantOrder> findOrderById(int id) {
		Session session = sessionFactory.getCurrentSession();

		Optional<RestaurantOrder> result = Optional.empty();
		try {
			RestaurantOrder order = (RestaurantOrder) session.get(RestaurantOrder.class, id);

			if(order!=null){
				result = Optional.of(order);
			}
		} catch (SQLGrammarException e) {
			logger.error("Cannot execute query : findOrderById", e);
		}

		return result;
	}

	@Override
	public List<RestaurantOrder> getOrdersToDo(Restaurant restaurantConcerned) {
		Session session = sessionFactory.getCurrentSession();
		CriteriaBuilder builder = session.getCriteriaBuilder();
		CriteriaQuery<RestaurantOrder> criteria = builder.createQuery(RestaurantOrder.class);
		Root<RestaurantOrder> root =  criteria.from(RestaurantOrder.class);
		criteria.select(root).where(
				builder.and(
						builder.equal(root.get("restaurant"), restaurantConcerned),
						builder.equal(root.get("state"), OrderState.TO_PREPARE))
				);
		Query<RestaurantOrder> query = session.createQuery(criteria);
		
		return query.getResultList();
	}

	@Override
	public Optional<RestaurantOrder> findOrderWithMinimumsInfos(Restaurant restaurant, String deliveryAddress, List<Meal> meals, Date validationDate) {
		Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<RestaurantOrder> criteria = builder.createQuery(RestaurantOrder.class);
        Root<RestaurantOrder> root =  criteria.from(RestaurantOrder.class);
        // TODO 
        // Add delivery address
        criteria.select(root).where(
        		builder.and(
        				builder.equal(root.get("restaurant"), restaurant),
        				builder.equal(root.get("validationDate"), validationDate)
        				));
        Query<RestaurantOrder> query = session.createQuery(criteria);

        /*
         * Controlling meals is heavy in SQL, it's faster to do it here :
         */
        try {
        	List<RestaurantOrder> res = query.getResultList();
        	// If there is only one result and there is no meals to control
        	if(meals.isEmpty() && res.size()==1) {
        		return Optional.of(res.get(0));
        	}
        	
        	for(RestaurantOrder tmp : res) {
        		// The number of meals doesn't match
        		if(tmp.getMeals().size() != meals.size()) {
        			continue;
        		}
        		
        		boolean isTheOne = true;
        		for(Meal meal : meals) {
        			// If one of the meals doesn't match
            		if(!tmp.getMeals().contains(meal)) {
            			isTheOne = false;
            			break;
            		}
            	}
        		// If every filters are green
        		if(isTheOne) {
        			return Optional.of(tmp);
        		}
        	}
        	
        	// If no order matched
        	return Optional.empty();
        	
        }catch(Exception e) {
            return Optional.empty();
        }
	}

}

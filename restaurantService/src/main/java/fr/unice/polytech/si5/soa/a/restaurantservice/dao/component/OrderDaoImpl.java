package fr.unice.polytech.si5.soa.a.restaurantservice.dao.component;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.exception.SQLGrammarException;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import fr.unice.polytech.si5.soa.a.restaurantservice.dao.IOrderDao;
import fr.unice.polytech.si5.soa.a.restaurantservice.model.OrderToPrepare;

@Primary
@Repository
@Transactional
public class OrderDaoImpl implements IOrderDao {
	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	public OrderToPrepare addOrder(OrderToPrepare orderToAdd) {
		Session session = sessionFactory.getCurrentSession();

		try {
			session.save(orderToAdd);
		} catch (SQLGrammarException e) {
			session.getTransaction().rollback();
		}

		return orderToAdd;
	}

	@Override
	public List<OrderToPrepare> getOrders() {
		Session session = sessionFactory.getCurrentSession();
		CriteriaBuilder builder = session.getCriteriaBuilder();
		CriteriaQuery<OrderToPrepare> criteria = builder.createQuery(OrderToPrepare.class);
		Root<OrderToPrepare> root =  criteria.from(OrderToPrepare.class);
		
		criteria.select(root);
		Query<OrderToPrepare> query = session.createQuery(criteria);
		
		return query.getResultList();
	}

}

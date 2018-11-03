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

import fr.unice.polytech.si5.soa.a.dao.IOrderTakerDao;
import fr.unice.polytech.si5.soa.a.entities.UberooOrder;

/**
 * Class name	OrderTakerImpl
 * @see 		IOrderTakerDao
 * Date			29/09/2018
 * @author		PierreRainero
**/
@Primary
@Repository
@Transactional
public class OrderTakerDaoImpl implements IOrderTakerDao {
	private static Logger logger = LogManager.getLogger(OrderTakerDaoImpl.class);
	
	@Autowired
	private SessionFactory sessionFactory;

	@Override
	/**
     * {@inheritDoc}
     */
	public UberooOrder addOrder(UberooOrder orderToAdd) {
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
	/**
     * {@inheritDoc}
     */
	public UberooOrder updateOrder(UberooOrder orderToUpdate) {
		Session session = sessionFactory.getCurrentSession();

		UberooOrder result = null;
		try {
            result = (UberooOrder) session.merge(orderToUpdate);
		} catch (SQLGrammarException e) {
			session.getTransaction().rollback();
			logger.error("Cannot execute query : updateOrder", e);
		}

		return result;
	}

	@Override
	/**
     * {@inheritDoc}
     */
	public Optional<UberooOrder> findOrderById(int orderId) {
		Session session = sessionFactory.getCurrentSession();

		Optional<UberooOrder> result = Optional.empty();
		try {
			UberooOrder order = (UberooOrder) session.get(UberooOrder.class, orderId);

			if(order!=null){
				result = Optional.of(order);
			}
		} catch (SQLGrammarException e) {
			logger.error("Cannot execute query : findOrderById", e);
		}

		return result;
	}

}

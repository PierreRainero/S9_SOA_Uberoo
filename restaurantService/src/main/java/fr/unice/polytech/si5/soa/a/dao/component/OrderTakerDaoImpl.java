package fr.unice.polytech.si5.soa.a.dao.component;

import fr.unice.polytech.si5.soa.a.dao.IOrderTakerDao;
import fr.unice.polytech.si5.soa.a.entities.OrderToPrepare;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.exception.SQLGrammarException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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
	public OrderToPrepare addOrder(OrderToPrepare commandToAdd) {
		Session session = sessionFactory.getCurrentSession();

		try {
			session.save(commandToAdd);
		} catch (SQLGrammarException e) {
			session.getTransaction().rollback();
			logger.error("Cannot execute query : addCommand", e);
		}

		return commandToAdd;
	}

	@Override
	/**
     * {@inheritDoc}
     */
	public OrderToPrepare updateOrder(OrderToPrepare orderToUpdate) {
		Session session = sessionFactory.getCurrentSession();

		OrderToPrepare result = null;
		try {
            result = (OrderToPrepare) session.merge(orderToUpdate);
		} catch (SQLGrammarException e) {
			session.getTransaction().rollback();
			logger.error("Cannot execute query : updateOrder", e);
		}

		return result;
	}


	@Override
	public List<OrderToPrepare> getOrdersToDo() {
		return null;
	}

	@Override
	/**
     * {@inheritDoc}
     */
	public Optional<OrderToPrepare> findOrderById(int orderId) {
		Session session = sessionFactory.getCurrentSession();

		Optional<OrderToPrepare> result = Optional.empty();
		try {
			OrderToPrepare order = (OrderToPrepare) session.get(OrderToPrepare.class, orderId);

			if(order!=null){
				result = Optional.of(order);
			}
		} catch (SQLGrammarException e) {
			logger.error("Cannot execute query : findOrderById", e);
		}

		return result;
	}

}

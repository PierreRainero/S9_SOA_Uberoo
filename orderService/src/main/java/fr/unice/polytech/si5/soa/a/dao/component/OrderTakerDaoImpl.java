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

import fr.unice.polytech.si5.soa.a.dao.IOrderTakerDao;
import fr.unice.polytech.si5.soa.a.entities.Order;

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
	public Order addOrder(Order commandToAdd) {
		Session session = sessionFactory.getCurrentSession();

		try {
			session.save(commandToAdd);
		} catch (SQLGrammarException e) {
			session.getTransaction().rollback();
			logger.error("Cannot execute query : addCommand", e);
		}

		return commandToAdd;
	}

}

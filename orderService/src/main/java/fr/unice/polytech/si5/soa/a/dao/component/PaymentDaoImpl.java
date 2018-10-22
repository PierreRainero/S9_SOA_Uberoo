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

import fr.unice.polytech.si5.soa.a.dao.IPaymentDao;
import fr.unice.polytech.si5.soa.a.entities.Payment;

/**
 * Class name	PaymentDAOImpl
 * @see 		IRestaurantDao
 * Date			22/10/2018
 * @author		PierreRainero
**/
@Primary
@Repository
@Transactional
public class PaymentDaoImpl implements IPaymentDao {
	private static Logger logger = LogManager.getLogger(PaymentDaoImpl.class);
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	/**
     * {@inheritDoc}
     */
	public Payment addPayment(Payment paymentToAdd) {
		Session session = sessionFactory.getCurrentSession();

		try {
			session.save(paymentToAdd);
		} catch (SQLGrammarException e) {
			session.getTransaction().rollback();
			logger.error("Cannot execute query : addPayment", e);
		}

		return paymentToAdd;
	}

	@Override
	/**
     * {@inheritDoc}
     */
	public Optional<Payment> findPaymentById(int idToSearch) {
		Session session = sessionFactory.getCurrentSession();

		Optional<Payment> result = Optional.empty();
		try {
			Payment restaurant = (Payment) session.get(Payment.class, idToSearch);

			if(restaurant!=null){
				result = Optional.of(restaurant);
			}
		} catch (SQLGrammarException e) {
			logger.error("Cannot execute query : findPaymentById", e);
		}

		return result;
	}

}

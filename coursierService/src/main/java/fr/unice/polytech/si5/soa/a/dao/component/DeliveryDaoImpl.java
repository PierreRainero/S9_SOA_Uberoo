package fr.unice.polytech.si5.soa.a.dao.component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

import fr.unice.polytech.si5.soa.a.dao.IDeliveryDao;
import fr.unice.polytech.si5.soa.a.entities.Delivery;

/**
 * Class name	DeliveryDaoImpl
 *
 * @author PierreRainero
 * @see IDeliveryDao Date			08/10/2018
 */
@Primary
@Repository
@Transactional
public class DeliveryDaoImpl implements IDeliveryDao {
    private static Logger logger = LogManager.getLogger(DeliveryDaoImpl.class);

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public Delivery addDelivery(Delivery deliveryToAdd) {
        Session session = sessionFactory.getCurrentSession();

        try {
            session.save(deliveryToAdd);
        } catch (SQLGrammarException e) {
            session.getTransaction().rollback();
            logger.error("Cannot execute query : addDelivery", e);
        }

        return deliveryToAdd;
    }

    @Override
    public Delivery updateDelivery(Delivery deliveryToUpdate) {
        Session session = sessionFactory.getCurrentSession();

        Delivery result = null;
        try {
            result = (Delivery) session.merge(deliveryToUpdate);
        } catch (SQLGrammarException e) {
            session.getTransaction().rollback();
            logger.error("Cannot execute query : updateDelivery", e);
        }

        return result;
    }

    @Override
    public Optional<Delivery> findDeliveryById(int id) {
        Session session = sessionFactory.getCurrentSession();

        Optional<Delivery> result = Optional.empty();
        try {
            Delivery delivery = (Delivery) session.get(Delivery.class, id);

            if (delivery != null) {
                result = Optional.of(delivery);
            }
        } catch (SQLGrammarException e) {
            logger.error("Cannot execute query : findDeliveryById", e);
        }

        return result;
    }

    @Override
    public List<Delivery> getDeliveriesToDo() {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Delivery> criteria = builder.createQuery(Delivery.class);
        Root<Delivery> root = criteria.from(Delivery.class);
        criteria.select(root).where(builder.equal(root.get("state"), Boolean.FALSE));
        Query<Delivery> query = session.createQuery(criteria);

        return query.getResultList();
    }

    @Override
    public List<Delivery> getDeliveriesDoneBy(Integer idCoursier) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Delivery> criteria = builder.createQuery(Delivery.class);
        Root<Delivery> root = criteria.from(Delivery.class);
        criteria.select(root).where(builder.equal(root.get("coursierId"), idCoursier));
        Query<Delivery> query = session.createQuery(criteria);
        return query.getResultList()
                .stream()
                .filter(Delivery::getState)
                .collect(Collectors.toList());
    }
}

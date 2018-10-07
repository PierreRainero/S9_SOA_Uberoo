package fr.unice.polytech.si5.soa.a.service.implementation;

import fr.unice.polytech.si5.soa.a.entities.Delivery;
import fr.unice.polytech.si5.soa.a.service.IDeliveryService;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.exception.SQLGrammarException;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementation of the delivery service.
 * @author Alexis Deslandes
 */
@Service
public class DeliveryServiceImpl implements IDeliveryService {
    @Autowired
    private SessionFactory sessionFactory;

    /**
     * @return all the deliveries that have not been delivered yet.
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<Delivery> findTobeDeliveredDeliveries() {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Delivery> criteria = builder.createQuery(Delivery.class);
        Root<Delivery> root = criteria.from(Delivery.class);

        criteria.select(root);
        Query<Delivery> query = session.createQuery(criteria);
        return query.getResultList().stream().filter(Delivery::getToBeDelivered).collect(Collectors.toList());
    }

    /**
     * Update the state of the delivery with the corresponding idDelivery.
     * After the call of this method, the delivery is delivered.
     * @param idDelivery Id of the delivery.
     * @throws Exception If the delivery has not been found.
     */
    @Override
    public void updateDeliveryToDelivered(Long idDelivery) throws Exception {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Delivery> criteria = builder.createQuery(Delivery.class);
        Root<Delivery> root = criteria.from(Delivery.class);
        criteria.select(root).where(builder.equal(root.get("id"),idDelivery));
        Query<Delivery> query = session.createQuery(criteria);
        Optional<Delivery> optionalDelivery = Optional.of(query.getSingleResult());
        if (optionalDelivery.isPresent()){
            Delivery delivery = optionalDelivery.get();
            delivery.setToBeDelivered(true);
            session.merge(delivery);
        }else{
            throw new Exception("Delivery not found.");
        }
    }

    @Override
    public void createDelivery(Delivery delivery) {
        Session session = sessionFactory.getCurrentSession();
        try {
            session.save(delivery);
        } catch (SQLGrammarException e) {
            session.getTransaction().rollback();
        }
    }
}

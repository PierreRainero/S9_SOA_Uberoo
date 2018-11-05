package fr.unice.polytech.si5.soa.a.dao.component;

import fr.unice.polytech.si5.soa.a.dao.ICoursierDao;
import fr.unice.polytech.si5.soa.a.entities.Coursier;
import fr.unice.polytech.si5.soa.a.entities.Delivery;

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

import java.util.List;
import java.util.Optional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

@Primary
@Repository
@Transactional
public class CoursierDaoImpl implements ICoursierDao {
    private static Logger logger = LogManager.getLogger(DeliveryDaoImpl.class);

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public Optional<Coursier> findCoursierById(Integer idCoursier) {
        Session session = sessionFactory.openSession();
        Optional<Coursier> result = Optional.empty();
        try {
            Coursier coursier = session.get(Coursier.class, idCoursier);
            if (coursier != null) {
                result = Optional.of(coursier);
            }
        } catch (SQLGrammarException e) {
            logger.error("Cannot execute query : findDeliveryById", e);
        }
        return result;
    }

    @Override
    public Coursier updateCoursier(Coursier coursierToUpdate) {
        Session session = sessionFactory.openSession();

        Coursier result = null;
        try{
            result = (Coursier) session.merge(coursierToUpdate);
        }catch (SQLGrammarException e){
            session.getTransaction().rollback();
            logger.error("Cannot execute query : updateDelivery", e);
        }
        return result;
    }

	@Override
	public Coursier addCoursier(Coursier coursier) {
		Session session = sessionFactory.getCurrentSession();

        try {
            session.save(coursier);
        } catch (SQLGrammarException e) {
            session.getTransaction().rollback();
            logger.error("Cannot execute query : addCoursier", e);
        }

        return coursier;
	}

	@Override
	public Optional<Coursier> getNearestCoursier(String address) {
		Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Coursier> criteria = builder.createQuery(Coursier.class);
        Root<Coursier> root = criteria.from(Coursier.class);
        criteria.select(root);
        Query<Coursier> query = session.createQuery(criteria);

        List<Coursier> res = query.getResultList();
        if(res.isEmpty()) {
        	return Optional.empty();
        }else {
        	return Optional.of(res.get(0));
        }
	}
}

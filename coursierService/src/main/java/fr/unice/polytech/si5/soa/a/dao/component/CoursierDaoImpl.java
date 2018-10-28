package fr.unice.polytech.si5.soa.a.dao.component;

import fr.unice.polytech.si5.soa.a.dao.ICoursierDao;
import fr.unice.polytech.si5.soa.a.entities.Coursier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.exception.SQLGrammarException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Primary
@Repository
@Transactional
public class CoursierDaoImpl implements ICoursierDao {

    private static Logger logger = LogManager.getLogger(DeliveryDaoImpl.class);

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public Optional<Coursier> findCoursierById(Integer idCoursier) {
        Session session = sessionFactory.getCurrentSession();
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
}

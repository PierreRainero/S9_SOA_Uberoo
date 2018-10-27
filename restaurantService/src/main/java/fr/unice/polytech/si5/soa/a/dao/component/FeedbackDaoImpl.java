package fr.unice.polytech.si5.soa.a.dao.component;


import fr.unice.polytech.si5.soa.a.dao.IFeedbackDao;
import fr.unice.polytech.si5.soa.a.entities.Feedback;
import fr.unice.polytech.si5.soa.a.entities.RestaurantOrder;
import org.apache.commons.logging.Log;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.exception.SQLGrammarException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Primary
@Repository
@Transactional
public class FeedbackDaoImpl implements IFeedbackDao {
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public Feedback addFeedback(Feedback feedback) {
        Session session = sessionFactory.getCurrentSession();

        try {
            session.save(feedback);
        } catch (SQLGrammarException e) {
            session.getTransaction().rollback();
        }

        return feedback;
    }
}

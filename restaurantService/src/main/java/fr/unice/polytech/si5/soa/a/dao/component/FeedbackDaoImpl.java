package fr.unice.polytech.si5.soa.a.dao.component;


import fr.unice.polytech.si5.soa.a.dao.IFeedbackDao;
import fr.unice.polytech.si5.soa.a.entities.Feedback;
import fr.unice.polytech.si5.soa.a.entities.Meal;

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

	@Override
	public List<Feedback> findFeedBackByMeal(Meal meal) {
		Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Feedback> criteria = builder.createQuery(Feedback.class);
        Root<Feedback> root =  criteria.from(Feedback.class);
        criteria.select(root).where(builder.equal(root.get("meal"), meal));
        Query<Feedback> query = session.createQuery(criteria);

        return query.getResultList();
	}
}

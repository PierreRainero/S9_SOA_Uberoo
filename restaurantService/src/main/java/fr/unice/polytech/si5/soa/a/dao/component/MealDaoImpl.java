package fr.unice.polytech.si5.soa.a.dao.component;

import fr.unice.polytech.si5.soa.a.dao.IMealDao;
import fr.unice.polytech.si5.soa.a.entities.Meal;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.Optional;

@Primary
@Repository
@Transactional
public class MealDaoImpl implements IMealDao {
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public Optional<Meal> findMealByName(String name) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Meal> criteria = builder.createQuery(Meal.class);
        Root<Meal> root =  criteria.from(Meal.class);
        criteria.select(root).where(builder.equal(root.get("name"), name));
        Query<Meal> query = session.createQuery(criteria);

        try {
            return Optional.of(query.getSingleResult());
        }catch(Exception e) {
            return Optional.empty();
        }
    }
}

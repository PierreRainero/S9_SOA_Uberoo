package fr.unice.polytech.si5.soa.a.dao.component;

import fr.unice.polytech.si5.soa.a.dao.IMealDao;
import fr.unice.polytech.si5.soa.a.entities.Ingredient;
import fr.unice.polytech.si5.soa.a.entities.Meal;

import org.hibernate.SessionFactory;
import org.hibernate.exception.SQLGrammarException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.Optional;

@Primary
@Repository
@Transactional
public class MealDaoImpl implements IMealDao {
	private static Logger logger = LogManager.getLogger(MealDaoImpl.class);
	
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

	@Override
	public Optional<Meal> findMealById(int id) {
		Session session = sessionFactory.getCurrentSession();

		Optional<Meal> result = Optional.empty();
		try {
			Meal meal = (Meal) session.get(Meal.class, id);

			if(meal!=null){
				result = Optional.of(meal);
			}
		} catch (SQLGrammarException e) {
			logger.error("Cannot execute query : findMealById", e);
		}

		return result;
	}

	@Override
	public Meal addMeal(Meal meal) {
		Session session = sessionFactory.getCurrentSession();

		try {
			session.save(meal);
		} catch (SQLGrammarException e) {
			session.getTransaction().rollback();
			logger.error("Cannot execute query : addMeal", e);
		}

		return meal;
	}

	@Override
	public Ingredient addIngredient(Ingredient ingredient) {
		Session session = sessionFactory.getCurrentSession();

		try {
			session.save(ingredient);
		} catch (SQLGrammarException e) {
			session.getTransaction().rollback();
			logger.error("Cannot execute query : addMeal", e);
		}

		return ingredient;
	}

	@Override
	public Optional<Ingredient> findIngredientByName(String name) {
		Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Ingredient> criteria = builder.createQuery(Ingredient.class);
        Root<Ingredient> root =  criteria.from(Ingredient.class);
        criteria.select(root).where(builder.equal(root.get("name"), name));
        Query<Ingredient> query = session.createQuery(criteria);

        try {
            return Optional.of(query.getSingleResult());
        }catch(Exception e) {
            return Optional.empty();
        }
	}
}

package fr.unice.polytech.si5.soa.a.dao.component;

import java.util.List;
import java.util.Optional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import fr.unice.polytech.si5.soa.a.dao.ICatalogDao;
import fr.unice.polytech.si5.soa.a.entities.Meal;

/**
 * Class name	CatalogImpl
 * @see 		ICatalogDao
 * Date			01/10/2018
 * @author		PierreRainero
 **/
@Primary
@Repository
@Transactional
public class CatalogDaoImpl implements ICatalogDao {
	@Autowired
	private SessionFactory sessionFactory;

	@Override
	/**
     * {@inheritDoc}
     */
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
	/**
     * {@inheritDoc}
     */
	public List<Meal> findMealsByTag(String tag) {
		Session session = sessionFactory.getCurrentSession();
		CriteriaBuilder builder = session.getCriteriaBuilder();
		CriteriaQuery<Meal> criteria = builder.createQuery(Meal.class);
		Root<Meal> root =  criteria.from(Meal.class);
		
		criteria.select(root).where(builder.isMember(tag, root.get("tags")));
		Query<Meal> query = session.createQuery(criteria);
		
		return query.getResultList();
	}
}

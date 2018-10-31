package fr.unice.polytech.si5.soa.a.dao.component;

import fr.unice.polytech.si5.soa.a.dao.IRestaurantDao;
import fr.unice.polytech.si5.soa.a.entities.Delivery;
import fr.unice.polytech.si5.soa.a.entities.Restaurant;
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
public class RestaurantDaoImpl implements IRestaurantDao {

    private static Logger logger = LogManager.getLogger(DeliveryDaoImpl.class);

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public Optional<Restaurant> findRestaurantById(Integer idRestaurant) {
        Session session = sessionFactory.getCurrentSession();
        Optional<Restaurant> result = Optional.empty();
        try {
            Restaurant restaurant = session.get(Restaurant.class, idRestaurant);
            if (restaurant != null) {
                result = Optional.of(restaurant);
            }
        } catch (SQLGrammarException e) {
            logger.error("Cannot execute query : findDeliveryById", e);
        }

        return result;
    }
}

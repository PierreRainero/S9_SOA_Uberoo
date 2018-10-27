package fr.unice.polytech.si5.soa.a.dao;

import fr.unice.polytech.si5.soa.a.configuration.TestConfiguration;
import fr.unice.polytech.si5.soa.a.entities.Meal;
import fr.unice.polytech.si5.soa.a.entities.OrderState;
import fr.unice.polytech.si5.soa.a.entities.Restaurant;
import fr.unice.polytech.si5.soa.a.entities.RestaurantOrder;
import org.hibernate.Transaction;
import org.hibernate.exception.SQLGrammarException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfiguration.class})
class OrderDaoTest {
    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private IOrderDao orderDao;

    private Restaurant tacosRestaurant;
    private Restaurant asianRestaurant;
    private RestaurantOrder tacosOrder, ramenOrder;
    private Meal tacos;
    private Meal buritos;
    private Meal ramen;

    @BeforeEach
    public void setup() throws Exception {
    	tacosRestaurant = new Restaurant();
    	tacosRestaurant.setName("Enroule moi");
    	tacosRestaurant.setRestaurantAddress("88 rue des frites");
    	
    	asianRestaurant = new Restaurant();
    	asianRestaurant.setName("RizRiz");
    	asianRestaurant.setRestaurantAddress("47 avenue des bols");
    	
        tacos = new Meal();
        tacos.setName("Tacos");
        tacos.setPrice(8);
        tacos.setRestaurant(tacosRestaurant);

        buritos = new Meal();
        buritos.setName("Buritos");
        buritos.setPrice(7.5);
        buritos.setRestaurant(tacosRestaurant);

        ramen = new Meal();
        ramen.setName("Ramen soup");
        ramen.setPrice(10);
        ramen.setRestaurant(asianRestaurant);

        tacosOrder = new RestaurantOrder();
        tacosOrder.addMeal(tacos);
        tacosOrder.addMeal(buritos);
        tacosOrder.setRestaurant(tacosRestaurant);

        ramenOrder = new RestaurantOrder();
        ramenOrder.addMeal(ramen);

        Session session = sessionFactory.openSession();
        try {
        	session.save(tacosRestaurant);
        	session.save(asianRestaurant);
            session.save(tacos);
            session.save(buritos);
            session.save(ramen);
            session.save(tacosOrder);
            session.beginTransaction().commit();
        } catch (SQLGrammarException e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
    }

    @AfterEach
    public void cleanUp() throws Exception {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();

            if(ramenOrder.getId()!=0){
                session.delete(ramenOrder);
            }

            session.delete(tacosRestaurant);
        	session.delete(asianRestaurant);
            session.delete(tacos);
            session.delete(buritos);
            session.delete(ramen);
            session.delete(tacosOrder);

            session.flush();
            transaction.commit();

            tacosRestaurant = null;
            asianRestaurant = null;
            tacos = null;
            buritos = null;
            ramen = null;
            ramenOrder = null;
            tacosOrder = null;
        } catch (SQLGrammarException e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
    }

    @Test
    public void addOrder() {
        RestaurantOrder responseOrder = orderDao.addOrder(ramenOrder);

        assertNotNull(responseOrder);
        assertNotEquals(0, responseOrder.getId());
        assertEquals(1, responseOrder.getMeals().size());
    }

    @Test
    public void updateOrder() {
        Meal pizza = new Meal();
        pizza.setName("Pizza");
        pizza.setPrice(12);
        Session session = sessionFactory.openSession();
        try {
            session.save(pizza);
            session.beginTransaction().commit();
        } catch (SQLGrammarException e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        assertEquals(2, tacosOrder.getMeals().size());
        session = sessionFactory.openSession();
        session.evict(tacosOrder);

        tacosOrder.addMeal(pizza);

        RestaurantOrder order = orderDao.updateOrder(tacosOrder);
        assertEquals(3, order.getMeals().size());

        // Clean context :
        tacosOrder.removeMeal(pizza);
        session = sessionFactory.openSession();
        try {
            session.merge(tacosOrder);
            session.beginTransaction().commit();
        } catch (SQLGrammarException e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.delete(pizza);

            session.flush();
            transaction.commit();

            pizza = null;
        } catch (SQLGrammarException e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
    }

    @Test
    public void findOrderById() {
        Optional<RestaurantOrder> order = orderDao.findOrderById(tacosOrder.getId());
        assertTrue(order.isPresent());
        assertEquals(tacosOrder.getId(), order.get().getId());
    }

    @Test
    public void getOrdersToDo() {
        List<RestaurantOrder> orders = orderDao.getOrdersToDo(tacosRestaurant);
        assertEquals(1, orders.size());

        tacosOrder.setState(OrderState.FINISHED);
        Session session = sessionFactory.openSession();
        try {
            session.merge(tacosOrder);
            session.beginTransaction().commit();
        } catch (SQLGrammarException e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }

        orders = orderDao.getOrdersToDo(tacosRestaurant);
        assertEquals(0, orders.size());
    }
}
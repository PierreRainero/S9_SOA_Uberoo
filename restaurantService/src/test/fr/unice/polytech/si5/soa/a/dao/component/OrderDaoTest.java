package fr.unice.polytech.si5.soa.a.dao.component;

import fr.unice.polytech.si5.soa.a.configuration.TestConfiguration;
import fr.unice.polytech.si5.soa.a.dao.IOrderDao;
import fr.unice.polytech.si5.soa.a.entities.OrderState;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfiguration.class})
class OrderDaoTest {

    private static final String ASIAN_CATEGORY = "Asian";

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private IOrderDao orderDao;

    private RestaurantOrder tacosOrder, ramenOrder;
    private List<String> meals;

    @BeforeEach
    void setup() throws Exception {
        meals = new ArrayList<>();
        meals.add("Tacos");
        tacosOrder = new RestaurantOrder();
        tacosOrder.setId(0);
        tacosOrder.setState(OrderState.TO_PREPARE);
        tacosOrder.setMeals(meals);

        meals = new ArrayList<>();
        meals.add("Tacos");
        ramenOrder = new RestaurantOrder();
        ramenOrder.setId(0);
        ramenOrder.setState(OrderState.TO_PREPARE);
        ramenOrder.setMeals(meals);

        Session session = sessionFactory.openSession();

        try {
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

            session.delete(tacosOrder);

            session.flush();
            transaction.commit();

            tacosOrder = null;
        } catch (SQLGrammarException e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
    }

    @Test
    void addOrder() {
        RestaurantOrder responseOrder = orderDao.addOrder(ramenOrder);

        assertNotNull(responseOrder);
        assertNotEquals(0, responseOrder.getId());
        assertEquals(1, responseOrder.getMeals().size());
    }

    @Test
    void updateOrder() {

    }

    @Test
    void findOrderById() {
        Optional<RestaurantOrder> order = orderDao.findOrderById(tacosOrder.getId());
        assertTrue(order.isPresent());
        assertEquals(tacosOrder.getId(), order.get().getId());
    }

    @Test
    void getOrdersToDo() {
        meals = new ArrayList<>();
        meals.add("Pizza");
        RestaurantOrder pizzaOrder = new RestaurantOrder();
        pizzaOrder.setId(0);
        pizzaOrder.setState(OrderState.FINISHED);
        pizzaOrder.setMeals(meals);

        Session session = sessionFactory.openSession();

        try {
            session.save(pizzaOrder);
            session.beginTransaction().commit();
        } catch (SQLGrammarException e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }

        List<RestaurantOrder> orders = orderDao.getOrdersToDo();

        assertEquals(1, orders.size());
        assertEquals(tacosOrder.getId(), orders.get(0).getId());
        assertEquals(tacosOrder.getState(), orders.get(0).getState());

    }
}
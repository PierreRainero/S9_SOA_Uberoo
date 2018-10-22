package fr.unice.polytech.si5.soa.a.dao.component;

import fr.unice.polytech.si5.soa.a.configuration.TestConfiguration;
import fr.unice.polytech.si5.soa.a.dao.IOrderDao;
import fr.unice.polytech.si5.soa.a.entities.Ingredient;
import fr.unice.polytech.si5.soa.a.entities.Meal;
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
    private List<Meal> meals;
    private List<Ingredient> ingredients;

    @BeforeEach
    void setup() throws Exception {
        ingredients = new ArrayList<>();
        Ingredient tacos = new Ingredient("Tacos", 6.50);
        ingredients.add(tacos);
        meals = new ArrayList<>();
        Meal tacosMeal = new Meal();
        tacosMeal.setIngredients(ingredients);
        meals.add(tacosMeal);
        tacosOrder = new RestaurantOrder();
        tacosOrder.setId(0);
        tacosOrder.setState(OrderState.TO_PREPARE);
        tacosOrder.setMeals(meals);

        Ingredient ramen = new Ingredient("Ramen", 4);
        ingredients.add(ramen);
        Meal ramenMeal = new Meal();
        ramenMeal.setIngredients(ingredients);
        meals.clear();
        meals.add(ramenMeal);

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
        meals.clear();
        Meal pizzaMeal = new Meal();
        Ingredient pizza = new Ingredient("Pizza", 9);
        ingredients.clear();
        ingredients.add(pizza);
        pizzaMeal.setIngredients(ingredients);
        meals.add(pizzaMeal);

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

        List<Meal> formerMeals = pizzaOrder.getMeals();

        Ingredient ramen = new Ingredient("Ramen", 4);
        ingredients.add(ramen);
        Meal ramenMeal = new Meal();
        ramenMeal.setIngredients(ingredients);
        meals.clear();
        meals.add(ramenMeal);

        session = sessionFactory.openSession();
        session.evict(pizzaOrder);

        pizzaOrder.setMeals(meals);
        RestaurantOrder order = orderDao.updateOrder(pizzaOrder);
        assertNotEquals(formerMeals, order.getMeals());
        assertEquals(meals, order.getMeals());

    }

    @Test
    void findOrderById() {
        Optional<RestaurantOrder> order = orderDao.findOrderById(tacosOrder.getId());
        assertTrue(order.isPresent());
        assertEquals(tacosOrder.getId(), order.get().getId());
    }

    /*@Test
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

        for (int i = 0; i < orders.size(); i++) {
            System.err.println(orders.get(i));

        }

        assertEquals(1, orders.size());
        assertEquals(tacosOrder.getId(), orders.get(0).getId());
        assertEquals(tacosOrder.getState(), orders.get(0).getState());

    }*/
}
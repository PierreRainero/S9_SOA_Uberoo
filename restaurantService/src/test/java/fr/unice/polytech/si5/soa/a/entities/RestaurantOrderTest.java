package fr.unice.polytech.si5.soa.a.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RestaurantOrderTest {

    private RestaurantOrder order;

    @BeforeEach
    public void setUp() throws Exception {
        order = new RestaurantOrder();

    }

    @Test
    void toDTO() {
        List<Meal> meals = new ArrayList<>();
        //meals.add(new Meal(new Ingredient("Coca")));
        order.setMeals(meals);
        order.setState(OrderState.TO_PREPARE);
        //assertEquals(order.toDTO(), new RestaurantOrderDTO(0, meals, OrderState.TO_PREPARE));
    }
}
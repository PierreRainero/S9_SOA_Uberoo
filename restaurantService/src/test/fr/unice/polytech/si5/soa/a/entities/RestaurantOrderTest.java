package fr.unice.polytech.si5.soa.a.entities;

import fr.unice.polytech.si5.soa.a.communication.RestaurantOrderDTO;
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
        meals.add(new Meal(new Ingredient("Coca", 2)));
        order.setMeals(meals);
        order.setState(OrderState.TO_PREPARE);
        order.setId(0);
        assertEquals(order.toDTO(), new RestaurantOrderDTO(0, meals, OrderState.TO_PREPARE));
    }
}
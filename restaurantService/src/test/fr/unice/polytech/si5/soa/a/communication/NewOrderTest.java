package fr.unice.polytech.si5.soa.a.communication;

import fr.unice.polytech.si5.soa.a.entities.Ingredient;
import fr.unice.polytech.si5.soa.a.entities.Meal;
import fr.unice.polytech.si5.soa.a.entities.OrderState;
import fr.unice.polytech.si5.soa.a.entities.RestaurantOrder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class NewOrderTest {

    private NewOrder order;

    @BeforeEach
    public void setUp() throws Exception {
        order = new NewOrder();

    }

    @Test
    void createRestaurantOrder() {
        List<Meal> meals= new ArrayList<>();
        meals.add(new Meal(new Ingredient("Kebab", 5)));
        order.setMeals(meals);
        order.setAddress("475 rue Evariste Galois");
        assertEquals(order.createRestaurantOrder(), new RestaurantOrderDTO(-1, meals, OrderState.TO_PREPARE));
    }

}
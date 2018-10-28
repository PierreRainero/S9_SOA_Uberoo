package fr.unice.polytech.si5.soa.a.communication;

import fr.unice.polytech.si5.soa.a.communication.bus.NewOrder;
import fr.unice.polytech.si5.soa.a.entities.Meal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class NewOrderTest {

    private NewOrder order;

    @BeforeEach
    public void setUp() throws Exception {
        order = new NewOrder();

    }

    @Test
    void createRestaurantOrder() {
        List<Meal> meals= new ArrayList<>();
        //meals.add(new Meal(new Ingredient("Kebab")));
        //order.setMeals(meals);
        order.setAddress("475 rue Evariste Galois");
        //assertEquals(order.createRestaurantOrder(), new RestaurantOrderDTO(-1, meals, OrderState.TO_PREPARE));
    }

}
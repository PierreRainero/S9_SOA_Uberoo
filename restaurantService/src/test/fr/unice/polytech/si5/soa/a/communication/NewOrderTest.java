package fr.unice.polytech.si5.soa.a.communication;

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
        List<String> food = new ArrayList<>();
        food.add("Kebab");
        order.setFood(food);
        order.setAddress("475 rue Evariste Galois");
        assertEquals(order.createRestaurantOrder(), new RestaurantOrderDTO(-1, food, OrderState.TO_PREPARE));
    }

}
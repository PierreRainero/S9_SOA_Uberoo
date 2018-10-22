package fr.unice.polytech.si5.soa.a.communication;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode()
@ToString()
public class MealDTO implements Serializable {

    private int id;
    private String name;
    private double price;
    private List<IngredientDTO> ingredients = new ArrayList<>();

    public MealDTO() {
        // Default constructor for Jackson databinding
    }

    public MealDTO(int id, String name, double price, List<IngredientDTO> ingredients) {
        this.id = id;
        this.ingredients = ingredients;
        this.price = price;
    }

}

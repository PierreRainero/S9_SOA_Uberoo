package fr.unice.polytech.si5.soa.a.communication;

import fr.unice.polytech.si5.soa.a.entities.Ingredient;
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
    private List<Ingredient> ingredients = new ArrayList<>();

    public MealDTO() {
        // Default constructor for Jackson databinding
    }

    public MealDTO(int id, List<Ingredient> ingredients) {
        this.id = id;
        this.ingredients = ingredients;
    }

}

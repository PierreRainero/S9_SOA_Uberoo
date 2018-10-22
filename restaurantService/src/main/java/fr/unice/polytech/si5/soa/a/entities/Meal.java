package fr.unice.polytech.si5.soa.a.entities;

import fr.unice.polytech.si5.soa.a.communication.MealDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.GenerationType.IDENTITY;


@Entity
@Data
@Table(name = "`MEALS`")
@EqualsAndHashCode(exclude={"id"})
@ToString()
public class Meal implements Serializable {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id")
    private int id;


    @ElementCollection(fetch = FetchType.EAGER)
    private List<Ingredient> ingredients = new ArrayList<>();

    public Meal() {
        // Default constructor for JPA
    }

    public Meal(Ingredient singleIngredient)
    {
        this.ingredients = new ArrayList<>();
        this.ingredients.add(singleIngredient);
    }

    public Meal(MealDTO data) {
        this.ingredients = data.getIngredients();
    }

    public MealDTO toDTO() {
        return new MealDTO(id, ingredients);
    }

}

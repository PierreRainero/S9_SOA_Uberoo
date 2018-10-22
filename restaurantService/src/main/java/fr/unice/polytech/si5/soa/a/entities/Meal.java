package fr.unice.polytech.si5.soa.a.entities;

import fr.unice.polytech.si5.soa.a.communication.MealDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.NONE;


@Entity
@Data
@Table(name = "`MEALS`")
@EqualsAndHashCode(exclude={"id"})
@ToString()
public class Meal implements Serializable {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id")
    @Setter(NONE)
    private int id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "price", nullable = false)
    private double price;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Ingredient> ingredients = new ArrayList<>();

    public Meal() {
        // Default constructor for JPA
    }

    public Meal(MealDTO data) {
        this.name = data.getName();
        this.price = data.getPrice();
    }

    public MealDTO toDTO() {
        return new MealDTO(id, name, price, ingredients.stream().map(ingredient -> ingredient.toDTO()).collect(Collectors.toList()));
    }

}

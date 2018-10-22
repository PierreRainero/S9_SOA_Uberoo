package fr.unice.polytech.si5.soa.a.entities;

import fr.unice.polytech.si5.soa.a.communication.IngredientDTO;
import lombok.Data;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.NONE;


@Entity
@Data
@ToString()
public class Ingredient implements Serializable {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id")
    @Setter(NONE)
    private int id;

    @Column(name = "name", nullable = false)
    private String name;

    public Ingredient() {

    }

    public Ingredient(IngredientDTO data) {
        this.name = data.getName();
    }

    public IngredientDTO toDTO() {
        return new IngredientDTO(name);
    }
}

package fr.unice.polytech.si5.soa.a.entities;

import fr.unice.polytech.si5.soa.a.communication.IngredientDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import java.io.Serializable;

import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.NONE;


@Entity
@Data
@Table(name = "`INGREDIENT`")
@EqualsAndHashCode(exclude={"id"})
@ToString()
public class Ingredient implements Serializable {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id")
    @Setter(NONE)
    private int id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    public Ingredient() {

    }

    public Ingredient(IngredientDTO data) {
        this.name = formalizeIngredientName(data.getName());
    }

    public IngredientDTO toDTO() {
        return new IngredientDTO(name);
    }
    
    private String formalizeIngredientName(String nameToFormalize) {
    	return nameToFormalize.isEmpty() ? 
    		   nameToFormalize : 
    		   nameToFormalize.substring(0, 1).toUpperCase() + nameToFormalize.substring(1).toLowerCase();
    }
}

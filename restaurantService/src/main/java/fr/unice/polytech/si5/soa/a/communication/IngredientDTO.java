package fr.unice.polytech.si5.soa.a.communication;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode()
@ToString()
public class IngredientDTO {
    private String name;

    public IngredientDTO(){

    }

    public IngredientDTO(String name){
        this.name = name;
    }
}

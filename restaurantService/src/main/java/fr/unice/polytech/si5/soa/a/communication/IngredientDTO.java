package fr.unice.polytech.si5.soa.a.communication;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Class name	IngredientDTO
 * Date			22/10/2018
 * @author		PierreRainero
 */
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

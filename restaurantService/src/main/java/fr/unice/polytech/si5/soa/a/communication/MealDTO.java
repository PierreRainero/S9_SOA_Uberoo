package fr.unice.polytech.si5.soa.a.communication;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Class name	MealDTO
 * Date			22/10/2018
 * @author		PierreRainero
 */
@Data
@EqualsAndHashCode()
@ToString()
public class MealDTO implements Serializable {
    /**
	 * Generated UID version
	 */
	private static final long serialVersionUID = -6513144823703707161L;
	
	private int id;
    private String name;
    private double price;
    private List<IngredientDTO> ingredients = new ArrayList<>();
    private List<String> tags = new ArrayList<>();

    public MealDTO() {
        // Default constructor for Jackson databinding
    }

    public MealDTO(int id, String name, double price, List<IngredientDTO> ingredients, List<String> tags) {
        this.id = id;
        this.name = name;
        this.ingredients = ingredients;
        this.price = price;
        this.tags = tags;
    }

}

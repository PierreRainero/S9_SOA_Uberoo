package fr.unice.polytech.si5.soa.a.entities;

import fr.unice.polytech.si5.soa.a.communication.MealDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.lang.NonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.NONE;


/**
 * Class name	Meal
 * Date			22/10/2018
 * @author		PierreRainero
 */
@Entity
@Data
@Table(name = "`MEALS`")
@EqualsAndHashCode(exclude={"id", "ingredients", "feedbacks", "tags", "restaurant"})
@ToString()
public class Meal implements Serializable {
    /**
	 * Generated UID version
	 */
	private static final long serialVersionUID = -3885514079403275908L;

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
    
    @ManyToOne(cascade = CascadeType.MERGE)
	@NonNull
	private Restaurant restaurant;
    
    @Setter(NONE)
	@OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY, mappedBy = "meal")
	@ToString.Exclude
	private List<Feedback> feedbacks = new ArrayList<>();
    
    @Fetch(FetchMode.SELECT)
    @ElementCollection(fetch = FetchType.EAGER)
	private List<String> tags = new ArrayList<>();

    public Meal() {
        // Default constructor for JPA
    }

    public Meal(MealDTO data) {
        this.name = data.getName();
        this.price = data.getPrice();
    }

    public MealDTO toDTO() {
        return new MealDTO(id, name, price, ingredients.stream().map(ingredient -> ingredient.toDTO()).collect(Collectors.toList()), tags);
    }
    
    public void addTag(String tag) {
		if(!tags.contains(tag)) {
			tags.add(tag);
		}
	}
	
	public void removeTag(String tag) {
		tags.remove(tag);
	}
    
    public void addIngredient(Ingredient ingredient) {
    	if(!ingredients.contains(ingredient)) {
    		ingredients.add(ingredient);
    	}
    }

    public void removeIngredient(Ingredient ingredient) {
    	ingredients.remove(ingredient);
    }
}

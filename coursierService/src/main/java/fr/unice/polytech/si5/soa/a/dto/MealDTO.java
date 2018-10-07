package fr.unice.polytech.si5.soa.a.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Class name	MealDTO
 * Date			29/09/2018
 * @author		PierreRainero
 */
@Data
@EqualsAndHashCode()
@ToString()
public class MealDTO implements Serializable {
    /**
     * Generated UID version
     */
    private static final long serialVersionUID = -8640334793101869876L;

    private String name;
    private List<String> tags = new ArrayList<>();

    /**
     * Default constructor
     */
    public MealDTO() {
        // Default constructor for Jackson databinding
    }

    /**
     * Normal constructor
     * @param name meal's name
     */
    public MealDTO(String name, List<String> tags) {
        this.name = name;
        this.tags = tags;
    }
}
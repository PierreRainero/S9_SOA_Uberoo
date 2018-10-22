package fr.unice.polytech.si5.soa.a.entities;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString()
public class Ingredient implements Serializable {


    private String name;
    private double price;

    public Ingredient(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public Ingredient(String name) {
        this(name, 0);
    }

    public Ingredient() {
        this("", 0);
    }

}

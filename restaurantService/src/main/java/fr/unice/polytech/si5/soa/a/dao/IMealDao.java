package fr.unice.polytech.si5.soa.a.dao;

import fr.unice.polytech.si5.soa.a.entities.Meal;

import java.util.Optional;

public interface IMealDao {
    Optional<Meal> findMealByName(String name);
}

package fr.unice.polytech.si5.soa.a.services;

import fr.unice.polytech.si5.soa.a.communication.MealDTO;
import fr.unice.polytech.si5.soa.a.exceptions.UnknowMealException;


public interface IMealService {
    MealDTO findMealByName(String name) throws UnknowMealException;
}

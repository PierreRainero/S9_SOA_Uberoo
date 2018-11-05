package fr.unice.polytech.si5.soa.a.dao;

import java.util.List;

import fr.unice.polytech.si5.soa.a.entities.Feedback;
import fr.unice.polytech.si5.soa.a.entities.Meal;

public interface IFeedbackDao {
    Feedback addFeedback(Feedback feedback);
    List<Feedback> findFeedBackByMeal(Meal meal);
}

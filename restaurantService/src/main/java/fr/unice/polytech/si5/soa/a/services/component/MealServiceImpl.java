package fr.unice.polytech.si5.soa.a.services.component;

import fr.unice.polytech.si5.soa.a.communication.MealDTO;
import fr.unice.polytech.si5.soa.a.dao.IMealDao;
import fr.unice.polytech.si5.soa.a.entities.Meal;
import fr.unice.polytech.si5.soa.a.exceptions.UnknowMealException;
import fr.unice.polytech.si5.soa.a.services.IMealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Primary
@Service("MealService")
public class MealServiceImpl implements IMealService {
    @Autowired
    private IMealDao mealDao;


    @Override
    public MealDTO findMealByName(String name) throws UnknowMealException {
        Optional<Meal> resultWrapped = mealDao.findMealByName(name);

        if(!resultWrapped.isPresent()){
            throw new UnknowMealException("Can't find meal with name = "+name);
        }

        return resultWrapped.get().toDTO();
    }
}

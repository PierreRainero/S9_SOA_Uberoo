package fr.unice.polytech.si5.soa.a.services.component;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import fr.unice.polytech.si5.soa.a.dto.MealDTO;
import fr.unice.polytech.si5.soa.a.dao.ICatalogDao;
import fr.unice.polytech.si5.soa.a.services.ICatalogService;

/**
 * Class name	CatalogServiceImpl
 * @see 		ICatalogService
 * Date			01/10/2018
 * @author		PierreRainero
**/
@Primary
@Service("CatalogService")
public class CatalogServiceImpl implements ICatalogService {
	@Autowired
	private ICatalogDao catalogDao;

	@Override
	public List<MealDTO> findMealsByTag(String tag) {
		return catalogDao.findMealsByTag(tag).stream().map(meal -> meal.toDTO()).collect(Collectors.toList());
	}
}

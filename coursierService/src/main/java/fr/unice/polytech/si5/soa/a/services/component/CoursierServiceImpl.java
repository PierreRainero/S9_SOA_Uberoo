package fr.unice.polytech.si5.soa.a.services.component;

import fr.unice.polytech.si5.soa.a.communication.DTO.CoursierDTO;
import fr.unice.polytech.si5.soa.a.communication.CoursierStatistics;
import fr.unice.polytech.si5.soa.a.controllers.DeliveryController;
import fr.unice.polytech.si5.soa.a.dao.ICoursierDao;
import fr.unice.polytech.si5.soa.a.dao.IRestaurantDao;
import fr.unice.polytech.si5.soa.a.entities.Coursier;
import fr.unice.polytech.si5.soa.a.entities.Delivery;
import fr.unice.polytech.si5.soa.a.entities.Restaurant;
import fr.unice.polytech.si5.soa.a.exceptions.UnknownCoursierException;
import fr.unice.polytech.si5.soa.a.exceptions.UnknownRestaurantException;
import fr.unice.polytech.si5.soa.a.services.ICoursierService;
import fr.unice.polytech.si5.soa.a.utils.Geoposition;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Primary
@Service("CoursierService")
public class CoursierServiceImpl implements ICoursierService {

    private static Logger logger = LogManager.getLogger(CoursierServiceImpl.class);

    @Autowired
    private ICoursierDao coursierDao;

    @Autowired
    private IRestaurantDao restaurantDao;

    @Override
    public CoursierDTO getCoursier(Integer idCoursier) throws UnknownCoursierException {
        Optional<Coursier> coursierWrapped = this.coursierDao.findCoursierById(idCoursier);
        if (coursierWrapped.isPresent()){
            return coursierWrapped.get().toDto();
        }else{
            throw new UnknownCoursierException(idCoursier.toString());
        }
    }

    @Override
    public CoursierStatistics getCoursierStatistics(Integer idCoursier, Integer idRestaurant) throws UnknownCoursierException, UnknownRestaurantException {
        Optional<Coursier> coursierWrapped = this.coursierDao.findCoursierById(idCoursier);
        if (!coursierWrapped.isPresent()) {
            throw new UnknownCoursierException(idCoursier.toString());
        }
        Coursier coursier = coursierWrapped.get();

        Optional<Restaurant> restaurantWrapped = this.restaurantDao.findRestaurantById(idRestaurant);

        if (!restaurantWrapped.isPresent()) {
            throw new UnknownRestaurantException(idRestaurant);
        }
        Restaurant restaurant = restaurantWrapped.get();

        List<Delivery> deliveries = coursier.getDeliveries().stream()
                .filter(delivery -> delivery.getRestaurant().getId().equals(idRestaurant))
                .collect(Collectors.toList());

        CoursierStatistics coursierStatistics = new CoursierStatistics();
        double speed = 0.0;
        int deliveriesCount = deliveries.size();
        logger.info(deliveries);
        logger.info(restaurant);
        double distance;
        for (Delivery delivery : deliveries) {
            distance = Geoposition.distance(restaurant.getLatitude(), delivery.getLatitude(), restaurant.getLongitude(), delivery.getLongitude());
            long timeDifference = delivery.getDeliveryDate().getTime() - delivery.getCreationDate().getTime();
            long timeDifferenceInHours = TimeUnit.MILLISECONDS.toHours(timeDifference);
            speed += distance / timeDifferenceInHours;
        }
        speed = speed / deliveriesCount;
        coursierStatistics.setSpeed(speed);
        return coursierStatistics;
    }

	@Override
	public CoursierDTO addCoursier(CoursierDTO coursiertoAdd) {
		Optional<Coursier> existingCoursier = coursierDao.findCoursierByName(coursiertoAdd.getName());
		if(existingCoursier.isPresent()) {
			return existingCoursier.get().toDto();
		}
		
		return coursierDao.addCoursier(new Coursier(coursiertoAdd)).toDto();
	}
}

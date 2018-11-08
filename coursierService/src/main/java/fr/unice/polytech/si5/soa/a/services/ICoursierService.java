package fr.unice.polytech.si5.soa.a.services;

import fr.unice.polytech.si5.soa.a.communication.DTO.CoursierDTO;
import fr.unice.polytech.si5.soa.a.communication.CoursierStatistics;
import fr.unice.polytech.si5.soa.a.exceptions.UnknownCoursierException;
import fr.unice.polytech.si5.soa.a.exceptions.UnknownRestaurantException;

public interface ICoursierService {
    /**
     * Add the following coursier to the system.
     * @param coursiertoAdd coursier to add
     * @return the coursier added
     */
	CoursierDTO addCoursier(CoursierDTO coursiertoAdd);

    /**
     * Get the coursier thanks to its id.
     * @param idCoursier Id of the coursier
     * @return The coursier to get
     * @throws UnknownCoursierException
     */
    CoursierDTO getCoursier(Integer idCoursier) throws UnknownCoursierException;

    /**
     * Get the coursier statistics, eg its speed
     * for a particular restaurant.
     * @param idCoursier the id of the coursier.
     * @param idRestaurant the id of the restaurant
     * @return the coursier statistics
     * @throws UnknownCoursierException
     * @throws UnknownRestaurantException
     */
    CoursierStatistics getCoursierStatistics(Integer idCoursier, Integer idRestaurant) throws UnknownCoursierException, UnknownRestaurantException;
}

package fr.unice.polytech.si5.soa.a.services;

import fr.unice.polytech.si5.soa.a.communication.DTO.CoursierDTO;
import fr.unice.polytech.si5.soa.a.communication.CoursierStatistics;
import fr.unice.polytech.si5.soa.a.exceptions.UnknownCoursierException;
import fr.unice.polytech.si5.soa.a.exceptions.UnknownRestaurantException;

public interface ICoursierService {
	CoursierDTO addCoursier(CoursierDTO coursiertoAdd);
    CoursierDTO getCoursier(Integer idCoursier) throws UnknownCoursierException;
    CoursierStatistics getCoursierStatistics(Integer idCoursier, Integer idRestaurant) throws UnknownCoursierException, UnknownRestaurantException;
}

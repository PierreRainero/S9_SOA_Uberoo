package fr.unice.polytech.si5.soa.a.services;

import fr.unice.polytech.si5.soa.a.communication.CoursierDto;
import fr.unice.polytech.si5.soa.a.communication.CoursierStatistics;
import fr.unice.polytech.si5.soa.a.entities.Coursier;
import fr.unice.polytech.si5.soa.a.exceptions.UnknownCoursierException;
import fr.unice.polytech.si5.soa.a.exceptions.UnknownRestaurantException;

public interface ICoursierService {
    CoursierDto getCoursier(Integer idCoursier) throws UnknownCoursierException;
    CoursierStatistics getCoursierStatistics(Integer idCoursier, Integer idRestaurant) throws UnknownCoursierException, UnknownRestaurantException;
}

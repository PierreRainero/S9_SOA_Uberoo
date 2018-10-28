package fr.unice.polytech.si5.soa.a.services;

import fr.unice.polytech.si5.soa.a.entities.Coursier;
import fr.unice.polytech.si5.soa.a.exceptions.UnknowCoursierException;

public interface ICoursierService {
    Coursier getCoursier(Integer idCoursier) throws UnknowCoursierException;
}

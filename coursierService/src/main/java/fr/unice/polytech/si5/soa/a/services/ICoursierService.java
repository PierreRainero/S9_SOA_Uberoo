package fr.unice.polytech.si5.soa.a.services;

import fr.unice.polytech.si5.soa.a.entities.Coursier;
import fr.unice.polytech.si5.soa.a.exceptions.UnknownCoursierException;

public interface ICoursierService {
    Coursier getCoursier(Integer idCoursier) throws UnknownCoursierException;
}

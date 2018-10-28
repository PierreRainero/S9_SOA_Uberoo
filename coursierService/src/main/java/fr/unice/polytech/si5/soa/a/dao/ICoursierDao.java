package fr.unice.polytech.si5.soa.a.dao;

import fr.unice.polytech.si5.soa.a.entities.Coursier;

import java.util.Optional;

public interface ICoursierDao {

    Optional<Coursier> findCoursierById(Integer idCoursier);
    Coursier updateCoursier(Coursier coursier);
}

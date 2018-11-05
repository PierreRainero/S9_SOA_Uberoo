package fr.unice.polytech.si5.soa.a.dao;

import fr.unice.polytech.si5.soa.a.entities.Coursier;

import java.util.Optional;

public interface ICoursierDao {
	Coursier addCoursier(Coursier coursier);
    Optional<Coursier> findCoursierById(Integer idCoursier);
    Coursier updateCoursier(Coursier coursier);
    Optional<Coursier> getNearestCoursier(String address);
}

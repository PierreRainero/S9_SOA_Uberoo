package fr.unice.polytech.si5.soa.a.dao;

import fr.unice.polytech.si5.soa.a.entities.Coursier;

import java.util.Optional;

public interface ICoursierDao {
    /**
     * Add the following coursier to the system.
     * @param coursier Coursier to add.
     * @return The coursier added.
     */
	Coursier addCoursier(Coursier coursier);

    /**
     * Find the coursier by the following name.
     * @param name Name of the coursier.
     * @return The coursier which correspond.
     */
	Optional<Coursier> findCoursierByName(String name);

    /**
     * Find the coursier by the following id.
     * @param idCoursier The id of the coursier.
     * @return The coursier which match.
     */
    Optional<Coursier> findCoursierById(Integer idCoursier);

    /**
     * Update the coursier in the system.
     * @param coursier The new version of the coursier.
     * @return The updated coursier.
     */
    Coursier updateCoursier(Coursier coursier);

    /**
     * Get the coursier that is next to the following address.
     * @param address The address we look for coursier
     * @return The nearest coursier
     */
    Optional<Coursier> getNearestCoursier(String address);
}

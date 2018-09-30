package fr.unice.polytech.si5.soa.a.dao;

import java.util.Optional;

import fr.unice.polytech.si5.soa.a.entities.User;

/**
 * Class name	IUserDao
 * Date			30/09/2018
 * @author		PierreRainero
 *
 */
public interface IUserDao {
	/**
	 * Search an user in the database using his id
	 * @param userId id to search
	 * @return the user wrapped in an {@link Optional} if the user exist, Optional.empty() otherwise
	 */
	Optional<User> findUserById(int userId);
}

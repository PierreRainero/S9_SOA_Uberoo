package fr.unice.polytech.si5.soa.a.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import fr.unice.polytech.si5.soa.a.communication.UserDTO;
import fr.unice.polytech.si5.soa.a.services.IUserService;
import fr.unice.polytech.si5.soa.a.services.component.UserServiceImpl;

/**
 * Class name	UserController
 * Date			03/11/2018
 * @author		PierreRainero
 */
@RestController
@RequestMapping("/users")
public class UserController {
	@Autowired
	private IUserService userService;
	
	@RequestMapping(value = "",
			method = RequestMethod.POST,
			consumes = {"application/JSON; charset=UTF-8"},
			produces = {"application/JSON; charset=UTF-8"})
	public ResponseEntity<?> addUser(@RequestBody UserDTO user) {
		return ResponseEntity.ok(userService.addUser(user));
	}
}

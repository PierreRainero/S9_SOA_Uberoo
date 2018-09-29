package fr.unice.polytech.si5.soa.a.services.component;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import fr.unice.polytech.si5.soa.a.communication.CommandDTO;
import fr.unice.polytech.si5.soa.a.dao.IOrderTakerDao;
import fr.unice.polytech.si5.soa.a.entities.Command;
import fr.unice.polytech.si5.soa.a.services.IOrderTakerService;

@Primary
@Service("OrderTakerService")
public class OrderTakerServiceImpl implements IOrderTakerService {
	private static Logger logger = LogManager.getLogger(OrderTakerServiceImpl.class);
	
	@Autowired
	private IOrderTakerDao orderDao;

	@Override
	/**
     * {@inheritDoc}
     */
	public CommandDTO addCommand(CommandDTO commandToAdd) {
		Command command = new Command();
		// @TODO Aller chercher les plats via un dao pour instancier l'objet command correctement avant de le passer
		return orderDao.addCommand(command).toDTO();
	}
	
	
}
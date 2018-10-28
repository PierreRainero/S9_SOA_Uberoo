package fr.unice.polytech.si5.soa.a.services.component;

import fr.unice.polytech.si5.soa.a.dao.ICoursierDao;
import fr.unice.polytech.si5.soa.a.entities.Coursier;
import fr.unice.polytech.si5.soa.a.exceptions.UnknowCoursierException;
import fr.unice.polytech.si5.soa.a.services.ICoursierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Primary
@Service("CoursierService")
public class CoursierServiceImpl implements ICoursierService {

    @Autowired
    private ICoursierDao coursierDao;

    @Override
    public Coursier getCoursier(Integer idCoursier) throws UnknowCoursierException {
        Optional<Coursier> coursierWrapped = this.coursierDao.findCoursierById(idCoursier);
        if (coursierWrapped.isPresent()){
            return coursierWrapped.get();
        }else{
            throw new UnknowCoursierException("Can't find the coursier with id : " + idCoursier);
        }
    }
}

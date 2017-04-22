package fr.gouv.agriculture.dal.ct.planCharge.metier.service;

import fr.gouv.agriculture.dal.ct.planCharge.ihm.NotImplementedException;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dao.DaoException;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dao.charge.PlanChargeDao;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dao.charge.PlanChargeWrapper;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.charge.PlanCharge;

import javax.xml.bind.JAXBContext;
import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Created by frederic.danna on 26/03/2017.
 */
public class PlanChargeService {

    public PlanCharge load(LocalDate date) throws ServiceException {
        try {
            PlanChargeDao planChargeDao = new PlanChargeDao();
            PlanCharge planCharge = planChargeDao.load(date);
            return planCharge;
        } catch (DaoException e) {
            throw new ServiceException("Impossible de charger le plan de charge en date du " + date.format(DateTimeFormatter.ofPattern("yyyy/MM/dd")) + ".", e);
        }
    }

    public void save(PlanCharge planCharge) throws ServiceException {
        throw new NotImplementedException(); // TODO FDA 2017/04 Coder.
    }
}

package fr.gouv.agriculture.dal.ct.planCharge.metier.service;

import com.sun.istack.internal.NotNull;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dao.DaoException;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dao.charge.PlanChargeDao;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.charge.PlanCharge;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Created by frederic.danna on 26/03/2017.
 */
public class PlanChargeService {

    @NotNull
    @Autowired
    private PlanChargeDao planChargeDao;

    @NotNull
    public PlanCharge load(@NotNull LocalDate dateEtat) throws ServiceException {
        try {
            PlanCharge planCharge = planChargeDao.load(dateEtat);
            return planCharge;
        } catch (DaoException e) {
            throw new ServiceException("Impossible de charger le plan de charge en date du " + dateEtat.format(DateTimeFormatter.ofPattern("yyyy/MM/dd")) + ".", e);
        }
    }

    public void save(@NotNull PlanCharge planCharge) throws ServiceException {
        LocalDate dateEtat = planCharge.getDateEtat();
        try {
            planChargeDao.save(planCharge);
        } catch (DaoException e) {
            throw new ServiceException("Impossible de sauver le plan de charge en date du " + dateEtat.format(DateTimeFormatter.ofPattern("yyyy/MM/dd")) + ".", e);
        }
    }
}

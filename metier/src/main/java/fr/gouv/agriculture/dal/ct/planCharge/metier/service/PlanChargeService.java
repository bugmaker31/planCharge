package fr.gouv.agriculture.dal.ct.planCharge.metier.service;

import fr.gouv.agriculture.dal.ct.planCharge.metier.dao.DaoException;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dao.charge.PlanChargeDao;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.charge.PlanCharge;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.Tache;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Created by frederic.danna on 26/03/2017.
 */
public class PlanChargeService {

    @NotNull
    @Autowired
    private PlanChargeDao planChargeDao;

    public void ajouterLigne(@NotNull PlanCharge planCharge, @NotNull Tache tache) {
        planCharge.getPlanifications().ajouter(tache);
    }

    @NotNull
    public PlanCharge charger(@NotNull LocalDate dateEtat) throws ServiceException {
        try {
            PlanCharge planCharge = planChargeDao.load(dateEtat);
            return planCharge;
        } catch (DaoException e) {
            throw new ServiceException("Impossible de charger le plan de charge en date du " + dateEtat.format(DateTimeFormatter.ofPattern("yyyy/MM/dd")) + ".", e);
        }
    }

    public void sauver(@NotNull PlanCharge planCharge) throws ServiceException {
        LocalDate dateEtat = planCharge.getDateEtat();
        try {
            planChargeDao.save(planCharge);
        } catch (DaoException e) {
            throw new ServiceException("Impossible de sauver le plan de charge en date du " + dateEtat.format(DateTimeFormatter.ofPattern("yyyy/MM/dd")) + ".", e);
        }
    }
}

package fr.gouv.agriculture.dal.ct.planCharge.metier.service;

import fr.gouv.agriculture.dal.ct.planCharge.metier.dao.DaoException;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dao.charge.PlanChargeDao;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.charge.PlanCharge;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.Tache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.constraints.NotNull;
import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Created by frederic.danna on 26/03/2017.
 */
public class PlanChargeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PlanChargeService.class);

    @NotNull
    @Autowired
    private PlanChargeDao planChargeDao;

    public void ajouterLigne(@NotNull PlanCharge planCharge, @NotNull Tache tache) {
/*
        if (planCharge.getPlanifications() == null) {
            planCharge.initPlanifications();
        }
*/
        planCharge.getPlanifications().ajouter(tache, planCharge.getDateEtat());
    }

    @NotNull
    public PlanCharge charger(@NotNull LocalDate dateEtat) throws ServiceException {
        try {
            return planChargeDao.load(dateEtat);
        } catch (DaoException e) {
            throw new ServiceException("Impossible de charger le plan de charge en date du " + dateEtat.format(DateTimeFormatter.ofPattern("yyyy/MM/dd")) + ".", e);
        }
    }

    public void sauver(@NotNull PlanCharge planCharge) throws ServiceException {
        LocalDate dateEtat = planCharge.getDateEtat();
        try {
            planChargeDao.sauver(planCharge);
        } catch (DaoException e) {
            throw new ServiceException("Impossible de sauver le plan de charge en date du " + dateEtat.format(DateTimeFormatter.ofPattern("yyyy/MM/dd")) + ".", e);
        }
    }

    @NotNull
    public PlanCharge importerDepuisCalc(@NotNull File ficCalc) throws ServiceException {
        try {
            return planChargeDao.importerDepuisCalc(ficCalc);
        } catch (DaoException e) {
            throw new ServiceException("Impossible d'importer le plan de charge depuis le fichier Calc '" + ficCalc.getAbsolutePath() + "'.", e);
        }
    }

}

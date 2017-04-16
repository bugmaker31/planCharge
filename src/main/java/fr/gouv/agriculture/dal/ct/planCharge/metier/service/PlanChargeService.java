package fr.gouv.agriculture.dal.ct.planCharge.metier.service;

import fr.gouv.agriculture.dal.ct.planCharge.metier.dao.charge.PlanChargeDao;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.charge.PlanCharge;

import java.time.LocalDate;

/**
 * Created by frederic.danna on 26/03/2017.
 */
public class PlanChargeService {

    public PlanCharge load(LocalDate date) {
        PlanChargeDao planChargeDao = new PlanChargeDao();
        PlanCharge planCharge = planChargeDao.load(date);
        return planCharge;
    }
}

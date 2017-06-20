package fr.gouv.agriculture.dal.ct.planCharge.metier.service;

/**
 * Created by frederic.danna on 05/05/2017.
 */
public interface RapportChargementPlanCharge extends RapportService {

    String getAvancement();

    void setAvancement(String avancement);

}

package fr.gouv.agriculture.dal.ct.planCharge.metier.dao.charge;

import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.charge.Planifications;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.charge.TacheSansPlanificationException;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.Tache;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by frederic.danna on 26/04/2017.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class PlanificationsWrapper {

    @XmlElement(name = "calendrier", required = true)
    private List<PlanificationWrapper> planifications;

    public PlanificationsWrapper() {
    }

    public PlanificationsWrapper(Planifications planifications) throws PlanChargeDaoException {
        this.planifications = new ArrayList<>();
        try {
            for (Tache tache : planifications.taches()) {
                PlanificationWrapper planif = new PlanificationWrapper(tache, planifications.calendrier(tache));
                this.planifications.add(planif);
            }
        } catch (TacheSansPlanificationException e) {
            throw new PlanChargeDaoException("Impossible de wrapper les planifications.", e);
        }
    }

    public List<PlanificationWrapper> getPlanifications() {
        return planifications;
    }

    public void setPlanifications(List<PlanificationWrapper> planifications) {
        this.planifications = planifications;
    }
}

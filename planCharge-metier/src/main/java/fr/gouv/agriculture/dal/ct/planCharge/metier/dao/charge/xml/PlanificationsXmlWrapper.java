package fr.gouv.agriculture.dal.ct.planCharge.metier.dao.charge.xml;

import fr.gouv.agriculture.dal.ct.planCharge.metier.dao.charge.PlanChargeDaoException;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.charge.Planifications;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.charge.TacheSansPlanificationException;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.Tache;

import javax.xml.bind.annotation.XmlElement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by frederic.danna on 26/04/2017.
 */
public class PlanificationsXmlWrapper {

    private List<PlanificationXmlWrapper> planifications;

    public PlanificationsXmlWrapper() {
        super();
    }

    public PlanificationsXmlWrapper(Planifications planifications) throws PlanChargeDaoException {
        try {
            this.planifications = new ArrayList<>();
            for (Tache tache : planifications.taches()) {
                PlanificationXmlWrapper planif = new PlanificationXmlWrapper(tache, planifications.calendrier(tache));
                this.planifications.add(planif);
            }
            this.planifications.sort(Comparator.comparing(p -> p.getTache().getId()));
        } catch (TacheSansPlanificationException e) {
            throw new PlanChargeDaoException("Impossible de wrapper les planifications.", e);
        }
    }

    @XmlElement(name = "planification", required = true)
    public List<PlanificationXmlWrapper> getPlanifications() {
        return planifications;
    }

    public void setPlanifications(List<PlanificationXmlWrapper> planifications) {
        this.planifications = planifications;
    }
}

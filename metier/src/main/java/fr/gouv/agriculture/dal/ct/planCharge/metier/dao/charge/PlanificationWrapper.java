package fr.gouv.agriculture.dal.ct.planCharge.metier.dao.charge;

import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.Tache;

import javax.xml.bind.annotation.XmlType;

/**
 * Created by frederic.danna on 26/04/2017.
 */
@XmlType
public class PlanificationWrapper {
    private Tache tache;

    public void setTache(Tache tache) {
        this.tache = tache;
    }

    public Tache getTache() {
        return tache;
    }
}

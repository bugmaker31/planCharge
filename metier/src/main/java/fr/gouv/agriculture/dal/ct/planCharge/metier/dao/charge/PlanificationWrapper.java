package fr.gouv.agriculture.dal.ct.planCharge.metier.dao.charge;

import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.Tache;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.time.LocalDate;
import java.util.Map;

/**
 * Created by frederic.danna on 26/04/2017.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class PlanificationWrapper {

    @XmlElement(name = "tache", required = true)
    private TacheWrapper tache;
    @XmlElement(name = "calendrier", required = true)
    private CalendrierWrapper calendrier;

    public PlanificationWrapper() {
    }

    public PlanificationWrapper(Tache tache, Map<LocalDate, Double> calendrier) {
        this.tache = new TacheWrapper(tache);
        this.calendrier = new CalendrierWrapper(calendrier);
    }

    public TacheWrapper getTache() {
        return tache;
    }

    public void setTache(TacheWrapper tache) {
        this.tache = tache;
    }

    public CalendrierWrapper getCalendrier() {
        return calendrier;
    }

    public void setCalendrier(CalendrierWrapper calendrier) {
        this.calendrier = calendrier;
    }
}

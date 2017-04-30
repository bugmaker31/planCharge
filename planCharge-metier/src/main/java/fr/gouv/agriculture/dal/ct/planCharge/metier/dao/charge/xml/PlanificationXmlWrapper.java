package fr.gouv.agriculture.dal.ct.planCharge.metier.dao.charge.xml;

import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.Tache;

import javax.xml.bind.annotation.XmlElement;
import java.time.LocalDate;
import java.util.Map;

/**
 * Created by frederic.danna on 26/04/2017.
 */
public class PlanificationXmlWrapper {

    private TacheXmlWrapper tache;

    private CalendrierXmlWrapper calendrier;

    public PlanificationXmlWrapper() {
        super();
    }

    public PlanificationXmlWrapper(Tache tache, Map<LocalDate, Double> calendrier) {
        this.tache = new TacheXmlWrapper(tache);
        this.calendrier = new CalendrierXmlWrapper(calendrier);
    }

    @XmlElement(name = "tache", required = true)
    public TacheXmlWrapper getTache() {
        return tache;
    }

    public void setTache(TacheXmlWrapper tache) {
        this.tache = tache;
    }

    @XmlElement(name = "calendrier", required = true)
    public CalendrierXmlWrapper getCalendrier() {
        return calendrier;
    }

    public void setCalendrier(CalendrierXmlWrapper calendrier) {
        this.calendrier = calendrier;
    }
}

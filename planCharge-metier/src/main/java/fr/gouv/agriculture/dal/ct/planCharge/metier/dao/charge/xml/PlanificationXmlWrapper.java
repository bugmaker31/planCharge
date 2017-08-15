package fr.gouv.agriculture.dal.ct.planCharge.metier.dao.charge.xml;

import fr.gouv.agriculture.dal.ct.planCharge.metier.dao.tache.xml.TacheXmlWrapper;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.tache.Tache;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlElement;
import java.time.LocalDate;
import java.util.Map;

/**
 * Created by frederic.danna on 26/04/2017.
 */
public class PlanificationXmlWrapper {

//    @Autowired
    @NotNull
    private TacheXmlWrapper tache = new TacheXmlWrapper();

//    @Autowired
    @NotNull
    private CalendrierChargesXmlWrapper calendrier = new CalendrierChargesXmlWrapper();

    /**
     * Constructeur vide (appelé notamment par JAXB).
     *
     * @return
     */
    public PlanificationXmlWrapper() {
        super();
    }

    public PlanificationXmlWrapper init(Tache tache, Map<LocalDate, Double> calendrier) {
        this.tache = this.tache.init(tache);
        this.calendrier = this.calendrier.init(calendrier);
        return this;
    }

    @XmlElement(required = true)
    public TacheXmlWrapper getTache() {
        return tache;
    }

    public void setTache(TacheXmlWrapper tache) {
        this.tache = tache;
    }

    @XmlElement(required = true)
    public CalendrierChargesXmlWrapper getCalendrier() {
        return calendrier;
    }

    public void setCalendrier(CalendrierChargesXmlWrapper calendrier) {
        this.calendrier = calendrier;
    }

}

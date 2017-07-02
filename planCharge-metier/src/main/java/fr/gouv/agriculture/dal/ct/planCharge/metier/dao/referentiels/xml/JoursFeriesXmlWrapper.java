package fr.gouv.agriculture.dal.ct.planCharge.metier.dao.referentiels.xml;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlElement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by frederic.danna on 30/04/2017.
 */
public class JoursFeriesXmlWrapper {

    private List<JourFerieXmlWrapper> joursFeries = new ArrayList<>();

    /**
     * Constructeur vide (appelé notamment par JAXB).
     *
     * @return
     */
    public JoursFeriesXmlWrapper() {
        super();
    }

    public JoursFeriesXmlWrapper init(@NotNull List<JourFerieXmlWrapper> joursFeries) {
        this.joursFeries.clear();
        this.joursFeries.addAll(joursFeries);
        return this;
    }

    @XmlElement(name = "jourFerie", required = true)
    @NotNull
    public List<JourFerieXmlWrapper> getJoursFeries() {
        return joursFeries;
    }

    public void setJoursFeries(@NotNull List<JourFerieXmlWrapper> joursFeries) {
        this.joursFeries = joursFeries;
    }
}

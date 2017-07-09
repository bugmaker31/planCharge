package fr.gouv.agriculture.dal.ct.planCharge.metier.dao.referentiels.xml;

import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.JourFerie;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlElement;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by frederic.danna on 30/04/2017.
 */
public class JoursFeriesXmlWrapper {

    private List<JourFerieXmlWrapper> joursFeriesWrappers = new ArrayList<>();


    /**
     * Constructeur vide (appelé notamment par JAXB).
     *
     * @return
     */
    public JoursFeriesXmlWrapper() {
        super();
    }


    @XmlElement(name = "jourFerie", required = true)
    @NotNull
    public List<JourFerieXmlWrapper> getJoursFeries() {
        return joursFeriesWrappers;
    }

    public void setJoursFeries(@NotNull List<JourFerieXmlWrapper> joursFeriesWrappers) {
        this.joursFeriesWrappers = joursFeriesWrappers;
    }


    public JoursFeriesXmlWrapper init(@NotNull List<JourFerieXmlWrapper> joursFeriesWrappers) {
        this.joursFeriesWrappers.clear();
        this.joursFeriesWrappers.addAll(joursFeriesWrappers);
        return this;
    }

    @NotNull
    public Set<JourFerie> extract() {
        Set<JourFerie> joursFeries = new TreeSet<>(); // TreeSet pour trier, juste pour faciliter le débogage.
        for (JourFerieXmlWrapper joursFeriesWrapper : joursFeriesWrappers) {
            JourFerie jf = joursFeriesWrapper.extract();
            joursFeries.add(jf);
        }
        return joursFeries;
    }
}

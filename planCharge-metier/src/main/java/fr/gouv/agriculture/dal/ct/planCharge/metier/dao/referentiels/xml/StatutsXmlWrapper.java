package fr.gouv.agriculture.dal.ct.planCharge.metier.dao.referentiels.xml;

import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.Statut;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlElement;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by frederic.danna on 30/04/2017.
 */
public class StatutsXmlWrapper {

    private List<StatutXmlWrapper> statuts = new ArrayList<>();


    /**
     * Constructeur vide (appelé notamment par JAXB).
     *
     * @return
     */
    public StatutsXmlWrapper() {
        super();
    }


    @XmlElement(name = "statut", required = true)
    @NotNull
    public List<StatutXmlWrapper> getStatuts() {
        return statuts;
    }

    public void setStatuts(List<StatutXmlWrapper> statuts) {
        this.statuts = statuts;
    }


    public StatutsXmlWrapper init(List<StatutXmlWrapper> statuts) {
        this.statuts.clear();
        this.statuts.addAll(statuts);
        return this;
    }

    @NotNull
    public Set<Statut> extract() {
        Set<Statut> statuts = new TreeSet<>(); // TreeSet pour trier, juste pour faciliter le débogage.
        // TODO FDA 2017/07
        return statuts;
    }
}

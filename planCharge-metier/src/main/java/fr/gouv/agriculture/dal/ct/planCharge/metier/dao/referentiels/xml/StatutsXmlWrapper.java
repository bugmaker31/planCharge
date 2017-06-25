package fr.gouv.agriculture.dal.ct.planCharge.metier.dao.referentiels.xml;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlElement;
import java.util.ArrayList;
import java.util.List;

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

    public StatutsXmlWrapper init(List<StatutXmlWrapper> statuts) {
        this.statuts.clear();
        this.statuts.addAll(statuts);
        return this;
    }

    @XmlElement(name = "statut", required = true)
    @NotNull
    public List<StatutXmlWrapper> getStatuts() {
        return statuts;
    }

    public void setStatuts(List<StatutXmlWrapper> statuts) {
        this.statuts = statuts;
    }
}

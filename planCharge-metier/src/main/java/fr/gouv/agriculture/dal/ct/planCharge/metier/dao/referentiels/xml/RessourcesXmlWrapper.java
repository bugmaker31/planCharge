package fr.gouv.agriculture.dal.ct.planCharge.metier.dao.referentiels.xml;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlElement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by frederic.danna on 30/04/2017.
 */
public class RessourcesXmlWrapper {

    private List<RessourceXmlWrapper> ressources = new ArrayList<>();

    /**
     * Constructeur vide (appelé notamment par JAXB).
     *
     * @return
     */
    public RessourcesXmlWrapper() {
        super();
    }

    public RessourcesXmlWrapper init(List<RessourceXmlWrapper> ressources) {
        this.ressources.clear();
        this.ressources.addAll(ressources);
        return this;
    }

    @XmlElement(name = "ressource", required = true)
    @NotNull
    public List<RessourceXmlWrapper> getRessources() {
        return ressources;
    }

    public void setRessources(List<RessourceXmlWrapper> ressources) {
        this.ressources = ressources;
    }
}

package fr.gouv.agriculture.dal.ct.planCharge.metier.dao.referentiels.xml;

import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.Ressource;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlElement;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

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


    @XmlElement(name = "ressource", required = true)
    @NotNull
    public List<RessourceXmlWrapper> getRessources() {
        return ressources;
    }

    public void setRessources(List<RessourceXmlWrapper> ressources) {
        this.ressources = ressources;
    }


    public RessourcesXmlWrapper init(List<RessourceXmlWrapper> ressources) {
        this.ressources.clear();
        this.ressources.addAll(ressources);
        return this;
    }

    public Set<Ressource> extract() {
        Set<Ressource> ressources = new TreeSet<>(); // TreeSet pour trier, juste pour faciliter le débogage.
        // TODO FDA 2017/07
        return ressources;
    }
}

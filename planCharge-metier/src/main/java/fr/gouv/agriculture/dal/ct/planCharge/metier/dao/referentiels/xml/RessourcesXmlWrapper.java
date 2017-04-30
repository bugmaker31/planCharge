package fr.gouv.agriculture.dal.ct.planCharge.metier.dao.referentiels.xml;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;

/**
 * Created by frederic.danna on 30/04/2017.
 */
public class RessourcesXmlWrapper {

    private final List<RessourceXmlWrapper> ressources;

    public RessourcesXmlWrapper(List<RessourceXmlWrapper> ressources) {
        this.ressources = ressources;
    }

    @XmlElement(name="ressource", required = true)
    @NotNull
    public List<RessourceXmlWrapper> getRessources() {
        return ressources;
    }
}

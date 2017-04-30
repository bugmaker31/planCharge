package fr.gouv.agriculture.dal.ct.planCharge.metier.dao.referentiels.xml;

import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.Profil;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.Ressource;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

/**
 * Created by frederic.danna on 30/04/2017.
 */
public class RessourceXmlWrapper {

    private final Ressource ressource;

    public RessourceXmlWrapper(Ressource ressource) {
        this.ressource = ressource;
    }

    @XmlAttribute(name="id", required = true)
    public String getId() {
        return ressource.getIdentity();
    }

    @XmlElement(name="trigramme", required = true)
    @NotNull
    public String getCode() {
        return ressource.getTrigramme();
    }

}

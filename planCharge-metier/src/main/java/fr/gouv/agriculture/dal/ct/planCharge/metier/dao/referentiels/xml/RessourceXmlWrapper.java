package fr.gouv.agriculture.dal.ct.planCharge.metier.dao.referentiels.xml;

import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.Ressource;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

/**
 * Created by frederic.danna on 30/04/2017.
 */
public class RessourceXmlWrapper {

    private String id;
    private String trigramme;

    /**
     * Constructeur vide (appelé notamment par JAXB).
     *
     * @return
     */
    public RessourceXmlWrapper() {
        super();
    }

    public RessourceXmlWrapper init(Ressource ressource) {
        this.id = ressource.getIdentity();
        this.trigramme = ressource.getTrigramme();
        return this;
    }

    @XmlAttribute(name = "id", required = true)
    public String getId() {
        return id;
    }

    @XmlElement(name = "trigramme", required = true)
    @NotNull
    public String getTrigramme() {
        return trigramme;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTrigramme(String trigramme) {
        this.trigramme = trigramme;
    }
}

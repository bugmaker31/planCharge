package fr.gouv.agriculture.dal.ct.planCharge.metier.dao.referentiels.xml;

import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.ProjetAppli;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.Ressource;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

/**
 * Created by frederic.danna on 30/04/2017.
 */
public class ProjetAppliXmlWrapper {

    private final ProjetAppli projetAppli;

    public ProjetAppliXmlWrapper(ProjetAppli projetAppli) {
        this.projetAppli = projetAppli;
    }

    @XmlAttribute(name="id", required = true)
    public String getId() {
        return projetAppli.getIdentity();
    }

    @XmlElement(name="code", required = true)
    @NotNull
    public String getCode() {
        return projetAppli.getCode();
    }

}

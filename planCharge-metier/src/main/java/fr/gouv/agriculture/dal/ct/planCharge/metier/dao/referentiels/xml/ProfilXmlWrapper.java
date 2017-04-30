package fr.gouv.agriculture.dal.ct.planCharge.metier.dao.referentiels.xml;

import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.Importance;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.Profil;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

/**
 * Created by frederic.danna on 30/04/2017.
 */
public class ProfilXmlWrapper {

    private final Profil profil;

    public ProfilXmlWrapper(Profil profil) {
        this.profil = profil;
    }

    @XmlAttribute(name="id", required = true)
    public String getId() {
        return profil.getIdentity();
    }

    @XmlElement(name="code", required = true)
    @NotNull
    public String getCode() {
        return profil.getCode();
    }

}

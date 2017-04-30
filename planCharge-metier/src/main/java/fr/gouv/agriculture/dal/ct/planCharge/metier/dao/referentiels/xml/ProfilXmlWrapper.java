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

    private String id;
    private String code;

    /**
     * Constructeur vide (appelé notamment par JAXB).
     *
     * @return
     */
    public ProfilXmlWrapper() {
        super();
    }

    public ProfilXmlWrapper init (Profil profil) {
        this.id = profil.getIdentity();
        this.code = profil.getCode();
        return this;
    }

    @XmlAttribute(name="id", required = true)
    public String getId() {
        return id;
    }

    @XmlElement(name="code", required = true)
    @NotNull
    public String getCode() {
        return code;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCode(String code) {
        this.code = code;
    }
}

package fr.gouv.agriculture.dal.ct.planCharge.metier.dao.referentiels.xml;

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


    @XmlAttribute(required = true)
    @NotNull
    public String getId() {
        return id;
    }

    @XmlElement(required = true)
    @NotNull
    public String getCode() {
        return code;
    }


    public void setId(@NotNull String id) {
        this.id = id;
    }

    public void setCode(@NotNull String code) {
        this.code = code;
    }


    public ProfilXmlWrapper init(@NotNull Profil profil) {
        this.id = profil.getIdentity();
        this.code = profil.getCode();
        return this;
    }

    public Profil extract() {
        return new Profil(code);
    }
}

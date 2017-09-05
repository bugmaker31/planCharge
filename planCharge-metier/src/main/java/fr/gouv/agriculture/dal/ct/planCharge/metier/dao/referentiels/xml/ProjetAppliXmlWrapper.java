package fr.gouv.agriculture.dal.ct.planCharge.metier.dao.referentiels.xml;

import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.ProjetAppli;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

/**
 * Created by frederic.danna on 30/04/2017.
 */
public class ProjetAppliXmlWrapper {

    @SuppressWarnings("InstanceVariableNamingConvention")
    private String id;
    private String code;
    private String nom;
    private String trigrammeCPI;


    /**
     * Constructeur vide (appelé notamment par JAXB).
     *
     * @return
     */
    public ProjetAppliXmlWrapper() {
        super();
    }


    @XmlAttribute(required = true)
    public String getId() {
        return id;
    }

    @XmlElement(required = true)
    @NotNull
    public String getCode() {
        return code;
    }

    @XmlElement(required = false)
    public String getNom() {
        return nom;
    }

    @XmlElement(required = false)
    public String getTrigrammeCPI() {
        return trigrammeCPI;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setTrigrammeCPI(String trigrammeCPI) {
        this.trigrammeCPI = trigrammeCPI;
    }


    public ProjetAppliXmlWrapper init(@NotNull ProjetAppli projetAppli) {
        this.id = projetAppli.getIdentity();
        this.code = projetAppli.getCode();
        this.nom = projetAppli.getNom();
        this.trigrammeCPI = projetAppli.getTrigrammeCPI();
        return this;
    }

    public ProjetAppli extract() {
        return new ProjetAppli(code, nom, trigrammeCPI);
    }
}

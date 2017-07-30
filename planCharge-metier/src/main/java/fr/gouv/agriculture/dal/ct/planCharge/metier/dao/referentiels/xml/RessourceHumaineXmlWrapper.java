package fr.gouv.agriculture.dal.ct.planCharge.metier.dao.referentiels.xml;

import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.Ressource;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.RessourceHumaine;
import fr.gouv.agriculture.dal.ct.planCharge.util.Dates;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import java.util.Date;

/**
 * Created by frederic.danna on 30/04/2017.
 */
public class RessourceHumaineXmlWrapper {

    @NotNull
    private String id;
    @NotNull
    private String trigramme;
    @NotNull
    private String nom;
    @NotNull
    private String prenom;
    @NotNull
    private String societe;
    @Null
    private Date debutMission;
    @Null
    private Date finMission;


    /**
     * Constructeur vide (appelé notamment par JAXB).
     *
     * @return
     */
    public RessourceHumaineXmlWrapper() {
        super();
    }


    @XmlAttribute(name = "id", required = true)
    @NotNull
    public String getId() {
        return id;
    }

    @XmlElement(name = "trigramme", required = true)
    @NotNull
    public String getTrigramme() {
        return trigramme;
    }

    @XmlElement(name = "nom", required = true)
    @NotNull
    public String getNom() {
        return nom;
    }

    @XmlElement(name = "prenom", required = true)
    @NotNull
    public String getPrenom() {
        return prenom;
    }

    @XmlElement(name = "societe", required = true)
    @NotNull
    public String getSociete() {
        return societe;
    }

    @XmlElement(name = "dateDebutMission", required = false)
    @Null
    public Date getDebutMission() {
        return debutMission;
    }

    @XmlElement(name = "dateFinMission", required = false)
    @Null
    public Date getFinMission() {
        return finMission;
    }


    public void setId(String id) {
        this.id = id;
    }

    public void setTrigramme(String trigramme) {
        this.trigramme = trigramme;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setSociete(String societe) {
        this.societe = societe;
    }

    public void setDebutMission(Date debutMission) {
        this.debutMission = debutMission;
    }

    public void setFinMission(Date finMission) {
        this.finMission = finMission;
    }


    public RessourceHumaineXmlWrapper init(@NotNull RessourceHumaine ressource) {
        this.id = ressource.getIdentity();
        this.trigramme = ressource.getTrigramme();
        this.nom = ressource.getNom();
        this.prenom = ressource.getPrenom();
        this.societe = ressource.getSociete();
        this.debutMission = Dates.asDate(ressource.getDebutMission());
        this.finMission = Dates.asDate(ressource.getFinMission());
        return this;
    }

    @NotNull
    public RessourceHumaine extract() {
        return new RessourceHumaine(trigramme, nom, prenom, societe, Dates.asLocalDate(debutMission), Dates.asLocalDate(finMission));
    }
}

package fr.gouv.agriculture.dal.ct.planCharge.metier.dao.charge;

import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.*;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.time.LocalDate;

/**
 * Created by frederic.danna on 26/04/2017.
 */
@XmlAccessorType(XmlAccessType.PROPERTY)
public class TacheWrapper {

    private Tache tache;

    public TacheWrapper() {
    }

    public TacheWrapper(Tache tache) {
        this.tache = tache;
    }

    @XmlElement(name="id", required = true)
    public Integer getId() {
        return tache.getId();
    }

    @XmlElement(name="noTicketIdal", required = true)
    public String getNoTicketIdal() {
        return tache.getNoTicketIdal();
    }

    @XmlElement(name="description", required = true)
    public String getDescription() {
        return tache.getDescription();
    }

    @XmlElement(required = true)
    public String getProjetAppli() {
        return tache.getProjetAppli().getCode();
    }

    @XmlElement(name="debut", required = false)
    public LocalDate getDebut() {
        return tache.getDebut();
    }

    @XmlElement(name="echeance", required = true)
    public LocalDate getEcheance() {
        return tache.getEcheance();
    }

    @XmlElement(name="importance", required = true)
    public String getImportance() {
        return tache.getImportance().getCode();
    }

    @XmlElement(name="charge", required = true)
    public double getCharge() {
        return tache.getCharge();
    }

    @XmlElement(name="ressource", required = true)
    public String getRessource() {
        return tache.getRessource().getTrigramme();
    }

    @XmlElement(name="profil", required = true)
    public String getProfil() {
        return tache.getProfil().getCode();
    }

}

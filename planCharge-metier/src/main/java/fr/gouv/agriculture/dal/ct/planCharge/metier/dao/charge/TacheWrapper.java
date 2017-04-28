package fr.gouv.agriculture.dal.ct.planCharge.metier.dao.charge;

import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.Tache;
import fr.gouv.agriculture.dal.ct.planCharge.util.Dates;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import java.util.Date;

/**
 * Created by frederic.danna on 26/04/2017.
 */
public class TacheWrapper {

    private Tache tache;

    public TacheWrapper() {
        super();
    }

    public TacheWrapper(Tache tache) {
        this.tache = tache;
    }

    @XmlAttribute(name = "id", required = true)
    public Integer getId() {
        return tache.getId();
    }

    @XmlElement(name = "noTache", required = true)
    public String getNoTache() {
        return tache.noTache();
    }

    @XmlElement(name = "noTicketIdal", required = true)
    public String getNoTicketIdal() {
        return tache.getNoTicketIdal();
    }

    @XmlElement(name = "description", required = true)
    public String getDescription() {
        return tache.getDescription();
    }

    @XmlElement(required = true)
    public String getProjetAppli() {
        if (tache.getProjetAppli() == null) {
            return null;
        }
        return tache.getProjetAppli().getCode();
    }

    @XmlElement(name = "debut", required = false)
    public Date getDebut() {
        if (tache.getDebut() == null) {
            return null;
        }
        return Dates.asDate(tache.getDebut());
    }

    @XmlElement(name = "echeance", required = true)
    public Date getEcheance() {
        if (tache.getEcheance() == null) {
            return null;
        }
        return Dates.asDate(tache.getEcheance());
    }

    @XmlElement(name = "importance", required = true)
    public String getImportance() {
        if (tache.getImportance() == null) {
            return null;
        }
        return tache.getImportance().getCode();
    }

    @XmlElement(name = "charge", required = true)
    public double getCharge() {
        return tache.getCharge();
    }

    @XmlElement(name = "ressource", required = true)
    public String getRessource() {
        if (tache.getRessource() == null) {
            return null;
        }
        return tache.getRessource().getTrigramme();
    }

    @XmlElement(name = "profil", required = true)
    public String getProfil() {
        if (tache.getProfil() == null) {
            return null;
        }
        return tache.getProfil().getCode();
    }

}

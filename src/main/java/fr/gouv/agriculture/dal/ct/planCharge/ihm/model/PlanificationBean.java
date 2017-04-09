package fr.gouv.agriculture.dal.ct.planCharge.ihm.model;

import java.time.format.DateTimeFormatter;

/**
 * Created by frederic.danna on 26/03/2017.
 */
public class PlanificationBean {

    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final TacheBean tache;

    public PlanificationBean(TacheBean tache) {
        this.tache = tache;
    }

    public TacheBean getTache() {
        return tache;
    }

    public boolean matcheNoTache(String otherValue) {
        if (new String(getTache().getId() + "").contains(otherValue)) {
            return true; // matches
        }
        if (new String("T" + getTache().getId()).contains(otherValue)) {
            return true; // matches
        }
        return false; // does not match.
    }

    public boolean matcheNoTicketIdal(String otherValue) {
        if (getTache().getNoTicketIdal().contains(otherValue)) {
            return true; // matches
        }
        return false; // does not match.
    }

    public boolean matcheDescription(String otherValue) {
        if (getTache().getDescription().contains(otherValue)) {
            return true; // matches
        }
        return false; // does not match.
    }

    public boolean matcheProjetAppli(String otherValue) {
        if (getTache().getProjetAppli().contains(otherValue)) {
            return true; // matches
        }
        return false; // does not match.
    }

    public boolean matcheImportance(String otherValue) {
        if (getTache().getImportance().contains(otherValue)) {
            return true; // matches
        }
        return false; // does not match.
    }

    public boolean matcheDebut(String otherValue) {
        if (getTache().getDebut().format(DATE_FORMATTER).contains(otherValue)) {
            return true; // matches
        }
        return false; // does not match.
    }

    public boolean matcheEcheance(String otherValue) {
        if (getTache().getEcheance().format(DATE_FORMATTER).contains(otherValue)) {
            return true; // matches
        }
        return false; // does not match.
    }


    public boolean matcheRessource(String otherValue) {
        if (getTache().getRessource().contains(otherValue)) {
            return true; // matches
        }
        return false; // does not match.
    }

    public boolean matcheProfil(String otherValue) {
        if (getTache().getProfil().contains(otherValue)) {
            return true; // matches
        }
        return false; // does not match.
    }
}

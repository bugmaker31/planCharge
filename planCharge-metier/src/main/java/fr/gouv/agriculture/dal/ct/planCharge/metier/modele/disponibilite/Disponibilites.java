package fr.gouv.agriculture.dal.ct.planCharge.metier.modele.disponibilite;

import fr.gouv.agriculture.dal.ct.metier.modele.AbstractEntity;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.Profil;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.RessourceHumaine;
import fr.gouv.agriculture.dal.ct.planCharge.util.number.Percentage;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Map;

public class Disponibilites extends AbstractEntity {


    // Fields:

    @NotNull
    private final Map<RessourceHumaine, Map<LocalDate, Float>> nbrsJoursAbsence;
    @NotNull
    private final Map<RessourceHumaine, Map<LocalDate, Percentage>> pctagesDispoCT;
    @NotNull
    private final Map<RessourceHumaine, Map<Profil, Map<LocalDate, Percentage>>> pctagesDispoMaxProfil;


    // Constructors:

    public Disponibilites(@NotNull Map<RessourceHumaine, Map<LocalDate, Float>> nbrsJoursAbsence, @NotNull Map<RessourceHumaine, Map<LocalDate, Percentage>> pctagesDispoCT, @NotNull Map<RessourceHumaine, Map<Profil, Map<LocalDate, Percentage>>> pctagesDispoMaxProfil) {
        super();
        this.nbrsJoursAbsence = nbrsJoursAbsence;
        this.pctagesDispoCT = pctagesDispoCT;
        this.pctagesDispoMaxProfil = pctagesDispoMaxProfil;
    }


    // Getters/Setters:

    @NotNull
    public Map<RessourceHumaine, Map<LocalDate, Float>> getNbrsJoursAbsence() {
        return nbrsJoursAbsence;
    }

    @NotNull
    public Map<RessourceHumaine, Map<LocalDate, Percentage>> getPctagesDispoCT() {
        return pctagesDispoCT;
    }

    @NotNull
    public Map<RessourceHumaine, Map<Profil, Map<LocalDate, Percentage>>> getPctagesDispoMaxProfil() {
        return pctagesDispoMaxProfil;
    }

    // Implémentation de AbstractEntity :

    @NotNull
    @Override
    public Serializable getIdentity() {
        return null; // TODO FDA 2017/07 Trouver mieux comme code.
    }


    @Override
    public int compareTo(Object o) {
        return 0; // TODO FDA 2017/07 Trouver mieux comme code.
    }


    @Override
    public String toString() {
        return nbrsJoursAbsence
                + " " + pctagesDispoCT
                + " " + pctagesDispoMaxProfil;
    }
}

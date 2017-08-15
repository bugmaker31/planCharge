package fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels;

import fr.gouv.agriculture.dal.ct.metier.regleGestion.RegleGestion;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Created by frederic.danna on 25/03/2017.
 */
public class RessourceHumaine extends Ressource<RessourceHumaine> {

    @NotNull
    private final String nom;
    @NotNull
    private final String prenom;
    @NotNull
    private final String societe;
    @Null
    private final LocalDate debutMission;
    @Null
    private final LocalDate finMission;


    public RessourceHumaine(@NotNull String trigramme, @NotNull String nom, @NotNull String prenom, @NotNull String societe, @Null LocalDate debutMission, @Null LocalDate finMission) {
        super(trigramme);
        this.nom = nom;
        this.prenom = prenom;
        this.societe = societe;
        this.debutMission = debutMission;
        this.finMission = finMission;
    }


    @SuppressWarnings("SuspiciousGetterSetter")
    @NotNull
    public String getTrigramme() {
        return getCode();
    }

    @NotNull
    public String getNom() {
        return nom;
    }

    @NotNull
    public String getPrenom() {
        return prenom;
    }

    @NotNull
    public String getSociete() {
        return societe;
    }

    @Null
    public LocalDate getDebutMission() {
        return debutMission;
    }

    @Null
    public LocalDate getFinMission() {
        return finMission;
    }


    @NotNull
    @Override
    public String getIdentity() {
        return getTrigramme();
    }


    // Juste pour faciliter le debogage.
    @Override
    public String toString() {
        return ("[" + getTrigramme() + "]")
                + " " + nom.toUpperCase()
                + " " + prenom
                + " " + ("(" + societe + ")")
                + " " + (
                (debutMission == null ? "N/C" : debutMission.format(DateTimeFormatter.ISO_LOCAL_DATE))
                        + ".." + (finMission == null ? "N/C" : finMission.format(DateTimeFormatter.ISO_LOCAL_DATE))
        );
    }
}

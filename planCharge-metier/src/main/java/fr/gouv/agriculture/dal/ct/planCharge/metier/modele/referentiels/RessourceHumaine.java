package fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels;

import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.AbstractEntity;
import fr.gouv.agriculture.dal.ct.planCharge.metier.regleGestion.RegleGestion;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

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


    @NotNull
    @Override
    public Set<RegleGestion<RessourceHumaine>> getReglesGestion() {
        return new HashSet<>(); // TODO FDA 2017/07 Coder les règles de gestion.
    }


    // Juste pour faciliter le debogage.
    @Override
    public String toString() {
        return ("[" + getTrigramme() + "]")
                + " " + nom.toUpperCase()
                + " " + prenom
                + " " + ("(" + societe + ")")
                + " " + (
                (debutMission == null ? "N/C" : debutMission.format(DateTimeFormatter.BASIC_ISO_DATE))
                        + ".." + (finMission == null ? "N/C" : finMission.format(DateTimeFormatter.BASIC_ISO_DATE))
        );
    }
}

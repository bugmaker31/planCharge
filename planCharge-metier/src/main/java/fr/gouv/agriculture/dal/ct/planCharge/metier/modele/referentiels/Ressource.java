package fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels;

import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.AbstractEntity;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Created by frederic.danna on 25/03/2017.
 */
public class Ressource extends AbstractEntity<String> implements Comparable<Ressource> {

    @NotNull
    private final String trigramme;
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


    public Ressource(@NotNull String trigramme, @NotNull String nom, @NotNull String prenom, @NotNull String societe, @Null LocalDate debutMission, @Null LocalDate finMission) {
        this.trigramme = trigramme;
        this.nom = nom;
        this.prenom = prenom;
        this.societe = societe;
        this.debutMission = debutMission;
        this.finMission = finMission;
    }


    @NotNull
    public String getTrigramme() {
        return trigramme;
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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Ressource ressource = (Ressource) o;

        return trigramme.equals(ressource.trigramme);
    }

    @Override
    public int hashCode() {
        return trigramme.hashCode();
    }


    @NotNull
    @Override
    public int compareTo(@NotNull Ressource o) {
        return trigramme.compareTo(o.getTrigramme());
    }


    // Juste pour faciliter le debogage.
    @Override
    public String toString() {
        return ("[" + trigramme + "]")
                + " " + nom.toUpperCase()
                + " " + prenom
                + " " + ("(" + societe + ")")
                + " " + (
                (debutMission == null ? "N/C" : debutMission.format(DateTimeFormatter.BASIC_ISO_DATE))
                        + ".." + (finMission == null ? "N/C" : finMission.format(DateTimeFormatter.BASIC_ISO_DATE))
        );
    }
}

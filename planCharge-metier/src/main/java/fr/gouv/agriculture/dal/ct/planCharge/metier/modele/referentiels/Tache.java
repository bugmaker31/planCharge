package fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels;

import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.ModeleException;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.time.LocalDate;

/**
 * Created by frederic.danna on 11/03/2017.
 */
public class Tache implements Comparable<Tache> {

    public static final String FORMAT_NO_TACHE = "T%04d";

    @NotNull
    private int id;
    @NotNull
    private String noTicketIdal;
    @NotNull
    private String description;
    @NotNull
    private ProjetAppli projetAppli;
    @Null
    private LocalDate debut;
    @NotNull
    private LocalDate echeance;
    @NotNull
    private Importance importance;
    @NotNull
    private double charge;
    @NotNull
    private Ressource ressource;
    @NotNull
    private Profil profil;

    public Tache(int id, String noTicketIdal, String description, ProjetAppli projetAppli, LocalDate debut, LocalDate echeance, Importance importance, double charge, Ressource ressource, Profil profil)
    throws ModeleException {

        this.id = id;

        if (noTicketIdal == null) {
            throw new ModeleException("No ticket IDAL non défini");
        }
        this.noTicketIdal = noTicketIdal;

        if (description == null) {
            throw new ModeleException("Description non définie");
        }
        this.description = description;

        if (projetAppli == null) {
            throw new ModeleException("Projet/Appli non défini(e)");
        }
        this.projetAppli = projetAppli;

        this.debut = debut;

        if (echeance == null) {
            throw new ModeleException("Echéance non définie");
        }
        this.echeance = echeance;

        if (importance == null) {
            throw new ModeleException("Importance non définie");
        }
        this.importance = importance;

        if (importance == null) {
            throw new ModeleException("Charge non définie");
        }
        this.charge = charge;

        if (ressource == null) {
            throw new ModeleException("Ressource non définie");
        }
        this.ressource = ressource;

        if (profil == null) {
            throw new ModeleException("Profil non défini");
        }
        this.profil = profil;
    }

    @NotNull
    public int getId() {
        return id;
    }

    @NotNull
    public String getNoTicketIdal() {
        return noTicketIdal;
    }

    @NotNull
    public String getDescription() {
        return description;
    }

    @NotNull
    public ProjetAppli getProjetAppli() {
        return projetAppli;
    }

    @Null
    public LocalDate getDebut() {
        return debut;
    }

    @NotNull
    public LocalDate getEcheance() {
        return echeance;
    }

    @NotNull
    public Importance getImportance() {
        return importance;
    }

    @NotNull
    public double getCharge() {
        return charge;
    }

    @NotNull
    public Ressource getRessource() {
        return ressource;
    }

    @NotNull
    public Profil getProfil() {
        return profil;
    }

    @NotNull
    public String noTache() {
        return noTache(id);
    }

    @NotNull
    public static String noTache(int id) {
        return String.format(FORMAT_NO_TACHE, id);
    }

    @Override
    @NotNull
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tache tache = (Tache) o;

        return id == tache.id;
    }

    @Override
    @NotNull
    public int hashCode() {
        return id;
    }

    @Override
    public int compareTo(Tache o) {
        return new Integer(getId()).compareTo(o.getId());
    }

    // Pour les débug, uniquement.
    @Override
    @NotNull
    public String toString() {
        return ("[" + projetAppli + "]")
                + " "
                + noTache()
                + " "
                + ("(" + (noTicketIdal == null || noTicketIdal.isEmpty() ? "N/A" : noTicketIdal) + ")")
                + " "
                + ("<< " + description + " >> ")
                ;
    }
}

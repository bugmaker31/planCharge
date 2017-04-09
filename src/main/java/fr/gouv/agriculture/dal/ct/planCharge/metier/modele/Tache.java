package fr.gouv.agriculture.dal.ct.planCharge.metier.modele;

import java.time.LocalDate;

/**
 * Created by frederic.danna on 11/03/2017.
 */
public class Tache {

    public static final String FORMAT_NO_TACHE = "T%04d";

    private int id;
    private String noTicketIdal;
    private String description;
    private ProjetAppli projetAppli;
    private LocalDate debut;
    private LocalDate echeance;
    private Importance importance;
    private double charge;
    private Ressource ressource;
    private Profil profil;

    public Tache(int id, String noTicketIdal, String description, ProjetAppli projetAppli, LocalDate debut, LocalDate echeance, Importance importance, double charge, Ressource ressource, Profil profil) {
        this.id = id;
        this.noTicketIdal = noTicketIdal;
        this.description = description;
        this.projetAppli = projetAppli;
        this.debut = debut;
        this.echeance = echeance;
        this.importance = importance;
        this.charge = charge;
        this.ressource = ressource;
        this.profil = profil;
    }

    public int getId() {
        return id;
    }

    public String getNoTicketIdal() {
        return noTicketIdal;
    }

    public String getDescription() {
        return description;
    }

    public ProjetAppli getProjetAppli() {
        return projetAppli;
    }

    public LocalDate getDebut() {
        return debut;
    }

    public LocalDate getEcheance() {
        return echeance;
    }

    public Importance getImportance() {
        return importance;
    }

    public double getCharge() {
        return charge;
    }

    public Ressource getRessource() {
        return ressource;
    }

    public Profil getProfil() {
        return profil;
    }

    public String noTache() {
        return (id + "").format(FORMAT_NO_TACHE);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tache tache = (Tache) o;

        return id == tache.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    // Pour les débug, uniquement.
    @Override
    public String toString() {
        return "[" + projetAppli + "] " + noTache() + " (" + noTicketIdal + ") " + description;
    }
}

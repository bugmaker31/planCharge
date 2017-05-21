package fr.gouv.agriculture.dal.ct.planCharge.metier.modele.tache;

import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.AbstractEntity;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.ModeleException;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.time.LocalDate;

/**
 * Created by frederic.danna on 11/03/2017.
 */
public class Tache extends AbstractEntity<Integer> implements Comparable<Tache> {

    public static final String FORMAT_NO_TACHE = "T%04d";

    @SuppressWarnings("InstanceVariableNamingConvention")
    private int id;
    @NotNull
    private CategorieTache categorie;
    @Null
    private SousCategorieTache sousCategorie;
    @Null
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
    private double charge;
    @NotNull
    private Ressource ressource;
    @NotNull
    private Profil profil;

    /**
     * Instancie une nouvelle tâche.
     * @param id
     * @param categorie
     * @param sousCategorie
     * @param noTicketIdal
     * @param description
     * @param projetAppli
     * @param debut
     * @param echeance
     * @param importance
     * @param charge
     * @param ressource
     * @param profil
     * @throws ModeleException Si une donnée obligatoire manque (la {@link #categorie catégorie}, la {@link #description description}, etc.).
     */
    @SuppressWarnings("ConstructorWithTooManyParameters")
    public Tache(int id, @Null CategorieTache categorie, @Null SousCategorieTache sousCategorie, @Null String noTicketIdal, @Null String description, @Null ProjetAppli projetAppli, @Null LocalDate debut, @Null LocalDate echeance, @Null Importance importance, double charge, @Null Ressource ressource, @Null Profil profil) throws ModeleException {
        super();

        this.id = id;

        if (categorie == null) {
            throw new ModeleException("Tache n°" + id + " : Catégorie de tâche non définie.");
        }
        this.categorie = categorie;

//        if (sousCategorie == null) {
//            throw new ModeleException("Tache n°" + id + " : Sous-catégorie de tâche non définie.");
//        }
        this.sousCategorie = sousCategorie;

        if (noTicketIdal == null) {
            throw new ModeleException("Tache n°" + id + " : N° de ticket IDAL non défini.");
        }
        this.noTicketIdal = noTicketIdal;

        if (description == null) {
            throw new ModeleException("Tache n°" + id + " : Description non définie.");
        }
        this.description = description;

        if (projetAppli == null) {
            throw new ModeleException("Tache n°" + id + " : Projet/Appli non défini(e).");
        }
        this.projetAppli = projetAppli;

        this.debut = debut;

        if (echeance == null) {
            throw new ModeleException("Tache n°" + id + " : Echéance non définie.");
        }
        this.echeance = echeance;

        if (importance == null) {
            throw new ModeleException("Tache n°" + id + " : Importance non définie.");
        }
        this.importance = importance;

/*
        if (charge == null) {
            throw new ModeleException("Tache n°" + id + " : Charge non définie.");
        }
*/
        this.charge = charge;

        if (ressource == null) {
            throw new ModeleException("Tache n°" + id + " : Ressource non définie.");
        }
        this.ressource = ressource;

        if (profil == null) {
            throw new ModeleException("Tache n°" + id + " : Profil non défini.");
        }
        this.profil = profil;
    }

    public int getId() {
        return id;
    }

    @NotNull
    public CategorieTache getCategorie() {
        return categorie;
    }

    @Null
    public SousCategorieTache getSousCategorie() {
        return sousCategorie;
    }

    @Null
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if ((o == null) || (getClass() != o.getClass())) return false;
        Tache tache = (Tache) o;
        return id == tache.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public int compareTo(Tache o) {
        return new Integer(getId()).compareTo(o.getId());
    }

    @NotNull
    @Override
    public Integer getIdentity() {
        return getId();
    }

    // Pour les débug, uniquement.
    @Override
    @NotNull
    public String toString() {
        return /*(categorie.getCode() + (sousCategorie == null ? "" : ("::" + sousCategorie.getCode())))
                + " "
                +*/ ("[" + projetAppli + "]")
                + " "
                + noTache()
                + " "
                + ("(" + (noTicketIdal.isEmpty() ? "N/A" : noTicketIdal) + ")")
                + " "
                + ("<< " + description + " >> ")
                ;
    }
}

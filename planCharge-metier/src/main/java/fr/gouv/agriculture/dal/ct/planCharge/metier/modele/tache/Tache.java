package fr.gouv.agriculture.dal.ct.planCharge.metier.modele.tache;

import fr.gouv.agriculture.dal.ct.metier.modele.AbstractEntity;
import fr.gouv.agriculture.dal.ct.metier.modele.ModeleException;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.time.LocalDate;

/**
 * Created by frederic.danna on 11/03/2017.
 */
public class Tache extends AbstractEntity<Integer, Tache> implements ITache, Comparable<Tache> {

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
    @NotNull
    private Statut statut;
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
     *
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
    public Tache(int id, @Null CategorieTache categorie, @Null SousCategorieTache sousCategorie, @Null String noTicketIdal, @Null String description, @Null ProjetAppli projetAppli, @Null Statut statut, @Null LocalDate debut, @Null LocalDate echeance, @Null Importance importance, double charge, @Null Ressource ressource, @Null Profil profil) /*throws ModeleException*/ {
        super();

        this.id = id;

//        ctrlNonNull(id, categorie, "catégorie");
        this.categorie = categorie;

//        ctrlNonNull(id, sousCategorie, "sous-catégorie");
        this.sousCategorie = sousCategorie;

//        ctrlNonNull(id, noTicketIdal, "n° de ticket IDAL");
        this.noTicketIdal = noTicketIdal;

//        ctrlNonNull(id, description, "description");
        this.description = description;

        //noinspection HardcodedFileSeparator
//        ctrlNonNull(id, projetAppli, "projet/appli");
        this.projetAppli = projetAppli;

//        ctrlNonNull(id, statut, "statut");
        this.statut = statut;

//        ctrlNonNull(id, debut, "date de début");
        this.debut = debut;

//        ctrlNonNull(id, echeance, "date d'échéance");
        this.echeance = echeance;

//        ctrlNonNull(id, importance, "importance");
        this.importance = importance;

//        ctrlNonNull(id, charge, "charge");
        this.charge = charge;

//        ctrlNonNull(id, ressource, "ressource");
        this.ressource = ressource;

//        ctrlNonNull(id, profil, "profil");
        this.profil = profil;
    }

    private void ctrlNonNull(int idTache, @Null Object propriete, @NotNull String nomPropriete) throws ModeleException {
        if (propriete == null) {
            throw new ModeleException("Tache n°" + idTache + " : " + nomPropriete + " de tâche non défini(e).");
        }
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

    @NotNull
    public Statut getStatut() {
        return statut;
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

    public boolean estProvision() {
        return statut.equals(Statut.PROVISION);
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
        //noinspection HardcodedFileSeparator
        return /*(categorie.getCode() + (sousCategorie == null ? "" : ("::" + sousCategorie.getCode())))
                + " " +*/ noTache()
                + " " + ("(" + (noTicketIdal.isEmpty() ? "N/A" : noTicketIdal) + ")")
                + " " + ("[" + projetAppli + "]")
                + " " + ("{" + statut.getIdentity() + "}")
                + " " + ("<< " + description + " >> ")
                ;
    }
}

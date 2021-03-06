package fr.gouv.agriculture.dal.ct.planCharge.metier.modele.tache;

import fr.gouv.agriculture.dal.ct.metier.modele.AbstractEntity;
import fr.gouv.agriculture.dal.ct.metier.modele.ModeleException;
import fr.gouv.agriculture.dal.ct.planCharge.metier.constante.TypeChangement;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.*;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.revision.StatutRevision;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.revision.ValidateurRevision;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.time.LocalDate;

/**
 * Created by frederic.danna on 11/03/2017.
 */
public class Tache extends AbstractEntity<Integer, Tache> implements ITache<Tache> {

    public static final String FORMAT_NO_TACHE = "T%04d";

    @SuppressWarnings("InstanceVariableNamingConvention")
    private final int id;
    @NotNull
    private CategorieTache categorie;
    @Null
    private SousCategorieTache sousCategorie;
    @NotNull
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
    private Ressource<?> ressource;
    @NotNull
    private Profil profil;

    @Null
    private TypeChangement typeChangement;

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
    public Tache(int id, @NotNull CategorieTache categorie, @Null SousCategorieTache sousCategorie, @NotNull String noTicketIdal, @NotNull String description, @NotNull ProjetAppli projetAppli, @NotNull Statut statut, @Null LocalDate debut, @NotNull LocalDate echeance, @NotNull Importance importance, double charge, @NotNull Ressource ressource, @NotNull Profil profil) /*throws ModeleException*/ {
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

/*
    private void ctrlNonNull(int idTache, @Null Object propriete, @NotNull String nomPropriete) throws ModeleException {
        if (propriete == null) {
            throw new ModeleException("Tache n°" + idTache + " : " + nomPropriete + " de tâche non défini(e).");
        }
    }
*/


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
    public Ressource<?> getRessource() {
        return ressource;
    }

    @NotNull
    public Profil getProfil() {
        return profil;
    }


    @Null
    public TypeChangement getTypeChangement() {
        return typeChangement;
    }

    public void setTypeChangement(@Null TypeChangement typeChangement) {
        this.typeChangement = typeChangement;
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


    /*
     Revisable :
     */

    @Null
    private StatutRevision statutRevision;

    @Override
    @Null
    public StatutRevision getStatutRevision() {
        return statutRevision;
    }

    public void setStatutRevision(@Null StatutRevision statutRevision) {
        this.statutRevision = statutRevision;
    }


    @Null
    private ValidateurRevision validateurRevision;

    @Override
    @Null
    public ValidateurRevision getValidateurRevision() {
        return validateurRevision;
    }

    public void setValidateurRevision(@Null ValidateurRevision validateurRevision) {
        this.validateurRevision = validateurRevision;
    }


    @Null
    private String commentaireRevision;

    @Override
    @Null
    public String getCommentaireRevision() {
        return commentaireRevision;
    }

    @Override
    public void setCommentaireRevision(@Null String commentaireRevision) {
        this.commentaireRevision = commentaireRevision;
    }


    /*
     Comparable
     */

    @Override
    public int compareTo(@Null Tache o) {
        if (o == null) {
            return 1; // Les IDs indéfinis sont triés en tête de liste.
        }
        return Integer.compare(id, o.getId());
    }

    @NotNull
    @Override
    public Integer getIdentity() {
        return id;
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

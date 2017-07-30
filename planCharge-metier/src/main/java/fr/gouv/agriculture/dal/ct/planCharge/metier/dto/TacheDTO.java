package fr.gouv.agriculture.dal.ct.planCharge.metier.dto;

import fr.gouv.agriculture.dal.ct.metier.dto.AbstractDTO;
import fr.gouv.agriculture.dal.ct.metier.dto.DTOException;
import fr.gouv.agriculture.dal.ct.metier.modele.AbstractEntity;
import fr.gouv.agriculture.dal.ct.metier.modele.ModeleException;
import fr.gouv.agriculture.dal.ct.metier.regleGestion.RegleGestion;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.*;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.tache.ITache;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.tache.Tache;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by frederic.danna on 11/03/2017.
 */
public class TacheDTO extends AbstractDTO<Tache, Integer, TacheDTO> implements ITache, Comparable<TacheDTO> {

    public static final String FORMAT_NO_TACHE = "T%04d";

    @SuppressWarnings("InstanceVariableNamingConvention")
    private int id;
    @Null
    private CategorieTacheDTO categorie;
    @Null
    private SousCategorieTacheDTO sousCategorie;
    @Null
    private String noTicketIdal;
    @Null
    private String description;
    @Null
    private ProjetAppliDTO projetAppli;
    @Null
    private StatutDTO statut;
    @Null
    private LocalDate debut;
    @Null
    private LocalDate echeance;
    @Null
    private ImportanceDTO importance;
    @Null
    private Double charge;
    @Null
    private RessourceDTO<? extends Ressource, ? extends RessourceDTO> ressource;
    @Null
    private ProfilDTO profil;


    private TacheDTO() {
        super();
    }


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
    public TacheDTO(int id, @Null CategorieTacheDTO categorie, @Null SousCategorieTacheDTO sousCategorie, @Null String noTicketIdal, @Null String description, @Null ProjetAppliDTO projetAppli, @Null StatutDTO statut, @Null LocalDate debut, @Null LocalDate echeance, @Null ImportanceDTO importance, @Null Double charge, @Null RessourceDTO ressource, @Null ProfilDTO profil) {
        this();
        this.id = id;
        this.categorie = categorie;
        this.sousCategorie = sousCategorie;
        this.noTicketIdal = noTicketIdal;
        this.description = description;
        this.projetAppli = projetAppli;
        this.statut = statut;
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

    @Null
    public CategorieTacheDTO getCategorie() {
        return categorie;
    }

    @Null
    public SousCategorieTacheDTO getSousCategorie() {
        return sousCategorie;
    }

    @Null
    public String getNoTicketIdal() {
        return noTicketIdal;
    }

    @Null
    public String getDescription() {
        return description;
    }

    @Null
    public ProjetAppliDTO getProjetAppli() {
        return projetAppli;
    }

    @Null
    public StatutDTO getStatut() {
        return statut;
    }

    @Null
    public LocalDate getDebut() {
        return debut;
    }

    @Null
    public LocalDate getEcheance() {
        return echeance;
    }

    @Null
    public ImportanceDTO getImportance() {
        return importance;
    }

    public double getCharge() {
        return charge;
    }

    @Null
    public RessourceDTO getRessource() {
        return ressource;
    }

    @Null
    public ProfilDTO getProfil() {
        return profil;
    }


    @Null
    @Override
    public Integer getIdentity() {
        return getId();
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


    @NotNull
    @Override
    public Tache toEntity() throws DTOException {
        return new Tache(id, categorie.toEntity(), sousCategorie.toEntity(), noTicketIdal, description, projetAppli.toEntity(), statut.toEntity(), debut, echeance, importance.toEntity(), charge, ressource.toEntity(), profil.toEntity());
    }

    @NotNull
    @Override
    public TacheDTO fromEntity(@NotNull Tache entity) throws DTOException {
        return new TacheDTO(
                entity.getId(),
                CategorieTacheDTO.from(entity.getCategorie()),
                SousCategorieTacheDTO.from(entity.getSousCategorie()),
                entity.getNoTicketIdal(),
                entity.getDescription(),
                ProjetAppliDTO.from(entity.getProjetAppli()),
                StatutDTO.from(entity.getStatut()),
                entity.getDebut(), entity.getEcheance(),
                ImportanceDTO.from(entity.getImportance()),
                entity.getCharge(),
                RessourceDTO.from(entity.getRessource()),
                ProfilDTO.from(entity.getProfil())
        );
    }

    @NotNull
    public static TacheDTO from(@NotNull Tache tacheImportee) throws DTOException {
        return new TacheDTO().fromEntity(tacheImportee);
    }


    @NotNull
    @Override
    public List<RegleGestion<TacheDTO>> getReglesGestion() {
        return Collections.emptyList(); // TODO FDA 2017/07 Coder les règles de gestion.
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if ((o == null) || (getClass() != o.getClass())) return false;
        TacheDTO tache = (TacheDTO) o;
        return id == tache.id;
    }

    @Override
    public int hashCode() {
        return id;
    }


    @Override
    public int compareTo(TacheDTO o) {
        return new Integer(getId()).compareTo(o.getId());
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

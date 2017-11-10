package fr.gouv.agriculture.dal.ct.planCharge.metier.dto;

import fr.gouv.agriculture.dal.ct.metier.dto.AbstractDTO;
import fr.gouv.agriculture.dal.ct.metier.dto.DTOException;
import fr.gouv.agriculture.dal.ct.metier.modele.ModeleException;
import fr.gouv.agriculture.dal.ct.metier.regleGestion.RegleGestion;
import fr.gouv.agriculture.dal.ct.planCharge.metier.constante.TypeChangement;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.Ressource;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.tache.ITache;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.tache.Tache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

/**
 * Created by frederic.danna on 11/03/2017.
 */
public class TacheDTO extends AbstractDTO<Tache, Integer, TacheDTO> implements ITache<TacheDTO> {

    @NotNull
    private static final Logger LOGGER = LoggerFactory.getLogger(TacheDTO.class);


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

    @Null
    private TypeChangement typeChangement;


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
    public TacheDTO(int id, @Null CategorieTacheDTO categorie, @Null SousCategorieTacheDTO sousCategorie, @Null String noTicketIdal, @Null String description, @Null ProjetAppliDTO projetAppli, @Null StatutDTO statut, @Null LocalDate debut, @Null LocalDate echeance, @Null ImportanceDTO importance, @Null Double charge, @Null RessourceDTO<? extends Ressource, ? extends RessourceDTO> ressource, @Null ProfilDTO profil) {
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

    @Null
    public Double getCharge() {
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
    public TypeChangement getTypeChangement() {
        return typeChangement;
    }

    public void setTypeChangement(@Null TypeChangement typeChangement) {
        this.typeChangement = typeChangement;
    }


    public void setCategorie(@Null CategorieTacheDTO categorie) {
        this.categorie = categorie;
    }

    public void setSousCategorie(@Null SousCategorieTacheDTO sousCategorie) {
        this.sousCategorie = sousCategorie;
    }


    @Null
    @Override
    public Integer getIdentity() {
        return getId();
    }


    @NotNull
    public String noTache() {
        return Tache.noTache(id);
    }


    @NotNull
    @Override
    public Tache toEntity() throws DTOException {
        Tache tache = new Tache(id,
                categorie.toEntity(),
                ((sousCategorie == null) ? null : sousCategorie.toEntity()),
                noTicketIdal,
                description,
                projetAppli.toEntity(),
                statut.toEntity(),
                debut,
                echeance,
                importance.toEntity(),
                charge,
                ressource.toEntity(),
                profil.toEntity()
        );
        tache.setTypeChangement(getTypeChangement());
        return tache;
    }

    @NotNull
    @Override
    public TacheDTO fromEntity(@NotNull Tache entity) throws DTOException {
        return from(entity);
    }

    @NotNull
    public static TacheDTO from(@NotNull Tache entity) throws DTOException {
        TacheDTO tacheDTO = new TacheDTO(
                entity.getId(),
                CategorieTacheDTO.from(entity.getCategorie()),
                (entity.getSousCategorie() == null) ? null : SousCategorieTacheDTO.from(entity.getSousCategorie()),
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
        tacheDTO.setTypeChangement(entity.getTypeChangement());
        return tacheDTO;
    }


    @NotNull
    @Override
    public List<RegleGestion<TacheDTO>> getReglesGestion() {
        return Collections.emptyList(); // TODO FDA 2017/07 Coder les règles de gestion.
    }


    // ITache

    public boolean estProvision() {
        return (statut != null) && statut.equals(StatutDTO.PROVISION);
    }

    @Override
    public int compareTo(@Null TacheDTO o) {
        try {
            Tache tache = toEntity();
            Tache autreTache = ((o == null) ? null : o.toEntity());
            return tache.compareTo(autreTache);
        } catch (DTOException e) {
            LOGGER.error("Impossible de comparer les 2 taches (DTO)s.", e);
            return 0;
        }
    }


    // Pour les débug, uniquement.
    @Override
    @NotNull
    public String toString() {
        //noinspection HardcodedFileSeparator
        return /*(categorie.getCode() + (sousCategorie == null ? "" : ("::" + sousCategorie.getCode())))
                + " " +*/ noTache()
                + " " + ((noTicketIdal.isEmpty() ? "N/A" : noTicketIdal))
                + " " + ("[" + projetAppli + "]")
                + " " + ("{" + statut.getIdentity() + "}")
                + " " + ("<< " + description + " >> ")
                ;
    }
}

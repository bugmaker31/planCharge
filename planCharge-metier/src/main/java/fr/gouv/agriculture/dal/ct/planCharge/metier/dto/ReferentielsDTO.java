package fr.gouv.agriculture.dal.ct.planCharge.metier.dto;

import fr.gouv.agriculture.dal.ct.metier.dto.AbstractDTO;
import fr.gouv.agriculture.dal.ct.metier.regleGestion.RegleGestion;
import fr.gouv.agriculture.dal.ct.metier.regleGestion.ViolationRegleGestion;
import fr.gouv.agriculture.dal.ct.metier.MetierException;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.Referentiels;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by frederic.danna on 01/07/2017.
 */
@SuppressWarnings("ClassHasNoToStringMethod")
public class ReferentielsDTO extends AbstractDTO<Referentiels, Serializable, ReferentielsDTO> {

    /* Rq : Contrairement à l'entité associée (Referentiels) qui utilise des Set, on utilise des List pour pouvoi g&rer les éventuels doublons dans la saisie. */

    @Null
    private List<JourFerieDTO> joursFeries;
    @Null
    private List<ImportanceDTO> importances;
    @Null
    private List<ProfilDTO> profils;
    @Null
    private List<ProjetAppliDTO> projetsApplis;
    @Null
    private List<StatutDTO> statuts;
    @Null
    private List<RessourceHumaineDTO> ressourcesHumaines;


    private ReferentielsDTO() {
        super();
    }

    public ReferentielsDTO(@Null List<JourFerieDTO> joursFeries, @Null List<ImportanceDTO> importances, @Null List<ProfilDTO> profils, @Null List<ProjetAppliDTO> projetsApplis, @Null List<StatutDTO> statuts, @Null List<RessourceHumaineDTO> ressourcesHumaines) {
        this.joursFeries = joursFeries;
        this.importances = importances;
        this.profils = profils;
        this.projetsApplis = projetsApplis;
        this.statuts = statuts;
        this.ressourcesHumaines = ressourcesHumaines;
    }

    @NotNull
    public static ReferentielsDTO from(Referentiels referentiels) {
        return new ReferentielsDTO().fromEntity(referentiels);
    }

    @Null
    public List<JourFerieDTO> getJoursFeries() {
        return joursFeries;
    }

    @Null
    public List<ImportanceDTO> getImportances() {
        return importances;
    }

    @Null
    public List<ProfilDTO> getProfils() {
        return profils;
    }

    @Null
    public List<ProjetAppliDTO> getProjetsApplis() {
        return projetsApplis;
    }

    @Null
    public List<StatutDTO> getStatuts() {
        return statuts;
    }

    @Null
    public List<RessourceHumaineDTO> getRessourcesHumaines() {
        return ressourcesHumaines;
    }

    @Null
    @Override
    public Serializable getIdentity() {
        return null; // TODO FDA 2017/07 Trouver mieux comme code.
    }

    @Override
    public int compareTo(@Null ReferentielsDTO o) {
        return 0; // TODO FDA 2017/07 Trouver mieux comme code.
    }

    @NotNull
    @Override
    public ReferentielsDTO fromEntity(@NotNull Referentiels entity) {
        return new ReferentielsDTO(
                entity.getJoursFeries().stream().map(JourFerieDTO::from).collect(Collectors.toList()),
                entity.getImportances().stream().map(ImportanceDTO::from).collect(Collectors.toList()),
                entity.getProfils().stream().map(ProfilDTO::from).collect(Collectors.toList()),
                entity.getProjetsApplis().stream().map(ProjetAppliDTO::from).collect(Collectors.toList()),
                entity.getStatuts().stream().map(StatutDTO::from).collect(Collectors.toList()),
                entity.getRessourcesHumaines().stream().map(RessourceHumaineDTO::from).collect(Collectors.toList())
        );
    }

    @NotNull
    @Override
    public Referentiels toEntity() {
        /* Rq : La transformation des List en Set enlève les éventuels doublons. */
        assert joursFeries != null; // TODO FDA 2017/07 Préciser la RG qui n'est pas respectée.
        assert importances != null; // TODO FDA 2017/07 Préciser la RG qui n'est pas respectée.
        assert profils != null; // TODO FDA 2017/07 Préciser la RG qui n'est pas respectée.
        assert projetsApplis != null; // TODO FDA 2017/07 Préciser la RG qui n'est pas respectée.
        assert statuts != null; // TODO FDA 2017/07 Préciser la RG qui n'est pas respectée.
        assert ressourcesHumaines != null; // TODO FDA 2017/07 Préciser la RG qui n'est pas respectée.
        return new Referentiels(
                joursFeries.stream().map(JourFerieDTO::toEntity).collect(Collectors.toSet()),
                importances.stream().map(ImportanceDTO::toEntity).collect(Collectors.toSet()),
                profils.stream().map(ProfilDTO::toEntity).collect(Collectors.toSet()),
                projetsApplis.stream().map(ProjetAppliDTO::toEntity).collect(Collectors.toSet()),
                statuts.stream().map(StatutDTO::toEntity).collect(Collectors.toSet()),
                ressourcesHumaines.stream().map(RessourceHumaineDTO::toEntity).collect(Collectors.toSet())
                );
    }

    @SuppressWarnings("MethodWithMultipleLoops")
    @NotNull
    @Override
    public List<ViolationRegleGestion> controlerReglesGestion() throws MetierException {
        List<ViolationRegleGestion> violations = super.controlerReglesGestion();
        assert joursFeries != null; // TODO FDA 2017/07 Préciser la RG qui n'est pas respectée.
        for (JourFerieDTO jourFerie : joursFeries) {
            violations.addAll(jourFerie.controlerReglesGestion());
        }
        assert importances != null; // TODO FDA 2017/07 Préciser la RG qui n'est pas respectée.
        for (ImportanceDTO importance : importances) {
            violations.addAll(importance.controlerReglesGestion());
        }
        assert profils != null; // TODO FDA 2017/07 Préciser la RG qui n'est pas respectée.
        for (ProfilDTO profil : profils) {
            violations.addAll(profil.controlerReglesGestion());
        }
        assert projetsApplis != null; // TODO FDA 2017/07 Préciser la RG qui n'est pas respectée.
        for (ProjetAppliDTO projetAppli : projetsApplis) {
            violations.addAll(projetAppli.controlerReglesGestion());
        }
        assert statuts != null; // TODO FDA 2017/07 Préciser la RG qui n'est pas respectée.
        for (StatutDTO statut : statuts) {
            violations.addAll(statut.controlerReglesGestion());
        }
        assert ressourcesHumaines != null; // TODO FDA 2017/07 Préciser la RG qui n'est pas respectée.
        for (RessourceHumaineDTO ressourceHumaine : ressourcesHumaines) {
            violations.addAll(ressourceHumaine.controlerReglesGestion());
        }
        return violations;
    }

    @NotNull
    @Override
    protected List<RegleGestion<ReferentielsDTO>> getReglesGestion() {
        return Collections.emptyList();
    }

}

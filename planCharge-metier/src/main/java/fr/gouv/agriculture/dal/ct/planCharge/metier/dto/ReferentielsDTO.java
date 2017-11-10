package fr.gouv.agriculture.dal.ct.planCharge.metier.dto;

import fr.gouv.agriculture.dal.ct.metier.MetierException;
import fr.gouv.agriculture.dal.ct.metier.dto.AbstractDTO;
import fr.gouv.agriculture.dal.ct.metier.dto.DTOException;
import fr.gouv.agriculture.dal.ct.metier.regleGestion.RegleGestion;
import fr.gouv.agriculture.dal.ct.metier.regleGestion.ViolationRegleGestion;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.Referentiels;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by frederic.danna on 01/07/2017.
 */
@SuppressWarnings("ClassHasNoToStringMethod")
public class ReferentielsDTO extends AbstractDTO<Referentiels, Serializable, ReferentielsDTO> {

    /* Rq : Contrairement à l'entité associée (Referentiels) qui utilise des Set, on utilise des List pour pouvoi g&rer les éventuels doublons dans la saisie. */

    @NotNull
    private List<JourFerieDTO> joursFeries;
    @NotNull
    private List<ImportanceDTO> importances;
    @NotNull
    private List<ProfilDTO> profils;
    @NotNull
    private List<ProjetAppliDTO> projetsApplis;
    @NotNull
    private List<StatutDTO> statuts;
    @NotNull
    private List<RessourceHumaineDTO> ressourcesHumaines;


    public ReferentielsDTO(@NotNull List<JourFerieDTO> joursFeries, @NotNull List<ImportanceDTO> importances, @NotNull List<ProfilDTO> profils, @NotNull List<ProjetAppliDTO> projetsApplis, @NotNull List<StatutDTO> statuts, @NotNull List<RessourceHumaineDTO> ressourcesHumaines) {
        this.joursFeries = joursFeries;
        this.importances = importances;
        this.profils = profils;
        this.projetsApplis = projetsApplis;
        this.statuts = statuts;
        this.ressourcesHumaines = ressourcesHumaines;
    }

    @NotNull
    public static ReferentielsDTO from(@NotNull Referentiels entity) {
        return new ReferentielsDTO(
                entity.getJoursFeries().stream().map(JourFerieDTO::from).collect(Collectors.toList()),
                entity.getImportances().stream().map(ImportanceDTO::from).collect(Collectors.toList()),
                entity.getProfils().stream().map(ProfilDTO::from).collect(Collectors.toList()),
                entity.getProjetsApplis().stream().map(ProjetAppliDTO::from).collect(Collectors.toList()),
                entity.getStatuts().stream().map(entity1 -> {
                    try {
                        return StatutDTO.from(entity1);
                    } catch (DTOException e) {
                        throw new RuntimeException(e);
                    }
                }).collect(Collectors.toList()),
                entity.getRessourcesHumaines().stream().map(RessourceHumaineDTO::from).collect(Collectors.toList())
        );
    }

    @NotNull
    public List<JourFerieDTO> getJoursFeries() {
        return joursFeries;
    }

    @NotNull
    public List<ImportanceDTO> getImportances() {
        return importances;
    }

    @NotNull
    public List<ProfilDTO> getProfils() {
        return profils;
    }

    @NotNull
    public List<ProjetAppliDTO> getProjetsApplis() {
        return projetsApplis;
    }

    @NotNull
    public List<StatutDTO> getStatuts() {
        return statuts;
    }

    @NotNull
    public List<RessourceHumaineDTO> getRessourcesHumaines() {
        return ressourcesHumaines;
    }

    @NotNull
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
    public ReferentielsDTO fromEntity(@NotNull Referentiels entity) throws DTOException {
        return from(entity);
    }

    @NotNull
    @Override
    public Referentiels toEntity() throws DTOException {
        /* Rq : La transformation des List en Set enlève les éventuels doublons. */
/*
        assert joursFeries != null; // TODO FDA 2017/07 Préciser la RG qui n'est pas respectée.
        assert importances != null; // TODO FDA 2017/07 Préciser la RG qui n'est pas respectée.
        assert profils != null; // TODO FDA 2017/07 Préciser la RG qui n'est pas respectée.
        assert projetsApplis != null; // TODO FDA 2017/07 Préciser la RG qui n'est pas respectée.
        assert statuts != null; // TODO FDA 2017/07 Préciser la RG qui n'est pas respectée.
        assert ressourcesHumaines != null; // TODO FDA 2017/07 Préciser la RG qui n'est pas respectée.
*/
        return new Referentiels(
                joursFeries.stream().map(JourFerieDTO::toEntity).collect(Collectors.toSet()),
                importances.stream().map(ImportanceDTO::toEntity).collect(Collectors.toSet()),
                profils.stream().map(ProfilDTO::toEntity).collect(Collectors.toSet()),
                projetsApplis.stream().map(ProjetAppliDTO::toEntity).collect(Collectors.toSet()),
                statuts.stream().map(statutDTO -> {
                    try {
                        return statutDTO.toEntity();
                    } catch (DTOException e) {
                        // TODO FDA 2017/11 Trouver mieux que thrower une RuntimeException.
                        throw new RuntimeException(e);
                    }
                }).collect(Collectors.toSet()),
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

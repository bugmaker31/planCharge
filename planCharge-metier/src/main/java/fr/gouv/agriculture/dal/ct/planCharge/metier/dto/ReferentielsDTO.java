package fr.gouv.agriculture.dal.ct.planCharge.metier.dto;

import fr.gouv.agriculture.dal.ct.metier.dto.AbstractDTO;
import fr.gouv.agriculture.dal.ct.metier.regleGestion.RegleGestion;
import fr.gouv.agriculture.dal.ct.metier.regleGestion.ViolationRegleGestion;
import fr.gouv.agriculture.dal.ct.metier.MetierException;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.Referentiels;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by frederic.danna on 01/07/2017.
 */
@SuppressWarnings("ClassHasNoToStringMethod")
public class ReferentielsDTO extends AbstractDTO<Referentiels, Serializable, ReferentielsDTO> {

    @NotNull
    private Collection<JourFerieDTO> joursFeries;
    @NotNull
    private Collection<ImportanceDTO> importances;
    @NotNull
    private Collection<ProfilDTO> profils;
    @NotNull
    private Collection<ProjetAppliDTO> projetsApplis;
    @NotNull
    private Collection<StatutDTO> statuts;
    @NotNull
    private Collection<RessourceHumaineDTO> ressourcesHumaines;


    public ReferentielsDTO() {
        this(new TreeSet<>(), new TreeSet<>(), new TreeSet<>(), new TreeSet<>(), new TreeSet<>(), new TreeSet<>()); // TreeSet (au lieu de HasHset) pour trier, juste pour faciliter le débogage.
    }

    public ReferentielsDTO(@NotNull Collection<JourFerieDTO> joursFeries, @NotNull Collection<ImportanceDTO> importances, @NotNull Collection<ProfilDTO> profils, @NotNull Collection<ProjetAppliDTO> projetsApplis, @NotNull Collection<StatutDTO> statuts, @NotNull Collection<RessourceHumaineDTO> ressourcesHumaines) {
        this.joursFeries = joursFeries;
        this.importances = importances;
        this.profils = profils;
        this.projetsApplis = projetsApplis;
        this.statuts = statuts;
        this.ressourcesHumaines = ressourcesHumaines;
    }


    @NotNull
    public Collection<JourFerieDTO> getJoursFeries() {
        return joursFeries;
    }

    @NotNull
    public Collection<ImportanceDTO> getImportances() {
        return importances;
    }

    @NotNull
    public Collection<ProfilDTO> getProfils() {
        return profils;
    }

    @NotNull
    public Collection<ProjetAppliDTO> getProjetsApplis() {
        return projetsApplis;
    }

    @NotNull
    public Collection<StatutDTO> getStatuts() {
        return statuts;
    }

    @NotNull
    public Collection<RessourceHumaineDTO> getRessourcesHumaines() {
        return ressourcesHumaines;
    }


    @NotNull
    @Override
    public Serializable getIdentity() {
        return null; // TODO FDA 2017/07 Trouver mieux comme code.
    }


    @Override
    public int compareTo(ReferentielsDTO o) {
        return 0; // TODO FDA 2017/07 Trouver mieux comme code.
    }


    @NotNull
    @Override
    public Referentiels toEntity() {
        return new Referentiels(
                getJoursFeries().stream().map(JourFerieDTO::toEntity).collect(Collectors.toList()),
                getImportances().stream().map(ImportanceDTO::toEntity).collect(Collectors.toList()),
                getProfils().stream().map(ProfilDTO::toEntity).collect(Collectors.toList()),
                getProjetsApplis().stream().map(ProjetAppliDTO::toEntity).collect(Collectors.toList()),
                getStatuts().stream().map(StatutDTO::toEntity).collect(Collectors.toList()),
                getRessourcesHumaines().stream().map(RessourceHumaineDTO::toEntity).collect(Collectors.toList())
                );
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
    public List<ViolationRegleGestion> controlerReglesGestion() throws MetierException {
        List<ViolationRegleGestion> violations = new ArrayList<>();
        for (JourFerieDTO jourFerie : joursFeries) {
            violations.addAll(jourFerie.controlerReglesGestion());
        }
        for (ImportanceDTO importance : importances) {
            violations.addAll(importance.controlerReglesGestion());
        }
        for (ProfilDTO profil : profils) {
            violations.addAll(profil.controlerReglesGestion());
        }
        for (ProjetAppliDTO projetAppli : projetsApplis) {
            violations.addAll(projetAppli.controlerReglesGestion());
        }
        for (StatutDTO statut : statuts) {
            violations.addAll(statut.controlerReglesGestion());
        }
        for (RessourceHumaineDTO ressourceHumaine : ressourcesHumaines) {
            violations.addAll(ressourceHumaine.controlerReglesGestion());
        }
        return violations;
    }

    @NotNull
    @Override
    protected List<RegleGestion<ReferentielsDTO>> getReglesGestion() {
        return Collections.EMPTY_LIST;
    }

}

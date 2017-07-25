package fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels;

import fr.gouv.agriculture.dal.ct.planCharge.metier.MetierException;
import fr.gouv.agriculture.dal.ct.planCharge.metier.regleGestion.Controlable;
import fr.gouv.agriculture.dal.ct.planCharge.metier.regleGestion.ViolationRegleGestion;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by frederic.danna on 01/07/2017.
 */
@SuppressWarnings("ClassHasNoToStringMethod")
public class Referentiels implements Controlable {

    @NotNull
    private Set<JourFerie> joursFeries;
    @NotNull
    private Set<Importance> importances;
    @NotNull
    private Set<Profil> profils;
    @NotNull
    private Set<ProjetAppli> projetsApplis;
    @NotNull
    private Set<Statut> statuts;
    @NotNull
    private Set<RessourceHumaine> ressourcesHumaines;


/*
    public Referentiels() {
        this(new TreeSet<>(), new TreeSet<>(), new TreeSet<>(), new TreeSet<>(), new TreeSet<>(), new TreeSet<>()); // TreeSet (au lieu de HasHset) pour trier, juste pour faciliter le débogage.
    }
*/

    public Referentiels(@NotNull Set<JourFerie> joursFeries, @NotNull Set<Importance> importances, @NotNull Set<Profil> profils, @NotNull Set<ProjetAppli> projetsApplis, @NotNull Set<Statut> statuts, @NotNull Set<RessourceHumaine> ressourcesHumaines) {
        this.joursFeries = joursFeries;
        this.importances = importances;
        this.profils = profils;
        this.projetsApplis = projetsApplis;
        this.statuts = statuts;
        this.ressourcesHumaines = ressourcesHumaines;
    }


    @NotNull
    public Set<JourFerie> getJoursFeries() {
        return joursFeries;
    }

    @NotNull
    public Set<Importance> getImportances() {
        return importances;
    }

    @NotNull
    public Set<Profil> getProfils() {
        return profils;
    }

    @NotNull
    public Set<ProjetAppli> getProjetsApplis() {
        return projetsApplis;
    }

    @NotNull
    public Set<Statut> getStatuts() {
        return statuts;
    }

    @NotNull
    public Set<RessourceHumaine> getRessourcesHumaines() {
        return ressourcesHumaines;
    }


    @NotNull
    @Override
    public List<ViolationRegleGestion> controlerReglesGestion() throws MetierException {
        List<ViolationRegleGestion> violations = new ArrayList<>();
        for (JourFerie jourFerie : joursFeries) {
            violations.addAll(jourFerie.controlerReglesGestion());
        }
        for (Importance importance : importances) {
            violations.addAll(importance.controlerReglesGestion());
        }
        for (Profil profil : profils) {
            violations.addAll(profil.controlerReglesGestion());
        }
        for (ProjetAppli projetAppli : projetsApplis) {
            violations.addAll(projetAppli.controlerReglesGestion());
        }
        for (Statut statut : statuts) {
            violations.addAll(statut.controlerReglesGestion());
        }
        for (RessourceHumaine ressourceHumaine : ressourcesHumaines) {
            violations.addAll(ressourceHumaine.controlerReglesGestion());
        }
        return violations;
    }
}

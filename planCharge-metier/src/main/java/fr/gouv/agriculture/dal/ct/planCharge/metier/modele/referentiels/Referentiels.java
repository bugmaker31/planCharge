package fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels;

import fr.gouv.agriculture.dal.ct.metier.modele.AbstractEntity;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by frederic.danna on 01/07/2017.
 */
@SuppressWarnings("ClassHasNoToStringMethod")
public class Referentiels extends AbstractEntity {

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


    public Referentiels() {
        this(new TreeSet<>(), new TreeSet<>(), new TreeSet<>(), new TreeSet<>(), new TreeSet<>(), new TreeSet<>()); // TreeSet (au lieu de HasHset) pour trier, juste pour faciliter le débogage.
    }

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
    public Serializable getIdentity() {
        return null; // TODO FDA 2017/07 Trouver mieux comme code.
    }


    @Override
    public int compareTo(Object o) {
        return 0; // TODO FDA 2017/07 Trouver mieux comme code.
    }

}

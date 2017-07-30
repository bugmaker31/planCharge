package fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels;

import fr.gouv.agriculture.dal.ct.metier.modele.AbstractEntity;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Collection;
import java.util.TreeSet;

/**
 * Created by frederic.danna on 01/07/2017.
 */
@SuppressWarnings("ClassHasNoToStringMethod")
public class Referentiels extends AbstractEntity {

    @NotNull
    private Collection<JourFerie> joursFeries;
    @NotNull
    private Collection<Importance> importances;
    @NotNull
    private Collection<Profil> profils;
    @NotNull
    private Collection<ProjetAppli> projetsApplis;
    @NotNull
    private Collection<Statut> statuts;
    @NotNull
    private Collection<RessourceHumaine> ressourcesHumaines;


    public Referentiels() {
        this(new TreeSet<>(), new TreeSet<>(), new TreeSet<>(), new TreeSet<>(), new TreeSet<>(), new TreeSet<>()); // TreeSet (au lieu de HasHset) pour trier, juste pour faciliter le débogage.
    }

    public Referentiels(@NotNull Collection<JourFerie> joursFeries, @NotNull Collection<Importance> importances, @NotNull Collection<Profil> profils, @NotNull Collection<ProjetAppli> projetsApplis, @NotNull Collection<Statut> statuts, @NotNull Collection<RessourceHumaine> ressourcesHumaines) {
        this.joursFeries = joursFeries;
        this.importances = importances;
        this.profils = profils;
        this.projetsApplis = projetsApplis;
        this.statuts = statuts;
        this.ressourcesHumaines = ressourcesHumaines;
    }


    @NotNull
    public Collection<JourFerie> getJoursFeries() {
        return joursFeries;
    }

    @NotNull
    public Collection<Importance> getImportances() {
        return importances;
    }

    @NotNull
    public Collection<Profil> getProfils() {
        return profils;
    }

    @NotNull
    public Collection<ProjetAppli> getProjetsApplis() {
        return projetsApplis;
    }

    @NotNull
    public Collection<Statut> getStatuts() {
        return statuts;
    }

    @NotNull
    public Collection<RessourceHumaine> getRessourcesHumaines() {
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

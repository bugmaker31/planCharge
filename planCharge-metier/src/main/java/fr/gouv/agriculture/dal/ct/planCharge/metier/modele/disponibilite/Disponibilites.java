package fr.gouv.agriculture.dal.ct.planCharge.metier.modele.disponibilite;

import fr.gouv.agriculture.dal.ct.metier.modele.AbstractEntity;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.RessourceHumaine;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Map;

public class Disponibilites extends AbstractEntity {


    // Fields:

    @NotNull
    private final Map<RessourceHumaine, Map<LocalDate, Double>> absences;


    // Constructors:

    public Disponibilites(@NotNull Map<RessourceHumaine, Map<LocalDate, Double>> absences) {
        super();
        this.absences = absences;
    }


    // Getters/Setters:

    @NotNull
    public Map<RessourceHumaine, Map<LocalDate, Double>> getAbsences() {
        return absences;
    }


    // Implémentation de AbstractEntity :

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

package fr.gouv.agriculture.dal.ct.planCharge.ihm.model.disponibilite;

import fr.gouv.agriculture.dal.ct.ihm.model.AbstractBean;
import fr.gouv.agriculture.dal.ct.metier.dto.AbstractDTO;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.referentiels.RessourceHumaineBean;
import javafx.beans.property.IntegerProperty;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public abstract class AbstractDisponibilitesRessourceBean<D extends AbstractDTO, B extends AbstractDisponibilitesRessourceBean<D, B, T>, T> extends AbstractDisponibilitesBean<D, B, T> {


    // Fields :

    @NotNull
    final RessourceHumaineBean ressourceHumaineBean;


    // Constructors:

    public AbstractDisponibilitesRessourceBean(@NotNull RessourceHumaineBean ressourceHumaineBean, @NotNull Map<LocalDate, T> calendrier) {
        super(calendrier);
        this.ressourceHumaineBean = ressourceHumaineBean;
    }


    // Getters/Setters:

    @NotNull
    public final RessourceHumaineBean getRessourceHumaineBean() {
        return ressourceHumaineBean;
    }


    // Utilitie's methods:

    @Override
    public final String toString() {
        return ressourceHumaineBean + " : " + super.toString();
    }
}

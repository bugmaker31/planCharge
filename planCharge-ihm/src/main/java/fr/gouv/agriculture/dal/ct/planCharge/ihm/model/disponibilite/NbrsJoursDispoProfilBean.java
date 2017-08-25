package fr.gouv.agriculture.dal.ct.planCharge.ihm.model.disponibilite;

import fr.gouv.agriculture.dal.ct.ihm.IhmException;
import fr.gouv.agriculture.dal.ct.metier.dto.AbstractDTO;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.referentiels.ProfilBean;
import javafx.beans.property.IntegerProperty;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Map;

public class NbrsJoursDispoProfilBean extends AbstractDisponibilitesBean<AbstractDTO, NbrsJoursDispoProfilBean, IntegerProperty> {

    // Fields :

    @NotNull
    private ProfilBean profilBean;


    // Constructors :

    public NbrsJoursDispoProfilBean() {
        super();
    }

    public NbrsJoursDispoProfilBean(@NotNull Map<LocalDate, IntegerProperty> calendrier) throws IhmException {
        super(calendrier);
    }


    // Getters/Setters:

    @NotNull
    public final ProfilBean getProfilBean() {
        return profilBean;
    }


    // Implementation of AbstractBean :

    // Utilitie's methods:

    @Override
    public String toString() {
        return profilBean + " " +  super.toString();
    }
}

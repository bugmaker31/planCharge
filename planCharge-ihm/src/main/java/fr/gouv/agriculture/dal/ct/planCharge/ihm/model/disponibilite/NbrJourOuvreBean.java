package fr.gouv.agriculture.dal.ct.planCharge.ihm.model.disponibilite;

import fr.gouv.agriculture.dal.ct.ihm.IhmException;
import fr.gouv.agriculture.dal.ct.ihm.model.AbstractBean;
import fr.gouv.agriculture.dal.ct.ihm.model.BeanException;
import fr.gouv.agriculture.dal.ct.metier.dto.AbstractDTO;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.tache.TacheBean;
import javafx.beans.property.DoubleProperty;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class NbrJourOuvreBean extends AbstractBean<AbstractDTO, NbrJourOuvreBean> {

    @NotNull
    private Map<LocalDate, DoubleProperty> calendrier;


    public NbrJourOuvreBean() {
        super();
        calendrier = new TreeMap<>(); // treeMaop au lieu de HashMap juste pour faicliter le débogage en gardant le tri sur la Key (date).
    }

    public NbrJourOuvreBean(@NotNull Map<LocalDate, DoubleProperty> calendrier) throws IhmException {
        super();
        this.calendrier = calendrier;
    }


    @NotNull
    @Override
    public AbstractDTO toDto() throws BeanException {
        return null; // FIXME FDA 2017/08
    }

    @NotNull
    @Override
    public NbrJourOuvreBean fromDto(@NotNull AbstractDTO dto) throws BeanException {
        return null; // FIXME FDA 2017/08
    }
}

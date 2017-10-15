package fr.gouv.agriculture.dal.ct.planCharge.ihm.view;

import fr.gouv.agriculture.dal.ct.metier.dto.AbstractDTO;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.AbstractCalendrierParRessourceBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.referentiels.RessourceBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.view.converter.Converters;
import javafx.beans.property.FloatProperty;
import javafx.util.StringConverter;
import javafx.util.converter.IntegerStringConverter;

import javax.validation.constraints.Null;

public class CalendrierNbrsDemisJoursParRessourceCell<R extends RessourceBean<?, ?>, T extends AbstractCalendrierParRessourceBean<R, AbstractDTO, T, FloatProperty>> extends AbstractCalendrierParRessourceCell<R, T, Float> {

    private static final StringConverter<Integer> CONVERTER =  new IntegerStringConverter();

    public CalendrierNbrsDemisJoursParRessourceCell(int noSemaine, @Null Runnable cantEditErrorDisplayer) {
        super(noSemaine, Converters.FRACTION_JOURS_STRING_CONVERTER, cantEditErrorDisplayer);
    }

    public CalendrierNbrsDemisJoursParRessourceCell(int noSemaine) {
        this(noSemaine, null);
    }

}
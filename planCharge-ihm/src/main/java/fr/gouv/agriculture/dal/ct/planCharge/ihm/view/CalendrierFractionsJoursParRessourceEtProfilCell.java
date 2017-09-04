package fr.gouv.agriculture.dal.ct.planCharge.ihm.view;

import fr.gouv.agriculture.dal.ct.metier.dto.AbstractDTO;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.AbstractCalendrierParRessourceEtProfilBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.charge.PlanChargeBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.referentiels.RessourceBean;
import javafx.beans.property.FloatProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

public class CalendrierFractionsJoursParRessourceEtProfilCell<R extends RessourceBean, T extends AbstractCalendrierParRessourceEtProfilBean<R, AbstractDTO, T, FloatProperty>> extends AbstractCalendrierParRessourceCell<R, T, Float> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CalendrierFractionsJoursParRessourceEtProfilCell.class);

    @SuppressWarnings("OverlyComplexAnonymousInnerClass")


    public CalendrierFractionsJoursParRessourceEtProfilCell(@NotNull PlanChargeBean planChargeBean, int noSemaine, @Null Runnable cantEditErrorDisplayer) {
        super(planChargeBean, noSemaine, Converters.FRACTION_JOURS_STRING_CONVERTER, cantEditErrorDisplayer);
    }

    public CalendrierFractionsJoursParRessourceEtProfilCell(@NotNull PlanChargeBean planChargeBean, int noSemaine) {
        this(planChargeBean, noSemaine, null);
    }

}


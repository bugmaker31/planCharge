package fr.gouv.agriculture.dal.ct.planCharge.ihm.view;

import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.charge.PlanChargeBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.referentiels.RessourceBean;
import fr.gouv.agriculture.dal.ct.planCharge.util.Collections;
import javafx.util.StringConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

@SuppressWarnings({"NonStaticInnerClassInSecureContext", "ClassHasNoToStringMethod"})
public class RessourceBeanConverter extends StringConverter<RessourceBean<?, ?>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(RessourceBeanConverter.class);

    @NotNull
    private final PlanChargeBean planChargeBean = PlanChargeBean.instance();

    @Null
    @Override
    public String toString(@Null RessourceBean<?, ?> ressourceBean) {
        if (ressourceBean == null) {
            return null;
        }
        return ressourceBean.getCode();
    }

    @Override
    @Null
    public RessourceBean<?, ?> fromString(@Null String trigramme) {
        if (trigramme == null) {
            return null;
        }
        return Collections.any(
                planChargeBean.getRessourcesBeans(),
                ressourceBean -> (ressourceBean.getCode() != null) && ressourceBean.getCode().equals(trigramme)
        );
    }

}



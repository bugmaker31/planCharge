package fr.gouv.agriculture.dal.ct.planCharge.ihm.view.converter;

import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.charge.PlanChargeBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.referentiels.RessourceBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.referentiels.StatutBean;
import fr.gouv.agriculture.dal.ct.planCharge.util.Collections;
import javafx.util.StringConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

@SuppressWarnings({"NonStaticInnerClassInSecureContext", "ClassHasNoToStringMethod"})
public class StatutBeanConverter extends StringConverter<StatutBean> {

    private static final Logger LOGGER = LoggerFactory.getLogger(StatutBeanConverter.class);

    @NotNull
    private final PlanChargeBean planChargeBean = PlanChargeBean.instance();


    @Null
    @Override
    public String toString(@Null StatutBean statutBean) {
        if (statutBean == null) {
            return null;
        }
        return statutBean.getCode();
    }

    @Null
    @Override
    public StatutBean fromString(@Null String codeStatut) {
        if (codeStatut == null) {
            return null;
        }
        return Collections.any(
                planChargeBean.getStatutsBeans(),
                statutBean -> (statutBean.getCode() != null) && statutBean.getCode().equals(codeStatut)
        );
    }

}



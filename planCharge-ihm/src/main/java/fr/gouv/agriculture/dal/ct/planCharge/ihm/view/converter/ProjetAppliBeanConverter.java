package fr.gouv.agriculture.dal.ct.planCharge.ihm.view.converter;

import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.charge.PlanChargeBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.referentiels.ProfilBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.referentiels.ProjetAppliBean;
import fr.gouv.agriculture.dal.ct.planCharge.util.Collections;
import javafx.util.StringConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

@SuppressWarnings({"NonStaticInnerClassInSecureContext", "ClassHasNoToStringMethod"})
public class ProjetAppliBeanConverter extends StringConverter<ProjetAppliBean> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProjetAppliBeanConverter.class);

    @NotNull
    private final PlanChargeBean planChargeBean = PlanChargeBean.instance();

    @Null
    @Override
    public String toString(@Null ProjetAppliBean projetAppliBean) {
        if (projetAppliBean == null) {
            return null;
        }
        return projetAppliBean.getCode();
    }

    @Override
    @Null
    public ProjetAppliBean fromString(@Null String code) {
        if (code == null) {
            return null;
        }
        return Collections.any(
                planChargeBean.getProjetsApplisBeans(),
                projetAppliBean -> (projetAppliBean.getCode() != null) && projetAppliBean.getCode().equals(code)
        );
    }

}



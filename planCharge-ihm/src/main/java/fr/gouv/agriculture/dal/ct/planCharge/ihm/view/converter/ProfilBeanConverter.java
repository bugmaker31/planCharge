package fr.gouv.agriculture.dal.ct.planCharge.ihm.view.converter;

import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.charge.PlanChargeBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.referentiels.ProfilBean;
import fr.gouv.agriculture.dal.ct.planCharge.util.Collections;
import javafx.util.StringConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

@SuppressWarnings({"NonStaticInnerClassInSecureContext", "ClassHasNoToStringMethod"})
public class ProfilBeanConverter extends StringConverter<ProfilBean> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProfilBeanConverter.class);

    @NotNull
    private final PlanChargeBean planChargeBean = PlanChargeBean.instance();

    @Null
    @Override
    public String toString(@Null ProfilBean profilBean) {
        if (profilBean == null) {
            return null;
        }
        return profilBean.getCode();
    }

    @Override
    @Null
    public ProfilBean fromString(@Null String code) {
        if (code == null) {
            return null;
        }
        return Collections.any(
                planChargeBean.getProfilsBeans(),
                profilBean -> (profilBean.getCode() != null) && profilBean.getCode().equals(code)
        );
    }

}



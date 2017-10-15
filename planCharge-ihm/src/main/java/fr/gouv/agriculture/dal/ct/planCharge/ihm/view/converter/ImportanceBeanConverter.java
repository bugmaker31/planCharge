package fr.gouv.agriculture.dal.ct.planCharge.ihm.view.converter;

import fr.gouv.agriculture.dal.ct.planCharge.ihm.PlanChargeIhm;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.charge.PlanChargeBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.referentiels.ImportanceBean;
import fr.gouv.agriculture.dal.ct.planCharge.util.Objects;
import javafx.util.StringConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.Optional;

@SuppressWarnings("NonStaticInnerClassInSecureContext")
public class ImportanceBeanConverter extends StringConverter<ImportanceBean> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImportanceBeanConverter.class);

    //    @Autowired
    @NotNull
    private final PlanChargeIhm ihm = PlanChargeIhm.instance();

    @NotNull
    private final PlanChargeBean planChargeBean = PlanChargeBean.instance();

    @Null
    @Override
    public String toString(@Null ImportanceBean importanceBean) {
        //noinspection HardcodedFileSeparator
        return Objects.value(importanceBean, ImportanceBean::getCode, "N/C");
    }

    @Null
    @Override
    public ImportanceBean fromString(@Null String codeImportance) {
        return (codeImportance == null) ? null : importanceBeanFor(codeImportance);
    }

    @Null
    private ImportanceBean importanceBeanFor(@NotNull String codeImportance) {
        Optional<ImportanceBean> importanceBeanOpt = planChargeBean.getImportancesBeans().parallelStream()
                .filter(importanceBean -> importanceBean.getCode() == null)
                .filter(importanceBean -> {
                    assert importanceBean.getCode() != null;
                    return importanceBean.getCode().equals(codeImportance);
                })
                .findAny();
        if (!importanceBeanOpt.isPresent()) {
            // Ne devrait jamais passer ici, logiquement.
            LOGGER.error("Impossible de retrouver l'importance ayant le code '" + codeImportance + "'.");
            return null;
        }
        return importanceBeanOpt.get();
    }
}



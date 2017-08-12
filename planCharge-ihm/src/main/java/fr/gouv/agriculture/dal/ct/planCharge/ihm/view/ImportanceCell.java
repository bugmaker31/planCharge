package fr.gouv.agriculture.dal.ct.planCharge.ihm.view;

import fr.gouv.agriculture.dal.ct.ihm.IhmException;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.PlanChargeIhm;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.referentiels.ImportanceBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.charge.PlanChargeBean;
import fr.gouv.agriculture.dal.ct.planCharge.util.Objects;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.util.StringConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.Optional;

/**
 * Created by frederic.danna on 14/05/2017.
 *
 * @author frederic.danna
 */
@SuppressWarnings("ClassHasNoToStringMethod")
public class ImportanceCell<S> extends ComboBoxTableCell<S, ImportanceBean> {

    @NotNull
    private static final Logger LOGGER = LoggerFactory.getLogger(ImportanceCell.class);

    @NotNull
    private PlanChargeIhm ihm = PlanChargeIhm.instance();

    @NotNull
    private PlanChargeBean planChargeBean = PlanChargeBean.instance();

    public ImportanceCell(@NotNull ObservableList<ImportanceBean> items) {
        super(items);
        setConverter( new ImportanceBeanConverter());
    }

    @SuppressWarnings("NonStaticInnerClassInSecureContext")
    private class ImportanceBeanConverter extends StringConverter<ImportanceBean> {

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


    @Override
    public void updateItem(@Null ImportanceBean item, boolean empty) {
        super.updateItem(item, empty);

        getStyleClass().removeAll("importanceAvecEngagement", "importanceMaximale", "importanceHaute", "importanceNormale", "importanceBasse");

        if (empty || (getItem() == null)) {
            return;
        }

        try {
            String styleClass = importanceStyleClass();
            getStyleClass().add(styleClass);
        } catch (IhmException e) {
            LOGGER.error("Impossible de màj le style de la cellule Importance de la ligne n°'" + (getIndex() + 1) + "'.", e);
        }
    }

    @NotNull
    private String importanceStyleClass() throws IhmException {
        String codeImportance = getItem().getCode();
        if (codeImportance == null) {
            return null;
        }
        if (codeImportance.equals("Avec engag.")) {
            return "importanceAvecEngagement";
        }
        if (codeImportance.equals("Maximale")) {
            return "importanceMaximale";
        }
        if (codeImportance.equals("Haute")) {
            return "importanceHaute";
        }
        if (codeImportance.equals("Normale")) {
            return "importanceNormale";
        }
        if (codeImportance.equals("Basse")) {
            return "importanceBasse";
        }
        throw new IhmException("Importance non gérée : '" + codeImportance + "'.");
    }

}

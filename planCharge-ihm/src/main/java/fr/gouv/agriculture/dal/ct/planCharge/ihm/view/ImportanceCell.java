package fr.gouv.agriculture.dal.ct.planCharge.ihm.view;

import fr.gouv.agriculture.dal.ct.ihm.IhmException;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.PlanChargeIhm;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.charge.PlanChargeBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.referentiels.ImportanceBean;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.scene.control.cell.ComboBoxTableCell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.Arrays;
import java.util.List;

/**
 * Created by frederic.danna on 14/05/2017.
 *
 * @author frederic.danna
 */
@SuppressWarnings("ClassHasNoToStringMethod")
public class ImportanceCell<S> extends ComboBoxTableCell<S, ImportanceBean> {

    public static final PseudoClass AVEC_ENGAGEMENT = PseudoClass.getPseudoClass("avecEngagement");
    public static final PseudoClass MAXIMALE = PseudoClass.getPseudoClass("maximale");
    public static final PseudoClass HAUTE = PseudoClass.getPseudoClass("haute");
    public static final PseudoClass NORMALE = PseudoClass.getPseudoClass("normale");
    public static final PseudoClass BASSE = PseudoClass.getPseudoClass("basse");
    public static final PseudoClass MINIMALE = PseudoClass.getPseudoClass("minimale");
    //
    public static final List<PseudoClass> PSEUDO_CLASSES = Arrays.asList(AVEC_ENGAGEMENT, MAXIMALE, HAUTE, NORMALE, BASSE, MINIMALE);


    @NotNull
    private static final Logger LOGGER = LoggerFactory.getLogger(ImportanceCell.class);

    //    @Autowired
    @NotNull
    private final PlanChargeIhm ihm = PlanChargeIhm.instance();

    @NotNull
    private PlanChargeBean planChargeBean = PlanChargeBean.instance();

    public ImportanceCell(@NotNull ObservableList<ImportanceBean> items) {
        super(new ImportanceBeanConverter(), items);
    }

    @Override
    public void updateItem(@Null ImportanceBean item, boolean empty) {
        super.updateItem(item, empty);

        PSEUDO_CLASSES.parallelStream()
                .forEach(pseudoClass -> pseudoClassStateChanged(pseudoClass, false));

        if (empty || (item == null)) {
            return;
        }

        PseudoClass pseudoClass;
        try {
            pseudoClass = importanceStyleClass(item);
        } catch (IhmException e) {
            LOGGER.error("Impossible de màj le style de la cellule Importance de la ligne n°'" + (getIndex() + 1) + "'.", e);
            return;
        }
        if (pseudoClass != null) {
            pseudoClassStateChanged(pseudoClass, true);
        }
    }

    @Null
    private PseudoClass importanceStyleClass(@NotNull ImportanceBean item) throws IhmException {
        String codeImportance = item.getCode();
        if (codeImportance == null) {
            return null;
        }
        switch (codeImportance) {
            case "Avec engag.":
                return AVEC_ENGAGEMENT;
            case "Maximale":
                return MAXIMALE;
            case "Haute":
                return HAUTE;
            case "Normale":
                return NORMALE;
            case "Basse":
                return BASSE;
            case "Minimale":
                return MINIMALE;
            default:
                throw new IhmException("Importance non gérée : '" + codeImportance + "'.");
        }
    }

}

package fr.gouv.agriculture.dal.ct.planCharge.ihm.view;

import fr.gouv.agriculture.dal.ct.planCharge.ihm.IhmException;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.ComboBoxTableCell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;

/**
 * Created by frederic.danna on 14/05/2017.
 *
 * @author frederic.danna
 */
public class ImportanceCell<S> extends ComboBoxTableCell<S, String> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImportanceCell.class);

    public ImportanceCell(ObservableList<String> items) {
        super(items);
    }

    @Override
    public void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);

        getStyleClass().clear();

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
        String codeImportance = getItem();
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

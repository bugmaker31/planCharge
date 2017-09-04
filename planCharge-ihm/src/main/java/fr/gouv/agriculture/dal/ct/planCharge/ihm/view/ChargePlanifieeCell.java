package fr.gouv.agriculture.dal.ct.planCharge.ihm.view;

import fr.gouv.agriculture.dal.ct.ihm.view.EditableAwareTextFieldTableCell;
import javafx.css.PseudoClass;

import javax.validation.constraints.Null;

public class ChargePlanifieeCell<S> extends EditableAwareTextFieldTableCell<S, Float> {

    private static final PseudoClass SURCHARGE = PseudoClass.getPseudoClass("surcharge");

    public ChargePlanifieeCell(@Null Runnable cantEditErrorDisplayer) {
        super(Converters.FRACTION_JOURS_STRING_CONVERTER, cantEditErrorDisplayer);
    }

    @Override
    public void updateItem(Float item, boolean empty) {
        super.updateItem(item, empty);
        styler(item, empty);
    }

    private void styler(@Null Float item, boolean empty) {
        pseudoClassStateChanged(SURCHARGE, false);

        if (empty || (item == null)) {
            return;
        }

        //noinspection UnnecessaryLocalVariable
        Float chargePlanifiee = item;
        if (chargePlanifiee < 0.0) {
            pseudoClassStateChanged(SURCHARGE, true);
        }
    }
}

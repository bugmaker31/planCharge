package fr.gouv.agriculture.dal.ct.planCharge.ihm.view;

import fr.gouv.agriculture.dal.ct.ihm.view.EditableAwareTextFieldTableCell;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.ChargesController;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.view.converter.Converters;
import javafx.css.PseudoClass;
import javafx.util.StringConverter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

@SuppressWarnings("ClassWithoutLogger")
public class ChargePlanifieeCell<S> extends EditableAwareTextFieldTableCell<S, Float> {

    private static final PseudoClass SURCHARGE = ChargesController.SURCHARGE;

    public ChargePlanifieeCell(@Null Runnable cantEditErrorDisplayer) {
        this(Converters.FRACTION_JOURS_STRING_CONVERTER, cantEditErrorDisplayer);
    }

    public ChargePlanifieeCell(@NotNull StringConverter<Float> floatStringConverter, @Null  Runnable cantEditErrorDisplayer) {
        super(floatStringConverter, cantEditErrorDisplayer);
    }

    @Override
    public void updateItem(Float item, boolean empty) {
        super.updateItem(item, empty);
//        formater();
        styler();
    }

    private void formater() {
        if (isEmpty() || (getItem() == null)) {
            setText(null);
            setGraphic(null);
            return;
        }
        //noinspection UnnecessaryLocalVariable
        Float charge = getItem();
        setText(getConverter().toString(charge));
    }

    private void styler() {
        pseudoClassStateChanged(SURCHARGE, false);

        if (isEmpty() || (getItem() == null)) {
            return;
        }

        //noinspection UnnecessaryLocalVariable
        Float chargePlanifiee = getItem();
        if (chargePlanifiee < 0.0) {
            pseudoClassStateChanged(SURCHARGE, true);
        }
    }
}

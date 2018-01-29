package fr.gouv.agriculture.dal.ct.ihm.view;

import fr.gouv.agriculture.dal.ct.planCharge.ihm.PlanChargeIhm;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.StringConverter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

@SuppressWarnings("ClassHasNoToStringMethod")
public class NotEditableTextFieldTableCell<S, T> extends TextFieldTableCell<S, T> {

/*
    @NotNull
    private final PlanChargeIhm ihm = PlanChargeIhm.instance();
*/

    @Null
    private final Runnable cantEditErrorDisplayer;

    public NotEditableTextFieldTableCell(@NotNull StringConverter<T> stringConverter, @Null Runnable cantEditErrorDisplayer) {
        super(stringConverter);
        this.cantEditErrorDisplayer = cantEditErrorDisplayer;

//        setEditable(true);
    }

    @Override
    public void startEdit() {
/*
        if (!isEditable()) {
            displayNotEditableError();
            return;
        }
*/
        if (cantEditErrorDisplayer != null) {
            cantEditErrorDisplayer.run();
            return;
        }

        super.startEdit();
    }

//    protected abstract void displayNotEditableError();

}


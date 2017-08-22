package fr.gouv.agriculture.dal.ct.ihm.view;

import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.StringConverter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

@SuppressWarnings("ClassHasNoToStringMethod")
public class EditableAwareTextFieldTableCell<S, T> extends TextFieldTableCell<S, T> {

    @Null
    private final Runnable cantEditErrorDisplayer;

    public EditableAwareTextFieldTableCell(@NotNull StringConverter<T> stringConverter, @Null Runnable cantEditErrorDisplayer) {
        super(stringConverter);
        this.cantEditErrorDisplayer = cantEditErrorDisplayer;
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


package fr.gouv.agriculture.dal.ct.planCharge.ihm.model;

import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.Importance;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.JourFerie;
import javafx.beans.property.*;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Created by frederic.danna on 01/07/2017.
 */
public class ImportanceBean {

    @NotNull
    private IntegerProperty ordre = new SimpleIntegerProperty();

    @NotNull
    private StringProperty code = new SimpleStringProperty();


    public ImportanceBean(@NotNull Importance importance) {
        this.ordre.set(importance.getOrdre());
        this.code.set(importance.getCode());
    }


    @NotNull
    public int getOrdre() {
        return ordre.get();
    }

    @NotNull
    public IntegerProperty ordreProperty() {
        return ordre;
    }

    @NotNull
    public String getCode() {
        return code.get();
    }

    @NotNull
    public StringProperty codeProperty() {
        return code;
    }


    @NotNull
    public Importance extract() {
        return new Importance(ordre.get(), code.get());
    }


    @Override
    public String toString() {
        return ordre.get() + "-" + code.get();
    }
}

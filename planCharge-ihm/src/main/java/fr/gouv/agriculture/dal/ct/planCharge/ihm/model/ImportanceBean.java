package fr.gouv.agriculture.dal.ct.planCharge.ihm.model;

import fr.gouv.agriculture.dal.ct.ihm.model.AbstractBean;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dto.ImportanceDTO;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dto.ProjetAppliDTO;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.Importance;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.JourFerie;
import fr.gouv.agriculture.dal.ct.planCharge.util.Objects;
import javafx.beans.property.*;
import javafx.beans.value.ObservableIntegerValue;
import javafx.beans.value.WritableObjectValue;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Created by frederic.danna on 01/07/2017.
 */
public class ImportanceBean extends AbstractBean<ImportanceDTO, ImportanceBean> {


    @NotNull
    private IntegerProperty ordre = new SimpleIntegerProperty();

    @NotNull
    private StringProperty code = new SimpleStringProperty();


    private ImportanceBean() {
        super();
    }

    private ImportanceBean(@Null Integer ordre, @Null String code) {
        this();
        this.ordre.set(ordre);
        this.code.set(code);
    }

    @SuppressWarnings("StaticMethodNamingConvention")
    public static ImportanceDTO to(@NotNull ImportanceBean importanceBean) {
        return importanceBean.toDto();
    }

    @NotNull
    public IntegerProperty ordreProperty() {
        return ordre;
    }

    @Null
    public Integer getOrdre() {
        return ordre.get();
    }

    @NotNull
    public StringProperty codeProperty() {
        return code;
    }


    @NotNull
    @Override
    public ImportanceBean fromDto(@NotNull ImportanceDTO dto) {
        return new ImportanceBean(dto.getOrdre(), dto.getCode());
    }

    @NotNull
    public static ImportanceBean from(@NotNull ImportanceDTO dto) {
        return new ImportanceBean().fromDto(dto);
    }

    @NotNull
    public ImportanceDTO toDto() {
        return new ImportanceDTO(ordre.get(), code.get());
    }

    @Null
    public String getCode() {
        return code.get();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if ((o == null) || (getClass() != o.getClass())) return false;

        ImportanceBean that = (ImportanceBean) o;

        if (getOrdre() != that.getOrdre()) return false;
        return getCode().equals(that.getCode());
    }

    @Override
    public int hashCode() {
        int result = new Integer(getOrdre()).hashCode();
        result = (31 * result) + getCode().hashCode();
        return result;
    }


    // Juste pour faciliter le débogage.
    @Override
    public String toString() {
        //noinspection HardcodedFileSeparator
        return Objects.value(ordre, IntegerProperty::get, "N/C")
                + "-"
                + Objects.value(code, StringProperty::get, "N/C");
    }

}

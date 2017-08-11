package fr.gouv.agriculture.dal.ct.planCharge.ihm.model.referentiels;

import fr.gouv.agriculture.dal.ct.ihm.model.AbstractBean;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dto.ImportanceDTO;
import fr.gouv.agriculture.dal.ct.planCharge.util.Objects;
import javafx.beans.property.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

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
    public static ImportanceBean from(@NotNull ImportanceDTO dto) {
        return new ImportanceBean().fromDto(dto);
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
        return "Importance"
                + " " + Objects.value(ordre.get(), "N/C")
                + "-" + Objects.value(code.get(), "N/C");
    }

}

package fr.gouv.agriculture.dal.ct.planCharge.ihm.model;

import fr.gouv.agriculture.dal.ct.ihm.model.AbstractBean;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dto.JourFerieDTO;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dto.ProfilDTO;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Created by frederic.danna on 01/07/2017.
 */
public class ProfilBean extends AbstractBean<ProfilDTO, ProfilBean> implements Comparable<ProfilBean> {

    @NotNull
    private StringProperty code = new SimpleStringProperty();


    public ProfilBean() {
        super();
    }

    public ProfilBean(@NotNull ProfilDTO profilDTO) {
        this.code.set(profilDTO.getCode());
    }

    public ProfilBean(@Null String code) {
        this.code.set(code);
    }

    @NotNull
    public static ProfilBean from(@NotNull ProfilDTO profilDTO) {
        return new ProfilBean().fromDto(profilDTO);
    }

    @NotNull
    public static ProfilDTO to(@NotNull ProfilBean profilBean) {
        return profilBean.toDto();
    }

    @Null
    public String getCode() {
        return code.get();
    }

    @NotNull
    public StringProperty codeProperty() {
        return code;
    }

    @NotNull
    @Override
    public ProfilBean fromDto(@NotNull ProfilDTO dto) {
        return new ProfilBean(dto);
    }

    @NotNull
    public ProfilDTO toDto() {
        return new ProfilDTO(code.get());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if ((o == null) || (getClass() != o.getClass())) return false;

        ProfilBean that = (ProfilBean) o;

        return (getCode() != null) ? getCode().equals(that.getCode()) : (that.getCode() == null);
    }

    @Override
    public int hashCode() {
        return (getCode() != null) ? getCode().hashCode() : 0;
    }


    @Override
    public int compareTo(@NotNull ProfilBean o) {
        return (
                (this.getCode() == null) && (o.getCode() == null)) ? 0
                : (((this.getCode() == null) && (o.getCode() != null)) ? -1
                : (((this.getCode() != null) && (o.getCode() == null)) ? 1
                : this.getCode().compareTo(o.getCode()))
        );
    }

    // Juste pour faciliter le débogage.
    @Override
    public String toString() {
        return ((code.get() == null) ? "N/C" : code.get());
    }

}

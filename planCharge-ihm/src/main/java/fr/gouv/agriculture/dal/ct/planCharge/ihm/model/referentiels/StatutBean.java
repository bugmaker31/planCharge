package fr.gouv.agriculture.dal.ct.planCharge.ihm.model.referentiels;

import fr.gouv.agriculture.dal.ct.ihm.model.AbstractBean;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dto.StatutDTO;
import fr.gouv.agriculture.dal.ct.planCharge.util.Objects;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

/**
 * Created by frederic.danna on 01/07/2017.
 */
public class StatutBean extends AbstractBean<StatutDTO, StatutBean> implements Comparable<StatutBean> {

    // Fields :

    @NotNull
    private StringProperty code = new SimpleStringProperty();


    // Constructeurs :

    public StatutBean() {
        super();
    }

    public StatutBean(@NotNull StatutDTO statutDTO) {
        this.code.set(statutDTO.getCode());
    }

    public StatutBean(@Null String code) {
        this.code.set(code);
    }


    // Getters/Setters :

    @Null
    public String getCode() {
        return code.get();
    }

    @NotNull
    public StringProperty codeProperty() {
        return code;
    }


    // Méthodes :


    // AbstractBean

    @NotNull
    public static StatutBean from(@NotNull StatutDTO statutDTO) {
        return new StatutBean().fromDto(statutDTO);
    }

    @NotNull
    public static StatutDTO to(@NotNull StatutBean statutBean) {
        return statutBean.toDto();
    }


    @NotNull
    @Override
    public StatutBean fromDto(@NotNull StatutDTO dto) {
        return new StatutBean(dto);
    }

    @NotNull
    public StatutDTO toDto() {
        return new StatutDTO(code.get());
    }

    // General

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if ((o == null) || (getClass() != o.getClass())) return false;

        StatutBean that = (StatutBean) o;

        return (getCode() != null) ? getCode().equals(that.getCode()) : (that.getCode() == null);
    }

    @Override
    public int hashCode() {
        return (getCode() != null) ? getCode().hashCode() : 0;
    }


    @Override
    public int compareTo(@NotNull StatutBean o) {
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
        //noinspection HardcodedFileSeparator
        return "Statut"
                + " " + Objects.value(getCode(), "N/C");
    }

}

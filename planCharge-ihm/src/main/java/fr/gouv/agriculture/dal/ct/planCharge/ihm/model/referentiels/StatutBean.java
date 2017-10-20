package fr.gouv.agriculture.dal.ct.planCharge.ihm.model.referentiels;

import fr.gouv.agriculture.dal.ct.ihm.model.AbstractBean;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dto.StatutDTO;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.Statut;
import fr.gouv.agriculture.dal.ct.planCharge.util.Objects;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

/**
 * Created by frederic.danna on 01/07/2017.
 */
public class StatutBean extends AbstractBean<StatutDTO, StatutBean> implements Comparable<StatutBean> {

    public static final StatutBean NOUVEAU = StatutBean.from(StatutDTO.NOUVEAU);
    public static final StatutBean EN_COURS = StatutBean.from(StatutDTO.EN_COURS);
    public static final StatutBean EN_ATTENTE = StatutBean.from(StatutDTO.EN_ATTENTE);
    public static final StatutBean RECURRENT = StatutBean.from(StatutDTO.RECURRENT);
    public static final StatutBean REPORTE = StatutBean.from(StatutDTO.REPORTE);
    public static final StatutBean ANNULE = StatutBean.from(StatutDTO.ANNULE);
    public static final StatutBean DOUBLON = StatutBean.from(StatutDTO.DOUBLON);
    public static final StatutBean TERMINE = StatutBean.from(StatutDTO.TERMINE);
    public static final StatutBean A_VENIR = StatutBean.from(StatutDTO.A_VENIR);
    //
    public static final StatutBean PROVISION = StatutBean.from(StatutDTO.PROVISION);


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

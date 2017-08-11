package fr.gouv.agriculture.dal.ct.planCharge.ihm.model.referentiels;

import fr.gouv.agriculture.dal.ct.ihm.model.AbstractBean;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dto.ProjetAppliDTO;
import fr.gouv.agriculture.dal.ct.planCharge.util.Objects;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

/**
 * Created by frederic.danna on 01/07/2017.
 */
public class ProjetAppliBean extends AbstractBean<ProjetAppliDTO, ProjetAppliBean> implements Comparable<ProjetAppliBean> {

    @NotNull
    private StringProperty code = new SimpleStringProperty();


    private ProjetAppliBean() {
        super();
    }

    private ProjetAppliBean(@NotNull ProjetAppliDTO projetAppliDTO) {
        this();
        this.code.set(projetAppliDTO.getCode());
    }

    private ProjetAppliBean(@Null String code) {
        this();
        this.code.set(code);
    }

    @NotNull
    public static ProjetAppliBean from(@NotNull ProjetAppliDTO projetAppliDTO) {
        return new ProjetAppliBean().fromDto(projetAppliDTO);
    }

    @SuppressWarnings("StaticMethodNamingConvention")
    @NotNull
    public static ProjetAppliDTO to(@NotNull ProjetAppliBean projetAppliBean) {
        return projetAppliBean.toDto();
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
    public ProjetAppliBean fromDto(@NotNull ProjetAppliDTO dto) {
        return new ProjetAppliBean(dto);
    }

    @NotNull
    public ProjetAppliDTO toDto() {
        return new ProjetAppliDTO(code.get());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if ((o == null) || (getClass() != o.getClass())) return false;

        ProjetAppliBean that = (ProjetAppliBean) o;

        return (getCode() != null) ? getCode().equals(that.getCode()) : (that.getCode() == null);
    }

    @Override
    public int hashCode() {
        return (getCode() != null) ? getCode().hashCode() : 0;
    }


    @Override
    public int compareTo(@NotNull ProjetAppliBean o) {
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
        return "Projet/Appli"
                + " " + Objects.value(code.get(), "N/C");
    }

}

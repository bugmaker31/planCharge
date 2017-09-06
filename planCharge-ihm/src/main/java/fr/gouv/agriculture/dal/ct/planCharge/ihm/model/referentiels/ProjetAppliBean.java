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

    @NotNull
    private StringProperty nom = new SimpleStringProperty();

    @NotNull
    private StringProperty trigrammeCPI = new SimpleStringProperty();


    public ProjetAppliBean() {
        super();
    }

    private ProjetAppliBean(@Null String code) {
        this();
        this.code.setValue(code);
        this.nom.setValue(null);
        this.trigrammeCPI.setValue(null);
    }

    private ProjetAppliBean(@NotNull ProjetAppliDTO projetAppliDTO) {
        this();
        this.code.setValue(projetAppliDTO.getCode());
        this.nom.setValue(projetAppliDTO.getNom());
        this.trigrammeCPI.setValue(projetAppliDTO.getTrigrammeCPI());
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
    public String getNom() {
        return nom.get();
    }

    @NotNull
    public StringProperty nomProperty() {
        return nom;
    }

    @NotNull
    public String getTrigrammeCPI() {
        return trigrammeCPI.get();
    }

    @NotNull
    public StringProperty trigrammeCPIProperty() {
        return trigrammeCPI;
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

    @NotNull
    @Override
    public ProjetAppliBean fromDto(@NotNull ProjetAppliDTO dto) {
        return new ProjetAppliBean(dto);
    }

    @NotNull
    public ProjetAppliDTO toDto() {
        return new ProjetAppliDTO(code.get(), nom.get(), trigrammeCPI.get());
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
                + " [" + Objects.value(code.get(), "N/C") + "]"
                + " " + Objects.value(nom.get(), "N/C")
                + " (" + Objects.value(trigrammeCPI.get(), "N/C") + ")";
    }

}

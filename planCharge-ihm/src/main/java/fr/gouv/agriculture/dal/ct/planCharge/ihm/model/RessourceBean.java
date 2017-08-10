package fr.gouv.agriculture.dal.ct.planCharge.ihm.model;

import fr.gouv.agriculture.dal.ct.ihm.model.AbstractBean;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dto.RessourceDTO;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dto.RessourceGeneriqueDTO;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dto.RessourceHumaineDTO;
import fr.gouv.agriculture.dal.ct.planCharge.util.Objects;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

/**
 * Created by frederic.danna on 01/07/2017.
 */
public abstract class RessourceBean<B extends RessourceBean<B, D>, D extends RessourceDTO> extends AbstractBean<D, B>  {

    @NotNull
    private StringProperty code = new SimpleStringProperty();


    public RessourceBean() {
        super();
    }

    public RessourceBean(@Null String code) {
        super();
        this.code.set(code);
    }

    public static RessourceDTO to(@NotNull RessourceBean ressourceBean) {
        return (ressourceBean instanceof RessourceHumaineBean)
                ? RessourceHumaineBean.to((RessourceHumaineBean) ressourceBean)
                : RessourceGeneriqueBean.to((RessourceGeneriqueBean) ressourceBean);
    }

    @NotNull
    public static RessourceBean from(@NotNull RessourceDTO ressourceDTO) {
        return (ressourceDTO instanceof RessourceHumaineDTO)
                ? RessourceHumaineBean.from((RessourceHumaineDTO) ressourceDTO)
                : RessourceGeneriqueBean.from((RessourceGeneriqueDTO) ressourceDTO);
    }

    @Null
    public String getCode() {
        return code.get();
    }

    public void setCode(@NotNull String code) {
        this.code.set(code);
    }

    @NotNull
    public StringProperty codeProperty() {
        return code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if ((o == null) || (getClass() != o.getClass())) return false;

        RessourceBean that = (RessourceBean) o;

        return (getCode() != null) ? getCode().equals(that.getCode()) : (that.getCode() == null);
    }

    @Override
    public int hashCode() {
        return (getCode() != null) ? getCode().hashCode() : 0;
    }


    // Juste pour faciliter le débogage.
    @Override
    public String toString() {
        //noinspection HardcodedFileSeparator
        return "Rsrc " + Objects.value(code.get(), "N/C");
    }

}

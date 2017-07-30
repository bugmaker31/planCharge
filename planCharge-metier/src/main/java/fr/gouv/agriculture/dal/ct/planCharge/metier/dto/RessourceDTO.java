package fr.gouv.agriculture.dal.ct.planCharge.metier.dto;

import fr.gouv.agriculture.dal.ct.metier.dto.AbstractDTO;
import fr.gouv.agriculture.dal.ct.metier.dto.DTOException;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.Ressource;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.RessourceGenerique;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.RessourceHumaine;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

/**
 * Created by frederic.danna on 25/03/2017.
 */
public abstract class RessourceDTO<E extends Ressource<E>, T extends RessourceDTO<E, T>> extends AbstractDTO<E, String, T> {


    @Null
    private String code;


    RessourceDTO() {
        super();
    }

    public RessourceDTO(@Null String code) {
        this();
        this.code = code;
    }


    @Null
    public String getCode() {
        return code;
    }


    @Null
    @Override
    public String getIdentity() {
        return getCode();
    }


    public boolean estHumain() {
        return this instanceof RessourceHumaineDTO;
    }


    @NotNull
    public static RessourceDTO from(@NotNull Ressource ressource) {
        return (ressource instanceof RessourceHumaine)
                ? RessourceHumaineDTO.from((RessourceHumaine) ressource)
                : RessourceGeneriqueDTO.from((RessourceGenerique) ressource);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RessourceDTO ressource = (RessourceDTO) o;

        return code.equals(ressource.code);
    }

    @Override
    public int hashCode() {
        return code.hashCode();
    }


    @Override
    final public int compareTo(@NotNull T o) {
        return code.compareTo(o.getCode());
    }


    // Juste pour faciliter le debogage.
    @Override
    public String toString() {
        return ("[" + code + "]");
    }

}

package fr.gouv.agriculture.dal.ct.planCharge.metier.dto;

import fr.gouv.agriculture.dal.ct.metier.dto.AbstractDTO;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.Ressource;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.RessourceGenerique;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.RessourceHumaine;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

/**
 * Created by frederic.danna on 25/03/2017.
 */
public abstract class RessourceDTO<E extends Ressource<E>, D extends RessourceDTO<E, D>> extends AbstractDTO<E, String, D> {


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
    public final String getIdentity() {
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
    public final int compareTo(@NotNull D o) {
        return code.compareTo(o.getCode());
    }


    // Juste pour faciliter le debogage.
    @Override
    public String toString() {
        return ("[" + code + "]");
    }

}

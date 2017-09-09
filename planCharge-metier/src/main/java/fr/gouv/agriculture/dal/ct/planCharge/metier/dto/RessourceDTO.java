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


    public boolean estHumain() throws DTOException {
        return toEntity().estHumain();
    }


    @NotNull
    public static RessourceDTO from(@NotNull Ressource ressource) {
        return (ressource instanceof RessourceHumaine)
                ? RessourceHumaineDTO.from((RessourceHumaine) ressource)
                : RessourceGeneriqueDTO.from((RessourceGenerique) ressource);
    }


    @Override
    public final int compareTo(@NotNull D o) {
        if (code == null) {
            return -1; // Les ressources sans code sont triées en tête de liste.
        }
        if (o.getCode() == null) {
            return 1; // Les ressources sans code sont triées en tête de liste.
        }
        return code.compareTo(o.getCode());
    }


    // Juste pour faciliter le debogage.
    @Override
    public String toString() {
        return ("[" + code + "]");
    }

}

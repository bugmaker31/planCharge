package fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels;

import fr.gouv.agriculture.dal.ct.metier.dto.DTOException;
import fr.gouv.agriculture.dal.ct.metier.regleGestion.RegleGestion;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dto.CategorieTacheDTO;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Created by frederic.danna on 25/03/2017.
 */
public class RessourceGenerique extends Ressource<RessourceGenerique> {

    public static final RessourceGenerique NIMPORTE_QUI = new RessourceGenerique("?");
    public static final RessourceGenerique TOUS = new RessourceGenerique("*");

    public static final RessourceGenerique[] RESSOURCES_GENERIQUES = {
      NIMPORTE_QUI,
      TOUS
    };

    public static final List<RessourceGenerique> values() {
        return Arrays.asList(RESSOURCES_GENERIQUES);
    }


    public RessourceGenerique(@NotNull String code) {
        super(code);
    }


    @NotNull
    public static RessourceGenerique valeur(@NotNull String texte) throws DTOException {
        Optional<RessourceGenerique> categ = Arrays.stream(RESSOURCES_GENERIQUES)
                .filter(cat -> texte.equals(cat.getCode()))
                .findAny();
        if (!categ.isPresent()) {
            throw new DTOException("Ressource générique inconnue : '" + texte + "'.");
        }
        return categ.get();
    }

    @Null
    public static RessourceGenerique valeurOuNull(@NotNull String texte) {
        Optional<RessourceGenerique> categ = Arrays.stream(RESSOURCES_GENERIQUES)
                .filter(cat -> texte.equals(cat.getCode()))
                .findAny();
        return categ.orElse(null);
    }

}

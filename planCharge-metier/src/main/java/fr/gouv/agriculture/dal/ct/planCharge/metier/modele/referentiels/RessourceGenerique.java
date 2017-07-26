package fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels;

import fr.gouv.agriculture.dal.ct.planCharge.metier.regleGestion.RegleGestion;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

/**
 * Created by frederic.danna on 25/03/2017.
 */
public class RessourceGenerique extends Ressource<RessourceGenerique> {

    public static final RessourceGenerique NIMPORTE_QUI = new RessourceGenerique("?");
    public static final RessourceGenerique TOUS = new RessourceGenerique("*");

    private RessourceGenerique(@NotNull String trigramme) {
        super(trigramme);
    }

    @NotNull
    @Override
    public List<RegleGestion<RessourceGenerique>> getReglesGestion() {
        return Collections.EMPTY_LIST; // TODO FDA 2017/07 Coder les règles de gestion.
    }
}

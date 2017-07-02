package fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Created by frederic.danna on 25/03/2017.
 */
public class PseudoRessource extends Ressource {

    public static final PseudoRessource NIMPORTE_QUI = new PseudoRessource("?");
    public static final PseudoRessource TOUS = new PseudoRessource("*");

    private PseudoRessource(@NotNull String trigramme) {
        super(trigramme);
    }

}

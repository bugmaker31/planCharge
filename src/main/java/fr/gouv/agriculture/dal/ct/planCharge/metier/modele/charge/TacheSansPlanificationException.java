package fr.gouv.agriculture.dal.ct.planCharge.metier.modele.charge;

import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.Tache;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.ModeleException;

/**
 * Created by frederic.danna on 16/04/2017.
 */
public class TacheSansPlanificationException extends ModeleException {
    public TacheSansPlanificationException(Tache tache) {
        super("Tâche sans planification : " + tache.noTache());
    }
}

package fr.gouv.agriculture.dal.ct.planCharge.metier.modele.charge;

import fr.gouv.agriculture.dal.ct.planCharge.metier.dto.TacheDTO;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.tache.ITache;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.tache.Tache;
import fr.gouv.agriculture.dal.ct.metier.modele.ModeleException;

import javax.validation.constraints.NotNull;

/**
 * Created by frederic.danna on 16/04/2017.
 */
public class TacheSansPlanificationException extends ModeleException {
    public TacheSansPlanificationException(@NotNull ITache tache) {
        super("Tâche sans calendrier : " + tache.noTache());
    }
}

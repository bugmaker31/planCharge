package fr.gouv.agriculture.dal.ct.planCharge.ihm.view.converter;

import fr.gouv.agriculture.dal.ct.planCharge.metier.constante.StatutRevision;
import javafx.util.StringConverter;
import org.slf4j.Logger;

import javax.validation.constraints.Null;

import static org.slf4j.LoggerFactory.getLogger;

public class StatutRevisionConverter extends StringConverter<StatutRevision> {

    @SuppressWarnings("InstanceVariableNamingConvention")
    private /*static*/ final Logger LOGGER = getLogger(StatutRevisionConverter.class);

    @Null
    @Override
    public String toString(StatutRevision sr) {
        if (sr == null) {
            return null;
        }
        return sr.getLibelle();
    }

    @Null
    @Override
    public StatutRevision fromString(String s) {
        if (s == null) {
            return null;
        }
        try {
            return StatutRevision.valueOf(s);
        } catch (IllegalArgumentException e) {
            LOGGER.error("Impossible de décoder un statut de révision dans la chaîne '" + s + "'.", e); // TODO FDA 2017/08 Trouver mieux que de loguer une erreur.
            return null;
        }
    }
}

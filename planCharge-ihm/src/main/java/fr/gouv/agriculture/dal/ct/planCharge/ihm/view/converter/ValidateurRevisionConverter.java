package fr.gouv.agriculture.dal.ct.planCharge.ihm.view.converter;

import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.revision.StatutRevision;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.revision.ValidateurRevision;
import javafx.util.StringConverter;
import org.slf4j.Logger;

import javax.validation.constraints.Null;

import static org.slf4j.LoggerFactory.getLogger;

public class ValidateurRevisionConverter extends StringConverter<ValidateurRevision> {

    @SuppressWarnings("InstanceVariableNamingConvention")
    private /*static*/ final Logger LOGGER = getLogger(ValidateurRevisionConverter.class);

    @Null
    @Override
    public String toString(ValidateurRevision sr) {
        if (sr == null) {
            return null;
        }
        return sr.getTrigramme();
    }

    @Null
    @Override
    public ValidateurRevision fromString(String s) {
        if (s == null) {
            return null;
        }
        try {
            return ValidateurRevision.valueOf(s);
        } catch (IllegalArgumentException e) {
            LOGGER.error("Impossible de décoder un validateur de révision dans la chaîne '" + s + "'.", e); // TODO FDA 2017/08 Trouver mieux que de loguer une erreur.
            return null;
        }
    }
}

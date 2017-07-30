package fr.gouv.agriculture.dal.ct.planCharge.ihm.model;

import fr.gouv.agriculture.dal.ct.metier.modele.ModeleException;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.CategorieTache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.Null;
import java.util.Comparator;

/**
 * Created by frederic.danna on 22/04/2017.
 * @author frederic.danna
 */
public class CodeCategorieTacheComparator implements Comparator<String> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CodeCategorieTacheComparator.class);

    public static final Comparator<String> COMPARATEUR = new CodeCategorieTacheComparator();

    @Override
    public int compare(@SuppressWarnings("ParameterNameDiffersFromOverriddenParameter") @Null String codeCategorie1, @SuppressWarnings("ParameterNameDiffersFromOverriddenParameter") @Null String codeCategorie2) {
        if ((codeCategorie1 == null) && (codeCategorie2 == null)) {
            return 0;
        }
        if ((codeCategorie1 != null) && (codeCategorie2 == null)) {
            return -1;
        }
        assert codeCategorie2 != null;
        if (codeCategorie1 == null) {
            return 1;
        }
        assert codeCategorie1 != null;
        try {
            CategorieTache c1 = CategorieTache.valeur(codeCategorie1);
            CategorieTache c2 = CategorieTache.valeur(codeCategorie2);
            assert c1 != null;
            assert c2 != null;
            return c1.compareTo(c2);
        } catch (ModeleException e) {
            LOGGER.error("Impossible de trier les catégories '{}' et '{}'.", codeCategorie1, codeCategorie2, e);
            return 0;
        }
    }

}

package fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels;

import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.AbstractEntity;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.ModeleException;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.Arrays;
import java.util.Optional;

/**
 * Created by frederic.danna on 01/05/2017.
 */
public enum CategorieTache {

    PROJET("Projets"),
    SERVICE("Services"),
    ORGANISATION_INTERNE("Organisation interne");

    @NotNull
    private final String code;

    CategorieTache(@NotNull String code) {
        this.code = code;
    }

    @NotNull
    public String getCode() {
        return code;
    }

    @NotNull
    public static CategorieTache valeur(@NotNull String texte) throws ModeleException {
        Optional<CategorieTache> categ = Arrays.stream(values())
                .filter(cat -> texte.startsWith(cat.code))
                .findAny();
        if (!categ.isPresent()) {
            throw new ModeleException("Catégorie inconnue : '" + texte + "'.");
        }
        return categ.get();
    }

    @Null
    public static CategorieTache valeurOuNull(@NotNull String texte) {
        Optional<CategorieTache> categ = Arrays.stream(values())
                .filter(cat -> texte.startsWith(cat.code))
                .findAny();
        return (categ.isPresent() ? categ.get() : null);
    }
}

package fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels;

import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.ModeleException;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.Arrays;
import java.util.Optional;

/**
 * Created by frederic.danna on 01/05/2017.
 */
public enum SousCategorieTache {

    ARCHI("Architecture"),
    REF_TECH("Référent technique"),
    GEST_CONF("GC"),
    EXPLOIT("Exploitation"),
    ASSIST("Assistance"),
    QUALITE("Qualité"),
    GEST_EQUIPE("Gestion d'équipe"),
    GEST_BUDGET("Gestion du budget");

    @NotNull
    private final String code;

    SousCategorieTache(@NotNull String code) {
        this.code = code;
    }

    @NotNull
    public String getCode() {
        return code;
    }

    @NotNull
    public static SousCategorieTache valeur(@NotNull String texte) throws ModeleException {
        Optional<SousCategorieTache> categ = Arrays.stream(values())
                .filter(cat -> texte.startsWith(cat.code))
                .findAny();
        if (!categ.isPresent()) {
            throw new ModeleException("Sous-catégorie inconnue : '" + texte + "'.");
        }
        return categ.get();
    }

    @Null
    public static SousCategorieTache valeurOuNull(@NotNull String texte) {
        Optional<SousCategorieTache> categ = Arrays.stream(values())
                .filter(cat -> texte.startsWith(cat.code))
                .findAny();
        return (categ.isPresent() ? categ.get() : null);
    }
}

package fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels;

import fr.gouv.agriculture.dal.ct.metier.modele.AbstractEntity;
import fr.gouv.agriculture.dal.ct.metier.modele.ModeleException;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Created by frederic.danna on 01/05/2017.
 */
public class SousCategorieTache extends AbstractEntity<String, SousCategorieTache> {

    @NotNull
    public static List<SousCategorieTache> values() {
        return Arrays.asList(
                new SousCategorieTache("Architecture"),
                new SousCategorieTache("Référent technique"),
                new SousCategorieTache("GC"),
                new SousCategorieTache("Exploitation"),
                new SousCategorieTache("Assistance"),
                new SousCategorieTache("Qualité"),
                new SousCategorieTache("Gestion d'équipe"),
                new SousCategorieTache("Gestion du budget")
        );
    }

    @NotNull
    private final String code;


    private SousCategorieTache(@NotNull String code) {
        this.code = code;
    }


    @NotNull
    public String getCode() {
        return code;
    }


    @NotNull
    @Override
    public String getIdentity() {
        return code;
    }

    @Override
    public int compareTo(SousCategorieTache o) {
        return code.compareTo(o.getCode());
    }


    @NotNull
    public static SousCategorieTache valeur(@NotNull String texte) throws ModeleException {
        Optional<SousCategorieTache> categ = values().stream()
                .filter(cat -> texte.startsWith(cat.code))
                .findAny();
        if (!categ.isPresent()) {
            throw new ModeleException("Sous-catégorie inconnue : '" + texte + "'.");
        }
        return categ.get();
    }

    @Null
    public static SousCategorieTache valeurOuNull(@NotNull String texte) {
        Optional<SousCategorieTache> categ = values().stream()
                .filter(cat -> texte.startsWith(cat.code))
                .findAny();
        return (categ.orElse(null));
    }
}

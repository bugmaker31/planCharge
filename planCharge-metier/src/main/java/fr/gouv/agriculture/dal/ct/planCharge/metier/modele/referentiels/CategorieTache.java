package fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels;

import fr.gouv.agriculture.dal.ct.metier.modele.AbstractEntity;
import fr.gouv.agriculture.dal.ct.metier.modele.ModeleException;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.io.Serializable;
import java.util.*;

/**
 * Created by frederic.danna on 01/05/2017.
 */
// Rq : Une 'class' et non une 'enum' juste pour pouvoir implémenter un 'compareTo' spécifique.
public class CategorieTache extends AbstractEntity<String, CategorieTache> {

    @NotNull
    public static final CategorieTache PROJET = new CategorieTache("Projets");
    @NotNull
    public static final CategorieTache SERVICE = new CategorieTache("Services");
    @NotNull
    public static final CategorieTache ORGANISATION_INTERNE = new CategorieTache("Organisation interne CT");

    // Rq : L'ordre est important, car c'est cet ordre de tri (par défaut) qui est repris dans les IHMs.
    @NotNull
    private static final CategorieTache[] VALUES = {
            PROJET,
            SERVICE,
            ORGANISATION_INTERNE
    };

    @NotNull
    public static final CategorieTache[] values() {
        return VALUES;
    }

    @NotNull
    public static final List<CategorieTache> CATEGORIES = Arrays.asList(values());


    @NotNull
    private final String code;


    private CategorieTache(@NotNull String code) {
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


    @NotNull
    public static CategorieTache valeur(@NotNull String texte) throws ModeleException {
        Optional<CategorieTache> categ = Arrays.stream(values())
                .filter(cat -> texte.equals(cat.getCode()))
                .findAny();
        if (!categ.isPresent()) {
            throw new ModeleException("Catégorie inconnue : '" + texte + "'.");
        }
        return categ.get();
    }

    @Null
    public static CategorieTache valeurOuNull(@NotNull String texte) {
        Optional<CategorieTache> categ = Arrays.stream(values())
                .filter(cat -> texte.equals(cat.getCode()))
                .findAny();
        return categ.orElse(null);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CategorieTache)) return false;

        CategorieTache that = (CategorieTache) o;

        return (getCode() != null) ? getCode().equals(that.getCode()) : (that.getCode() == null);
    }

    @Override
    public int hashCode() {
        return (getCode() != null) ? getCode().hashCode() : 0;
    }


    @Override
    public int compareTo(CategorieTache o) {
        Integer thisIndex = CATEGORIES.indexOf(this);
        assert thisIndex != -1;
        Integer otherIndex = CATEGORIES.indexOf(o);
        assert otherIndex != -1;
        return thisIndex.compareTo(otherIndex);
    }


    // Pour déboguer, uniquement.
    @Override
    public String toString() {
        return code;
    }
}

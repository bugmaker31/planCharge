package fr.gouv.agriculture.dal.ct.planCharge.metier.dto;

import fr.gouv.agriculture.dal.ct.metier.dto.DTOException;
import fr.gouv.agriculture.dal.ct.metier.modele.ModeleException;
import fr.gouv.agriculture.dal.ct.metier.dto.AbstractDTO;
import fr.gouv.agriculture.dal.ct.metier.regleGestion.RegleGestion;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.CategorieTache;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by frederic.danna on 01/05/2017.
 */
// Rq : Une 'class' et non une 'enum' juste pour pouvoir implémenter un 'compareTo' spécifique.
public class CategorieTacheDTO extends AbstractDTO<CategorieTache, String, CategorieTacheDTO> implements Comparable<CategorieTacheDTO> {

    // Rq : L'ordre est important, car c'est cet ordre de tri (par défaut) qui est repris dans les IHMs.
    @NotNull
    public static final CategorieTacheDTO[] values() {
        return CategorieTache.CATEGORIES.stream()
                .map(categorieTache -> new CategorieTacheDTO(categorieTache.getCode()))
                .collect(Collectors.toList())
                .toArray(new CategorieTacheDTO[0]);
    }

    @NotNull
    public static final List<CategorieTacheDTO> CATEGORIES = Arrays.asList(values());

    @NotNull
    public static final String[] CODES_CATEGORIES = Arrays.stream(values()).map(categorieTacheDTO -> categorieTacheDTO.getCode()).collect(Collectors.toList()).toArray(new String[0]);


    @NotNull
    private String code;


    private CategorieTacheDTO() {
        super();
    }

    private CategorieTacheDTO(@NotNull String code) {
        this();
        this.code = code;
    }


    @NotNull
    public String getCode() {
        return code;
    }


    @NotNull
    public static CategorieTacheDTO valeur(@NotNull String texte) throws DTOException {
        CategorieTacheDTO valeur = valeurOuNull(texte);
        if (valeur == null) {
            throw new DTOException("Catégorie inconnue : '" + texte + "'.");
        }
        return valeur;
    }

    @Null
    public static CategorieTacheDTO valeurOuNull(@NotNull String texte) {
        Optional<CategorieTacheDTO> categ = Arrays.stream(values())
                .filter(cat -> texte.equals(cat.getCode()))
                .findAny();
        return categ.orElse(null);
    }


    @NotNull
    @Override
    public String getIdentity() {
        return code;
    }


    @NotNull
    @Override
    public CategorieTache toEntity() throws DTOException {
        try {
            return CategorieTache.valeur(code);
        } catch (ModeleException e) {
            throw new DTOException("Impossible de transformer la catégorie de DTO en entité.", e);
        }
    }

    @NotNull
    @Override
    public CategorieTacheDTO fromEntity(@NotNull CategorieTache entity) throws DTOException {
        return CategorieTacheDTO.valeur(entity.getCode());
    }

    @NotNull
    public static CategorieTacheDTO from(@NotNull CategorieTache categorie) throws DTOException {
        return new CategorieTacheDTO().fromEntity(categorie);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CategorieTacheDTO)) return false;

        CategorieTacheDTO that = (CategorieTacheDTO) o;

        return (getCode() != null) ? getCode().equals(that.getCode()) : (that.getCode() == null);
    }

    @Override
    public int hashCode() {
        return (getCode() != null) ? getCode().hashCode() : 0;
    }


    public int compareTo(CategorieTacheDTO o) {
        Integer thisIndex = CATEGORIES.indexOf(this);
        assert thisIndex != -1;
        Integer otherIndex = CATEGORIES.indexOf(o);
        assert otherIndex != -1;
        return thisIndex.compareTo(otherIndex);
    }


    @NotNull
    @Override
    protected List<RegleGestion<CategorieTacheDTO>> getReglesGestion() {
        return Collections.emptyList(); // TODO FDA 2017/07 Coder les règles de gestion.
    }

    // Pour déboguer, uniquement.

    @Override
    public String toString() {
        return code;
    }
}

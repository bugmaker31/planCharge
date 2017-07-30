package fr.gouv.agriculture.dal.ct.planCharge.metier.dto;

import fr.gouv.agriculture.dal.ct.metier.dto.AbstractDTO;
import fr.gouv.agriculture.dal.ct.metier.dto.DTOException;
import fr.gouv.agriculture.dal.ct.metier.modele.ModeleException;
import fr.gouv.agriculture.dal.ct.metier.regleGestion.RegleGestion;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.SousCategorieTache;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by frederic.danna on 01/05/2017.
 */
public class SousCategorieTacheDTO extends AbstractDTO<SousCategorieTache, String, SousCategorieTacheDTO> {

    @NotNull
    public static final SousCategorieTacheDTO[] values() {
        return SousCategorieTache.values().stream()
                .map(sousCategorieTache -> new SousCategorieTacheDTO(sousCategorieTache.getCode()))
                .collect(Collectors.toList())
                .toArray(new SousCategorieTacheDTO[0]);
    }


    @NotNull
    private final String code;


    private SousCategorieTacheDTO(@NotNull String code) {
        this.code = code;
    }


    @NotNull
    public String getCode() {
        return code;
    }


    @Null
    @Override
    public String getIdentity() {
        return code;
    }


    @Override
    public int compareTo(SousCategorieTacheDTO o) {
        return code.compareTo(o.getCode());
    }


    @NotNull
    @Override
    public SousCategorieTache toEntity() throws DTOException {
        try {
            return SousCategorieTache.valeur(code);
        } catch (ModeleException e) {
            throw new DTOException("Impossible de transformer la sous catégorie d'entité en DTO.", e);
        }
    }

    @NotNull
    @Override
    public SousCategorieTacheDTO fromEntity(@NotNull SousCategorieTache entity) throws DTOException {
        return SousCategorieTacheDTO.valeur(entity.getCode());
    }

    @NotNull
    public static SousCategorieTacheDTO from(@NotNull SousCategorieTache sousCateg) throws DTOException {
        return valeur(sousCateg.getCode());
    }


    @NotNull
    @Override
    protected List<RegleGestion<SousCategorieTacheDTO>> getReglesGestion() {
        return Collections.emptyList(); // TODO FDA 2017/07 Coder les RGs.
    }


    @NotNull
    public static SousCategorieTacheDTO valeur(@NotNull String texte) throws DTOException {
        Optional<SousCategorieTacheDTO> categ = Arrays.stream(values())
                .filter(cat -> texte.startsWith(cat.code))
                .findAny();
        if (!categ.isPresent()) {
            throw new DTOException("Sous-catégorie inconnue : '" + texte + "'.");
        }
        return categ.get();
    }

    @Null
    public static SousCategorieTacheDTO valeurOuNull(@NotNull String texte) {
        Optional<SousCategorieTacheDTO> categ = Arrays.stream(values())
                .filter(cat -> texte.startsWith(cat.code))
                .findAny();
        return (categ.orElse(null));
    }
}

package fr.gouv.agriculture.dal.ct.planCharge.metier.dto;

import fr.gouv.agriculture.dal.ct.metier.dto.DTOException;
import fr.gouv.agriculture.dal.ct.metier.modele.ModeleException;
import fr.gouv.agriculture.dal.ct.metier.regleGestion.RegleGestion;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.CategorieTache;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.RessourceGenerique;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Created by frederic.danna on 25/03/2017.
 */
public class RessourceGeneriqueDTO extends RessourceDTO<RessourceGenerique, RessourceGeneriqueDTO> {

    public static final RessourceGeneriqueDTO NIMPORTE_QUI = new RessourceGeneriqueDTO("?");
    public static final RessourceGeneriqueDTO TOUS = new RessourceGeneriqueDTO("*");

    public static final RessourceGeneriqueDTO[] values() {
        return new RessourceGeneriqueDTO[] {NIMPORTE_QUI, TOUS};
    }


    private RessourceGeneriqueDTO() {
        super();
    }

    public RessourceGeneriqueDTO(@NotNull String code) {
        super(code);
    }


    @NotNull
    public static RessourceGeneriqueDTO valeur(@NotNull String code) throws DTOException {
        Optional<RessourceGeneriqueDTO> ressource = Arrays.stream(values())
                .filter(rsrc -> code.equals(rsrc.getCode()))
                .findAny();
        if (!ressource.isPresent()) {
            throw new DTOException("Ressource générique inconnue : '" + code + "'.");
        }
        return ressource.get();
    }

    @Null
    public static RessourceGeneriqueDTO valeurOuNull(@NotNull String code) {
        Optional<RessourceGeneriqueDTO> ressource = Arrays.stream(values())
                .filter(rsrc -> code.equals(rsrc.getCode()))
                .findAny();
        return ressource.orElse(null);
    }


    @NotNull
    @Override
    public RessourceGenerique toEntity() {
        return new RessourceGenerique(getCode());
    }

    @NotNull
    @Override
    public RessourceGeneriqueDTO fromEntity(@NotNull RessourceGenerique entity) {
        return new RessourceGeneriqueDTO(entity.getCode());
    }

    @SuppressWarnings("MissortedModifiers")
    @NotNull
    static public RessourceGeneriqueDTO from(@NotNull RessourceGenerique entity) {
        return new RessourceGeneriqueDTO().fromEntity(entity);
    }


    @NotNull
    @Override
    public List<RegleGestion<RessourceGeneriqueDTO>> getReglesGestion() {
        return Collections.emptyList(); // TODO FDA 2017/07 Coder les règles de gestion.
    }
}

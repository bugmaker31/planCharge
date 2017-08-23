package fr.gouv.agriculture.dal.ct.planCharge.metier.dto;

import fr.gouv.agriculture.dal.ct.metier.dto.AbstractDTO;
import fr.gouv.agriculture.dal.ct.metier.regleGestion.RegleGestion;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.Profil;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.Collections;
import java.util.List;

/**
 * Created by frederic.danna on 25/03/2017.
 */
@SuppressWarnings("ClassHasNoToStringMethod")
public class ProfilDTO extends AbstractDTO<Profil, String, ProfilDTO> {

    public static final ProfilDTO TOUS = new ProfilDTO("*");


    @Null
    private String code;


    private ProfilDTO() {
        super();
    }

    public ProfilDTO(@Null String code) {
        this();
        this.code = code;
    }


    @Null
    public String getCode() {
        return code;
    }


    @Null
    @Override
    public String getIdentity() {
        return getCode();
    }


    @NotNull
    @Override
    public Profil toEntity() {
        assert code != null;
        return new Profil(code);
    }

    @NotNull
    @Override
    public ProfilDTO fromEntity(@NotNull Profil entity) {
        return new ProfilDTO(entity.getCode());
    }

    @NotNull
    public static ProfilDTO from(@NotNull Profil entity) {
        return new ProfilDTO().fromEntity(entity);
    }


    @NotNull
    @Override
    public List<RegleGestion<ProfilDTO>> getReglesGestion() {
        return Collections.emptyList(); // TODO FDA 2017/07 Coder les règles de gestion.
    }


    @Override
    public int compareTo(@NotNull ProfilDTO o) {
        return code.compareTo(o.getCode());
    }
}

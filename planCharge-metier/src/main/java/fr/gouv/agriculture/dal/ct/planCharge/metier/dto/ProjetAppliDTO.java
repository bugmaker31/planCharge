package fr.gouv.agriculture.dal.ct.planCharge.metier.dto;

import fr.gouv.agriculture.dal.ct.metier.dto.AbstractDTO;
import fr.gouv.agriculture.dal.ct.metier.regleGestion.RegleGestion;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.ProjetAppli;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.Collections;
import java.util.List;

/**
 * Created by frederic.danna on 25/03/2017.
 */
public class ProjetAppliDTO extends AbstractDTO<ProjetAppli, String, ProjetAppliDTO> implements Comparable<ProjetAppliDTO> {

    @NotNull
    private String code;

    @Null
    private String nom;

    @Null
    private String trigrammeCPI;


    public ProjetAppliDTO(@NotNull String code) {
        this(code, null, null);
    }

    public ProjetAppliDTO(@NotNull String code, @Null String nom, @Null String trigrammeCPI) {
        super();
        this.code = code;
        this.nom = nom;
        this.trigrammeCPI = trigrammeCPI;
    }


    @NotNull
    public String getCode() {
        return code;
    }

    @Null
    public String getNom() {
        return nom;
    }

    @Null
    public String getTrigrammeCPI() {
        return trigrammeCPI;
    }


    @SuppressWarnings("SuspiciousGetterSetter")
    @NotNull
    @Override
    public String getIdentity() {
        return code;
    }


    @NotNull
    @Override
    public ProjetAppli toEntity() {
        assert code != null;
        return new ProjetAppli(code, nom, trigrammeCPI);
    }

    @NotNull
    @Override
    public ProjetAppliDTO fromEntity(@NotNull ProjetAppli entity) {
        return from(entity);
    }

    @NotNull
    static public ProjetAppliDTO from(@NotNull ProjetAppli entity) {
        return new ProjetAppliDTO(entity.getCode(), entity.getNom(), entity.getTrigrammeCPI());
    }


    @NotNull
    @Override
    public List<RegleGestion<ProjetAppliDTO>> getReglesGestion() {
        return Collections.emptyList(); // TODO FDA 2017/07 Coder les règles de gestion.
    }


    @Override
    public int compareTo(@NotNull ProjetAppliDTO o) {
        return code.compareTo(o.getCode());
    }


    // pour déboguer, uniquement.
    @Override
    public String toString() {
        return code;
    }

}

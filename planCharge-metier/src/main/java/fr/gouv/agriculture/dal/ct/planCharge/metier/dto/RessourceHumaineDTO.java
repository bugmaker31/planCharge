package fr.gouv.agriculture.dal.ct.planCharge.metier.dto;

import fr.gouv.agriculture.dal.ct.metier.dto.DTOException;
import fr.gouv.agriculture.dal.ct.metier.regleGestion.RegleGestion;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.RessourceHumaine;
import fr.gouv.agriculture.dal.ct.planCharge.metier.regleGestion.referentiels.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Created by frederic.danna on 25/03/2017.
 */
public class RessourceHumaineDTO extends RessourceDTO<RessourceHumaine, RessourceHumaineDTO> {

    @Null
    private String nom;
    @Null
    private String prenom;
    @Null
    private String societe;
    @Null
    private LocalDate debutMission;
    @Null
    private LocalDate finMission;


    private RessourceHumaineDTO() {
        super();
    }

    public RessourceHumaineDTO(@Null String trigramme, @Null String nom, @Null String prenom, @Null String societe, @Null LocalDate debutMission, @Null LocalDate finMission) {
        super(trigramme);
        this.nom = nom;
        this.prenom = prenom;
        this.societe = societe;
        this.debutMission = debutMission;
        this.finMission = finMission;
    }

    @SuppressWarnings("SuspiciousGetterSetter")
    @Null
    public String getTrigramme() {
        return getCode();
    }

    @Null
    public String getNom() {
        return nom;
    }

    @Null
    public String getPrenom() {
        return prenom;
    }

    @Null
    public String getSociete() {
        return societe;
    }

    @Null
    public LocalDate getDebutMission() {
        return debutMission;
    }

    @Null
    public LocalDate getFinMission() {
        return finMission;
    }


    @NotNull
    public static RessourceHumaineDTO from(@NotNull RessourceHumaine entity) {
        return new RessourceHumaineDTO().fromEntity(entity);
    }

    @NotNull
    @Override
    public RessourceHumaineDTO fromEntity(@NotNull RessourceHumaine entity) {
        return new RessourceHumaineDTO(entity.getTrigramme(), entity.getNom(), entity.getPrenom(), entity.getSociete(), entity.getDebutMission(), entity.getFinMission());
    }

    @NotNull
    @Override
    public RessourceHumaine toEntity() {
        assert getTrigramme() != null; // TODO FDA 201/07 Préciser la RG qui n'est pas respectée.
        assert nom != null; // TODO FDA 201/07 Préciser la RG qui n'est pas respectée.
        assert prenom != null; // TODO FDA 201/07 Préciser la RG qui n'est pas respectée.
        assert societe != null; // TODO FDA 201/07 Préciser la RG qui n'est pas respectée.
        return new RessourceHumaine(getTrigramme(), nom, prenom, societe, debutMission, finMission);
    }


    @NotNull
    @Override
    public List<RegleGestion<RessourceHumaineDTO>> getReglesGestion() {
        return Arrays.asList(
                RGRefRessHumTrigrammeObligatoire.INSTANCE,
                RGRefRessHumNomObligatoire.INSTANCE,
                RGRefRessHumPrenomObligatoire.INSTANCE,
                RGRefRessHumSocieteObligatoire.INSTANCE,
                RGRefRessHumUniciteTrigramme.INSTANCE
                );
    }

    // Juste pour faciliter le debogage.
    @Override
    public String toString() {
        return ("[" + getTrigramme() + "]")
                + " " + nom
                + " " + prenom
                + " " + ("(" + societe + ")")
                + " " + (
                (debutMission == null ? "N/C" : debutMission.format(DateTimeFormatter.ISO_LOCAL_DATE))
                        + ".." + (finMission == null ? "N/C" : finMission.format(DateTimeFormatter.ISO_LOCAL_DATE))
        );
    }
}

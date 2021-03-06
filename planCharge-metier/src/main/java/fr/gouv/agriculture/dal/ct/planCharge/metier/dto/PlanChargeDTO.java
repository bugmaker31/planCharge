package fr.gouv.agriculture.dal.ct.planCharge.metier.dto;

import fr.gouv.agriculture.dal.ct.metier.MetierException;
import fr.gouv.agriculture.dal.ct.metier.dto.AbstractDTO;
import fr.gouv.agriculture.dal.ct.metier.dto.DTOException;
import fr.gouv.agriculture.dal.ct.metier.regleGestion.RegleGestion;
import fr.gouv.agriculture.dal.ct.metier.regleGestion.ViolationRegleGestion;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.charge.PlanCharge;
import fr.gouv.agriculture.dal.ct.planCharge.metier.regleGestion.charge.RGChargePlanDateEtatObligatoire;
import fr.gouv.agriculture.dal.ct.planCharge.metier.regleGestion.charge.RGChargePlanPlanificationsObligatoires;
import fr.gouv.agriculture.dal.ct.planCharge.metier.regleGestion.charge.RGChargePlanReferentielsObligatoires;
import fr.gouv.agriculture.dal.ct.planCharge.util.Objects;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Created by frederic.danna on 11/03/2017.
 *
 * @author frederic.danna
 */
public class PlanChargeDTO extends AbstractDTO<PlanCharge, LocalDate, PlanChargeDTO> {

    @Null
    private LocalDate dateEtat;
    @SuppressWarnings("NullableProblems")
    @NotNull
    private ReferentielsDTO referentiels;
    @SuppressWarnings("NullableProblems")
    @NotNull
    private DisponibilitesDTO disponibilites;
    @SuppressWarnings("NullableProblems")
    @NotNull
    private PlanificationsDTO planifications;

    private PlanChargeDTO() {
        super();
    }

    public PlanChargeDTO(@Null LocalDate dateEtat, @NotNull ReferentielsDTO referentiels, @NotNull DisponibilitesDTO disponibilites, @NotNull PlanificationsDTO planifications) {
        this();
        this.dateEtat = dateEtat;
        this.referentiels = referentiels;
        this.disponibilites = disponibilites;
        this.planifications = planifications;
    }


    @SuppressWarnings("SuspiciousGetterSetter")
    @Null
    @Override
    public LocalDate getIdentity() {
        return dateEtat;
    }

    @Null
    public LocalDate getDateEtat() {
        return dateEtat;
    }

    @NotNull
    public ReferentielsDTO getReferentiels() {
        return referentiels;
    }

    @NotNull
    public DisponibilitesDTO getDisponibilites() {
        return disponibilites;
    }

    @NotNull
    public PlanificationsDTO getPlanifications() {
        return planifications;
    }


    @NotNull
    @Override
    public PlanCharge toEntity() throws DTOException {
        return to(this);
    }

    @NotNull
    public static PlanCharge to(@NotNull PlanChargeDTO planChargeDTO) throws DTOException {
        assert planChargeDTO.getDateEtat() != null; // TODO FDA 2017/07 Préciser la RG qui n'est pas respectée.
        return new PlanCharge(
                planChargeDTO.getDateEtat(),
                planChargeDTO.getReferentiels().toEntity(),
                planChargeDTO.getDisponibilites().toEntity(),
                planChargeDTO.getPlanifications().toEntity()
        );
    }


    @NotNull
    @Override
    public PlanChargeDTO fromEntity(@NotNull PlanCharge entity) throws DTOException {
        return from(entity);
    }

    @NotNull
    public static PlanChargeDTO from(@NotNull PlanCharge entity) throws DTOException {
        return new PlanChargeDTO(
                entity.getDateEtat(),
                ReferentielsDTO.from(entity.getReferentiels()),
                DisponibilitesDTO.from(entity.getDisponibilites()),
                PlanificationsDTO.from(entity.getPlanifications())
        );
    }


    @Override
    public int compareTo(@NotNull PlanChargeDTO o) {
        return 0; // TODO FDA 2017/07 Trouver mieux comme code.
    }


    @NotNull
    @Override
    public List<ViolationRegleGestion> controlerReglesGestion() throws MetierException {
        List<ViolationRegleGestion> violations = super.controlerReglesGestion();
//        assert referentiels != null; // TODO FDA 2017/07 Préciser la RG qui n'est pas respectée.
        violations.addAll(referentiels.controlerReglesGestion());
//        assert disponibilites != null; // TODO FDA 2017/07 Préciser la RG qui n'est pas respectée.
        violations.addAll(disponibilites.controlerReglesGestion());
//        assert planifications != null; // TODO FDA 2017/07 Préciser la RG qui n'est pas respectée.
        violations.addAll(planifications.controlerReglesGestion());
        return violations;
    }

    @NotNull
    @Override
    protected List<RegleGestion<PlanChargeDTO>> getReglesGestion() {
        return Arrays.asList(
                RGChargePlanDateEtatObligatoire.INSTANCE,
                RGChargePlanReferentielsObligatoires.INSTANCE,
                RGChargePlanPlanificationsObligatoires.INSTANCE
        );
    }


    // Pour faciliter le débogage uniquement.
    @Override
    public String toString() {
        return Objects.value(getDateEtat(), dateEtat -> dateEtat.format(DateTimeFormatter.ISO_LOCAL_DATE), "N/C")
                + " " + Objects.value(getPlanifications(), planifications -> planifications.taches().size(), "N/C")
                ;
    }

}

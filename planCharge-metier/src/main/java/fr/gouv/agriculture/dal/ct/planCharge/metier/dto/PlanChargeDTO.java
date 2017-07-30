package fr.gouv.agriculture.dal.ct.planCharge.metier.dto;

import fr.gouv.agriculture.dal.ct.metier.dto.AbstractDTO;
import fr.gouv.agriculture.dal.ct.metier.dto.DTOException;
import fr.gouv.agriculture.dal.ct.metier.regleGestion.RegleGestion;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.charge.PlanCharge;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.charge.TacheSansPlanificationException;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.charge.diff.Difference;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.charge.diff.StatutDifference;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.tache.Tache;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by frederic.danna on 11/03/2017.
 *
 * @author frederic.danna
 */
public class PlanChargeDTO extends AbstractDTO<PlanCharge, LocalDate, PlanChargeDTO> {

    @Null
    private LocalDate dateEtat;
    @Null
    private ReferentielsDTO referentiels;
    @Null
    private PlanificationsDTO planifications;


    private PlanChargeDTO() {
        super();
    }

    public PlanChargeDTO(@Null LocalDate dateEtat, @Null ReferentielsDTO referentiels, @Null PlanificationsDTO planifications) {
        this();
        this.dateEtat = dateEtat;
        this.referentiels = referentiels;
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

    @Null
    public ReferentielsDTO getReferentiels() {
        return referentiels;
    }

    @Null
    public PlanificationsDTO getPlanifications() {
        return planifications;
    }


    @NotNull
    @Override
    public PlanCharge toEntity() throws DTOException {
        return new PlanCharge(getDateEtat(), getReferentiels().toEntity(), getPlanifications().toEntity());
    }

    @NotNull
    @Override
    public PlanChargeDTO fromEntity(@NotNull PlanCharge entity) throws DTOException {
        return new PlanChargeDTO(
                entity.getDateEtat(),
                new ReferentielsDTO().fromEntity(entity.getReferentiels()),
                new PlanificationsDTO().fromEntity(entity.getPlanifications())
        );
    }

    @NotNull
    public static PlanChargeDTO from(@NotNull PlanCharge planCharge) throws DTOException {
        return new PlanChargeDTO().fromEntity(planCharge);
    }


    @Override
    public int compareTo(PlanChargeDTO o) {
        return 0; // TODO FDA 2017/07 Trouver mieux comme code.
    }


    @NotNull
    @Override
    protected List<RegleGestion<PlanChargeDTO>> getReglesGestion() {
        return Collections.emptyList(); // TODO FDA 2017/07 Coder les RG.
    }


    // Pour faciliter le débogage uniquement.
    @Override
    public String toString() {
        return getDateEtat() + " " + getPlanifications().taches().size();
    }

}

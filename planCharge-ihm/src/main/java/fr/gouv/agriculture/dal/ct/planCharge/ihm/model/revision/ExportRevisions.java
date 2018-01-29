package fr.gouv.agriculture.dal.ct.planCharge.ihm.model.revision;

import fr.gouv.agriculture.dal.ct.planCharge.ihm.PlanChargeIhm;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.tache.TacheBean;
import fr.gouv.agriculture.dal.ct.planCharge.util.Objects;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@SuppressWarnings({"ClassHasNoToStringMethod", "ClassWithoutLogger"})
public class ExportRevisions {


    @NotNull
    private final LocalDate dateEtat;

    @NotNull
    public LocalDate getDateEtat() {
        return dateEtat;
    }

    @NotNull
    private final List<TacheBean> tachesBeans;

    @SuppressWarnings("unused") // Utilisée dans "export_revisions.ftlh".
    @NotNull
    public List<TacheBean> getTachesBeans() {
        return tachesBeans;
    }


    public ExportRevisions(@NotNull LocalDate dateEtat, @NotNull List<TacheBean> tachesBeans) {
        super();
        this.dateEtat = dateEtat;
        this.tachesBeans = tachesBeans;
    }


    @SuppressWarnings("unused") // Utilisée dans "export_revisions.ftlh".
    @NotNull
    public String getDateEtatFormatee() {
        return Objects.value(dateEtat, date -> date.format(DateTimeFormatter.ofPattern(PlanChargeIhm.PATRON_FORMAT_DATE)), "N/C");
    }
}

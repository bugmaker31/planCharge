package fr.gouv.agriculture.dal.ct.planCharge.ihm.view;

import fr.gouv.agriculture.dal.ct.ihm.view.EditableAwareTextFieldTableCell;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.AbstractCalendrierProfilBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.charge.PlanChargeBean;
import javafx.util.StringConverter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

public abstract class AbstractCalendrierParProfilCell<S extends AbstractCalendrierProfilBean<?, ?, ?>, T> extends EditableAwareTextFieldTableCell<S, T> {

    @NotNull
    private PlanChargeBean planChargeBean;
    private int noSemaine;

    protected AbstractCalendrierParProfilCell(@NotNull PlanChargeBean planChargeBean, int noSemaine, @NotNull StringConverter<T> stringConverter, @Null Runnable cantEditErrorDisplayer) {
        super(stringConverter, cantEditErrorDisplayer);
        this.planChargeBean = planChargeBean;
        this.noSemaine = noSemaine;
    }

    protected AbstractCalendrierParProfilCell(@NotNull PlanChargeBean planChargeBean, int noSemaine, @NotNull StringConverter<T> stringConverter) {
        this(planChargeBean, noSemaine, stringConverter, null);
    }

    @NotNull
    protected PlanChargeBean getPlanChargeBean() {
        return planChargeBean;
    }

    protected int getNoSemaine() {
        return noSemaine;
    }

/* Rien de spécial... pour l'instant.
        @Override
        public void updateItem(T item, boolean empty) {
            super.updateItem(item, empty);
            styler();
        }

        private void styler() {

            // Réinit des style spécifiques :
            getStyleClass().removeAll("avantMission", "pendantMission", "apresMission");

            // Récupération des infos sur la cellule :
            //noinspection unchecked
            TableRow<? extends S> tableRow = getTableRow();
            S dispoBean = tableRow.getItem();
            if (dispoBean == null) {
                return;
            }
            LocalDate debutMission = dispoBean.getRessourceHumaineBean().getDebutMission();
            LocalDate finMission = dispoBean.getRessourceHumaineBean().getFinMission();

            LocalDate debutPeriode = planChargeBean.getDateEtat().plusDays((getNoSemaine() - 1) * 7); // TODO FDA 2017/06 [issue#26:PeriodeHebdo/Trim]
            LocalDate finPeriode = debutPeriode.plusDays(7);// TODO FDA 2017/06 [issue#26:PeriodeHebdo/Trim]

            // Formatage du style (CSS) de la cellule :
            if (debutMission != null) {
                if (debutPeriode.isBefore(debutMission)) {
                    getStyleClass().add("avantMission");
                    return;
                }
            }
            if (finMission != null) {
                if (finPeriode.isAfter(finMission.plusDays(7))) {
                    getStyleClass().add("apresMission");
                    return;
                }
            }
            getStyleClass().add("pendantMission");
        }
*/

}


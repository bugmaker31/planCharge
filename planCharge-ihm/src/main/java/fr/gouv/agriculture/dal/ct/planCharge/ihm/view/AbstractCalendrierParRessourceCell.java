package fr.gouv.agriculture.dal.ct.planCharge.ihm.view;

import fr.gouv.agriculture.dal.ct.ihm.model.BeanException;
import fr.gouv.agriculture.dal.ct.ihm.view.EditableAwareTextFieldTableCell;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.AbstractCalendrierParRessourceBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.charge.PlanChargeBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.referentiels.RessourceBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.referentiels.RessourceHumaineBean;
import javafx.scene.control.TableRow;
import javafx.util.StringConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.time.LocalDate;

public abstract class AbstractCalendrierParRessourceCell<R extends RessourceBean, S extends AbstractCalendrierParRessourceBean<R, ?, ?, ?>, T> extends EditableAwareTextFieldTableCell<S, T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractCalendrierParRessourceCell.class);

    @NotNull
    private PlanChargeBean planChargeBean;
    private int noSemaine;

    protected AbstractCalendrierParRessourceCell(@NotNull PlanChargeBean planChargeBean, int noSemaine, @NotNull StringConverter<T> stringConverter, @Null Runnable cantEditErrorDisplayer) {
        super(stringConverter, cantEditErrorDisplayer);
        this.planChargeBean = planChargeBean;
        this.noSemaine = noSemaine;
    }

    protected AbstractCalendrierParRessourceCell(@NotNull PlanChargeBean planChargeBean, int noSemaine, @NotNull StringConverter<T> stringConverter) {
        this(planChargeBean, noSemaine, stringConverter, null);
    }


    protected PlanChargeBean getPlanChargeBean() {
        return planChargeBean;
    }

    protected int getNoSemaine() {
        return noSemaine;
    }

    @Override
    public void updateItem(T item, boolean empty) {
        super.updateItem(item, empty);
        styler();
    }

    protected void styler() {

        // Réinit des style spécifiques :
        getStyleClass().removeAll("avantMission", "pendantMission", "apresMission");

            /* Non, surtout pas, sinon les cellules vides, ne seront pas stylées/décorées.
            // Stop, si cellule vide :
            if (empty || (item == null)) {
                return;
            }
            */

        // Récupération des infos sur la cellule :
        //noinspection unchecked
        TableRow<? extends S> tableRow = getTableRow();
        S bean = tableRow.getItem();
        if (bean == null) {
            return;
        }
        RessourceHumaineBean ressourceHumaineBean;
        try {
            if (!bean.getRessourceBean().estHumain()) {
                return;
            }
            ressourceHumaineBean = (RessourceHumaineBean) bean.getRessourceBean();
        } catch (BeanException e) {
            // TODO FDA 2017/09 Trouver mieux que juste loguer une erreur.
            LOGGER.error("Impossible de déterminer si la ressource '" + bean.getRessourceBean().getCode() + "' est humaine.", e);
            return;
        }
        LocalDate debutMission = ressourceHumaineBean.getDebutMission();
        LocalDate finMission = ressourceHumaineBean.getFinMission();

        if (planChargeBean.getDateEtat() == null) {
            return;
        }
        LocalDate debutPeriode = planChargeBean.getDateEtat().plusDays((noSemaine - 1) * 7); // TODO FDA 2017/06 [issue#26:PeriodeHebdo/Trim]
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

}

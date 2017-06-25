package fr.gouv.agriculture.dal.ct.planCharge.ihm.view;

import fr.gouv.agriculture.dal.ct.planCharge.ihm.IhmException;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.ModuleChargesController;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.PlanChargeBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.PlanificationBean;
import javafx.beans.property.DoubleProperty;
import javafx.scene.control.TableRow;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.DoubleStringConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Created by frederic.danna on 11/05/2017.
 *
 * @author frederic.danna
 */
public class PlanificationChargeCellFactory extends TextFieldTableCell<PlanificationBean, Double> {

    private static final Logger LOGGER = LoggerFactory.getLogger(PlanificationChargeCellFactory.class);


    private PlanChargeBean planChargeBean;
    private final int noPeriode;


    public PlanificationChargeCellFactory(@NotNull PlanChargeBean planChargeBean, @NotNull int noPeriode) {
        super();
        this.planChargeBean = planChargeBean;
        this.noPeriode = noPeriode;
        setConverter(new DoubleStringConverter()); // TODO FDA 2017/04 Mieux formater les charges ?
    }


    @Override
    public void updateItem(Double item, boolean empty) {
        super.updateItem(item, empty);

        // Réinit du texte et du style de la cellule :
        setText("");
        getStyleClass().remove("avantPeriodeDemandee");
        getStyleClass().remove("pendantPeriodeDemandee");
        getStyleClass().remove("apresPeriodeDemandee");

        // Stop, si cellule vide :
        if (empty) {
            return; // TODO FDA 2017/05 Confirmer.
        }

        // Récupération des infos sur la cellule :
        //noinspection unchecked
        TableRow<PlanificationBean> tableRow = this.getTableRow();
        PlanificationBean planifBean = tableRow.getItem();
        if (planifBean == null) {
            return;
        }
        LocalDate debutPeriode = planChargeBean.getDateEtat().plusDays((noPeriode - 1) * 7); // FIXME FDA 2017/06 Ne marche que quand les périodes sont des semaines, pas pour les trimestres.
        LocalDate finPeriode = debutPeriode.plusDays(7);// FIXME FDA 2017/06 Ne marche que quand les périodes sont des semaines, pas pour les trimestres.

        // Formatage du texte de la cellule :
        Double charge;
        try {
            DoubleProperty chargeProperty = planifBean.chargePlanifiee(debutPeriode);
            charge = chargeProperty.get();
        } catch (IhmException e) {
//            LOGGER.error("Impossible de déterminer la charge planifiée pour la tâche " + planifBean.noTache() + " sur la période qui commence le " + debutPeriode.format(DateTimeFormatter.ISO_LOCAL_DATE) + ".", e);
            // TODO FDA 2017/06 Gérer les périodes trimestrielles (en + des périodes hebdomadaires).
            charge = 0.0;
        }
        setText((charge == 0.0) ? "" : ModuleChargesController.FORMAT_CHARGE.format(charge));

        // Formatage du style (CSS) de la cellule :
        if (planifBean.getDebut() != null) {
            if (debutPeriode.isBefore(planifBean.getDebut())) {
                getStyleClass().add("avantPeriodeDemandee");
                return;
            }
        }
        if (planifBean.getEcheance() != null) {
            if (finPeriode.isAfter(planifBean.getEcheance().plusDays(7))) {
                getStyleClass().add("apresPeriodeDemandee");
                return;
            }
        }
        getStyleClass().add("pendantPeriodeDemandee");
    }
}
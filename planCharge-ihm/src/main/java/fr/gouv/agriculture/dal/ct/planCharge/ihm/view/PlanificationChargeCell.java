package fr.gouv.agriculture.dal.ct.planCharge.ihm.view;

import fr.gouv.agriculture.dal.ct.ihm.IhmException;
import fr.gouv.agriculture.dal.ct.ihm.controller.ControllerException;
import fr.gouv.agriculture.dal.ct.ihm.model.BeanException;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.PlanChargeIhm;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.ChargesController;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.charge.NbrsJoursParProfilBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.charge.NbrsJoursParRessourceBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.charge.PlanChargeBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.charge.PlanificationTacheBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.view.converter.Converters;
import fr.gouv.agriculture.dal.ct.planCharge.util.Collections;
import fr.gouv.agriculture.dal.ct.planCharge.util.Objects;
import javafx.beans.binding.DoubleExpression;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.FloatProperty;
import javafx.css.PseudoClass;
import javafx.scene.control.TableRow;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Created by frederic.danna on 11/05/2017.
 *
 * @author frederic.danna
 */
@SuppressWarnings("ClassHasNoToStringMethod")
public class PlanificationChargeCell extends TextFieldTableCell<PlanificationTacheBean, Double> {

    public static final PseudoClass AVANT_PERIODE_DEMANDEE = PseudoClass.getPseudoClass("avantPeriodeDemandee");
    public static final PseudoClass PENDANT_PERIODE_DEMANDEE = PseudoClass.getPseudoClass("pendantPeriodeDemandee");
    public static final PseudoClass APRES_PERIODE_DEMANDEE = PseudoClass.getPseudoClass("apresPeriodeDemandee");

    private static final PseudoClass SURCHARGE = ChargesController.SURCHARGE;

    @SuppressWarnings("unused")
    private static final Logger LOGGER = LoggerFactory.getLogger(PlanificationChargeCell.class);

    //    @Autowired
    @NotNull
    private final PlanChargeIhm ihm = PlanChargeIhm.instance();

    //    @Autowired
    @NotNull
    private final ChargesController chargesController = ChargesController.instance();

    //    Autowired
    @NotNull
    private final PlanChargeBean planChargeBean = PlanChargeBean.instance();


    private final int noPeriode;


    public PlanificationChargeCell(int noPeriode) {
        super(Converters.CHARGE_STRING_CONVERTER);
        this.noPeriode = noPeriode;

        definirTooltip();
    }

    private void definirTooltip() {
        setOnMouseEntered(event -> {
            PlanificationTacheBean planifBean = planificationTacheBean();
            if (planifBean == null) {
                return;
            }
            if ((planifBean.getRessource() == null) || (planifBean.getRessource().getCode() == null)) {
                return;
            }
            if (planChargeBean.getDateEtat() == null) {
                return;
            }
            LocalDate debutPeriode = planChargeBean.getDateEtat().plusDays(((long) noPeriode - 1L) * 7L); // TODO FDA 2017/06 [issue#26:PeriodeHebdo/Trim]
//            LocalDate finPeriode = debutPeriode.plusDays(7L);// TODO FDA 2017/06 [issue#26:PeriodeHebdo/Trim]
/*
            DoubleProperty chargePlanifieeProperty = planifBean.chargePlanifieePropertyOuNull(debutPeriode);
            if (chargePlanifieeProperty == null) {
                return;
            }
*/
            NbrsJoursParRessourceBean nbrJoursDispoRestantePourLaRessourceBean = Collections.any(
                    chargesController.getNbrsJoursDispoCTRestanteRsrcBeans(),
                    nbrsJoursDispoBean -> nbrsJoursDispoBean.getRessourceBean().equals(planifBean.getRessource())
            );
            if (nbrJoursDispoRestantePourLaRessourceBean == null) {
                return;
            }
            if (!nbrJoursDispoRestantePourLaRessourceBean.containsKey(debutPeriode)) {
                return;
            }
            FloatProperty nbrJoursDispoRestanteProperty = nbrJoursDispoRestantePourLaRessourceBean.get(debutPeriode);
            if (nbrJoursDispoRestanteProperty == null) {
                return;
            }
            float capaciteRestante = nbrJoursDispoRestanteProperty.get();
            String capaciteRestanteFormattee = Converters.FRACTION_JOURS_STRING_CONVERTER.toString(capaciteRestante);

            StringBuilder message = new StringBuilder("");
            message.append("Capacité restante");
            message.append(" de ").append(planifBean.getRessource().getCode());
            message.append(" sur [").append(debutPeriode.format(DateTimeFormatter.ISO_LOCAL_DATE)).append("..["); // TODO FDA 2017/06 [issue#26:PeriodeHebdo/Trim]
            message.append(" = ").append(capaciteRestanteFormattee).append("j");
            try {
                ihm.afficherPopup(this, "Capacité restante", message.toString());
            } catch (IhmException e) {
                LOGGER.error("Impossible d'afficher le tooltip.", e);
            }
        });
        setOnMouseExited(event -> {
            try {
                ihm.masquerPopup(this);
            } catch (IhmException e) {
                // Pas super-impactant pour l'utilisateur (la pop-up est bien masquée), donc un simple message d'erreur suffit, pas besoin d'aller jusqu'à thrower une exception.
                LOGGER.error("Impossible de masquer le tooltip.", e);
            }
        });
    }

    @NotNull
    public PlanificationTableView<PlanificationTacheBean, Double> getPlanificationTableView() {
        return (PlanificationTableView<PlanificationTacheBean, Double>) getTableView();
    }

    @Override
    public void updateItem(@Null Double item, boolean empty) {
        super.updateItem(item, empty);

//        formater();
        styler();
    }

    private void formater() {
        if (isEmpty() || (getItem() == null)) {
            setText(null);
            setGraphic(null);
            return;
        }

        //noinspection UnnecessaryLocalVariable
        Double charge = getItem();
        setText(getConverter().toString(charge));
    }

    private void styler() {

        // Réinit du texte et du style de la cellule :
        pseudoClassStateChanged(AVANT_PERIODE_DEMANDEE, false);
        pseudoClassStateChanged(PENDANT_PERIODE_DEMANDEE, false);
        pseudoClassStateChanged(APRES_PERIODE_DEMANDEE, false);
        pseudoClassStateChanged(SURCHARGE, false);

        // Stop, si cellule vide :
        if ((getItem() == null) || isEmpty()) {
            return;
        }

        PlanificationTacheBean planifBean = planificationTacheBean();
        if (planifBean == null) {
            return;
        }

        if (planChargeBean.getDateEtat() == null) {
            return;
        }
        LocalDate debutPeriode = planChargeBean.getDateEtat().plusDays(((long) noPeriode - 1L) * 7L); // TODO FDA 2017/06 [issue#26:PeriodeHebdo/Trim]
        LocalDate finPeriode = debutPeriode.plusDays(7L);// TODO FDA 2017/06 [issue#26:PeriodeHebdo/Trim]

        // Formatage du style (CSS) de la cellule :
        FORMAT_PERIODE:
        {
            if (planifBean.getDebut() != null) {
                if (debutPeriode.isBefore(planifBean.getDebut().minusDays(7L - 1L))) { // TODO FDA 2017/06 [issue#26:PeriodeHebdo/Trim]
                    pseudoClassStateChanged(AVANT_PERIODE_DEMANDEE, true);
                    break FORMAT_PERIODE;
                }
            }
            if (planifBean.getEcheance() != null) {
                if (finPeriode.isAfter(planifBean.getEcheance().plusDays(7L))) { // TODO FDA 2017/06 [issue#26:PeriodeHebdo/Trim]
                    pseudoClassStateChanged(APRES_PERIODE_DEMANDEE, true);
                    break FORMAT_PERIODE;
                }
            }
            pseudoClassStateChanged(PENDANT_PERIODE_DEMANDEE, true);
        }
        FORMAT_SURCHARGE:
        {
            if (getItem() == null) {
                break FORMAT_SURCHARGE;
            }
            if (getItem() == 0.0) {
                break FORMAT_SURCHARGE;
            }
            if (estRessourceSurchargeeSurPeriode(planifBean, debutPeriode)) {
                pseudoClassStateChanged(SURCHARGE, true);
                break FORMAT_SURCHARGE;
            }
            if (estProfilSurchargeeSurPeriode(planifBean, debutPeriode)) {
                pseudoClassStateChanged(SURCHARGE, true);
                //noinspection UnnecessaryBreak
                break FORMAT_SURCHARGE;
            }
        }
    }

    @Null
    private PlanificationTacheBean planificationTacheBean() {
        if (isEmpty() || (getItem() == null)) {
            return null;
        }
        // Récupération des infos sur la cellule :
        //noinspection unchecked
        TableRow<PlanificationTacheBean> tableRow = getTableRow();
        if (tableRow == null) { // Un clic sur le TableHeaderRow fait retourner null à getTableRow().
            return null;
        }
        PlanificationTacheBean planifBean = tableRow.getItem();
        return planifBean;
    }


    private boolean estRessourceSurchargeeSurPeriode(@NotNull PlanificationTacheBean planifBean, @NotNull LocalDate debutPeriode) {
        if ((planifBean.getTacheBean().getRessource() == null) || !planifBean.getTacheBean().getRessource().estHumain()) {
            return false;
        }
        NbrsJoursParRessourceBean nbrsJoursDispoRestanteBean = Collections.any(
                chargesController.getNbrsJoursDispoCTRestanteRsrcBeans(),
                nbrsJoursParRessourceBean -> nbrsJoursParRessourceBean.getRessourceBean().equals(planifBean.getTacheBean().getRessource())
        );
        if (nbrsJoursDispoRestanteBean == null) {
            return false;
        }
        FloatProperty nbrJoursDispoRestanteSurPeriodeProperty = nbrsJoursDispoRestanteBean.get(debutPeriode);
        if (nbrJoursDispoRestanteSurPeriodeProperty == null) {
            return false;
        }
        return nbrJoursDispoRestanteSurPeriodeProperty.get() < 0.0f; // TODO FDA 2017/09 Mettre cette RG dans la couche métier.
    }

    private boolean estProfilSurchargeeSurPeriode(@NotNull PlanificationTacheBean planifBean, @NotNull LocalDate debutPeriode) {
        if (planifBean.getTacheBean().getProfil() == null) {
            return false;
        }
        NbrsJoursParProfilBean nbrsJoursDispoRestanteBean = Collections.any(
                chargesController.getNbrsJoursDispoCTMaxRestanteProfilBeans(),
                nbrsJoursParRessourceBean -> nbrsJoursParRessourceBean.getProfilBean().equals(planifBean.getTacheBean().getProfil())
        );
        if (nbrsJoursDispoRestanteBean == null) {
            return false;
        }
        FloatProperty nbrJoursDispoRestanteSurPeriodeProperty = nbrsJoursDispoRestanteBean.get(debutPeriode);
        if (nbrJoursDispoRestanteSurPeriodeProperty == null) {
            return false;
        }
        return nbrJoursDispoRestanteSurPeriodeProperty.get() < 0.0f; // TODO FDA 2017/09 Mettre cette RG dans la couche métier.
    }

    @Override
    public void commitEdit(@Null Double newValue) {

        //noinspection unchecked
        PlanificationTacheBean planifBean = planificationTacheBean();
        if (planifBean == null) {
            LOGGER.error("Impossible de retrouver la planification de la tâche.");
            return;
        }

        Double chargePrecedente;
        {
            if (planChargeBean.getDateEtat() == null) {
                return;
            }
            LocalDate debutPeriode = planChargeBean.getDateEtat().plusDays(((long) noPeriode - 1L) * 7L); // TODO FDA 2017/06 [issue#26:PeriodeHebdo/Trim]
            LocalDate finPeriode = debutPeriode.plusDays(7L);// TODO FDA 2017/06 [issue#26:PeriodeHebdo/Trim]

            try {
                chargePrecedente = planifBean.chargePlanifiee(debutPeriode, finPeriode);
            } catch (BeanException e) {
                LOGGER.error("Impossible de retrouver la charge planifiée.", e);
                return;
            }
        }

        super.commitEdit(newValue);

        // Mathématiquement parlant, 'null' vaut zéro, donc pas toujours besoin de recalculer :
        if (!((Objects.value(chargePrecedente, 0.0) == 0.0) && (Objects.value(newValue, 0.0) == 0.0))) {
            planifBean.majChargePlanifieeTotale();
            getTableView().refresh(); // Pour que les styles CSS soient re-appliqués (notamment celui de la colonne "Charge".
        }
    }
}
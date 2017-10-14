package fr.gouv.agriculture.dal.ct.planCharge.ihm.view;

import fr.gouv.agriculture.dal.ct.ihm.IhmException;
import fr.gouv.agriculture.dal.ct.ihm.model.BeanException;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.PlanChargeIhm;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.ChargesController;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.charge.NbrsJoursParProfilBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.charge.NbrsJoursParRessourceBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.charge.PlanChargeBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.charge.PlanificationTacheBean;
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
        super(Converters.CHARGE_STRING_CONVERTER); // TODO FDA 2017/04 Mieux formater les charges ?
        this.noPeriode = noPeriode;

        if (ihm.estEnDeveloppement()) {
//            definirTooltip();
        }
    }

    private void definirTooltip() {
        addEventHandler(MouseEvent.MOUSE_ENTERED, event -> {
            StringBuilder message = new StringBuilder("");
            message.append("cell at col ").append(getId()).append(", line " + getIndex());
            message.append("\n");
            PlanificationTacheBean planifBean = planificationTacheBean();
            if (planifBean != null) {
                message.append(planifBean.getTacheBean().noTache())
                        .append(" : ")
                        .append(planifBean.getCharge())
                        .append(" vs ")
                        .append(planifBean.getChargePlanifieeTotale());
            }
            message.append("\n");
            if (planChargeBean.getDateEtat() != null) {
                LocalDate debutPeriode = planChargeBean.getDateEtat().plusDays(((long) noPeriode - 1L) * 7L); // TODO FDA 2017/06 [issue#26:PeriodeHebdo/Trim]
                LocalDate finPeriode = debutPeriode.plusDays(7L);// TODO FDA 2017/06 [issue#26:PeriodeHebdo/Trim]
                message.append(debutPeriode.format(DateTimeFormatter.ISO_LOCAL_DATE));
                if (planifBean != null) {
                    DoubleProperty chargePlanifieeProperty = planifBean.chargePlanifieePropertyOuNull(debutPeriode);
                    message.append(" : ")
                            .append(Objects.value(chargePlanifieeProperty, DoubleExpression::getValue, 0.0))
                            .append(" ")
                            .append("(").append(Objects.value(chargePlanifieeProperty, Objects::idInstance, "N/C")).append(")");
                }
            }
            try {
                ihm.afficherPopup(this, "Période n°" + noPeriode, message.toString());
            } catch (IhmException e) {
                LOGGER.error("Impossible d'afficher le tooltip.", e);
            }
        });
        addEventHandler(MouseEvent.MOUSE_EXITED, event -> {
            try {
                ihm.masquerPopup(this);
            } catch (IhmException e) {
                LOGGER.error("Impossible de masquer le tooltip.", e);
            }
        });
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
                chargePrecedente = planifBean.chargePlanifiee(debutPeriode/*, finPeriode*/);
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
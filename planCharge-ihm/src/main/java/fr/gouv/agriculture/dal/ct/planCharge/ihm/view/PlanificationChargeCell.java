package fr.gouv.agriculture.dal.ct.planCharge.ihm.view;

import fr.gouv.agriculture.dal.ct.ihm.IhmException;
import fr.gouv.agriculture.dal.ct.ihm.model.BeanException;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.PlanChargeIhm;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.ChargesController;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.DisponibilitesController;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.charge.NbrsJoursChargeParRessourceBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.charge.NbrsJoursChargeParProfilBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.charge.PlanChargeBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.charge.PlanificationTacheBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.disponibilite.NbrsJoursDispoParRessourceBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.referentiels.RessourceBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.view.converter.Converters;
import fr.gouv.agriculture.dal.ct.planCharge.util.Collections;
import fr.gouv.agriculture.dal.ct.planCharge.util.Objects;
import javafx.beans.property.FloatProperty;
import javafx.css.PseudoClass;
import javafx.scene.control.TableRow;
import javafx.scene.control.cell.TextFieldTableCell;
import org.slf4j.Logger;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.time.LocalDate;

import static org.slf4j.LoggerFactory.getLogger;

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
    private static final Logger LOGGER = getLogger(PlanificationChargeCell.class);

    //    @Autowired
    @NotNull
    private final PlanChargeIhm ihm = PlanChargeIhm.instance();

    //    @Autowired
    @NotNull
    private final DisponibilitesController disponibilitesController = DisponibilitesController.instance();

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

        getStyleClass().add("planificationCharge-cell");

        definirTooltip();
    }

    private void definirTooltip() {
        setOnMouseEntered(event -> {
//            LOGGER.trace("mouseEntered sur {}...", toString());
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
            //
            Float nbrJoursDispo = nbrJoursDispo(planifBean.getRessource(), debutPeriode);
            if (nbrJoursDispo == null) {
                return;
            }
            String nbrJoursDispoFormate = Converters.FRACTION_JOURS_STRING_CONVERTER.toString(nbrJoursDispo);
            //
            Float nbrJoursCharge = nbrJoursCharge(planifBean.getRessource(), debutPeriode);
            if (nbrJoursCharge == null) {
                return;
            }
            String nbrJoursChargeFormate = Converters.FRACTION_JOURS_STRING_CONVERTER.toString(nbrJoursCharge);
            //
            Float nbrJoursDispoCTRestante = nbrJoursDispoCTRestante(planifBean.getRessource(), debutPeriode);
            if (nbrJoursDispoCTRestante == null) {
                return;
            }
            String nbrJoursDispoCTRestanteFormate = Converters.FRACTION_JOURS_STRING_CONVERTER.toString(nbrJoursDispoCTRestante);

            StringBuilder message = new StringBuilder("");
            assert planifBean.getRessource() != null;
            message.append("Dispo. restante");
//            message.append(" de ").append(planifBean.getRessource().getCode());
//            message.append(" sur [").append(debutPeriode.format(DateTimeFormatter.ISO_LOCAL_DATE)).append("..["); // TODO FDA 2017/06 [issue#26:PeriodeHebdo/Trim]
            message.append(" = ").append(nbrJoursDispoFormate).append(" - ").append(nbrJoursChargeFormate);
            message.append(" = ").append(nbrJoursDispoCTRestanteFormate).append(" j");
            try {
                ihm.afficherPopup(this, "Capacité restante", message.toString());
                LOGGER.debug("Tooltip affiché pour cellule {}.", toString());
            } catch (IhmException e) {
                LOGGER.error("Impossible d'afficher le tooltip de la cellule " + toString() + ".", e);
            }

            event.consume(); // Utile ?
        });
        setOnMouseExited(event -> {
//            LOGGER.trace("mouseExited sur {}...", toString());
            try {
                ihm.masquerPopup(this);
                LOGGER.debug("Tooltip masqué pour cellule {}.", toString());
            } catch (IhmException ignored) {
                // Pas impactant du tout pour l'utilisateur (la pop-up est bien masquée), ni pour la stabilité de l'application,
                // donc un simple "warn" suffit, pas besoin d'aller jusqu'à thrower une exception, ni même une "error".
                //LOGGER.warn("Impossible de masquer le tooltip de la cellule " + toString() + ".", ignored);
                LOGGER.warn("Impossible de masquer le tooltip de la cellule {}.", toString());
            }

            event.consume(); // Utile ?
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


    @Null
    private Float nbrJoursDispo(@NotNull RessourceBean<?, ?> ressource, @NotNull LocalDate debutPeriode) {
        NbrsJoursDispoParRessourceBean nbrsJoursDispoPourRessourceBean = Collections.any(
                disponibilitesController.getNbrsJoursDispoCTBeans(),
                nbrsJoursDispoBean -> java.util.Objects.equals(nbrsJoursDispoBean.getRessourceBean(), ressource)
        );
        if (nbrsJoursDispoPourRessourceBean == null) {
            return null;
        }
        if (!nbrsJoursDispoPourRessourceBean.containsKey(debutPeriode)) {
            return null;
        }
        FloatProperty nbrJoursDispoPourRessourceProperty = nbrsJoursDispoPourRessourceBean.get(debutPeriode);
        if (nbrJoursDispoPourRessourceProperty == null) {
            return null;
        }
        float nbrJoursDispoPourRessource = nbrJoursDispoPourRessourceProperty.get();
        return nbrJoursDispoPourRessource;
    }

    @Null
    private Float nbrJoursCharge(@NotNull RessourceBean<?, ?> ressource, @NotNull LocalDate debutPeriode) {
        NbrsJoursChargeParRessourceBean nbrsJoursChargePourRessourceBean = Collections.any(
                chargesController.getNbrsJoursChargeRsrcBeans(),
                nbrJoursChargeBean -> java.util.Objects.equals(nbrJoursChargeBean.getRessourceBean(), ressource)
        );
        if (nbrsJoursChargePourRessourceBean == null) {
            return null;
        }
        if (!nbrsJoursChargePourRessourceBean.containsKey(debutPeriode)) {
            return null;
        }
        FloatProperty nbrJoursChargePourRessourceProperty = nbrsJoursChargePourRessourceBean.get(debutPeriode);
        if (nbrJoursChargePourRessourceProperty == null) {
            return null;
        }
        float nbrJoursChargePourRessource = nbrJoursChargePourRessourceProperty.get();
        return nbrJoursChargePourRessource;
    }

    @Null
    private Float nbrJoursDispoCTRestante(@NotNull RessourceBean<?, ?> ressource, @NotNull LocalDate debutPeriode) {
        NbrsJoursChargeParRessourceBean nbrJoursDispoRestantePourRessourceBean = Collections.any(
                chargesController.getNbrsJoursDispoCTRestanteRsrcBeans(),
                nbrsJoursDispoBean -> java.util.Objects.equals(nbrsJoursDispoBean.getRessourceBean(), ressource)
        );
        if (nbrJoursDispoRestantePourRessourceBean == null) {
            return null;
        }
        if (!nbrJoursDispoRestantePourRessourceBean.containsKey(debutPeriode)) {
            return null;
        }
        FloatProperty nbrJoursDispoRestantePourRessourceProperty = nbrJoursDispoRestantePourRessourceBean.get(debutPeriode);
        if (nbrJoursDispoRestantePourRessourceProperty == null) {
            return null;
        }
        float nbrJoursDispoCTRestantePourRessource = nbrJoursDispoRestantePourRessourceProperty.get();
        return nbrJoursDispoCTRestantePourRessource;
    }


    private boolean estRessourceSurchargeeSurPeriode(@NotNull PlanificationTacheBean planifBean, @NotNull LocalDate debutPeriode) {
        if ((planifBean.getTacheBean().getRessource() == null) || !planifBean.getTacheBean().getRessource().estHumain()) {
            return false;
        }
        Float nbrJoursDispoCTRestante = nbrJoursDispoCTRestante(planifBean.getTacheBean().getRessource(), debutPeriode);
        if (nbrJoursDispoCTRestante == null) {
            return false;
        }
        return nbrJoursDispoCTRestante < 0.0f; // TODO FDA 2017/09 Mettre cette RG dans la couche métier.
    }

    private boolean estProfilSurchargeeSurPeriode(@NotNull PlanificationTacheBean planifBean, @NotNull LocalDate debutPeriode) {
        if (planifBean.getTacheBean().getProfil() == null) {
            return false;
        }
        NbrsJoursChargeParProfilBean nbrsJoursDispoRestanteBean = Collections.any(
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

    @Override
    public String toString() {
        PlanificationTacheBean planifTacheBean = planificationTacheBean();
        return "Période n°" + noPeriode
                + " / " + Objects.value(planifTacheBean, planifBean -> planifBean.getId(), "N/C")
                + " : " + Objects.value(getItem(), "-") + "j";
    }
}
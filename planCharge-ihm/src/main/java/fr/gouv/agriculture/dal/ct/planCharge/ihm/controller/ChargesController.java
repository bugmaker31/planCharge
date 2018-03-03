package fr.gouv.agriculture.dal.ct.planCharge.ihm.controller;

import com.sun.javafx.scene.control.skin.TableColumnHeader;
import com.sun.javafx.scene.control.skin.TableHeaderRow;
import fr.gouv.agriculture.dal.ct.ihm.IhmException;
import fr.gouv.agriculture.dal.ct.ihm.controller.ControllerException;
import fr.gouv.agriculture.dal.ct.ihm.controller.calculateur.Calculateur;
import fr.gouv.agriculture.dal.ct.ihm.model.BeanException;
import fr.gouv.agriculture.dal.ct.ihm.module.Module;
import fr.gouv.agriculture.dal.ct.ihm.util.ObservableLists;
import fr.gouv.agriculture.dal.ct.ihm.view.NotEditableTextFieldTableCell;
import fr.gouv.agriculture.dal.ct.ihm.view.TableViews;
import fr.gouv.agriculture.dal.ct.metier.service.ServiceException;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.PlanChargeIhm;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.calculateur.CalculateurCharges;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.suiviActionsUtilisateur.ActionUtilisateur;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.suiviActionsUtilisateur.AffichageModuleCharges;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.suiviActionsUtilisateur.SuiviActionsUtilisateurException;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.suiviActionsUtilisateur.suiviActionsUtilisateurSurTache.AjoutTache;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.charge.*;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.referentiels.ProfilBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.referentiels.RessourceBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.referentiels.StatutBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.tache.TacheBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.view.*;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.view.converter.Converters;
import fr.gouv.agriculture.dal.ct.planCharge.metier.constante.TypeChangement;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dto.PlanificationsDTO;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.charge.Planifications;
import fr.gouv.agriculture.dal.ct.planCharge.metier.service.ChargeService;
import fr.gouv.agriculture.dal.ct.planCharge.util.Collections;
import fr.gouv.agriculture.dal.ct.planCharge.util.Dates;
import fr.gouv.agriculture.dal.ct.planCharge.util.Exceptions;
import fr.gouv.agriculture.dal.ct.planCharge.util.Objects;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Created by frederic.danna on 26/03/2017.
 *
 * @author frederic.danna
 */
@SuppressWarnings({"ClassHasNoToStringMethod", "ClassWithTooManyFields", "OverlyComplexClass", "AnonymousInnerClass"})
public class ChargesController extends AbstractTachesController<PlanificationTacheBean> implements Module {

    public static final PseudoClass CHARGE_NON_PLANIFIEE = PseudoClass.getPseudoClass("chargeNonPlanifiee");
    public static final PseudoClass INCOHERENCE = PseudoClass.getPseudoClass("incoherence");
    public static final PseudoClass SURCHARGE = PseudoClass.getPseudoClass("surcharge");
    public static final PseudoClass ECHEANCE_NON_TENUE = PseudoClass.getPseudoClass("echeanceNonTenue");

    public static final DecimalFormat FORMAT_CHARGE = new DecimalFormat("0", DecimalFormatSymbols.getInstance());

    static {
        FORMAT_CHARGE.setMinimumFractionDigits(0); // Les divisions de nbrs entiers (charges / jour) par 8 (heures) tombent parfois juste (pas de décimale).
        FORMAT_CHARGE.setMaximumFractionDigits(3); // Les divisions de nbrs entiers (charges / jour) par 8 (heures) se terminent par ".125", "0.25", ".325", ".5", ".625", ".75" ou ".825".
    }


    private static final Logger LOGGER = LoggerFactory.getLogger(ChargesController.class);

    private static ChargesController instance;

    public static ChargesController instance() {
        return instance;
    }


    /*
     La couche métier :
      */

    //    @Autowired
    @NotNull
    // 'final' pour éviter que quiconque resette cette variable.
    private final ChargeService planChargeService = ChargeService.instance();

    //    @Autowired
    @NotNull
    // 'final' pour éviter que quiconque resette cette variable.
    private final PlanChargeBean planChargeBean = PlanChargeBean.instance();

    @NotNull
    // 'final' pour éviter que quiconque resette cette liste et ne détruise les listeners enregistrés dessus.
    private final ObservableList<PlanificationTacheBean> planificationsBeans = planChargeBean.getPlanificationsBeans();

    @NotNull
    // 'final' pour éviter que quiconque resette cette liste et ne détruise les listeners enregistrés dessus.
    private final ObservableList<NbrsJoursChargeParRessourceBean> nbrsJoursChargeRsrcBeans = FXCollections.observableArrayList();

    @NotNull
    // 'final' pour éviter que quiconque resette cette liste et ne détruise les listeners enregistrés dessus.
    private final ObservableList<NbrsJoursChargeParProfilBean> nbrsJoursChargeProfilBeans = FXCollections.observableArrayList();

    @NotNull
    // 'final' pour éviter que quiconque resette cette liste et ne détruise les listeners enregistrés dessus.
    private final ObservableList<NbrsJoursChargeParRessourceBean> nbrsJoursDispoCTRestanteRsrcBeans = FXCollections.observableArrayList();

    @NotNull
    // 'final' pour éviter que quiconque resette cette liste et ne détruise les listeners enregistrés dessus.
    private final ObservableList<TotauxNbrsJoursChargeBean> totauxNbrsJoursDispoCTRestanteRsrcBeans = FXCollections.observableArrayList();

    @NotNull
    // 'final' pour éviter que quiconque resette cette liste et ne détruise les listeners enregistrés dessus.
    private final ObservableList<NbrsJoursChargeParProfilBean> nbrsJoursDispoCTMaxRestanteProfilBeans = FXCollections.observableArrayList();

    @NotNull
    // 'final' pour éviter que quiconque resette cette liste et ne détruise les listeners enregistrés dessus.
    private final ObservableList<TotauxNbrsJoursChargeBean> totauxNbrsJoursDispoCTMaxRestanteProfilBeans = FXCollections.observableArrayList();

    /*
     La couche "View" :
      */

    @Override
    public Node getView() {
        return ihm.getChargesView();
    }

    @NotNull
    @Override
    public ActionUtilisateur actionUtilisateurAffichageModule(@Null Module modulePrecedent) {
        return new AffichageModuleCharges(modulePrecedent);
    }

    /*
    //    @Autowired
    @NotNull
    private PlanChargeIhm ihm = PlanChargeIhm.instance();
*/

    // Les paramètres (TabedPane "Paramètres") :

/*
    @FXML
    @NotNull
    @SuppressWarnings("NullableProblems")
    private TextField nbrLignesTablePlanificationsField;
*/

    /*
        @FXML
        @NotNull
        @SuppressWarnings("NullableProblems")
        private Spinner<Integer> nbrLignesMaxTablePlanificationsSpinner;
    */
    @FXML
    @NotNull
    @SuppressWarnings("NullableProblems")
    private Slider nbrLignesMaxTablePlanificationsSlider;

    // Les filtres (TabedPane "Filtres")) :
    // Ajouter ici les filtres spécifiques des charges : Charge planifiée, Charge  planifiée dans le mois, Planifiée dans le mois ?, Tâche doublon ?, Reste à planifier, N° sem échéance, Échéance tenue ?, Durée restante, Charge / semaine, Charge / T

    /*
        @SuppressWarnings("NullableProblems")
        @NotNull
        @FXML
        private TitledPane planChargePane;

        @SuppressWarnings("NullableProblems")
        @NotNull
        @FXML
        private VBox planificationsTableVBox;
    */
/*
    @SuppressWarnings("NullableProblems")
    @NotNull
    @FXML
    private ToggleButton filtrePlanifToutToggleButton;
*/
    @SuppressWarnings("NullableProblems")
    @NotNull
    @FXML
    private ToggleButton filtrePlanifChargeToutToggleButton;
    @SuppressWarnings("NullableProblems")
    @NotNull
    @FXML
    private ToggleButton filtrePlanifInfChargeToggleButton;
    @SuppressWarnings("NullableProblems")
    @NotNull
    @FXML
    private ToggleButton filtrePlanifSupChargeToggleButton;
    @SuppressWarnings("NullableProblems")
    @NotNull
    @FXML
    private ToggleButton filtrePlanifDansMoisToggleButton;
    @SuppressWarnings("NullableProblems")
    @NotNull
    @FXML
    private ToggleButton filtreEcheanceTouteToggleButton;
    @SuppressWarnings("NullableProblems")
    @NotNull
    @FXML
    private ToggleButton filtreEcheanceTenueToggleButton;
    @SuppressWarnings("NullableProblems")
    @NotNull
    @FXML
    private ToggleButton filtreEcheanceNonTenueToggleButton;
    @SuppressWarnings("NullableProblems")
    @NotNull
    @FXML
    private ToggleButton filtreSurchargeRessourceToggleButton;
    @SuppressWarnings("NullableProblems")
    @NotNull
    @FXML
    private ToggleButton filtreSurchargeProfilToggleButton;

    // Les TableView :

    @SuppressWarnings("NullableProblems")
    @NotNull
    @FXML
    private PlanificationTableView<PlanificationTacheBean, Double> planificationsTable;
    /*
        @FXML
        @SuppressWarnings("NullableProblems")
        @NotNull
        private TableColumn<PlanificationTacheBean, ?> tacheColumn;
    */
    // Les colonnes spécifiques du calendrier des tâches :
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<PlanificationTacheBean, Double> semaine1Column;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<PlanificationTacheBean, Double> semaine2Column;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<PlanificationTacheBean, Double> semaine3Column;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<PlanificationTacheBean, Double> semaine4Column;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<PlanificationTacheBean, Double> semaine5Column;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<PlanificationTacheBean, Double> semaine6Column;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<PlanificationTacheBean, Double> semaine7Column;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<PlanificationTacheBean, Double> semaine8Column;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<PlanificationTacheBean, Double> semaine9Column;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<PlanificationTacheBean, Double> semaine10Column;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<PlanificationTacheBean, Double> semaine11Column;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<PlanificationTacheBean, Double> semaine12Column;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<PlanificationTacheBean, Double> chargePlanifieeTotaleColumn;

    @SuppressWarnings("NullableProblems")
    @FXML
    @NotNull
    private PlanificationTableView<NbrsJoursChargeParRessourceBean, Float> nbrsJoursChargeRsrcTable;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursChargeParRessourceBean, ?> espaceNbrsJoursChargeRsrcColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursChargeParRessourceBean, String> ressourceNbrsJoursChargeRsrcColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursChargeParRessourceBean, String> profilNbrsJoursChargeRsrcColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursChargeParRessourceBean, Float> semaine1NbrsJoursChargeRsrcColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursChargeParRessourceBean, Float> semaine2NbrsJoursChargeRsrcColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursChargeParRessourceBean, Float> semaine3NbrsJoursChargeRsrcColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursChargeParRessourceBean, Float> semaine4NbrsJoursChargeRsrcColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursChargeParRessourceBean, Float> semaine5NbrsJoursChargeRsrcColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursChargeParRessourceBean, Float> semaine6NbrsJoursChargeRsrcColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursChargeParRessourceBean, Float> semaine7NbrsJoursChargeRsrcColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursChargeParRessourceBean, Float> semaine8NbrsJoursChargeRsrcColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursChargeParRessourceBean, Float> semaine9NbrsJoursChargeRsrcColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursChargeParRessourceBean, Float> semaine10NbrsJoursChargeRsrcColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursChargeParRessourceBean, Float> semaine11NbrsJoursChargeRsrcColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursChargeParRessourceBean, Float> semaine12NbrsJoursChargeRsrcColumn;

    @SuppressWarnings("NullableProblems")
    @FXML
    @NotNull
    private PlanificationTableView<NbrsJoursChargeParProfilBean, Float> nbrsJoursChargeProfilTable;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursChargeParProfilBean, ?> espaceNbrsJoursChargeProfilColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursChargeParProfilBean, String> ressourceNbrsJoursChargeProfilColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursChargeParProfilBean, String> profilNbrsJoursChargeProfilColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursChargeParProfilBean, Float> semaine1NbrsJoursChargeProfilColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursChargeParProfilBean, Float> semaine2NbrsJoursChargeProfilColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursChargeParProfilBean, Float> semaine3NbrsJoursChargeProfilColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursChargeParProfilBean, Float> semaine4NbrsJoursChargeProfilColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursChargeParProfilBean, Float> semaine5NbrsJoursChargeProfilColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursChargeParProfilBean, Float> semaine6NbrsJoursChargeProfilColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursChargeParProfilBean, Float> semaine7NbrsJoursChargeProfilColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursChargeParProfilBean, Float> semaine8NbrsJoursChargeProfilColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursChargeParProfilBean, Float> semaine9NbrsJoursChargeProfilColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursChargeParProfilBean, Float> semaine10NbrsJoursChargeProfilColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursChargeParProfilBean, Float> semaine11NbrsJoursChargeProfilColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursChargeParProfilBean, Float> semaine12NbrsJoursChargeProfilColumn;

    @SuppressWarnings("NullableProblems")
    @FXML
    @NotNull
    private PlanificationTableView<NbrsJoursChargeParRessourceBean, Float> nbrsJoursDispoCTRestanteRsrcTable;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursChargeParRessourceBean, ?> espaceNbrsJoursDispoCTRestanteRsrcColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursChargeParRessourceBean, String> ressourceNbrsJoursDispoCTRestanteRsrcColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursChargeParRessourceBean, String> profilNbrsJoursDispoCTRestanteRsrcColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursChargeParRessourceBean, Float> semaine1NbrsJoursDispoCTRestanteRsrcColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursChargeParRessourceBean, Float> semaine2NbrsJoursDispoCTRestanteRsrcColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursChargeParRessourceBean, Float> semaine3NbrsJoursDispoCTRestanteRsrcColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursChargeParRessourceBean, Float> semaine4NbrsJoursDispoCTRestanteRsrcColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursChargeParRessourceBean, Float> semaine5NbrsJoursDispoCTRestanteRsrcColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursChargeParRessourceBean, Float> semaine6NbrsJoursDispoCTRestanteRsrcColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursChargeParRessourceBean, Float> semaine7NbrsJoursDispoCTRestanteRsrcColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursChargeParRessourceBean, Float> semaine8NbrsJoursDispoCTRestanteRsrcColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursChargeParRessourceBean, Float> semaine9NbrsJoursDispoCTRestanteRsrcColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursChargeParRessourceBean, Float> semaine10NbrsJoursDispoCTRestanteRsrcColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursChargeParRessourceBean, Float> semaine11NbrsJoursDispoCTRestanteRsrcColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursChargeParRessourceBean, Float> semaine12NbrsJoursDispoCTRestanteRsrcColumn;

    @SuppressWarnings("NullableProblems")
    @FXML
    @NotNull
    private PlanificationTableView<TotauxNbrsJoursChargeBean, Float> totauxNbrsJoursDispoCTRestanteRsrcTable;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<TotauxNbrsJoursChargeBean, String> espaceTotauxNbrsJoursDispoCTRestanteRsrcColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<TotauxNbrsJoursChargeBean, Float> semaine1TotauxNbrsJoursDispoCTRestanteRsrcColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<TotauxNbrsJoursChargeBean, Float> semaine2TotauxNbrsJoursDispoCTRestanteRsrcColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<TotauxNbrsJoursChargeBean, Float> semaine3TotauxNbrsJoursDispoCTRestanteRsrcColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<TotauxNbrsJoursChargeBean, Float> semaine4TotauxNbrsJoursDispoCTRestanteRsrcColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<TotauxNbrsJoursChargeBean, Float> semaine5TotauxNbrsJoursDispoCTRestanteRsrcColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<TotauxNbrsJoursChargeBean, Float> semaine6TotauxNbrsJoursDispoCTRestanteRsrcColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<TotauxNbrsJoursChargeBean, Float> semaine7TotauxNbrsJoursDispoCTRestanteRsrcColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<TotauxNbrsJoursChargeBean, Float> semaine8TotauxNbrsJoursDispoCTRestanteRsrcColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<TotauxNbrsJoursChargeBean, Float> semaine9TotauxNbrsJoursDispoCTRestanteRsrcColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<TotauxNbrsJoursChargeBean, Float> semaine10TotauxNbrsJoursDispoCTRestanteRsrcColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<TotauxNbrsJoursChargeBean, Float> semaine11TotauxNbrsJoursDispoCTRestanteRsrcColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<TotauxNbrsJoursChargeBean, Float> semaine12TotauxNbrsJoursDispoCTRestanteRsrcColumn;

    @SuppressWarnings("NullableProblems")
    @FXML
    @NotNull
    private PlanificationTableView<NbrsJoursChargeParProfilBean, Float> nbrsJoursDispoCTMaxRestanteProfilTable;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursChargeParProfilBean, ?> espaceNbrsJoursDispoCTMaxRestanteProfilColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursChargeParProfilBean, String> ressourceNbrsJoursDispoCTMaxRestanteProfilColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursChargeParProfilBean, String> profilNbrsJoursDispoCTMaxRestanteProfilColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursChargeParProfilBean, Float> semaine1NbrsJoursDispoCTMaxRestanteProfilColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursChargeParProfilBean, Float> semaine2NbrsJoursDispoCTMaxRestanteProfilColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursChargeParProfilBean, Float> semaine3NbrsJoursDispoCTMaxRestanteProfilColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursChargeParProfilBean, Float> semaine4NbrsJoursDispoCTMaxRestanteProfilColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursChargeParProfilBean, Float> semaine5NbrsJoursDispoCTMaxRestanteProfilColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursChargeParProfilBean, Float> semaine6NbrsJoursDispoCTMaxRestanteProfilColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursChargeParProfilBean, Float> semaine7NbrsJoursDispoCTMaxRestanteProfilColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursChargeParProfilBean, Float> semaine8NbrsJoursDispoCTMaxRestanteProfilColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursChargeParProfilBean, Float> semaine9NbrsJoursDispoCTMaxRestanteProfilColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursChargeParProfilBean, Float> semaine10NbrsJoursDispoCTMaxRestanteProfilColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursChargeParProfilBean, Float> semaine11NbrsJoursDispoCTMaxRestanteProfilColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursChargeParProfilBean, Float> semaine12NbrsJoursDispoCTMaxRestanteProfilColumn;


    @SuppressWarnings("NullableProblems")
    @FXML
    @NotNull
    private PlanificationTableView<TotauxNbrsJoursChargeBean, Float> totauxNbrsJoursDispoCTMaxRestanteProfilTable;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<TotauxNbrsJoursChargeBean, String> espaceTotauxNbrsJoursDispoCTMaxRestanteProfilColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<TotauxNbrsJoursChargeBean, Float> semaine1TotauxNbrsJoursDispoCTMaxRestanteProfilColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<TotauxNbrsJoursChargeBean, Float> semaine2TotauxNbrsJoursDispoCTMaxRestanteProfilColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<TotauxNbrsJoursChargeBean, Float> semaine3TotauxNbrsJoursDispoCTMaxRestanteProfilColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<TotauxNbrsJoursChargeBean, Float> semaine4TotauxNbrsJoursDispoCTMaxRestanteProfilColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<TotauxNbrsJoursChargeBean, Float> semaine5TotauxNbrsJoursDispoCTMaxRestanteProfilColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<TotauxNbrsJoursChargeBean, Float> semaine6TotauxNbrsJoursDispoCTMaxRestanteProfilColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<TotauxNbrsJoursChargeBean, Float> semaine7TotauxNbrsJoursDispoCTMaxRestanteProfilColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<TotauxNbrsJoursChargeBean, Float> semaine8TotauxNbrsJoursDispoCTMaxRestanteProfilColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<TotauxNbrsJoursChargeBean, Float> semaine9TotauxNbrsJoursDispoCTMaxRestanteProfilColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<TotauxNbrsJoursChargeBean, Float> semaine10TotauxNbrsJoursDispoCTMaxRestanteProfilColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<TotauxNbrsJoursChargeBean, Float> semaine11TotauxNbrsJoursDispoCTMaxRestanteProfilColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<TotauxNbrsJoursChargeBean, Float> semaine12TotauxNbrsJoursDispoCTMaxRestanteProfilColumn;

    /*
    La couche "Controller" :
     */

    //    @Autowired
    @NotNull
    private final CalculateurCharges calculateurCharges = CalculateurCharges.instance();


    // Constructors:

    /**
     * The constructor.
     * The constructor is called before the initialize() method.
     */
    public ChargesController() throws ControllerException {
        super();
        if (instance != null) {
            throw new ControllerException("Instanciation à plus d'1 exemplaire.");
        }
        instance = this;
    }


    // Getters/Setters:

    @NotNull
    @SuppressWarnings("SuspiciousGetterSetter")
    @Override
    ObservableList<PlanificationTacheBean> getTachesBeans() {
        return planificationsBeans;
    }

    @NotNull
    @SuppressWarnings("SuspiciousGetterSetter")
    @Override
    PlanificationTableView<PlanificationTacheBean, Double> getTachesTable() {
        return planificationsTable;
    }

    @NotNull
    public ObservableList<NbrsJoursChargeParRessourceBean> getNbrsJoursChargeRsrcBeans() {
        return nbrsJoursChargeRsrcBeans;
    }

    @NotNull
    public ObservableList<NbrsJoursChargeParProfilBean> getNbrsJoursChargeProfilBeans() {
        return nbrsJoursChargeProfilBeans;
    }

    @NotNull
    public ObservableList<NbrsJoursChargeParRessourceBean> getNbrsJoursDispoCTRestanteRsrcBeans() {
        return nbrsJoursDispoCTRestanteRsrcBeans;
    }

    @NotNull
    public ObservableList<TotauxNbrsJoursChargeBean> getTotauxNbrsJoursDispoCTRestanteRsrcBeans() {
        return totauxNbrsJoursDispoCTRestanteRsrcBeans;
    }

    @NotNull
    public ObservableList<NbrsJoursChargeParProfilBean> getNbrsJoursDispoCTMaxRestanteProfilBeans() {
        return nbrsJoursDispoCTMaxRestanteProfilBeans;
    }

    @NotNull
    public ObservableList<TotauxNbrsJoursChargeBean> getTotauxNbrsJoursDispoCTMaxRestanteProfilBeans() {
        return totauxNbrsJoursDispoCTMaxRestanteProfilBeans;
    }


    // Module

    @Override
    public String getTitre() {
        return "Charge";
    }


    // Methods:

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @SuppressWarnings("OverlyLongMethod")
    @FXML
    @Override
    protected void initialize() throws ControllerException {
        try {
            Calculateur.executerSansCalculer(() -> {

                super.initialize();

                initBeans();
                initTables();

                // Gestion des undo/redo :
                initSuiviActionsUtilisateur();
            });
        } catch (IhmException e) {
            throw new ControllerException("Impossible d'initialiser le contrôleur.", e);
        }
    }


/* Le menu contextuel est défini dans le fichier FXML.
    void definirMenuContextuel() {
        super.definirMenuContextuel();

        if (menuVoirTache == null) {
            menuVoirTache = new MenuItem("Voir le détail de la tâche");
            menuVoirTache.setOnAction(event -> afficherTache());
        }
        if (!tachesTableContextMenu.getItems().contains(menuVoirTache)) {
            tachesTableContextMenu.getItems().add(menuVoirTache);
        }

    }
*/

    private void initBeans() {
        initBeansPlanifications();
        initBeansNbrsJoursChargeRsrc();
        initBeansNbrsJoursChargeProfil();
        initBeansNbrsJoursDispoCTRestanteRsrc();
        initBeansTotauxNbrsJoursDispoCTRestanteRsrc();
        initBeansNbrsJoursDispoCTMaxRestanteProfil();
        initBeansTotauxNbrsJoursDispoCTMaxRestanteProfil();

/* TODO FDA 2017/10 Calculer les charges pour la RH ajoutée.
        planChargeBean.getRessourcesHumainesBeans().addListener((ListChangeListener<? super RessourceHumaineBean>) change -> {
            Set<RessourceHumaineBean> ressourcesHumainesARecalculer = new HashSet<>(10);
            while (change.next()) {
                if (change.wasAdded()) {
                    ressourcesHumainesARecalculer.addAll(change.getAddedSubList());
                }
                // TODO FDA 2017/08 Coder les autres changements (permutations, etc. ?).
            }
            if (!ressourcesHumainesARecalculer.isEmpty()) {
                for (RessourceHumaineBean ressourceHumaineBean : ressourcesHumainesARecalculer) {

                    ObjectProperty<LocalDate> debutMissionProperty = ressourceHumaineBean.debutMissionProperty();
                    debutMissionProperty.addListener((observable, oldValue, newValue) -> {
                        try {
                            calculateurCharges.calculer(ressourceHumaineBean);
                        } catch (ControllerException e) {
                            //noinspection DuplicateStringLiteralInspection
                            LOGGER.error("Impossible de calculer les charges de la ressource " + ressourceHumaineBean.getTrigramme() + " en fonction de sa date de début de mission.", e); // TODO FDA 2017/08 Trouver mieux que juste loguer une erreur.
                        }
                    });

                    ObjectProperty<LocalDate> finMissionProperty = ressourceHumaineBean.finMissionProperty();
                    finMissionProperty.addListener((observable, oldValue, newValue) -> {
                        try {
                            calculateurCharges.calculer(ressourceHumaineBean);
                        } catch (ControllerException e) {
                            //noinspection DuplicateStringLiteralInspection
                            LOGGER.error("Impossible de calculer les charges de la ressource " + ressourceHumaineBean.getTrigramme() + " en fonction de sa date de fin de mission.", e); // TODO FDA 2017/08 Trouver mieux que juste loguer une erreur.
                        }
                    });
                }
            }
        });
*/
    }

    private void initBeansPlanifications() {
        // Rien de plus... pour l'instant.
    }

    private void initBeansNbrsJoursChargeRsrc() {
        ObservableLists.Binding binding = new ObservableLists.Binding("ressourcesBeans", "nbrsJoursChargeRsrcBeans");
        binding.ensureContains(
                planChargeBean.getRessourcesBeans(),
                ressourceBean -> new NbrsJoursChargeParRessourceBean(ressourceBean, new TreeMap<>()), // TreeMap au lieu de HashMap pour trier, juste afin de faciliter le débogage.
                nbrsJoursChargeRsrcBeans
        );
    }

    private void initBeansNbrsJoursChargeProfil() {
        ObservableLists.Binding binding = new ObservableLists.Binding("profilsBeans", "nbrsJoursChargeProfilBeans");
        binding.ensureContains(
                planChargeBean.getProfilsBeans(),
                profilBean -> new NbrsJoursChargeParProfilBean(profilBean, new TreeMap<>()), // TreeMap au lieu de HashMap pour trier, juste afin de faciliter le débogage.
                nbrsJoursChargeProfilBeans
        );
    }

    private void initBeansNbrsJoursDispoCTRestanteRsrc() {
        ObservableLists.Binding binding = new ObservableLists.Binding("ressourcesBeans", "nbrsJoursDispoCTRestanteRsrcBeans");
        binding.ensureContains(
                planChargeBean.getRessourcesBeans(),
                ressourceHumaineBean -> new NbrsJoursChargeParRessourceBean(ressourceHumaineBean, new TreeMap<>()), // TreeMap au lieu de HashMap pour trier, juste afin de faciliter le débogage.
                nbrsJoursDispoCTRestanteRsrcBeans
        );
    }

    private void initBeansTotauxNbrsJoursDispoCTRestanteRsrc() {

        TotauxNbrsJoursChargeBean totauxNbrsJoursBean = new TotauxNbrsJoursChargeBean();
        totauxNbrsJoursDispoCTRestanteRsrcBeans.add(totauxNbrsJoursBean);

/* Sans effet. Implémenté dans CalculateurCharges#calculerTotalNbrsJoursDispoCTRestanteParRessource.
        nbrsJoursDispoCTRestanteRsrcBeans.addListener((ListChangeListener<? super NbrsJoursChargeParRessourceBean>) c -> {
            while (c.next()) {
                // Rien, juste pour consommer les changements.
            }
            nbrsJoursDispoCTRestanteRsrcBeans.parallelStream()
                    .forEach(nbrsJoursParRessourceBean -> {
                        nbrsJoursParRessourceBean.keySet().parallelStream()
                                .forEach(debutPeriode -> {
                                    double totalChargePourRessourceSurPeriode = nbrsJoursParRessourceBean.values().parallelStream()
                                            .mapToDouble(charge -> charge.getValue().doubleValue())
                                            .sum();
                                    if (!totauxNbrsJoursBean.containsKey(debutPeriode)) {
                                        totauxNbrsJoursBean.put(debutPeriode, new SimpleFloatProperty());
                                    }
                                    totauxNbrsJoursBean.get(debutPeriode).setValue(totalChargePourRessourceSurPeriode);
                                });
                    });
        });
*/
    }

    private void initBeansNbrsJoursDispoCTMaxRestanteProfil() {
        ObservableLists.Binding binding = new ObservableLists.Binding("profilsBeans", "nbrsJoursDispoCTMaxRestanteProfilBeans");
        binding.ensureContains(
                planChargeBean.getProfilsBeans(),
                profilBean -> new NbrsJoursChargeParProfilBean(profilBean, new TreeMap<>()), // TreeMap au lieu de HashMap pour trier, juste afin de faciliter le débogage.
                nbrsJoursDispoCTMaxRestanteProfilBeans
        );
    }

    private void initBeansTotauxNbrsJoursDispoCTMaxRestanteProfil() {
        TotauxNbrsJoursChargeBean totauxNbrsJoursBean = new TotauxNbrsJoursChargeBean();
        totauxNbrsJoursDispoCTMaxRestanteProfilBeans.add(totauxNbrsJoursBean);
    }


    public List<PlanificationTableView<?, ?>> tables() {
        return Arrays.asList(new PlanificationTableView<?, ?>[]{
                planificationsTable,
                nbrsJoursChargeRsrcTable,
                nbrsJoursChargeProfilTable,
                nbrsJoursDispoCTRestanteRsrcTable,
                totauxNbrsJoursDispoCTRestanteRsrcTable,
                nbrsJoursDispoCTMaxRestanteProfilTable
        });
    }

    private void initTables() {
        initTablePlanifications();
        initTableNbrsJoursChargeRsrc();
        initTableNbrsJoursChargeProfil();
        initTableNbrsJoursDispoCTRestanteRsrc();
        initTableTotauxNbrsJoursDispoCTRestanteRsrc();
        initTableNbrsJoursDispoCTMaxRestanteProfilTable();
        initTableTotauxNbrsJoursDispoCTMaxRestanteProfil();

        synchroniserLargeurColonnes();
        synchronizerAscenceursHorizontaux();
    }

    private void initTablePlanifications() {

        // Définition du contenu de la table (ses lignes) :
//        planificationsTable.setItems(planificationsBeans); Non, sinon le filtre global ne fontionnera plus (défini par la classe mère lors "super.initialize()").

        planificationsTable.setCalendrierColumns(Arrays.asList(
                semaine1Column,
                semaine2Column,
                semaine3Column,
                semaine4Column,
                semaine5Column,
                semaine6Column,
                semaine7Column,
                semaine8Column,
                semaine9Column,
                semaine10Column,
                semaine11Column,
                semaine12Column
        ));

        // Paramétrage de l'affichage des valeurs des colonnes (mode "consultation") :
        //
        for (int cptColonne = 1; cptColonne <= planificationsTable.getCalendrierColumns().size(); cptColonne++) {
            TableColumn<PlanificationTacheBean, Double> colonne = planificationsTable.getCalendrierColumns().get(cptColonne - 1);
            CalendrierChargeCellCallback calendrierChargeCellCallback = new CalendrierChargeCellCallback(cptColonne);
            colonne.setCellValueFactory(calendrierChargeCellCallback);
        }
        chargePlanifieeTotaleColumn.setCellValueFactory(cellData -> cellData.getValue().chargePlanifieeTotaleProperty().asObject());

        // Paramétrage de la saisie des valeurs des colonnes (mode "édition") et
        // du formatage qui symbolise les incohérences/surcharges/etc. (Cf. http://code.makery.ch/blog/javafx-8-tableview-cell-renderer/) :
        //
        getDebutColumn().setOnEditCommit(event -> { // On ajoute un Handler sur "onCommit" plutôt que un CellFactory pour ne pas écraser la CellFactory définie par la classe mère.
            PlanificationTacheBean planificationTacheBean = event.getTableView().getItems().get(event.getTablePosition().getRow());
            planificationTacheBean.setDebut(event.getNewValue());

//            planificationsTable.refresh(); // Pour que les styles CSS soient re-appliqués (notamment ceux des colonnes du calendrier).
        });
        //noinspection OverlyComplexAnonymousInnerClass
        getEcheanceColumn().setCellFactory(param -> new EcheanceCell<PlanificationTacheBean>(PlanChargeIhm.PATRON_FORMAT_DATE, PlanChargeIhm.PROMPT_FORMAT_DATE, planificationsTable) {

            @Override
            protected void styler() {
                super.styler();

                // Réinit du style de la cellule :
                pseudoClassStateChanged(ECHEANCE_NON_TENUE, false);

                // Stop, si cellule vide :
                if (isEmpty() || (getItem() == null)) {
                    return;
                }

                // Récupération des infos sur la cellule :
                PlanificationTacheBean planifBean = tacheBean();
                if (planifBean == null) {
                    return;
                }

                // Formatage du style (CSS) de la cellule :
                boolean estEcheanceTenue = estEcheanceTenue(planifBean);
                if (!estEcheanceTenue) {
                    pseudoClassStateChanged(ECHEANCE_NON_TENUE, true);
                }
            }

            // TODO FDA 2017/09 Coder cette RG dans la couche métier.
            private boolean estEcheanceTenue(@NotNull PlanificationTacheBean planifBean) {
                LocalDate echeance = planifBean.getEcheance();
                if (echeance == null) {
                    return true;
                }
                LocalDate debutDernierePeriodePlanifiee = Dates.max(planifBean.getDebutsPeriodesPlanifiees());
                if (debutDernierePeriodePlanifiee == null) {
                    return false;
                }
                LocalDate finDernierePeriodePlanifiee = debutDernierePeriodePlanifiee.plusDays(7L); // TODO [issue#26:PeriodeHebdo/Trim]
                return !finDernierePeriodePlanifiee.isAfter(echeance);
            }

        });
        //noinspection OverlyComplexAnonymousInnerClass
        getChargeColumn().setCellFactory(column -> new TextFieldTableCell<PlanificationTacheBean, Double>(Converters.CHARGE_STRING_CONVERTER) {// TODO FDA 2017/08 Mieux formater.

            @Override
            public void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
//                formater();
                styler();
            }

            private void formater() {
                if ((getItem() == null) || isEmpty()) {
                    setText(null);
                    setGraphic(null);
                    return;
                }
                setText(FORMAT_CHARGE.format(getItem()));
            }

            private void styler() {

                // Réinit du texte et du style de la cellule :
                pseudoClassStateChanged(INCOHERENCE, false);
                pseudoClassStateChanged(CHARGE_NON_PLANIFIEE, false);

                if ((getItem() == null) || isEmpty()) {
                    return;
                }

                // Style with a different color:
                Double chargePlanifiee = chargePlanifieeTotaleColumn.getCellData(getIndex());
                if (chargePlanifiee != null) {
                    if (chargePlanifiee < getItem()) {
                        pseudoClassStateChanged(CHARGE_NON_PLANIFIEE, true);
                    }
                    if (chargePlanifiee > getItem()) {
                        pseudoClassStateChanged(INCOHERENCE, true);
                    }
                }
            }
        });
        //noinspection OverlyComplexAnonymousInnerClass
        getRessourceColumn().setCellFactory(param -> new RessourceCell<PlanificationTacheBean>(planChargeBean.getRessourcesBeans(), this::afficherRessourceHumaine, this::filtrerSurRessource) {

            @Override
            public void updateItem(RessourceBean<?, ?> item, boolean empty) {
                super.updateItem(item, empty);
//                formater();
                styler();
            }

            private void formater() {
                if (isEmpty() || (getItem() == null)) {
                    setText(null);
                    setGraphic(null);
                    return;
                }
                setText(getConverter().toString(getItem()));
            }

            private void styler() {
                pseudoClassStateChanged(SURCHARGE, false);

                if (isEmpty() || (getItem() == null)) {
                    return;
                }

                // Récupération des infos sur la cellule :
                //noinspection unchecked
                TableRow<PlanificationTacheBean> tableRow = getTableRow();
                if (tableRow == null) { // Un clic sur le TableHeaderRow fait retourner null à getTableRow().
                    return;
                }
                PlanificationTacheBean planifBean = tableRow.getItem();
                if (planifBean == null) {
                    return;
                }

                if (estRessourceSurchargee(planifBean)) {
                    pseudoClassStateChanged(SURCHARGE, true);
                }
            }
        });
        //noinspection OverlyComplexAnonymousInnerClass
        getProfilColumn().setCellFactory(param -> new ProfilCell<PlanificationTacheBean>(planChargeBean.getProfilsBeans(), this::afficherProfil, this::filtrerSurProfil) {
            @Override
            public void updateItem(ProfilBean item, boolean empty) {
                super.updateItem(item, empty);
//                formater();
                styler();
            }

            private void formater() {
                if (isEmpty() || (getItem() == null)) {
                    setText(null);
                    setGraphic(null);
                    return;
                }
                setText(getConverter().toString(getItem()));
            }

            private void styler() {
                pseudoClassStateChanged(SURCHARGE, false);

                if (isEmpty() || (getItem() == null)) {
                    return;
                }

                // Récupération des infos sur la cellule :
                //noinspection unchecked
                TableRow<PlanificationTacheBean> tableRow = getTableRow();
                if (tableRow == null) { // Un clic sur le TableHeaderRow fait retourner null à getTableRow().
                    return;
                }
                PlanificationTacheBean planifBean = tableRow.getItem();
                if (planifBean == null) {
                    return;
                }

                if (estProfilSurcharge(planifBean)) {
                    pseudoClassStateChanged(SURCHARGE, true);
                }
            }
        });
        for (int cptColonne = 1; cptColonne <= planificationsTable.getCalendrierColumns().size(); cptColonne++) {
            TableColumn<PlanificationTacheBean, Double> colonne = planificationsTable.getCalendrierColumns().get(cptColonne - 1);
            int finalCptColonne = cptColonne;
            colonne.setCellFactory(param -> new PlanificationChargeCell(finalCptColonne, this));
        }
        //noinspection OverlyComplexAnonymousInnerClass
        chargePlanifieeTotaleColumn.setCellFactory(column -> new NotEditableTextFieldTableCell<PlanificationTacheBean, Double>(Converters.CHARGE_STRING_CONVERTER, () -> ihm.afficherInterdictionEditer("Cette colonne n'est pas saisissable, elle est calculée.")) {

            @Override
            public void updateItem(@Null Double item, boolean empty) {
                super.updateItem(item, empty);
//                formater();
                styler();
            }

            private void formater() {
                if ((getItem() == null) || isEmpty()) {
                    setText(null);
                    setGraphic(null);
                    return;
                }
                setText(getConverter().toString(getItem()));
            }

            private void styler() {

                pseudoClassStateChanged(INCOHERENCE, false);

                if ((getItem() == null) || isEmpty()) {
                    return;
                }

                // Style with a different color:
                Double charge = getChargeColumn().getCellData(getIndex());
                if (getItem() > charge) {
                    pseudoClassStateChanged(INCOHERENCE, true);
                }
            }
        });

//        FIXME FDA 2017/10 Les colonnes sont toujours réagençables.
        TableViews.disableReagencingColumns(planificationsTable);

        TableViews.ensureDisplayingRows(planificationsTable, 25);
/*
        nbrLignesMaxTablePlanificationsSpinner.valueProperty().addListener((observable, oldValue, newValue) -> {
            Integer nbrlignes = Objects.value(newValue, 30);
            TableViews.ensureDisplayingRows(planificationsTable, nbrlignes);
        });
*/
        nbrLignesMaxTablePlanificationsSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            Integer nbrlignes = Objects.value(newValue.intValue(), 25);
            TableViews.ensureDisplayingRows(planificationsTable, nbrlignes);
        });

    }

    private void initTableNbrsJoursChargeRsrc() {

        nbrsJoursChargeRsrcTable.setCalendrierColumns(Arrays.asList(
                semaine1NbrsJoursChargeRsrcColumn,
                semaine2NbrsJoursChargeRsrcColumn,
                semaine3NbrsJoursChargeRsrcColumn,
                semaine4NbrsJoursChargeRsrcColumn,
                semaine5NbrsJoursChargeRsrcColumn,
                semaine6NbrsJoursChargeRsrcColumn,
                semaine7NbrsJoursChargeRsrcColumn,
                semaine8NbrsJoursChargeRsrcColumn,
                semaine9NbrsJoursChargeRsrcColumn,
                semaine10NbrsJoursChargeRsrcColumn,
                semaine11NbrsJoursChargeRsrcColumn,
                semaine12NbrsJoursChargeRsrcColumn
        ));

        // Paramétrage de l'affichage des valeurs des colonnes (mode "consultation") :
        ressourceNbrsJoursChargeRsrcColumn.setCellValueFactory(cell -> cell.getValue().getRessourceBean().codeProperty());
        //noinspection HardcodedFileSeparator
        profilNbrsJoursChargeRsrcColumn.setCellValueFactory(cell -> new SimpleStringProperty("N/A"));
        {
            int cptColonne = 0;
            for (TableColumn<NbrsJoursChargeParRessourceBean, Float> column : nbrsJoursChargeRsrcTable.getCalendrierColumns()) {
                cptColonne++;
                column.setCellValueFactory(new CalendrierFractionsJoursCellCallback<>(cptColonne));
            }
        }

        // Paramétrage de la saisie des valeurs des colonnes (mode "édition") :
        ihm.interdireEdition(ressourceNbrsJoursChargeRsrcColumn, "Cette colonne reprend les ressources humaines (ajouter une ressource humaine pour ajouter une ligne dans cette table).");
        ihm.interdireEdition(profilNbrsJoursChargeRsrcColumn, "Cette colonne reprend les profils (ajouter un profil pour ajouter une ligne dans cette table).");
        {
            for (TableColumn<NbrsJoursChargeParRessourceBean, Float> column : nbrsJoursChargeRsrcTable.getCalendrierColumns()) {
                column.setCellFactory(owningColumn -> new NotEditableTextFieldTableCell<>(Converters.FRACTION_JOURS_STRING_CONVERTER, () -> ihm.afficherInterdictionEditer("Le nombre de jours de charge par ressource est calculé.")));
            }
        }

        // Paramétrage des ordres de tri :
        TableViews.ensureSorting(nbrsJoursChargeRsrcTable, nbrsJoursChargeRsrcBeans);

        // Ajout des filtres "globaux" (à la TableList, pas sur chaque TableColumn) :
        //Pas sur cet écran (pas nécessaire, ni même utile).

        // Ajout des filtres "par colonne" (sur des TableColumn, pas sur la TableView) :
        //Pas sur cet écran (pas nécessaire, ni même utile).

        TableViews.disableReagencingColumns(nbrsJoursChargeRsrcTable);

        TableViews.ensureDisplayingAllRows(nbrsJoursChargeRsrcTable);
    }

    private void initTableNbrsJoursChargeProfil() {

        nbrsJoursChargeProfilTable.setCalendrierColumns(Arrays.asList(
                semaine1NbrsJoursChargeProfilColumn,
                semaine2NbrsJoursChargeProfilColumn,
                semaine3NbrsJoursChargeProfilColumn,
                semaine4NbrsJoursChargeProfilColumn,
                semaine5NbrsJoursChargeProfilColumn,
                semaine6NbrsJoursChargeProfilColumn,
                semaine7NbrsJoursChargeProfilColumn,
                semaine8NbrsJoursChargeProfilColumn,
                semaine9NbrsJoursChargeProfilColumn,
                semaine10NbrsJoursChargeProfilColumn,
                semaine11NbrsJoursChargeProfilColumn,
                semaine12NbrsJoursChargeProfilColumn
        ));

        // Paramétrage de l'affichage des valeurs des colonnes (mode "consultation") :
        //noinspection HardcodedFileSeparator
        ressourceNbrsJoursChargeProfilColumn.setCellValueFactory(cell -> new SimpleStringProperty("N/A"));
        profilNbrsJoursChargeProfilColumn.setCellValueFactory(cell -> cell.getValue().getProfilBean().codeProperty());
        {
            int cptColonne = 0;
            for (TableColumn<NbrsJoursChargeParProfilBean, Float> column : nbrsJoursChargeProfilTable.getCalendrierColumns()) {
                cptColonne++;
                column.setCellValueFactory(new CalendrierFractionsJoursCellCallback<>(cptColonne));
            }
        }

        // Paramétrage de la saisie des valeurs des colonnes (mode "édition") :
        ihm.interdireEdition(ressourceNbrsJoursChargeProfilColumn, "Cette colonne reprend les ressources humaines (ajouter une ressource humaine pour ajouter une ligne dans cette table).");
        ihm.interdireEdition(profilNbrsJoursChargeProfilColumn, "Cette colonne reprend les profils (ajouter un profil pour ajouter une ligne dans cette table).");
        {
            for (TableColumn<NbrsJoursChargeParProfilBean, Float> column : nbrsJoursChargeProfilTable.getCalendrierColumns()) {
                column.setCellFactory(owningColumn -> new NotEditableTextFieldTableCell<>(Converters.FRACTION_JOURS_STRING_CONVERTER, () -> ihm.afficherInterdictionEditer("Le nombre de jours de charge par profil est calculé.")));
            }
        }

        // Paramétrage des ordres de tri :
        //Pas sur cet écran (pas d'ordre de tri spécial, les tris standards de JavaFX font l'affaire).
        TableViews.ensureSorting(nbrsJoursChargeProfilTable, nbrsJoursChargeProfilBeans);

        // Ajout des filtres "globaux" (à la TableList, pas sur chaque TableColumn) :
        //Pas sur cet écran (pas nécessaire, ni même utile).

        // Ajout des filtres "par colonne" (sur des TableColumn, pas sur la TableView) :
        //Pas sur cet écran (pas nécessaire, ni même utile).

        TableViews.disableReagencingColumns(nbrsJoursChargeProfilTable);

        TableViews.ensureDisplayingAllRows(nbrsJoursChargeProfilTable);
    }

    private void initTableNbrsJoursDispoCTRestanteRsrc() {

        nbrsJoursDispoCTRestanteRsrcTable.setCalendrierColumns(Arrays.asList(
                semaine1NbrsJoursDispoCTRestanteRsrcColumn,
                semaine2NbrsJoursDispoCTRestanteRsrcColumn,
                semaine3NbrsJoursDispoCTRestanteRsrcColumn,
                semaine4NbrsJoursDispoCTRestanteRsrcColumn,
                semaine5NbrsJoursDispoCTRestanteRsrcColumn,
                semaine6NbrsJoursDispoCTRestanteRsrcColumn,
                semaine7NbrsJoursDispoCTRestanteRsrcColumn,
                semaine8NbrsJoursDispoCTRestanteRsrcColumn,
                semaine9NbrsJoursDispoCTRestanteRsrcColumn,
                semaine10NbrsJoursDispoCTRestanteRsrcColumn,
                semaine11NbrsJoursDispoCTRestanteRsrcColumn,
                semaine12NbrsJoursDispoCTRestanteRsrcColumn
        ));

        // Paramétrage de l'affichage des valeurs des colonnes (mode "consultation") :
        ressourceNbrsJoursDispoCTRestanteRsrcColumn.setCellValueFactory(cell -> cell.getValue().getRessourceBean().codeProperty());
        //noinspection HardcodedFileSeparator
        profilNbrsJoursDispoCTRestanteRsrcColumn.setCellValueFactory(cell -> new SimpleStringProperty("N/A"));
        {
            int cptColonne = 0;
            for (TableColumn<NbrsJoursChargeParRessourceBean, Float> column : nbrsJoursDispoCTRestanteRsrcTable.getCalendrierColumns()) {
                cptColonne++;
                column.setCellValueFactory(new CalendrierFractionsJoursCellCallback<>(cptColonne));
            }
        }

        // Paramétrage de la saisie des valeurs des colonnes (mode "édition") :
        ihm.interdireEdition(ressourceNbrsJoursDispoCTRestanteRsrcColumn, "Cette colonne reprend les ressources humaines (ajouter une ressource humaine pour ajouter une ligne dans cette table).");
        ihm.interdireEdition(profilNbrsJoursDispoCTRestanteRsrcColumn, "Cette colonne reprend les profils (ajouter un profil pour ajouter une ligne dans cette table).");
        for (TableColumn<NbrsJoursChargeParRessourceBean, Float> column : nbrsJoursDispoCTRestanteRsrcTable.getCalendrierColumns()) {
            column.setCellFactory(owningColumn -> new ChargePlanifieeCell<>(() -> ihm.afficherInterdictionEditer("Le nombre de jours de dispo. CT restante par ressource est calculé.")));
        }

        // Paramétrage des ordres de tri :
        //Pas sur cet écran (pas d'ordre de tri spécial, les tris standards de JavaFX font l'affaire).
        TableViews.ensureSorting(nbrsJoursDispoCTRestanteRsrcTable, nbrsJoursDispoCTRestanteRsrcBeans);

        // Ajout des filtres "globaux" (à la TableList, pas sur chaque TableColumn) :
        //Pas sur cet écran (pas nécessaire, ni même utile).

        // Ajout des filtres "par colonne" (sur des TableColumn, pas sur la TableView) :
        //Pas sur cet écran (pas nécessaire, ni même utile).

        TableViews.disableReagencingColumns(nbrsJoursDispoCTRestanteRsrcTable);

        TableViews.ensureDisplayingAllRows(nbrsJoursDispoCTRestanteRsrcTable);

        definirMenuContextuelColonneRessource(nbrsJoursDispoCTRestanteRsrcTable, ressourceNbrsJoursDispoCTRestanteRsrcColumn);
    }

    private void definirMenuContextuelColonneRessource(@NotNull PlanificationTableView<NbrsJoursChargeParRessourceBean, Float> table, @NotNull TableColumn<NbrsJoursChargeParRessourceBean, String> ressourceColumn) {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem menuItem = new MenuItem("Filtrer les tâches sur la ressource");
        menuItem.setOnAction(event -> {

            NbrsJoursChargeParRessourceBean nbrsJoursParRessourceBean = TableViews.selectedItem(table);
            if (nbrsJoursParRessourceBean == null) {
                ihm.afficherDialog(
                        Alert.AlertType.WARNING,
                        "Aucune ressource sélectionnée",
                        "Sélectionnez d'abord une ligne, puis recliquez.",
                        400, 100
                );
                return;
            }

            RessourceBean ressourceBean = nbrsJoursParRessourceBean.getRessourceBean();
            assert ressourceBean != null;

            filtrerSurRessource(ressourceBean);
        });
        contextMenu.getItems().add(menuItem);
        ressourceColumn.setContextMenu(contextMenu);
    }

    private void initTableTotauxNbrsJoursDispoCTRestanteRsrc() {

        totauxNbrsJoursDispoCTRestanteRsrcTable.setCalendrierColumns(Arrays.asList(
                semaine1TotauxNbrsJoursDispoCTRestanteRsrcColumn,
                semaine2TotauxNbrsJoursDispoCTRestanteRsrcColumn,
                semaine3TotauxNbrsJoursDispoCTRestanteRsrcColumn,
                semaine4TotauxNbrsJoursDispoCTRestanteRsrcColumn,
                semaine5TotauxNbrsJoursDispoCTRestanteRsrcColumn,
                semaine6TotauxNbrsJoursDispoCTRestanteRsrcColumn,
                semaine7TotauxNbrsJoursDispoCTRestanteRsrcColumn,
                semaine8TotauxNbrsJoursDispoCTRestanteRsrcColumn,
                semaine9TotauxNbrsJoursDispoCTRestanteRsrcColumn,
                semaine10TotauxNbrsJoursDispoCTRestanteRsrcColumn,
                semaine11TotauxNbrsJoursDispoCTRestanteRsrcColumn,
                semaine12TotauxNbrsJoursDispoCTRestanteRsrcColumn
        ));

        // Paramétrage de l'affichage des valeurs des colonnes (mode "consultation") :
        espaceTotauxNbrsJoursDispoCTRestanteRsrcColumn.setCellValueFactory(cell -> new SimpleStringProperty("Totaux"));
        {
            int cptColonne = 0;
            for (TableColumn<TotauxNbrsJoursChargeBean, Float> column : totauxNbrsJoursDispoCTRestanteRsrcTable.getCalendrierColumns()) {
                cptColonne++;
                column.setCellValueFactory(new CalendrierFractionsJoursCellCallback<>(cptColonne));
            }
        }

        // Paramétrage de la saisie des valeurs des colonnes (mode "édition") :
        for (TableColumn<TotauxNbrsJoursChargeBean, Float> column : totauxNbrsJoursDispoCTRestanteRsrcTable.getCalendrierColumns()) {
            column.setCellFactory(owningColumn ->
                    new ChargePlanifieeCell<>(
                            Converters.SOMME_FRACTION_JOURS_STRING_CONVERTER,
                            () -> ihm.afficherInterdictionEditer("Le total de nombre de jours de dispo. CT restante par ressource est calculé.")
                    )
            );
        }

        // Paramétrage des ordres de tri :
        //Pas sur cet écran (pas d'ordre de tri spécial, les tris standards de JavaFX font l'affaire).
        TableViews.ensureSorting(totauxNbrsJoursDispoCTRestanteRsrcTable, totauxNbrsJoursDispoCTRestanteRsrcBeans);

        // Ajout des filtres "globaux" (à la TableList, pas sur chaque TableColumn) :
        //Pas sur cet écran (pas nécessaire, ni même utile).

        // Ajout des filtres "par colonne" (sur des TableColumn, pas sur la TableView) :
        //Pas sur cet écran (pas nécessaire, ni même utile).

        TableViews.disableReagencingColumns(totauxNbrsJoursDispoCTRestanteRsrcTable);

        TableViews.ensureDisplayingAllRows(totauxNbrsJoursDispoCTRestanteRsrcTable);
    }

    private void initTableNbrsJoursDispoCTMaxRestanteProfilTable() {

        nbrsJoursDispoCTMaxRestanteProfilTable.setCalendrierColumns(Arrays.asList(
                semaine1NbrsJoursDispoCTMaxRestanteProfilColumn,
                semaine2NbrsJoursDispoCTMaxRestanteProfilColumn,
                semaine3NbrsJoursDispoCTMaxRestanteProfilColumn,
                semaine4NbrsJoursDispoCTMaxRestanteProfilColumn,
                semaine5NbrsJoursDispoCTMaxRestanteProfilColumn,
                semaine6NbrsJoursDispoCTMaxRestanteProfilColumn,
                semaine7NbrsJoursDispoCTMaxRestanteProfilColumn,
                semaine8NbrsJoursDispoCTMaxRestanteProfilColumn,
                semaine9NbrsJoursDispoCTMaxRestanteProfilColumn,
                semaine10NbrsJoursDispoCTMaxRestanteProfilColumn,
                semaine11NbrsJoursDispoCTMaxRestanteProfilColumn,
                semaine12NbrsJoursDispoCTMaxRestanteProfilColumn
        ));

        // Paramétrage de l'affichage des valeurs des colonnes (mode "consultation") :
        //noinspection HardcodedFileSeparator
        ressourceNbrsJoursDispoCTMaxRestanteProfilColumn.setCellValueFactory(cell -> new SimpleStringProperty("N/A"));
        profilNbrsJoursDispoCTMaxRestanteProfilColumn.setCellValueFactory(cell -> cell.getValue().getProfilBean().codeProperty());
        {
            int cptColonne = 0;
            for (TableColumn<NbrsJoursChargeParProfilBean, Float> column : nbrsJoursDispoCTMaxRestanteProfilTable.getCalendrierColumns()) {
                cptColonne++;
                column.setCellValueFactory(new CalendrierFractionsJoursCellCallback<>(cptColonne));
            }
        }

        // Paramétrage de la saisie des valeurs des colonnes (mode "édition") :
        ihm.interdireEdition(ressourceNbrsJoursDispoCTMaxRestanteProfilColumn, "Cette colonne reprend les ressources humaines (ajouter une ressource humaine pour ajouter une ligne dans cette table).");
        ihm.interdireEdition(profilNbrsJoursDispoCTMaxRestanteProfilColumn, "Cette colonne reprend les profils (ajouter un profil pour ajouter une ligne dans cette table).");
        for (TableColumn<NbrsJoursChargeParProfilBean, Float> column : nbrsJoursDispoCTMaxRestanteProfilTable.getCalendrierColumns()) {
            column.setCellFactory(owningColumn -> new ChargePlanifieeCell<>(() -> ihm.afficherInterdictionEditer("Le nombre de jours de dispo. max. CT restante par profil est calculé.")));
        }

        // Paramétrage des ordres de tri :
        //Pas sur cet écran (pas d'ordre de tri spécial, les tris standards de JavaFX font l'affaire).
        TableViews.ensureSorting(nbrsJoursDispoCTMaxRestanteProfilTable, nbrsJoursDispoCTMaxRestanteProfilBeans);

        // Ajout des filtres "globaux" (à la TableList, pas sur chaque TableColumn) :
        //Pas sur cet écran (pas nécessaire, ni même utile).

        // Ajout des filtres "par colonne" (sur des TableColumn, pas sur la TableView) :
        //Pas sur cet écran (pas nécessaire, ni même utile).

        TableViews.disableReagencingColumns(nbrsJoursDispoCTMaxRestanteProfilTable);

        TableViews.ensureDisplayingAllRows(nbrsJoursDispoCTMaxRestanteProfilTable);
    }

    private void initTableTotauxNbrsJoursDispoCTMaxRestanteProfil() {

        totauxNbrsJoursDispoCTMaxRestanteProfilTable.setCalendrierColumns(Arrays.asList(
                semaine1TotauxNbrsJoursDispoCTMaxRestanteProfilColumn,
                semaine2TotauxNbrsJoursDispoCTMaxRestanteProfilColumn,
                semaine3TotauxNbrsJoursDispoCTMaxRestanteProfilColumn,
                semaine4TotauxNbrsJoursDispoCTMaxRestanteProfilColumn,
                semaine5TotauxNbrsJoursDispoCTMaxRestanteProfilColumn,
                semaine6TotauxNbrsJoursDispoCTMaxRestanteProfilColumn,
                semaine7TotauxNbrsJoursDispoCTMaxRestanteProfilColumn,
                semaine8TotauxNbrsJoursDispoCTMaxRestanteProfilColumn,
                semaine9TotauxNbrsJoursDispoCTMaxRestanteProfilColumn,
                semaine10TotauxNbrsJoursDispoCTMaxRestanteProfilColumn,
                semaine11TotauxNbrsJoursDispoCTMaxRestanteProfilColumn,
                semaine12TotauxNbrsJoursDispoCTMaxRestanteProfilColumn
        ));

        // Paramétrage de l'affichage des valeurs des colonnes (mode "consultation") :
        espaceTotauxNbrsJoursDispoCTMaxRestanteProfilColumn.setCellValueFactory(cell -> new SimpleStringProperty("Totaux"));
        {
            int cptColonne = 0;
            for (TableColumn<TotauxNbrsJoursChargeBean, Float> column : totauxNbrsJoursDispoCTMaxRestanteProfilTable.getCalendrierColumns()) {
                cptColonne++;
                column.setCellValueFactory(new CalendrierFractionsJoursCellCallback<>(cptColonne));
            }
        }

        // Paramétrage de la saisie des valeurs des colonnes (mode "édition") :
        for (TableColumn<TotauxNbrsJoursChargeBean, Float> column : totauxNbrsJoursDispoCTMaxRestanteProfilTable.getCalendrierColumns()) {
            column.setCellFactory((TableColumn<TotauxNbrsJoursChargeBean, Float> owningColumn) ->
                    new ChargePlanifieeCell<>(
                            Converters.SOMME_FRACTION_JOURS_STRING_CONVERTER,
                            () -> ihm.afficherInterdictionEditer("Le total de nombre de jours de dispo. max. CT restante par profil est calculé.")
                    )
            );
        }

        // Paramétrage des ordres de tri :
        //Pas sur cet écran (pas d'ordre de tri spécial, les tris standards de JavaFX font l'affaire).
        TableViews.ensureSorting(totauxNbrsJoursDispoCTMaxRestanteProfilTable, totauxNbrsJoursDispoCTMaxRestanteProfilBeans);

        // Ajout des filtres "globaux" (à la TableList, pas sur chaque TableColumn) :
        //Pas sur cet écran (pas nécessaire, ni même utile).

        // Ajout des filtres "par colonne" (sur des TableColumn, pas sur la TableView) :
        //Pas sur cet écran (pas nécessaire, ni même utile).

        TableViews.disableReagencingColumns(totauxNbrsJoursDispoCTMaxRestanteProfilTable);

        TableViews.ensureDisplayingAllRows(totauxNbrsJoursDispoCTMaxRestanteProfilTable);
    }


    private void initSuiviActionsUtilisateur() {
        // Le suivi des actions de l'utilisateur est fait dans la méthode 'commit' de la classe PlanificationChargeCell.
    }


    // TODO FDA 2017/09 Mettre cette RG dans la couche métier.
    private boolean estRessourceSurchargee(@NotNull PlanificationTacheBean planifBean) {
        if ((planifBean.getTacheBean().getRessource() == null) || !planifBean.getTacheBean().getRessource().estHumain()) {
            return false;
        }
        NbrsJoursChargeParRessourceBean nbrsJoursDispoRestanteBean = Collections.any(
                nbrsJoursDispoCTRestanteRsrcBeans,
                nbrsJoursParRessourceBean -> nbrsJoursParRessourceBean.getRessourceBean().equals(planifBean.getTacheBean().getRessource())
        );
        if (nbrsJoursDispoRestanteBean == null) {
            return false;
        }
        for (Map.Entry<LocalDate, FloatProperty> entry : nbrsJoursDispoRestanteBean.entrySet()) {
            FloatProperty nbrJoursDispoRestantePeriode = entry.getValue();
            if (nbrJoursDispoRestantePeriode.get() < 0) {
                return true;
            }
        }
        return false;
    }

    // TODO FDA 2017/09 Mettre cette RG dans la couche métier.
    private boolean estProfilSurcharge(@NotNull PlanificationTacheBean planifBean) {
        if (planifBean.getTacheBean().getProfil() == null) {
            return false;
        }
        NbrsJoursChargeParProfilBean nbrsJoursDispoRestanteBean = Collections.any(
                nbrsJoursDispoCTMaxRestanteProfilBeans,
                nbrsJoursParRessourceBean -> nbrsJoursParRessourceBean.getProfilBean().equals(planifBean.getTacheBean().getProfil())
        );
        if (nbrsJoursDispoRestanteBean == null) {
            return false;
        }
        for (Map.Entry<LocalDate, FloatProperty> entry : nbrsJoursDispoRestanteBean.entrySet()) {
            FloatProperty nbrJoursDispoRestantePeriode = entry.getValue();
            if (nbrJoursDispoRestantePeriode.get() < 0) {
                return true;
            }
        }
        return false;
    }


    private void synchroniserLargeurColonnes() {

        // Synchronisation de certaines colonnes de la 1ère table et de celles de la 2nde table :
        TableViews.synchronizeColumnsWidth(
                espaceNbrsJoursChargeRsrcColumn,
                Arrays.asList(
                        getCategorieColumn(),
                        getSousCategorieColumn(),
                        getSousCategorieColumn(),
                        getNoTacheColumn(),
                        getNoTicketIdalColumn(),
                        getDescriptionColumn(),
                        getProjetAppliColumn(),
                        getStatutColumn(),
                        getDebutColumn(),
                        getEcheanceColumn(),
                        getImportanceColumn(),
                        getChargeColumn()
/*
                        getRessourceColumn(),
                        getProfilColumn()
*/
                )
        );
        TableViews.synchronizeColumnsWidth(getRessourceColumn(), ressourceNbrsJoursChargeRsrcColumn);
        TableViews.synchronizeColumnsWidth(getProfilColumn(), profilNbrsJoursChargeRsrcColumn);
        TableViews.synchronizeColumnsWidth(planificationsTable.getCalendrierColumns(), nbrsJoursChargeRsrcTable.getCalendrierColumns());

        // Synchronisation de toutes les colonnes de la 2nde table avec les colonnes de la plupart des tables affichées en dessous :
        for (PlanificationTableView<?, ?> table : tables()) {
            if (java.util.Objects.equals(table, planificationsTable) || java.util.Objects.equals(table, nbrsJoursChargeRsrcTable)) {
                continue;
            }
            if (table.isAggregate()) {
                continue;
            }
            TableViews.synchronizeColumnsWidth(nbrsJoursChargeRsrcTable, table);
        }

        // Synchronisation des tables des totaux :
        TableViews.synchronizeColumnsWidth(
                espaceTotauxNbrsJoursDispoCTRestanteRsrcColumn,
                Arrays.asList(
                        getCategorieColumn(),
                        getSousCategorieColumn(),
                        getSousCategorieColumn(),
                        getNoTacheColumn(),
                        getNoTicketIdalColumn(),
                        getDescriptionColumn(),
                        getProjetAppliColumn(),
                        getStatutColumn(),
                        getDebutColumn(),
                        getEcheanceColumn(),
                        getImportanceColumn(),
                        getChargeColumn(),
                        getRessourceColumn(),
                        getProfilColumn()
                )
        );
        TableViews.synchronizeColumnsWidth(planificationsTable.getCalendrierColumns(), totauxNbrsJoursDispoCTRestanteRsrcTable.getCalendrierColumns());

        TableViews.synchronizeColumnsWidth(
                espaceTotauxNbrsJoursDispoCTMaxRestanteProfilColumn,
                Arrays.asList(
                        getCategorieColumn(),
                        getSousCategorieColumn(),
                        getSousCategorieColumn(),
                        getNoTacheColumn(),
                        getNoTicketIdalColumn(),
                        getDescriptionColumn(),
                        getProjetAppliColumn(),
                        getStatutColumn(),
                        getDebutColumn(),
                        getEcheanceColumn(),
                        getImportanceColumn(),
                        getChargeColumn(),
                        getRessourceColumn(),
                        getProfilColumn()
                )
        );
        TableViews.synchronizeColumnsWidth(planificationsTable.getCalendrierColumns(), totauxNbrsJoursDispoCTMaxRestanteProfilTable.getCalendrierColumns());
    }

    private void synchronizerAscenceursHorizontaux() {
/* FIXME FDA 2017/10 Sans effet : les ascenceurs horizontaux ne sont pas synchronisés.
        planificationsTable.setOnScrollToColumn(event -> {
            TableColumn<PlanificationTacheBean, ?> scrollColumnTarget = event.getScrollTarget();
            TableView<PlanificationTacheBean> table = scrollColumnTarget.getTableView();
            int scrollColumnIndex = table.getColumns().indexOf(scrollColumnTarget);
            assert  scrollColumnIndex != -1 ;
            nbrsJoursChargeRsrcTable.scrollToColumnIndex(scrollColumnIndex);
        });
*/
    }

    @Override
    MenuButton menuActions(@NotNull TableColumn.CellDataFeatures<PlanificationTacheBean, MenuButton> cellData) {
        MenuButton menuActions = super.menuActions(cellData);
        PlanificationTacheBean tacheBean = cellData.getValue();
        {
            MenuItem menuItemProvisionner = new MenuItem("Provisionner la tâche " + tacheBean.noTache());
            menuItemProvisionner.setOnAction(event -> {
                try {
                    provisionnerTache(tacheBean);
                } catch (ControllerException e) {
                    LOGGER.error("Impossible de provisionner la tâche " + tacheBean.noTache() + ".", e);
                }
            });
            menuItemProvisionner.disableProperty().bind(tacheBean.statutProperty().isNotEqualTo(StatutBean.PROVISION));
            menuActions.getItems().add(menuItemProvisionner);
        }
        {
            MenuItem menuItemSupprimer = new MenuItem("Voir le détail de la tâche " + tacheBean.noTache());
            menuItemSupprimer.setOnAction(event -> {
                ihm.getTachesController().afficherTache(tacheBean);
                try {
                    ihm.getApplicationController().afficherModuleTaches();
                } catch (ControllerException e) {
                    LOGGER.error("Impossible d'afficher le module des Tâches.", e);
                }
            });
            menuActions.getItems().add(menuItemSupprimer);
        }
        return menuActions;
    }


    @NotNull
    protected PlanificationTacheBean ajouterTache() throws ControllerException {
        LOGGER.debug("ajouterTache...");
        if (planChargeBean.getDateEtat() == null) {
            throw new ControllerException("Impossible d'ajouter une tâche car la date d'état n'est pas définie. Précisez une date auparavant.");
        }
        try {

            PlanificationTacheBean planifBean = super.ajouterTache();

            planChargeBean.vientDEtreModifie();
            getSuiviActionsUtilisateur().historiser(new AjoutTache<>(planifBean, planChargeBean.getPlanificationsBeans()));

            ihm.getApplicationController().majBarreEtat();

            return planifBean;
        } catch (SuiviActionsUtilisateurException e) {
            throw new ControllerException("Impossible d'ajouter une tâche.", e);
        }
    }

    @NotNull
    @Override
    PlanificationTacheBean nouveauBean() throws ControllerException {

        // TODO FDA 2017/09 Déplacer cette RG dans la couche métier (service, DTO et/ou Entity).
        TacheBean tacheBean = new TacheBean(
                idTacheSuivant(),
                null,
                null,
                "(pas de ticket)",
                null,
                null,
                StatutBean.NOUVEAU,
                null,
                null,
                null,
                0.0,
                null,
                null
        );
        tacheBean.setTypeChangement(TypeChangement.AJOUT);

        // TODO FDA 2017/09 Bloc à supprimer, sans doute.
        Map<LocalDate, DoubleProperty> calendrier = new TreeMap<>(); // TreeMap au lieu de HashMap pour trier, juste afin de faciliter le débogage.
        if (planChargeBean.getDateEtat() != null) {
            LocalDate debutSemaine = planChargeBean.getDateEtat();
            for (int noSemaine = 1; noSemaine <= Planifications.NBR_SEMAINES_PLANIFIEES; noSemaine++) {
                calendrier.put(debutSemaine, new SimpleDoubleProperty(0.0));
                debutSemaine = debutSemaine.plusDays(7); // TODO FDA 2017/06 [issue#26:PeriodeHebdo/Trim]
            }
        }

/*
        try {
*/
        return new PlanificationTacheBean(tacheBean, calendrier);
/*
        } catch (BeanException e) {
            throw new ControllerException("Impossible d'instancier un nouvelle tâche du plan de charge.", e);
        }
*/
    }


    public void definirValeursCalendrier() {

        LocalDate dateEtat = planChargeBean.getDateEtat();
        if (dateEtat == null) {
/*
            ihm.afficherDialog(
                    Alert.AlertType.ERROR,
                    "Impossible d'afficher la planification (valeurs du calendrier)",
                    "Date d'état non définie."
            );
*/
            return;
        }

        try {
            definirValeursCalendrier(dateEtat);
        } catch (ControllerException e) {
            LOGGER.error("Impossible de màj la planification (valeurs du calendrier).", e);
            ihm.afficherDialog(
                    Alert.AlertType.ERROR,
                    "Impossible de mettre à jour la planification (valeurs du calendrier)",
                    Exceptions.causes(e)
            );
        }
    }

    private void definirValeursCalendrier(@NotNull LocalDate dateEtat) throws ControllerException {
        try {
            LOGGER.debug("Màj de la planification : ");

            PlanificationsDTO planificationsInitiales = planChargeBean.toPlanificationDTOs();
            PlanificationsDTO planifications = planChargeService.replanifier(planificationsInitiales, dateEtat);

            planChargeBean.fromPlanificationDTOs(planifications);

//            planificationsTable.refresh();
            LOGGER.debug("Planification màj.");
        } catch (BeanException | ServiceException e) {
            throw new ControllerException("Impossible de màj la planification.", e);
        }
    }


/* TODO FDA 2017/08 Coder.
    @FXML
    private void filtrerTachesNonPlanifieesDansLeMois(@SuppressWarnings("unused") ActionEvent actionEvent) {
        // TODO FDA 2017/07 Coder.
    }

    @FXML
    private void filtrerTachesNonPlanifieesEntierement(@SuppressWarnings("unused") ActionEvent actionEvent) {
        filtrerTachesNonPlanifieesEntierement();
    }

    private void filtrerTachesNonPlanifieesEntierement() {
        getFilteredTachesBeans().setPredicate(tacheBean -> (tacheBean.getCharge() == null) || (tacheBean.getCharge() != tacheBean.getChargePlanifieeTotale()));
    }
*/

    @SuppressWarnings("unused")
    @FXML
    private void afficherTache(@SuppressWarnings("unused") ActionEvent actionEvent) {
        afficherTache();
    }

    private void afficherTache() {
        PlanificationTacheBean tacheBean = TableViews.selectedItem(planificationsTable);
        if (tacheBean == null) {
            //noinspection HardcodedLineSeparator
            ihm.afficherDialog(
                    Alert.AlertType.ERROR,
                    "Impossible d'afficher la tâche",
                    "Aucune ligne du plan de charge n'est actuellement sélectionnée."
                            + "\nSélectionnez d'abord une ligne, puis re-cliquez.",
                    400, 200
            );
            return;
        }
        ihm.getTachesController().afficherTache(tacheBean);
    }


    @Override
    List<ToggleButton> filtresButtons() {
        List<ToggleButton> buttons = new ArrayList<>(30);
        buttons.addAll(super.filtresButtons());
        buttons.addAll(Arrays.asList(
//                filtrePlanifToutToggleButton,
                //
                filtrePlanifChargeToutToggleButton,
                filtrePlanifInfChargeToggleButton,
                filtrePlanifSupChargeToggleButton,
                filtrePlanifDansMoisToggleButton,
                //
                filtreEcheanceTouteToggleButton,
                filtreEcheanceTenueToggleButton,
                filtreEcheanceNonTenueToggleButton,
                //
                filtreSurchargeRessourceToggleButton,
                filtreSurchargeProfilToggleButton
        ));
        return buttons;
    }

    @Override
    protected boolean estTacheAvecAutreFiltreAVoir(@NotNull PlanificationTacheBean tache) throws ControllerException {
        if (estTacheAvecFiltrePlanifAVoir(tache)) {
            return true;
        }
        if (estTacheAvecFiltreSurchargeAVoir(tache)) {
            return true;
        }
        return false;
    }

    private boolean estTacheAvecFiltrePlanifAVoir(@NotNull PlanificationTacheBean tache) throws ControllerException {
/*
        if (filtrePlanifToutToggleButton.isSelected()) {
            return true;
        }
*/
        if (filtrePlanifInfChargeToggleButton.isSelected()) {
            if (tache.getCharge() == null) {
                return true;
            }
            // TODO FDA 2017/09 Coder cette RG dans un DTO/Entity/Service.
            if (tache.getChargePlanifieeTotale() < tache.getCharge()) {
                return true;
            }
        }
        if (filtrePlanifSupChargeToggleButton.isSelected()) {
            if (tache.getCharge() == null) {
                return true;
            }
            // TODO FDA 2017/09 Coder cette RG dans un DTO/Entity/Service.
            if (tache.getChargePlanifieeTotale() > tache.getCharge()) {
                return true;
            }
        }
        if (filtrePlanifDansMoisToggleButton.isSelected()) {
            if (planChargeBean.getDateEtat() == null) {
                return true;
            }
            // TODO FDA 2017/09 Coder cette RG dans un DTO/Entity/Service.
            LocalDate debutPeriode = planChargeBean.getDateEtat();
            LocalDate finPeriode = debutPeriode.plusDays((long) (4 * 7));
            try {
                if (tache.aChargePlanifiee(debutPeriode, finPeriode)) {
                    return true;
                }
            } catch (BeanException e) {
                throw new ControllerException("Impossible de déterminer la charge planifiée de la tâche " + tache.noTache() + " pour la période de " + debutPeriode.format(DateTimeFormatter.ISO_LOCAL_DATE) + " à " + finPeriode.format(DateTimeFormatter.ISO_LOCAL_DATE) + ".", e);
            }
        }
        return false;
    }

    private boolean estTacheAvecFiltreSurchargeAVoir(@NotNull PlanificationTacheBean tache) throws ControllerException {
        if (filtreSurchargeRessourceToggleButton.isSelected()) {
            RessourceBean<?, ?> ressourceBean = tache.getRessource();
            if ((ressourceBean != null) && ressourceBean.estHumain()) {
                NbrsJoursChargeParRessourceBean nbrsJoursParRessourceBean = Collections.any(
                        nbrsJoursDispoCTRestanteRsrcBeans,
                        nbrsJoursBean -> nbrsJoursBean.getRessourceBean().equals(ressourceBean),
                        new ControllerException("Impossible de retrouver la ressource '" + ressourceBean.getCode() + "' dans la liste des nombres de jours de disponibilité CT restante par ressource.")
                );
                FloatProperty unNbrJoursDispoNegatif = Collections.any(
                        nbrsJoursParRessourceBean.values(),
                        nbrJourDispoRestante -> (nbrJourDispoRestante != null) && (nbrJourDispoRestante.getValue() != null) && (nbrJourDispoRestante.getValue() < 0.0)
                );
                //noinspection VariableNotUsedInsideIf
                if (unNbrJoursDispoNegatif != null) {// Au moins 1 période de surchargée pour cette ressource.
                    return true;
                }
            }
        }
        if (filtreSurchargeProfilToggleButton.isSelected()) {
            ProfilBean profilBean = tache.getProfil();
            if (profilBean != null) {
                NbrsJoursChargeParProfilBean nbrsJoursParProfilBean = Collections.any(
                        nbrsJoursDispoCTMaxRestanteProfilBeans,
                        nbrsJoursBean -> nbrsJoursBean.getProfilBean().equals(profilBean),
                        new ControllerException("Impossible de retrouver le profil '" + profilBean.getCode() + "' dans la liste des nombres de jours de disponibilité CT max restante par profil.")
                );
                FloatProperty unNbrJoursDispoNegatif = Collections.any(
                        nbrsJoursParProfilBean.values(),
                        nbrJourDispoRestante -> (nbrJourDispoRestante != null) && (nbrJourDispoRestante.getValue() != null) && (nbrJourDispoRestante.getValue() < 0.0)
                );
                //noinspection VariableNotUsedInsideIf
                if (unNbrJoursDispoNegatif != null) {// Au moins 1 période de surchargée pour ce profil.
                    return true;
                }
            }
        }
        return false;
    }


    @FXML
    private void filtrerSurRessourceTableNbrsJoursDispoCTRestanteRsrc(@SuppressWarnings("unused") @NotNull ActionEvent actionEvent) {
        filtrerSurRessourceTableNbrsJoursDispoCTRestanteRsrc();
    }

    private void filtrerSurRessourceTableNbrsJoursDispoCTRestanteRsrc() {

        NbrsJoursChargeParRessourceBean nbrsJoursParRessourceBean = TableViews.selectedItem(nbrsJoursDispoCTRestanteRsrcTable);
        if (nbrsJoursParRessourceBean == null) {
            ihm.afficherDialog(
                    Alert.AlertType.WARNING,
                    "Aucune ressource sélectionnée",
                    "Sélectionnez d'abord une ligne, puis recliquez.",
                    400, 100
            );
            return;
        }

        RessourceBean ressourceBean = nbrsJoursParRessourceBean.getRessourceBean();
        assert ressourceBean != null;

        filtrerSurRessource(ressourceBean);
    }

    @FXML
    private void filtrerSurProfilTableNbrsJoursChargeProfil(@SuppressWarnings("unused") @NotNull ActionEvent actionEvent) {
        filtrerSurProfilTableNbrsJoursChargeProfil();
    }

    private void filtrerSurProfilTableNbrsJoursChargeProfil() {

        NbrsJoursChargeParProfilBean nbrsJoursParProfilBean = TableViews.selectedItem(nbrsJoursChargeProfilTable);
        if (nbrsJoursParProfilBean == null) {
            ihm.afficherDialog(
                    Alert.AlertType.WARNING,
                    "Aucun profil sélectionné",
                    "Sélectionnez d'abord une ligne, puis recliquez.",
                    400, 100
            );
            return;
        }

        ProfilBean profilBean = nbrsJoursParProfilBean.getProfilBean();
        assert profilBean != null;

        filtrerSurProfil(profilBean);
    }

    @FXML
    private void filtrerSurRessourceTableNbrsJoursChargeRsrc(@SuppressWarnings("unused") @NotNull ActionEvent actionEvent) {
        filtrerSurRessourceTableNbrsJoursChargeRsrc();
    }

    private void filtrerSurRessourceTableNbrsJoursChargeRsrc() {

        NbrsJoursChargeParRessourceBean nbrsJoursParRessourceBean = TableViews.selectedItem(nbrsJoursChargeRsrcTable);
        if (nbrsJoursParRessourceBean == null) {
            ihm.afficherDialog(
                    Alert.AlertType.WARNING,
                    "Aucune ressource sélectionnée",
                    "Sélectionnez d'abord une ligne, puis recliquez.",
                    400, 100
            );
            return;
        }

        RessourceBean ressourceBean = nbrsJoursParRessourceBean.getRessourceBean();
        assert ressourceBean != null;

        filtrerSurRessource(ressourceBean);
    }

    @FXML
    private void filtrerSurProfilTableNbrsJoursDispoCTMaxRestanteProfil(@SuppressWarnings("unused") @NotNull ActionEvent actionEvent) {
        filtrerSurProfilTableNbrsJoursDispoCTMaxRestanteProfil();
    }

    private void filtrerSurProfilTableNbrsJoursDispoCTMaxRestanteProfil() {

        NbrsJoursChargeParProfilBean nbrsJoursParProfilBean = TableViews.selectedItem(nbrsJoursDispoCTMaxRestanteProfilTable);
        if (nbrsJoursParProfilBean == null) {
            ihm.afficherDialog(
                    Alert.AlertType.WARNING,
                    "Aucune ressource sélectionnée",
                    "Sélectionnez d'abord une ligne, puis recliquez.",
                    400, 100
            );
            return;
        }

        ProfilBean profilBean = nbrsJoursParProfilBean.getProfilBean();
        assert profilBean != null;

        filtrerSurProfil(profilBean);
    }

    void filtrerSurRessourceSurchargees() {
        deselectionnerTousFiltres();
        filtreSurchargeRessourceToggleButton.setSelected(true);
        filtrer();
    }

    void filtrerSurProfilsSurcharges() {
        deselectionnerTousFiltres();
        filtreSurchargeProfilToggleButton.setSelected(true);
        filtrer();
    }


    public void calculerCharges() throws ControllerException {
        LOGGER.debug("Calcul des charges : ");
        calculateurCharges.calculer();
        symboliserSurchargesParPeriode();
        tables().forEach(TableView::refresh); // Notamment pour que les cellules qui étaient vides et qui ont une valeur suite au calcul (les provisions, typiquement) soient affichées.
        LOGGER.debug("Charges calculées.");
    }

    private void symboliserSurchargesParPeriode() {

        if (planChargeBean.getDateEtat() == null) {
            return;
        }

        TableHeaderRow headerRow = TableViews.headerRow(planificationsTable);
        if (headerRow == null) {
            return;
        }

        assert totauxNbrsJoursDispoCTRestanteRsrcBeans.size() == 1;
        TotauxNbrsJoursChargeBean totauxNbrJoursChargePeriodeBean = totauxNbrsJoursDispoCTRestanteRsrcBeans.get(0);

        for (int cptPeriode = 1; cptPeriode <= ihm.NBR_SEMAINES_PLANIFIEES; cptPeriode++) {
            LocalDate debutPeriode = planChargeBean.getDateEtat().plusDays(((long) cptPeriode - 1L) * 7L); // [issue#26:PeriodeHebdo/Trim]
            FloatProperty chargePeriodeProperty = totauxNbrJoursChargePeriodeBean.get(debutPeriode);

            boolean estPeriodeSurchargee;
            if (chargePeriodeProperty == null) {
                estPeriodeSurchargee = false;
            } else {
                Float chargePeriode = chargePeriodeProperty.getValue();
                estPeriodeSurchargee = (chargePeriode < 0.0f);
            }

            TableColumn<PlanificationTacheBean, Double> periodeColumn = planificationsTable.getCalendrierColumns().get(cptPeriode - 1);
            TableColumnHeader periodeColumnHeader = headerRow.getColumnHeaderFor(periodeColumn);
            periodeColumnHeader.pseudoClassStateChanged(SURCHARGE, estPeriodeSurchargee);
            LOGGER.debug("Période n°{} ({}) : surcharge ? = {}", cptPeriode, debutPeriode.format(DateTimeFormatter.ISO_LOCAL_DATE), estPeriodeSurchargee);
        }
    }

    @FXML
    private void provisionnerTache(@SuppressWarnings("unused") @NotNull ActionEvent actionEvent) throws ControllerException {
        LOGGER.debug("provisionnerTache...");
        provisionnerTache();
    }

    @SuppressWarnings("FinalPrivateMethod")
    final void provisionnerTache() throws ControllerException {
        PlanificationTacheBean focusedItem = TableViews.selectedItem(getTachesTable());
        if (focusedItem == null) {
            LOGGER.debug("Aucune tâche sélectionnée, donc on en sait pas que provisionner, on ne fait rien.");
            return;
        }
        provisionnerTache(focusedItem);
    }

    private void provisionnerTache(@NotNull PlanificationTacheBean tacheBean) throws ControllerException {
        LOGGER.debug("Provisionning de la tâche {} : ", tacheBean.noTache());
        calculateurCharges.calculerProvision(tacheBean);
        tables().forEach(TableView::refresh); // Notamment pour que les cellules qui étaient vides et qui ont une valeur suite au calcul (les provisions, typiquement) soient affichées.
        LOGGER.debug("Tâche {} provisionnée.", tacheBean.noTache());
    }

}

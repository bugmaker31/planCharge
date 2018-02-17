package fr.gouv.agriculture.dal.ct.planCharge.ihm.controller;

import fr.gouv.agriculture.dal.ct.ihm.controller.ControllerException;
import fr.gouv.agriculture.dal.ct.ihm.view.TableViews;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.PlanChargeIhm;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.controlsfx.dialog.Wizard;
import org.controlsfx.dialog.WizardPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

// Cf. https://controlsfx.bitbucket.io/org/controlsfx/dialog/Wizard.html
@SuppressWarnings({"ClassHasNoToStringMethod", "ClassWithTooManyFields"})
public class RevueWizardController extends AbstractController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RevueWizardController.class);


    @Null
    private Stage wizardStage;

    @Null
    private Wizard wizard;

    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private WizardPane etapeDefinirDateEtatPane;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private WizardPane etapeMajDisponibilitesPane;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private WizardPane etapeMajTachesPane;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private WizardPane etapeMajPlanChargePane;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private WizardPane etapeDiffuserPane;


    private WizardPane[] etapes;

    @FXML
    private CheckBox majDateEtatCheckBox;

    @FXML
    private CheckBox majJoursFeriesCheckBox;

    @FXML
    private CheckBox majRessourcesHumainesCheckBox;

    @FXML
    private CheckBox majPrevisionsAbsencesCheckBox;

    @FXML
    private CheckBox majDisponibilitesCheckBox;

    @FXML
    private CheckBox majTachesCheckBox;


    public RevueWizardController() {
        super();
    }


    @Override
    protected void initialize() throws ControllerException {
        etapes = new WizardPane[]{
                etapeDefinirDateEtatPane,
                etapeMajDisponibilitesPane,
                etapeMajTachesPane,
                etapeMajPlanChargePane,
                etapeDiffuserPane
        };
    }

    void show() {

        if ((wizardStage != null) && wizardStage.isShowing()) {
            LOGGER.debug("Assistant de revue déjà affiché, rien à faire.");
            return;
        }

        if (wizardStage == null) {
            wizardStage = new Stage();
            wizardStage.setTitle(PlanChargeIhm.APP_NAME + " - Assistant de revue");
            wizardStage.getIcons().addAll(ihm.getPrimaryStage().getIcons());

            wizard = new Wizard(wizardStage.getOwner());
            wizard.setFlow(new Wizard.LinearFlow(etapes));
            wizard.resultProperty().addListener((observable, oldValue, newValue) -> {
                LOGGER.debug("Assistant de revue terminé (résultat = {}).", wizard.getResult().getButtonData());
                wizardStage.hide();
                wizardStage = null;
                LOGGER.debug("Assistant de revue masqué.");
            });
            ihm.appliquerTheme(wizard.getStylesheets());

            wizardStage.setScene(wizard.getScene());

            int noEcran = ihm.noEcranSecondaire();
            ihm.positionnerSurEcran(wizardStage, noEcran);

//            wizardStage.setMaximized(true);
        }

/*
        wizard.showAndWait().ifPresent(result -> {
            if (Objects.equals(result, ButtonType.FINISH)) {
                prendreEnCompte();
            }
        });
*/
//        wizard.show();
        if (!wizardStage.isShowing()) {
            wizardStage.show();
        }
        if (!wizardStage.isFocused()) {
            wizardStage.requestFocus();
        }

        LOGGER.debug("Assistant de revue affiché.");
    }

    private void passerEtapeSuivante() throws ControllerException {
        assert wizard != null;
        wizard.goToNextStep();
    }


    @FXML
    private void majJoursFeries(@SuppressWarnings("unused") @NotNull ActionEvent actionEvent) throws ControllerException {
        ihm.getApplicationController().afficherModuleJoursFeries();
        majJoursFeriesCheckBox.setSelected(true); // TODO FDA 2017/10 Améliorer : ne cocher que lorsque/si les jours fériés ont été màj.
    }

    @FXML
    private void majRessourcesHumaines(@SuppressWarnings("unused") @NotNull ActionEvent actionEvent) throws ControllerException {
        ihm.getApplicationController().afficherModuleRessourcesHumaines();
        majRessourcesHumainesCheckBox.setSelected(true); // TODO FDA 2017/10 Améliorer : ne cocher que lorsque/si les RH ont été màj.
    }

    @FXML
    private void majPrevisionsAbsence(@SuppressWarnings("unused") @NotNull ActionEvent actionEvent) throws ControllerException {
        ihm.getApplicationController().afficherModuleDisponibilites();
        majPrevisionsAbsencesCheckBox.setSelected(true); // TODO FDA 2017/10 Améliorer : ne cocher que lorsque/si les prévisions d'absence ont été màj.
    }

    @FXML
    private void majDisponibilites(@SuppressWarnings("unused") @NotNull ActionEvent actionEvent) throws ControllerException {
        ihm.getApplicationController().afficherModuleDisponibilites();
        ihm.getDisponibilitesController().getNbrsJoursAbsencePane().setExpanded(true);
        if (ihm.getDisponibilitesController().getNbrsJoursAbsenceTable().getItems().isEmpty()) {
            ihm.getDisponibilitesController().getNbrsJoursAbsenceTable().requestFocus();
        } else {
            TableViews.focusOnItem(
                    ihm.getDisponibilitesController().getNbrsJoursAbsenceTable(),
                    ihm.getDisponibilitesController().getNbrsJoursAbsenceTable().getItems().get(0)
            );
        }
        majDisponibilitesCheckBox.setSelected(true); // TODO FDA 2017/10 Améliorer : ne cocher que lorsque/si les disponibilités ont été màj.
    }

    @FXML
    private void majDateEtat(@SuppressWarnings("unused") @NotNull ActionEvent actionEvent) throws ControllerException {
        ihm.getApplicationController().deplierParametresPane();
        ihm.getPrimaryStage().requestFocus();
        {
            ChangeListener changeListener = (observable, oldValue, newValue) -> {
                try {

                    majDateEtatCheckBox.setSelected(true);

                    ihm.getApplicationController().replierParametresPane();

                    // Une seule action à cette étape, ou dernière action de cette étape, donc on passe à la suivante automatiquement.
                    passerEtapeSuivante();

                } catch (ControllerException e) {
                    LOGGER.error("Impossibile de déplier l'accordéaon des paramètres suite à une modification de la date d'état.", e);
                }
            };
            ihm.getApplicationController().getNoSemaineEtatPicker().valueProperty().addListener(changeListener);
            ihm.getApplicationController().getNoSemaineEtatPicker().valueProperty().addListener((observable, oldValue, newValue) -> {
                ihm.getApplicationController().getNoSemaineEtatPicker().valueProperty().removeListener(changeListener);
            });
        }
        ihm.getApplicationController().getNoSemaineEtatPicker().show();
    }

    @FXML
    private void afficherModuleCharges(@SuppressWarnings("unused") @NotNull ActionEvent actionEvent) throws ControllerException {
        ihm.getApplicationController().afficherModuleCharges();
    }


    @FXML
    private void filtrerTachesEchues(@SuppressWarnings("unused") @NotNull ActionEvent actionEvent) throws ControllerException {
        ihm.getApplicationController().afficherModuleTaches();
        ihm.getTachesController().filtrerTout();
        ihm.getTachesController().getFiltrePeriodeEchueToggleButton().setSelected(true);
        ihm.getTachesController().filtrer();
        ihm.afficherNotificationInfo("Tâches filtrées", "Seules les tâches échues sont désormais affichées.");
    }

    @FXML
    private void modifierEcheances(@SuppressWarnings("unused") @NotNull ActionEvent actionEvent) throws ControllerException {
        ihm.getApplicationController().afficherModuleTaches();
        ihm.getTachesController().modifierEcheances();
    }

    @FXML
    private void filtrerTachesAyantRessourcesSurchargees(@SuppressWarnings("unused") @NotNull ActionEvent actionEvent) throws ControllerException {
        ihm.getApplicationController().activerModule(ihm.getChargesController());
        ihm.getChargesController().filtrerSurRessourceSurchargees();
    }

    @FXML
    private void filtrerTachesAyantProfilsSurcharges(@SuppressWarnings("unused") @NotNull ActionEvent actionEvent) throws ControllerException {
        ihm.getApplicationController().activerModule(ihm.getChargesController());
        ihm.getChargesController().filtrerSurProfilsSurcharges();
    }

    @FXML
    private void filtrerTachesAjoutees(@SuppressWarnings("unused") @NotNull ActionEvent actionEvent) throws ControllerException {
        ihm.getApplicationController().activerModule(ihm.getChargesController());
        ihm.getChargesController().filtrerSurTachesAjoutees();
    }

    public void importerTaches(@SuppressWarnings("unused") @NotNull ActionEvent actionEvent) throws ControllerException {
        ihm.getApplicationController().majTachesDepuisCalc();
        majTachesCheckBox.setSelected(true);
    }
}

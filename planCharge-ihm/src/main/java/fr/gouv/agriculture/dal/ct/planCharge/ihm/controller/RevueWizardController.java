package fr.gouv.agriculture.dal.ct.planCharge.ihm.controller;

import fr.gouv.agriculture.dal.ct.ihm.controller.ControllerException;
import fr.gouv.agriculture.dal.ct.ihm.view.TableViews;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.PlanChargeIhm;
import fr.gouv.agriculture.dal.ct.planCharge.util.NotImplementedException;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.controlsfx.dialog.Wizard;
import org.controlsfx.dialog.WizardPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import java.util.Optional;

// Cf. https://controlsfx.bitbucket.io/org/controlsfx/dialog/Wizard.html
@SuppressWarnings("ClassHasNoToStringMethod")
public class RevueWizardController extends AbstractController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RevueWizardController.class);


    @SuppressWarnings("NullableProblems")
    @NotNull
    private Stage wizardStage;

    @SuppressWarnings("NullableProblems")
    @NotNull
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


    @SuppressWarnings("NullableProblems")
    @NotNull
    private Integer noEtapeCourante;

    private WizardPane[] etapes;

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

//        if (wizardStage == null) {
        wizardStage = new Stage();
        wizardStage.setTitle(PlanChargeIhm.APP_NAME + " - Assistant de revue");
        wizardStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/planCharge-logo.png")));
//        }

//        if (wizard == null) {
        wizard = new Wizard(wizardStage.getOwner());
        wizard.setFlow(new Wizard.LinearFlow(etapes));
        wizard.resultProperty().addListener((observable, oldValue, newValue) -> {
            LOGGER.debug("Assistant de revue terminé (résultat = {}).", wizard.getResult().getButtonData());
            wizardStage.hide();
            LOGGER.debug("Assistant de revue masqué.");
        });
//        }

/*
        wizard.showAndWait().ifPresent(result -> {
            if (Objects.equals(result, ButtonType.FINISH)) {
                prendreEnCompte();
            }
        });
*/
//        wizard.show();
        wizardStage.setScene(wizard.getScene());
        noEtapeCourante = 1;
        wizardStage.show();

        LOGGER.debug("Assistant de revue affiché.");
    }

    @FXML
    private void afficherModuleJoursFeries(@SuppressWarnings("unused") @NotNull ActionEvent actionEvent) throws ControllerException {
        ihm.getApplicationController().afficherModuleJoursFeries();
    }

    @FXML
    private void afficherModuleRessourcesHumaines(@SuppressWarnings("unused") @NotNull ActionEvent actionEvent) throws ControllerException {
        ihm.getApplicationController().afficherModuleRessourcesHumaines();
    }

    @FXML
    private void afficherModuleDisponibilites(@SuppressWarnings("unused") @NotNull ActionEvent actionEvent) throws ControllerException {
        ihm.getApplicationController().afficherModuleDisponibilites();
    }

    @FXML
    private void afficherModuleTaches(@SuppressWarnings("unused") @NotNull ActionEvent actionEvent) throws ControllerException {
        ihm.getApplicationController().afficherModuleTaches();
    }

    @FXML
    private void afficherModuleCharges(@SuppressWarnings("unused") @NotNull ActionEvent actionEvent) throws ControllerException {
        ihm.getApplicationController().afficherModuleCharges();
    }

    @FXML
    private void saisirPrevisionsAbsence(@SuppressWarnings("unused") @NotNull ActionEvent actionEvent) throws ControllerException {
        ihm.getApplicationController().afficherModuleDisponibilites();
        ihm.getDisponibilitesController().getNbrsJoursAbsenceAccordion().setExpandedPane(ihm.getDisponibilitesController().getNbrsJoursAbsencePane());
        if (ihm.getDisponibilitesController().getNbrsJoursAbsenceTable().getItems().isEmpty()) {
            ihm.getDisponibilitesController().getNbrsJoursAbsenceTable().requestFocus();
        } else {
            TableViews.focusOnItem(
                    ihm.getDisponibilitesController().getNbrsJoursAbsenceTable(),
                    ihm.getDisponibilitesController().getNbrsJoursAbsenceTable().getItems().get(0)
            );
        }
    }

    @FXML
    private void deplierAccordeonParametres(@SuppressWarnings("unused") @NotNull ActionEvent actionEvent) throws ControllerException {
        ihm.getApplicationController().deplierAccordeonParametres();
        ihm.getPrimaryStage().requestFocus();
        {
            ChangeListener changeListener = (observable, oldValue, newValue) -> {
                try {
                    passerEtapeSuivante();
                } catch (ControllerException e) {
                    LOGGER.error("Impossibile de déplier l'accordéaon des paramètres suite à une modification de la date d'état.", e);
                }
            };
            ihm.getApplicationController().getDateEtatPicker().valueProperty().addListener(changeListener);
            ihm.getApplicationController().getDateEtatPicker().valueProperty().addListener((observable, oldValue, newValue) -> {
                ihm.getApplicationController().getDateEtatPicker().valueProperty().removeListener(changeListener);
            });
        }
        ihm.getApplicationController().getDateEtatPicker().show();
    }

    private void passerEtapeSuivante() throws ControllerException {

        WizardPane etapeCourante = etapes[noEtapeCourante - 1];

        // FIXME FDA 2017/08 Sans effet : ne passe pas à l'étape suivanrte.
        Optional<WizardPane> etapeSuivante = wizard.getFlow().advance(etapeCourante);
        if (!etapeSuivante.isPresent()) {
            throw new ControllerException("Impossible de passer à l'étape suivante.");
        }

        noEtapeCourante++;
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
    private void reporterTaches(@SuppressWarnings("unused") @NotNull ActionEvent actionEvent) throws ControllerException {
        ihm.getApplicationController().afficherModuleTaches();
        ihm.getTachesController().reporterTaches();
    }

    @FXML
    private void afficherTachesAyantRessourcesSurchargees(@SuppressWarnings("unused") @NotNull ActionEvent actionEvent) throws ControllerException {
        ihm.getApplicationController().activerModuleCharges();
        ihm.getChargesController().filtrerSurRessourceSurchargees();
    }

    @FXML
    private void afficherTachesAyantProfilsSurcharges(@SuppressWarnings("unused") @NotNull ActionEvent actionEvent) throws ControllerException {
        ihm.getApplicationController().activerModuleCharges();
        ihm.getApplicationController().activerModuleCharges();
        ihm.getChargesController().filtrerSurProfilsSurcharges();
    }
}

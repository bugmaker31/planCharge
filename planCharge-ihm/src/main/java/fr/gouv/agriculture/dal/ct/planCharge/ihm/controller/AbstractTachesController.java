package fr.gouv.agriculture.dal.ct.planCharge.ihm.controller;

import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.TacheBean;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.Importance;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import org.controlsfx.control.CheckComboBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.OptionalInt;

/**
 * Created by frederic.danna on 01/05/2017.
 */
public abstract class AbstractTachesController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractTachesController.class);

    // Les beans :
    ObservableList<TacheBean> taches = FXCollections.observableArrayList();
    ObservableList<Importance> importancesTaches = FXCollections.observableArrayList();
    ObservableList<String> codesProjetsApplisTaches = FXCollections.observableArrayList();
    ObservableList<String> codesRessourcesTaches = FXCollections.observableArrayList();
    ObservableList<String> codesProfilsTaches = FXCollections.observableArrayList();

    // Les filtres :
    @FXML
    protected TextField filtreGlobalField;
    @FXML
    protected TextField filtreNoTacheField;
    @FXML
    protected TextField filtreNoTicketIdalField;
    @FXML
    protected TextField filtreDescriptionField;
    @FXML
    protected CheckComboBox<String> filtreProjetsApplisField;
    @FXML
    protected DatePicker filtreDebutField;
    @FXML
    protected DatePicker filtreEcheanceField;
    @FXML
    protected CheckComboBox<String> filtreImportancesField;
    @FXML
    protected CheckComboBox<String> filtreRessourcesField;
    @FXML
    protected CheckComboBox<String> filtreProfilsField;

    @FXML
    protected void ajouterTache(ActionEvent event) {
        LOGGER.debug("ajouterTache...");
        TacheBean nouvTache = new TacheBean(
                idTacheSuivant(),
                "(pas de ticket IDAL)",
                null,
                null,
                null,
                null,
                null,
                0.0,
                null,
                null
        );
        taches.add(nouvTache);
    }

    int idTacheSuivant() {
        OptionalInt max = taches.stream().mapToInt(tacheBean -> tacheBean.getId()).max();
        return (!max.isPresent()) ? 1 : (max.getAsInt() + 1);
    }

    @FXML
    protected void razFiltres(ActionEvent event) {
        LOGGER.debug("RAZ des filtres...");
        filtreGlobalField.clear();
        filtreNoTacheField.clear();
        filtreNoTicketIdalField.clear();
        filtreDescriptionField.clear();
        filtreDebutField.setValue(null);
        filtreEcheanceField.setValue(null);
        filtreProjetsApplisField.getCheckModel().checkAll();
        filtreImportancesField.getCheckModel().checkAll();
        filtreProfilsField.getCheckModel().checkAll();
    }
}

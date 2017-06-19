package fr.gouv.agriculture.dal.ct.ihm.javafx;

import fr.gouv.agriculture.dal.ct.planCharge.metier.service.RapportService;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;

/**
 * Created by frederic.danna on 19/06/2017.
 */
@SuppressWarnings("ClassHasNoToStringMethod")
public class WorkProgressController {

    private static final Logger LOGGER = LoggerFactory.getLogger(WorkProgressController.class);

    @FXML
    private Label titreLabel;
    @FXML
    private Label messageLabel;
    @FXML
    private ProgressBar workProgressBar;
    @FXML
    private Label termineLabel;
    @FXML
    Button closeButton;

    public void start(@NotNull String titre, @NotNull String message, @NotNull Task<? extends RapportService> task, @NotNull final Stage workProgressStage) {
        titreLabel.setText(titre);
        messageLabel.setText(message);
        termineLabel.setVisible(false);
        closeButton.setVisible(false);
        closeButton.setOnAction(event -> workProgressStage.close());
        // Cf. https://stackoverflow.com/questions/29854627/progressbar-doesnt-work-with-a-fxml-file-and-a-controller
        workProgressBar.progressProperty().unbind();
        workProgressBar.progressProperty().bind(task.progressProperty());
        // Cf. https://stackoverflow.com/questions/29625170/display-popup-with-progressbar-in-javafx
        task.setOnSucceeded(event -> {
            termineLabel.setVisible(true);
            closeButton.setVisible(true);
        });
        new Thread(task).start();
        LOGGER.debug("Task start'ée...");
    }
}

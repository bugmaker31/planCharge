package fr.gouv.agriculture.dal.ct.ihm.view.component.controlStructure;

import javafx.beans.property.BooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

@SuppressWarnings("ClassHasNoToStringMethod")
public class If extends Pane {


    @SuppressWarnings("NullableProblems")
    @FXML
    @NotNull
    private BooleanProperty condition;

    @FXML
    @Null
    private Node thenContent;

    @FXML
    @Null
    private Node elseContent;

    public If() {
        super();
    }

    @SuppressWarnings("BooleanMethodNameMustStartWithQuestion")
    @NotNull
    public BooleanProperty getCondition() {
        return condition;
    }

    @FXML
    public void setCondition(@NotNull BooleanProperty condition) {
        this.condition = condition;

        condition.addListener((observable, oldValue, newValue) -> {
            getChildren().clear();
            if ((thenContent != null) && condition.get()) {
//            thenContent.setVisible(condition);
                getChildren().setAll(thenContent);
            }
            if ((elseContent != null) && !condition.get()) {
//            elseContent.setVisible(!condition);
                getChildren().add(elseContent);
            }
        });
    }

    @FXML
    @Null
    public Node getThenContent() {
        return thenContent;
    }

    @FXML
    public void setThenContent(@Null Node thenContent) {
        this.thenContent = thenContent;
    }

    @FXML
    @Null
    public Node getElseContent() {
        return elseContent;
    }

    @FXML
    public void setElseContent(@Null Node elseContent) {
        this.elseContent = elseContent;
    }

}

package fr.gouv.agriculture.dal.ct.ihm.view;

import javafx.scene.control.TableView;
import javafx.scene.layout.Pane;

public class HeaderHidableTableView<S> extends TableView<S> {


    private boolean isHeaderHidden = false;

    public boolean isHeaderHidden() {
        return isHeaderHidden;
    }

    public void setHeaderHidden(boolean headerHidden) {
        isHeaderHidden = headerHidden;
    }

    public void hideHeader() {
        setHeaderHidden(true);
    }

    public void showHeader() {
        setHeaderHidden(false);
    }

    // Cf. https://stackoverflow.com/questions/27118872/how-to-hide-tableview-column-header-in-javafx-8
    @Override
    public void resize(double width, double height) {
        super.resize(width, height);

        if (isHeaderHidden) {
            Pane headerRow = (Pane) lookup("TableHeaderRow");
            headerRow.setMinHeight(0);
            headerRow.setPrefHeight(0);
            headerRow.setMaxHeight(0);
            headerRow.setVisible(false);
        }
    }

}

package fr.gouv.agriculture.dal.ct.planCharge.ihm.view;

import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.referentiels.RessourceBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.referentiels.RessourceHumaineBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.tache.TacheBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.view.converter.Converters;
import javafx.collections.ObservableList;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.cell.ComboBoxTableCell;

import javax.validation.constraints.NotNull;

@SuppressWarnings("ClassHasNoToStringMethod")
public class RessourceCell<TB extends TacheBean> extends ComboBoxTableCell<TB, RessourceBean<?, ?>> {

    private final ContextMenu contextMenu = new ContextMenu();
    private final MenuItem afficherRessourceHumaineMenuItem = new MenuItem();
    private final MenuItem filtrerSurRessourceMenuItem = new MenuItem();


    public RessourceCell(@NotNull ObservableList<RessourceBean<?, ?>> items, @NotNull Runnable afficherRessourceHumaineRunnable, @NotNull Runnable filtrerSurRessourceRunnable) {
        super(Converters.RESSOURCE_BEAN_CONVERTER, items);

        afficherRessourceHumaineMenuItem.setOnAction(event -> afficherRessourceHumaineRunnable.run());
        filtrerSurRessourceMenuItem.setOnAction(event -> filtrerSurRessourceRunnable.run());

        setContextMenu(contextMenu);
    }


    @Override
    public void updateItem(RessourceBean<?, ?> item, boolean empty) {
        super.updateItem(item, empty);
        definirMenuContextuel();
    }

    private void definirMenuContextuel() {

        contextMenu.getItems().clear();

        if ((getItem() == null) || isEmpty()) {
            return;
        }

        String nomRsrc;
        if (!getItem().estHumain()) {
            nomRsrc = "'" + getItem().getCode() + "'";
        } else {
            RessourceHumaineBean rsrcHum = (RessourceHumaineBean) getItem();
            nomRsrc = rsrcHum.getPrenom() + " " + rsrcHum.getNom();
        }

        if (getItem().estHumain()) {
            afficherRessourceHumaineMenuItem.setText("Afficher la ressource humaine " + nomRsrc);
            contextMenu.getItems().add(afficherRessourceHumaineMenuItem);
        }

        filtrerSurRessourceMenuItem.setText("Filtrer sur " + nomRsrc);
        contextMenu.getItems().add(filtrerSurRessourceMenuItem);

    }
}
package fr.gouv.agriculture.dal.ct.planCharge.ihm.view;

import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.referentiels.ProjetAppliBean;
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
public class ProjetAppliCell<TB extends TacheBean> extends ComboBoxTableCell<TB, ProjetAppliBean> {

    private final ContextMenu contextMenu = new ContextMenu();
    private final MenuItem afficherProjetAppliMenuItem = new MenuItem();
    private final MenuItem filtrerSurProjetAppliMenuItem = new MenuItem();


    public ProjetAppliCell(@NotNull ObservableList<ProjetAppliBean> items, @NotNull Runnable afficherProjetAppliRunnable, @NotNull Runnable filtrerSurprojetAppliRunnable) {
        super(Converters.PROJET_APPLI_STRING_CONVERTER, items);

        afficherProjetAppliMenuItem.setOnAction(event -> afficherProjetAppliRunnable.run());
        filtrerSurProjetAppliMenuItem.setOnAction(event -> filtrerSurprojetAppliRunnable.run());

        setContextMenu(contextMenu);
    }


    @Override
    public void updateItem(ProjetAppliBean item, boolean empty) {
        super.updateItem(item, empty);
        definirMenuContextuel();
    }

    private void definirMenuContextuel() {

        contextMenu.getItems().clear();

        if ((getItem() == null) || isEmpty()) {
            return;
        }

        ProjetAppliBean projetAppliBean = getItem();

        //noinspection HardcodedFileSeparator
        afficherProjetAppliMenuItem.setText("Afficher le projet/appli '" + projetAppliBean.getCode() + "'");
        contextMenu.getItems().add(afficherProjetAppliMenuItem);

        //noinspection HardcodedFileSeparator
        filtrerSurProjetAppliMenuItem.setText("Filtrer sur le projet/appli '" + projetAppliBean.getCode() + "'");
        contextMenu.getItems().add(filtrerSurProjetAppliMenuItem);

    }
}
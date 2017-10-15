package fr.gouv.agriculture.dal.ct.planCharge.ihm.view;

import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.referentiels.ProfilBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.referentiels.ProjetAppliBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.tache.TacheBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.view.converter.Converters;
import javafx.collections.ObservableList;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.cell.ComboBoxTableCell;

import javax.validation.constraints.NotNull;

@SuppressWarnings("ClassHasNoToStringMethod")
public class ProfilCell<TB extends TacheBean> extends ComboBoxTableCell<TB, ProfilBean> {

    private final ContextMenu contextMenu = new ContextMenu();
    private final MenuItem afficherProfilMenuItem = new MenuItem();
    private final MenuItem filtrerSurProfilMenuItem = new MenuItem();


    public ProfilCell(@NotNull ObservableList<ProfilBean> items, @NotNull Runnable afficherProfilMenuItem, @NotNull Runnable filtrerSurProfilMenuItem) {
        super(Converters.PROFIL_BEAN_CONVERTER, items);

        this.afficherProfilMenuItem.setOnAction(event -> afficherProfilMenuItem.run());
        this.filtrerSurProfilMenuItem.setOnAction(event -> filtrerSurProfilMenuItem.run());

        setContextMenu(contextMenu);
    }


    @Override
    public void updateItem(ProfilBean item, boolean empty) {
        super.updateItem(item, empty);
        definirMenuContextuel();
    }

    private void definirMenuContextuel() {

        contextMenu.getItems().clear();

        if ((getItem() == null) || isEmpty()) {
            return;
        }

        ProfilBean profilBean = getItem();

        //noinspection HardcodedFileSeparator
        afficherProfilMenuItem.setText("Afficher le profil '" + profilBean.getCode() + "'");
        contextMenu.getItems().add(afficherProfilMenuItem);

        //noinspection HardcodedFileSeparator
        filtrerSurProfilMenuItem.setText("Filtrer sur le profil '" + profilBean.getCode() + "'");
        contextMenu.getItems().add(filtrerSurProfilMenuItem);

    }
}
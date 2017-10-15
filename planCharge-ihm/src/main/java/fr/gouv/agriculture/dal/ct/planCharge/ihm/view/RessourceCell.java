package fr.gouv.agriculture.dal.ct.planCharge.ihm.view;

import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.referentiels.RessourceBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.referentiels.RessourceHumaineBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.tache.TacheBean;
import fr.gouv.agriculture.dal.ct.planCharge.util.Objects;
import javafx.collections.ObservableList;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.cell.ComboBoxTableCell;

import javax.validation.constraints.NotNull;

@SuppressWarnings("ClassHasNoToStringMethod")
public class RessourceCell<TB extends TacheBean> extends ComboBoxTableCell<TB, RessourceBean<?, ?>> {

    private final Runnable filtrerSurRessourceRunnable;
    private MenuItem menuItem;

    public RessourceCell(@NotNull ObservableList<RessourceBean<?, ?>> items, @NotNull Runnable filtrerSurRessourceRunnable) {
        super(Converters.RESSOURCE_BEAN_CONVERTER, items);
        this.filtrerSurRessourceRunnable = filtrerSurRessourceRunnable;
        definirMenuContextuel();
    }

    @Override
    public void updateItem(RessourceBean<?, ?> item, boolean empty) {
        super.updateItem(item, empty);
        if ((item == null) || empty) {
            return;
        }
        String nomRsrc;
        if (!item.estHumain()) {
            nomRsrc = "'" + item.getCode() + "'";
        } else {
            RessourceHumaineBean rsrcHum = (RessourceHumaineBean) item;
            nomRsrc = rsrcHum.getPrenom() + " " + rsrcHum.getNom();
        }
        menuItem.setText("Filtrer sur " + nomRsrc);
    }

    private void definirMenuContextuel() {

        menuItem = new MenuItem();
        menuItem.setOnAction(event -> filtrerSurRessourceRunnable.run());

        ContextMenu contextMenu = Objects.value(getContextMenu(), new ContextMenu());
        if (!contextMenu.getItems().isEmpty())  {
            contextMenu.getItems().add(new SeparatorMenuItem());
        }
        contextMenu.getItems().addAll(
                menuItem
        );
        setContextMenu(contextMenu);
    }
}
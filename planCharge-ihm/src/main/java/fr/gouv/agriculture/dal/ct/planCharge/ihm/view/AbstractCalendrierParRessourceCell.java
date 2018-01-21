package fr.gouv.agriculture.dal.ct.planCharge.ihm.view;

import fr.gouv.agriculture.dal.ct.ihm.IhmException;
import fr.gouv.agriculture.dal.ct.ihm.controller.calculateur.Calculateur;
import fr.gouv.agriculture.dal.ct.ihm.view.NotEditableTextFieldTableCell;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.PlanChargeIhm;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.DisponibilitesController;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.AbstractCalendrierParRessourceBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.charge.PlanChargeBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.referentiels.RessourceBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.referentiels.RessourceHumaineBean;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TableRow;
import javafx.util.StringConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.time.LocalDate;
import java.util.Objects;

public abstract class AbstractCalendrierParRessourceCell<R extends RessourceBean<?, ?>, S extends AbstractCalendrierParRessourceBean<R, ?, S, ?>, T extends Number>
        extends NotEditableTextFieldTableCell<S, T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractCalendrierParRessourceCell.class);

    //    Autowired
    @NotNull
    private final PlanChargeBean planChargeBean = PlanChargeBean.instance();


    private int noSemaine;


    protected AbstractCalendrierParRessourceCell(int noSemaine, @NotNull StringConverter<T> stringConverter, @Null Runnable cantEditErrorDisplayer) {
        super(stringConverter, cantEditErrorDisplayer);
        this.noSemaine = noSemaine;

        definirMenuContextuel();
    }

    protected AbstractCalendrierParRessourceCell(int noSemaine, @NotNull StringConverter<T> stringConverter) {
        this(noSemaine, stringConverter, null);
    }


    @NotNull
    protected PlanChargeBean getPlanChargeBean() {
        return planChargeBean;
    }

    protected int getNoSemaine() {
        return noSemaine;
    }

    //    TODO FDA 2017/09 Définir le menu contextuel dans le FXML.
    private void definirMenuContextuel() {
        Platform.runLater(() -> { // Sinon getTableView() == null.

            // NB : Le menu contextuel peut partiellement etre défini dans le FXML, donc il ne faut pas l'écraser.
            if ((getTableView().getContextMenu() == null) || getTableView().getContextMenu().getItems().isEmpty()) {
                setContextMenu(new ContextMenu());
            } else {
                ContextMenu tableContextMenu = getTableView().getContextMenu();

                ContextMenu contextMenu = new ContextMenu(tableContextMenu.getItems().toArray(new MenuItem[0]));
                contextMenu.getItems().add(new SeparatorMenuItem());
                setContextMenu(contextMenu);
            }

            {
                MenuItem prolongerADroiteMenuItem = new MenuItem("Prolonger sur la droite");
                prolongerADroiteMenuItem.setOnAction((ActionEvent event) -> {
                    //noinspection unchecked
                    TableRow<S> tableRow = getTableRow();
                    if (tableRow == null) { // Un clic sur le TableHeaderRow fait retourner null à getTableRow().
                        return;
                    }
                    S item = tableRow.getItem();
                    if (item == null) {
                        return;
                    }
                    T valeur = getItem();
                    if (valeur == null) {
                        return;
                    }
                    if (planChargeBean.getDateEtat() == null) {
                        LOGGER.warn("Date d'état non définie !?");
                        return;
                    }
                    try {
                        Calculateur.executerSansCalculer(() -> prolonger(item, valeur, planChargeBean.getDateEtat()));
                    } catch (IhmException e) {
//                TODO FDA 2017/09 Trouver mieux que juste loguer une erreur.
                        LOGGER.error("Impossible de prolonger la valeur sur la droite.", e);
                    }

                });
                getContextMenu().getItems().add(prolongerADroiteMenuItem);
            }

            {
                MenuItem prolongerTousProfilsADroiteMenuItem = new MenuItem("Prolonger sur la droite pour la ressource");
                prolongerTousProfilsADroiteMenuItem.setOnAction((ActionEvent event) -> {
                    //noinspection unchecked
                    TableRow<S> tableRow = getTableRow();
                    if (tableRow == null) { // Un clic sur le TableHeaderRow fait retourner null à getTableRow().
                        return;
                    }
                    S item = tableRow.getItem();
                    if (item == null) {
                        return;
                    }
                    T valeur = getItem();
                    if (valeur == null) {
                        return;
                    }
                    if (planChargeBean.getDateEtat() == null) {
                        LOGGER.warn("Date d'état non définie !?");
                        return;
                    }
                    item.ressourceBeanProperty().addListener((observable, oldValue, newValue) -> prolongerTousProfilsADroiteMenuItem.setDisable(newValue == null));
                    if (item.getRessourceBean() != null) {
                        try {
                            Calculateur.executerSansCalculer(() -> prolongerPourRessource(item.getRessourceBean(), planChargeBean.getDateEtat()));
                        } catch (IhmException e) {
//                TODO FDA 2017/09 Trouver mieux que juste loguer une erreur.
                            LOGGER.error("Impossible de prolonger la valeur sur la droite.", e);
                        }
                    }
                });
                getContextMenu().getItems().add(prolongerTousProfilsADroiteMenuItem);
            }
        });
    }

    private void prolonger(@NotNull S item, @NotNull T valeur, @NotNull LocalDate dateEtat) {
        for (int cptSemaine = noSemaine + 1; cptSemaine <= PlanChargeIhm.NBR_SEMAINES_PLANIFIEES; cptSemaine++) {
            LocalDate debutPeriode = dateEtat.plusDays(((long) cptSemaine - 1L) * 7L); // TODO FDA 2017/06 [issue#26:PeriodeHebdo/Trim]
            //noinspection unchecked
            item.get(debutPeriode).setValue(valeur);
        }
    }

    private void prolongerPourRessource(@NotNull R ressourceBean, @NotNull LocalDate dateEtat) {
        LocalDate debutPeriode = dateEtat.plusDays(((long) noSemaine - 1L) * 7L); // TODO FDA 2017/06 [issue#26:PeriodeHebdo/Trim]
        for (S item : getTableView().getItems()) {
            if (!Objects.equals(item.getRessourceBean(), ressourceBean)) {
                continue;
            }
            T valeur = (T) item.get(debutPeriode).getValue();
            prolonger(item, valeur, dateEtat);
        }
    }


    @Override
    public void updateItem(@Null T item, boolean empty) {
        super.updateItem(item, empty);
//        formater();
        styler();
    }

    private void formater() {
        if ((getItem() == null) || isEmpty()) {
            setText(null);
            setGraphic(null);
            return;
        }
        setText(getConverter().toString(getItem()));
    }

    protected void styler() {

        // Réinit des style spécifiques :
        pseudoClassStateChanged(DisponibilitesController.AVANT_MISSION, false);
        pseudoClassStateChanged(DisponibilitesController.PENDANT_MISSION, false);
        pseudoClassStateChanged(DisponibilitesController.APRES_MISSION, false);


        /* Non, surtout pas, sinon les cellules vides, ne seront pas stylées/décorées.
        // Stop, si cellule vide :
        if (empty || (item == null)) {
            return;
        }
        */

        // Récupération des infos sur la cellule :
        //noinspection unchecked
        TableRow<? extends S> tableRow = getTableRow();
        if (tableRow == null) { // Un clic sur le TableHeaderRow fait retourner null à getTableRow().
            return;
        }
        S bean = tableRow.getItem();
        if (bean == null) {
            return;
        }
        RessourceHumaineBean ressourceHumaineBean;
/*
        try {
*/
        if (!bean.getRessourceBean().estHumain()) {
            return;
        }
        ressourceHumaineBean = (RessourceHumaineBean) bean.getRessourceBean();
/*
        } catch (BeanException e) {
            // TODO FDA 2017/09 Trouver mieux que juste loguer une erreur.
            LOGGER.error("Impossible de déterminer si la ressource '" + bean.getRessourceBean().getCode() + "' est humaine.", e);
            return;
        }
*/
        LocalDate debutMission = ressourceHumaineBean.getDebutMission();
        LocalDate finMission = ressourceHumaineBean.getFinMission();

        if (planChargeBean.getDateEtat() == null) {
            return;
        }
        LocalDate debutPeriode = planChargeBean.getDateEtat().plusDays(((long) noSemaine - 1L) * 7L); // TODO FDA 2017/06 [issue#26:PeriodeHebdo/Trim]
        LocalDate finPeriode = debutPeriode.plusDays(7L);// TODO FDA 2017/06 [issue#26:PeriodeHebdo/Trim]

        // Formatage du style (CSS) de la cellule :
        FORMAT_PERIODE:
        {
            if (debutMission != null) {
                if (debutPeriode.isBefore(debutMission)) {
                    pseudoClassStateChanged(DisponibilitesController.AVANT_MISSION, true);
                    break FORMAT_PERIODE;
                }
            }
            if (finMission != null) {
                if (finPeriode.isAfter(finMission.plusDays(7))) { // TODO FDA 2017/06 [issue#26:PeriodeHebdo/Trim]
                    pseudoClassStateChanged(DisponibilitesController.APRES_MISSION, true);
                    break FORMAT_PERIODE;
                }
            }
            pseudoClassStateChanged(DisponibilitesController.PENDANT_MISSION, true);
        }
    }

}

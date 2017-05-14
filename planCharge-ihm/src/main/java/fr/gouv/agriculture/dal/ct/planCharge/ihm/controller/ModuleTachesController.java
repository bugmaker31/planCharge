package fr.gouv.agriculture.dal.ct.planCharge.ihm.controller;

import fr.gouv.agriculture.dal.ct.planCharge.ihm.IhmException;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.PlanChargeBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.TacheBean;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;

/**
 * Created by frederic.danna on 26/03/2017.
 */
public class ModuleTachesController extends AbstractTachesController<TacheBean> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ModuleTachesController.class);

    private static ModuleTachesController instance;

    public static ModuleTachesController instance() {
        return instance;
    }

    @NotNull
    private PlanChargeBean planChargeBean = PlanChargeBean.instance();

    // TODO FDA 2017/05 Résoudre le warning de compilation (unchecked assignement).
    @NotNull
    private ObservableList<TacheBean> planificationsBeans = (ObservableList)planChargeBean.getPlanificationsBeans();

    /**
     * The constructor.
     * The constructor is called before the initialize() method.
     */
    public ModuleTachesController() throws IhmException {
        super();
        if (instance != null) {
            throw new IhmException("Instanciation à plus d'1 exemplaire.");
        }
        instance = this;
    }

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    void initialize() throws IhmException {
        setTachesBeans(planificationsBeans);
        super.initialize();
    }

    // Surchargée juste pour pouvoir ajouter le @FXML.
    @FXML
    @Override
    protected void ajouterTache(ActionEvent event) {
        super.ajouterTache(event);
    }

    @Override
    TacheBean nouveauBean() {
        TacheBean tacheBean;
        tacheBean = new TacheBean(
                idTacheSuivant(),
                null,
                null,
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
        return tacheBean;
    }
}

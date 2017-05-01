package fr.gouv.agriculture.dal.ct.planCharge.ihm.controller;

import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.PlanChargeBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.PlanificationBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.TacheBean;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;

/**
 * Created by frederic.danna on 26/03/2017.
 */
public class ModuleTachesController extends AbstractTachesController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ModuleTachesController.class);

    @NotNull
    private PlanChargeBean planChargeBean = PlanChargeBean.instance();

    @NotNull
    private ObservableList<PlanificationBean> planificationsBeans = planChargeBean.getPlanificationsBeans();

    /**
     * The constructor.
     * The constructor is called before the initialize() method.
     */
    public ModuleTachesController() {
        super();
    }

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    void initialize() {
        setTachesBeans(planificationsBeans);
        super.initialize();
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

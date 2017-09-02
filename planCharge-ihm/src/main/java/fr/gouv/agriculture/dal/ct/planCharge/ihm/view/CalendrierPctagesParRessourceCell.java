package fr.gouv.agriculture.dal.ct.planCharge.ihm.view;

import fr.gouv.agriculture.dal.ct.ihm.controller.ControllerException;
import fr.gouv.agriculture.dal.ct.ihm.model.BeanException;
import fr.gouv.agriculture.dal.ct.ihm.view.PercentageStringConverter;
import fr.gouv.agriculture.dal.ct.metier.dto.AbstractDTO;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.calculateur.CalculateurDisponibilites;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.AbstractCalendrierParRessourceBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.charge.PlanChargeBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.referentiels.RessourceBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.referentiels.RessourceHumaineBean;
import fr.gouv.agriculture.dal.ct.planCharge.metier.service.DisponibilitesService;
import fr.gouv.agriculture.dal.ct.planCharge.util.number.Percentage;
import fr.gouv.agriculture.dal.ct.planCharge.util.number.PercentageProperty;
import javafx.scene.control.TableRow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.time.LocalDate;

public class CalendrierPctagesParRessourceCell<R extends RessourceBean, T extends AbstractCalendrierParRessourceBean<R, AbstractDTO, T, PercentageProperty>> extends AbstractCalendrierParRessourceCell<R, T, Percentage> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CalendrierPctagesParRessourceCell.class);

    //    @Autowired
    @NotNull
    private final CalculateurDisponibilites calculateurDisponibilites = CalculateurDisponibilites.instance(); /*new CalculateurDisponibilites();*/


    public CalendrierPctagesParRessourceCell(@NotNull PlanChargeBean planChargeBean, int noSemaine, @Null Runnable cantEditErrorDisplayer) {
        super(planChargeBean, noSemaine, new PercentageStringConverter(), cantEditErrorDisplayer);
    }

    public CalendrierPctagesParRessourceCell(@NotNull PlanChargeBean planChargeBean, int noSemaine) {
        this(planChargeBean, noSemaine, null);
    }

    @Override
    public void commitEdit(Percentage newValue) {
        super.commitEdit(newValue);

        //noinspection unchecked
        TableRow<T> tableRow = getTableRow();
        T pctagesDispoCTBean = tableRow.getItem();
        if (pctagesDispoCTBean == null) {
            return;
        }
        if (getPlanChargeBean().getDateEtat() == null) {
            LOGGER.warn("Date d'état non définie !?");
            return;
        }
        LocalDate debutPeriode = getPlanChargeBean().getDateEtat().plusDays((getNoSemaine() - 1) * 7); // TODO FDA 2017/06 [issue#26:PeriodeHebdo/Trim]
        if (!pctagesDispoCTBean.containsKey(debutPeriode)) {
            pctagesDispoCTBean.put(debutPeriode, new PercentageProperty(DisponibilitesService.PCTAGE_DISPO_RSRC_DEFAUT.floatValue()));
        }
        PercentageProperty pctageDispoCTPeriodeProperty = pctagesDispoCTBean.get(debutPeriode);
        pctageDispoCTPeriodeProperty.setValue(newValue);

        try {
            R ressourceBean = pctagesDispoCTBean.getRessourceBean();
            if (ressourceBean.estHumain()) {
                RessourceHumaineBean ressourceHumaineBean = (RessourceHumaineBean) ressourceBean;
                calculateurDisponibilites.calculer(ressourceHumaineBean, getNoSemaine());
            }
        } catch (BeanException | ControllerException e) {
            // TODO FDA 2017/08 Trouver mieux que juste loguer une erreur.
            LOGGER.error("Impossible de calculer.", e);
        }
    }
}


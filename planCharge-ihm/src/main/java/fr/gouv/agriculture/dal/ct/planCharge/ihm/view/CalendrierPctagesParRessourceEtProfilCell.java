package fr.gouv.agriculture.dal.ct.planCharge.ihm.view;

import fr.gouv.agriculture.dal.ct.ihm.controller.ControllerException;
import fr.gouv.agriculture.dal.ct.ihm.view.PercentageStringConverter;
import fr.gouv.agriculture.dal.ct.metier.dto.AbstractDTO;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.calculateur.CalculateurDisponibilites;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.AbstractCalendrierRessourceProfilBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.charge.PlanChargeBean;
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

public class CalendrierPctagesParRessourceEtProfilCell<T extends AbstractCalendrierRessourceProfilBean<RessourceHumaineBean, AbstractDTO, T, PercentageProperty>> extends AbstractCalendrierParRessourceCell<RessourceHumaineBean, T, Percentage> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CalendrierPctagesParRessourceEtProfilCell.class);

    //    @Autowired
    @NotNull
    private final CalculateurDisponibilites calculateurDisponibilites = CalculateurDisponibilites.instance(); /*new CalculateurDisponibilites();*/


    public CalendrierPctagesParRessourceEtProfilCell(@NotNull PlanChargeBean planChargeBean, int noSemaine, @Null Runnable cantEditErrorDisplayer) {
        super(planChargeBean, noSemaine, new PercentageStringConverter(), cantEditErrorDisplayer);
    }

    public CalendrierPctagesParRessourceEtProfilCell(@NotNull PlanChargeBean planChargeBean, int noSemaine) {
        this(planChargeBean, noSemaine, null);
    }

    @Override
    public void commitEdit(Percentage newValue) {
        super.commitEdit(newValue);

        //noinspection unchecked
        TableRow<T> tableRow = getTableRow();
        T pctagesDispoRsrcProfilBean = tableRow.getItem();
        if (pctagesDispoRsrcProfilBean == null) {
            return;
        }
        if (getPlanChargeBean().getDateEtat() == null) {
            LOGGER.warn("Date d'état non définie !?");
            return;
        }
        LocalDate debutPeriode = getPlanChargeBean().getDateEtat().plusDays((getNoSemaine() - 1) * 7); // TODO FDA 2017/06 [issue#26:PeriodeHebdo/Trim]
        if (!pctagesDispoRsrcProfilBean.containsKey(debutPeriode)) {
            pctagesDispoRsrcProfilBean.put(debutPeriode, new PercentageProperty(DisponibilitesService.PCTAGE_DISPO_RSRC_DEFAUT.floatValue()));
        }
        PercentageProperty pctageDispoCTPeriodeProperty = pctagesDispoRsrcProfilBean.get(debutPeriode);
        pctageDispoCTPeriodeProperty.setValue(newValue);

        try {
            calculateurDisponibilites.calculer(pctagesDispoRsrcProfilBean.getRessourceBean(), getNoSemaine());
        } catch (ControllerException e) {
            // TODO FDA 2017/08 Trouver mieux que juste loguer une erreur.
            LOGGER.error("Impossible de màj les disponibilités.", e);
        }
    }
}


package fr.gouv.agriculture.dal.ct.planCharge.ihm.view;

import fr.gouv.agriculture.dal.ct.ihm.controller.ControllerException;
import fr.gouv.agriculture.dal.ct.ihm.model.BeanException;
import fr.gouv.agriculture.dal.ct.metier.dto.AbstractDTO;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.calculateur.CalculateurDisponibilites;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.AbstractCalendrierParRessourceBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.charge.PlanChargeBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.referentiels.RessourceBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.referentiels.RessourceHumaineBean;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.scene.control.TableRow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.time.LocalDate;

@SuppressWarnings("ClassHasNoToStringMethod")
public class CalendrierFractionsJoursParRessourceCell<R extends RessourceBean, T extends AbstractCalendrierParRessourceBean<R, AbstractDTO, T, FloatProperty>> extends AbstractCalendrierParRessourceCell<R, T, Float> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CalendrierFractionsJoursParRessourceCell.class);

    //    @Autowired
    @NotNull
    private final CalculateurDisponibilites calculateurDisponibilites = CalculateurDisponibilites.instance(); /*new CalculateurDisponibilites();*/


    public CalendrierFractionsJoursParRessourceCell(@NotNull PlanChargeBean planChargeBean, int noSemaine, @Null Runnable cantEditErrorDisplayer) {
        super(planChargeBean, noSemaine, Converters.HUITIEMES_JOURS_STRING_CONVERTER, cantEditErrorDisplayer); // TODO FDA 2017/09 Confimer le Converter.
    }

    public CalendrierFractionsJoursParRessourceCell(@NotNull PlanChargeBean planChargeBean, int noSemaine) {
        this(planChargeBean, noSemaine, null);
    }

    @Override
    public void commitEdit(Float newValue) {
        super.commitEdit(newValue);

        //noinspection unchecked
        TableRow<T> tableRow = getTableRow();
        if (tableRow == null) { // Un clic sur le TableHeaderRow fait retourner null à getTableRow().
            return;
        }
        T nbrJoursAbsenceBean = tableRow.getItem();
        if (nbrJoursAbsenceBean == null) {
            return;
        }
        if (getPlanChargeBean().getDateEtat() == null) {
            LOGGER.warn("Date d'état non définie !?");
            return;
        }
        LocalDate debutPeriode = getPlanChargeBean().getDateEtat().plusDays((getNoSemaine() - 1) * 7); // TODO FDA 2017/06 [issue#26:PeriodeHebdo/Trim]
        if (!nbrJoursAbsenceBean.containsKey(debutPeriode)) {
            nbrJoursAbsenceBean.put(debutPeriode, new SimpleFloatProperty());
        }
        FloatProperty nbrJoursDAbsencePeriode = nbrJoursAbsenceBean.get(debutPeriode);
        nbrJoursDAbsencePeriode.set(newValue);

        try {
            R ressourceBean = nbrJoursAbsenceBean.getRessourceBean();
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


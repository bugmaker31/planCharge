package fr.gouv.agriculture.dal.ct.planCharge.metier.service;

import fr.gouv.agriculture.dal.ct.metier.dao.DaoException;
import fr.gouv.agriculture.dal.ct.metier.service.ServiceException;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dao.referentiels.JourFerieDao;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.JourFerie;
import fr.gouv.agriculture.dal.ct.planCharge.util.Dates;
import fr.gouv.agriculture.dal.ct.planCharge.util.number.Percentage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.stream.Collectors;

public class DisponibilitesService {

    public static final Percentage PCTAGE_DISPO_CT_MIN = new Percentage(90f);


    @NotNull
    private static final Logger LOGGER = LoggerFactory.getLogger(DisponibilitesService.class);


    @NotNull
    public static DisponibilitesService instance() {
        return InstanceHolder.INSTANCE;
    }


    // Fields :

    //    @Autowired
    @NotNull
    private JourFerieDao jourFerieDao = JourFerieDao.instance();


    // Constructeurs :

    // 'private' pour empêcher quiconque d'autre d'instancier cette classe (pattern "Factory").
    private DisponibilitesService() {
        super();
    }


    // Méthods :

    public int nbrJoursOuvres(@NotNull LocalDate debutPeriode, @NotNull LocalDate finPeriode) throws ServiceException {
        assert finPeriode.isAfter(debutPeriode);
/*
        Period period = Period.between(debutPeriode, finPeriode); // Cf. https://stackoverflow.com/questions/33530011/java-most-efficient-way-to-subtract-dates
        int nbrJoursDsPeriode = period.getDays();
        int nbrJoursFeriesDsPeriode = nbrJoursFeries(debutPeriode, finPeriode);
        return nbrJoursDsPeriode - nbrJoursFeriesDsPeriode;
*/
        int nbrJoursOuvresDsPeriode = 0;

        Period period = Period.between(debutPeriode, finPeriode); // Cf. https://stackoverflow.com/questions/33530011/java-most-efficient-way-to-subtract-dates
        int nbrJoursDsPeriode = period.getDays();

        List<JourFerie> joursFeries;
        try {
            joursFeries = jourFerieDao.list();
        } catch (DaoException e) {
            throw new ServiceException("Impossible de récupérer la liste des jours fériés.", e);
        }
        List<LocalDate> datesJoursFeries = joursFeries.stream()
                .map(jourFerie -> jourFerie.getDate())
                .collect(Collectors.toList());

        for (int cptJour = 0; cptJour < nbrJoursDsPeriode; cptJour++) {
            LocalDate jour = debutPeriode.plusDays(cptJour);
            if (Dates.isWeekEnd(jour)) {
                continue;
            }
            if (datesJoursFeries.contains(jour)) {
                continue;
            }
            nbrJoursOuvresDsPeriode++;
        }

        return nbrJoursOuvresDsPeriode;
    }


    private static class InstanceHolder {
        private static final DisponibilitesService INSTANCE = new DisponibilitesService();
    }

}

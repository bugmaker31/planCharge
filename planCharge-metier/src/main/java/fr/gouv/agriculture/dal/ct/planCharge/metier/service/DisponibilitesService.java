package fr.gouv.agriculture.dal.ct.planCharge.metier.service;

import fr.gouv.agriculture.dal.ct.metier.dao.DaoException;
import fr.gouv.agriculture.dal.ct.metier.service.ServiceException;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dao.referentiels.JourFerieDao;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.JourFerie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class DisponibilitesService {

    @NotNull
    private static final Logger LOGGER = LoggerFactory.getLogger(DisponibilitesService.class);


    @NotNull
    public static DisponibilitesService instance() {
        return InstanceHolder.INSTANCE;
    }

    public int nbrJoursOuvres(@NotNull LocalDate debutPeriode, @NotNull LocalDate finPeriode) throws ServiceException {
        assert finPeriode.isAfter(debutPeriode);
        Period period = Period.between(debutPeriode, finPeriode); // Cf. https://stackoverflow.com/questions/33530011/java-most-efficient-way-to-subtract-dates
        int nbrJoursDsPeriode = period.getDays();
        int nbrJoursFeriesDsPeriode = nbrJoursFeries(debutPeriode, finPeriode);
        return nbrJoursDsPeriode - nbrJoursFeriesDsPeriode;
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

    private int nbrJoursFeries(@NotNull LocalDate debutPeriode, @NotNull LocalDate finPeriode) throws ServiceException {
        try {
            List<JourFerie> joursFeries = jourFerieDao.list();
            return 0;  // TODO FDA 2017/08 Coder.
        } catch (DaoException e) {
            throw new ServiceException("Impossible de calculer le nombre de jours fériés entre le " + debutPeriode.format(DateTimeFormatter.ISO_LOCAL_DATE) + " et le " + finPeriode.format(DateTimeFormatter.ISO_LOCAL_DATE) + ".", e);
        }
    }

    private static class InstanceHolder {
        private static final DisponibilitesService INSTANCE = new DisponibilitesService();
    }

}

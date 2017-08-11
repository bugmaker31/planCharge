package fr.gouv.agriculture.dal.ct.planCharge.metier.service;

import fr.gouv.agriculture.dal.ct.metier.dao.DaoException;
import fr.gouv.agriculture.dal.ct.metier.service.ServiceException;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dao.referentiels.JourFerieDao;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.JourFerie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class DisponibilitesService {

    @NotNull
    private static final Logger LOGGER = LoggerFactory.getLogger(DisponibilitesService.class);
    //    @Autowired
    @NotNull
    private JourFerieDao jourFerieDao = JourFerieDao.instance();

    // 'private' pour empêcher quiconque d'autre d'instancier cette classe (pattern "Factory").
    private DisponibilitesService() {
        super();
    }

    @NotNull
    public static DisponibilitesService instance() {
        return InstanceHolder.INSTANCE;
    }

    public int nbrJoursOuvres(@NotNull LocalDate debutPeriode) throws ServiceException {
        try {
            List<JourFerie> joursFeries = jourFerieDao.list();
            return 5; // TODO FDA 2017/08 Coder.
        } catch (DaoException e) {
            throw new ServiceException("Impossible de calculer le nombre de jours ouvrés dans la période débutant le " + debutPeriode.format(DateTimeFormatter.ISO_LOCAL_DATE) + "." , e);
        }
    }

    private static class InstanceHolder {
        private static final DisponibilitesService INSTANCE = new DisponibilitesService();
    }

}

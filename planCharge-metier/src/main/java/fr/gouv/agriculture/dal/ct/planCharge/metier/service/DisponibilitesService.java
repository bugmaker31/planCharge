package fr.gouv.agriculture.dal.ct.planCharge.metier.service;

import fr.gouv.agriculture.dal.ct.metier.dao.DaoException;
import fr.gouv.agriculture.dal.ct.metier.service.ServiceException;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dao.referentiels.JourFerieDao;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dto.ProfilDTO;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dto.RessourceHumaineDTO;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.JourFerie;
import fr.gouv.agriculture.dal.ct.planCharge.util.Dates;
import fr.gouv.agriculture.dal.ct.planCharge.util.number.Percentage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@SuppressWarnings("ClassHasNoToStringMethod")
public final class DisponibilitesService {

    public static final Percentage PCTAGE_DISPO_CT_MIN = new Percentage(90f);

    public static final Percentage PCTAGE_DISPO_RSRC_DEFAUT = new Percentage(0f);

    public static final Float NBR_JOURS_ABSENCE_RSRC_DEFAUT = 0f;


    @NotNull
    private static final Logger LOGGER = LoggerFactory.getLogger(DisponibilitesService.class);

    @SuppressWarnings("UtilityClass")
    private static final class InstanceHolder {

        private static final DisponibilitesService INSTANCE = new DisponibilitesService();
    }

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
                .map(JourFerie::getDate)
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

    // TODO FDA 2017/08 Gérer le cas où la mission commence/s'arrête en milieu de période (semaine).
    public float nbrJoursDispoMinAgri(@NotNull LocalDate debutPeriode, @Null LocalDate debutMission, @Null LocalDate finMission, int nbrJoursOuvresPeriode, float nbrsJoursAbsencePeriode) throws ServiceException {
        if (estHorsMission(debutPeriode, debutMission, finMission)) {
            return 0f;
        }
        return Math.max(nbrJoursOuvresPeriode - nbrsJoursAbsencePeriode, 0f);
    }

    public Percentage pctageDispoCT(@NotNull LocalDate debutPeriode, @Null LocalDate debutMission, @Null LocalDate finMission) throws ServiceException {
        if (estHorsMission(debutPeriode, debutMission, finMission)) {
            return new Percentage(0f);
        }
        return PCTAGE_DISPO_CT_MIN;
    }

    public float nbrJoursDispoCT(float nbrJoursDispoMinAgriPeriode, @NotNull Percentage pctageDispoCTPeriode) throws ServiceException {
        //noinspection MagicNumber
        return (nbrJoursDispoMinAgriPeriode * pctageDispoCTPeriode.floatValue()) / 100f;
    }

    public float nbrJoursDispoMaxRsrcProfil(@NotNull LocalDate debutPeriode, @Null LocalDate debutMission, @Null LocalDate finMission, float nbrJoursDispo, @NotNull Percentage pctageDispo) throws ServiceException {
        if (estHorsMission(debutPeriode, debutMission, finMission)) {
            return 0f;
        }
        //noinspection MagicNumber
        return (nbrJoursDispo * pctageDispo.floatValue()) / 100f;
    }

    public Map<ProfilDTO, Float> nbrsJoursDispoMaxProfil(@NotNull LocalDate debutPeriode, @NotNull Map<RessourceHumaineDTO, Map<ProfilDTO, Map<LocalDate, Float>>> nbrsJoursDispoMaxRsrcProfil) throws ServiceException {
        Map<ProfilDTO, Float> nbrsJoursDispoMaxProfil = new TreeMap<>(); // TreeMap au lieu de HashMap pour trier, juste afin de faciliter le débogage.
        for (Map<ProfilDTO, Map<LocalDate, Float>> nbrsJoursDispoMaxProfils : nbrsJoursDispoMaxRsrcProfil.values()) {
            for (ProfilDTO profilDTO : nbrsJoursDispoMaxProfils.keySet()) {
                if (!nbrsJoursDispoMaxProfil.containsKey(profilDTO)) {
                    nbrsJoursDispoMaxProfil.put(profilDTO, 0f);
                }
                for (LocalDate periode : nbrsJoursDispoMaxProfils.get(profilDTO).keySet().stream().filter(localDate -> localDate.equals(debutPeriode)).collect(Collectors.toList())) {
                    Float nbrJoursDispoRsrcProfil = nbrsJoursDispoMaxProfils.get(profilDTO).get(periode);

                    Float nbrJoursDispoMaxProfils = nbrsJoursDispoMaxProfil.get(profilDTO);
                    nbrJoursDispoMaxProfils += nbrJoursDispoRsrcProfil;
                    nbrsJoursDispoMaxProfil.put(profilDTO, nbrJoursDispoMaxProfils);
                }
            }
        }
        return nbrsJoursDispoMaxProfil;
    }


    private boolean estHorsMission(@NotNull LocalDate debutPeriode, @Null LocalDate debutMission, @Null LocalDate finMission) {
        return estAvantLaMission(debutPeriode, debutMission) || estApresLaMission(debutPeriode, finMission);
    }

    private boolean estAvantLaMission(@NotNull LocalDate debutPeriode, @Null LocalDate debutMission) {
        return (debutMission != null) && debutMission.isAfter(debutPeriode);
    }

    private boolean estApresLaMission(@NotNull LocalDate debutPeriode, @Null LocalDate finMission) {
        return ((finMission != null) && finMission.isBefore(debutPeriode));
    }

}

package fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.calculateur;

import fr.gouv.agriculture.dal.ct.ihm.controller.ControllerException;
import fr.gouv.agriculture.dal.ct.ihm.controller.calculateur.Calculateur;
import fr.gouv.agriculture.dal.ct.ihm.model.BeanException;
import fr.gouv.agriculture.dal.ct.metier.service.ServiceException;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.PlanChargeIhm;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.DisponibilitesController;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.charge.PlanChargeBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.disponibilite.*;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.referentiels.ProfilBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.referentiels.RessourceHumaineBean;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dto.ProfilDTO;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dto.RessourceHumaineDTO;
import fr.gouv.agriculture.dal.ct.planCharge.metier.service.DisponibilitesService;
import fr.gouv.agriculture.dal.ct.planCharge.util.Collections;
import fr.gouv.agriculture.dal.ct.planCharge.util.Objects;
import fr.gouv.agriculture.dal.ct.planCharge.util.number.Percentage;
import fr.gouv.agriculture.dal.ct.planCharge.util.number.PercentageProperty;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ObservableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@SuppressWarnings("ClassHasNoToStringMethod")
public class CalculateurDisponibilites extends Calculateur {

    @NotNull
    private static final Logger LOGGER = LoggerFactory.getLogger(CalculateurDisponibilites.class);


    /*
     La couche métier :
    */

    //    @Autowired
    @NotNull
    private final DisponibilitesService disponibilitesService = DisponibilitesService.instance();

    //    @Autowired
    @NotNull
    private final PlanChargeBean planChargeBean = PlanChargeBean.instance();

    /*
     La couche controller :
    */

    /*
        //    @Autowired
        @NotNull
        private final DisponibilitesController disponibilitesController = DisponibilitesController.instance();
    */
    //    @Autowired
    @NotNull
    private final DisponibilitesController getDisponibilitesController() {
        return DisponibilitesController.instance();
    }


    // Constructeurs :

    public CalculateurDisponibilites() {
        super();
        // Rien... pour l'instant.
    }


    // Méthodes :

    //    @Override
    public void calculer() throws ControllerException {
        if (!estActive()) {
            LOGGER.debug("Calculateur désactivé, donc sans effet.");
            return;
        }

        LOGGER.debug("Calcul des disponibilités : ");
        for (int noSemaine = 1; noSemaine <= PlanChargeIhm.NBR_SEMAINES_PLANIFIEES; noSemaine++) {
            calculer(noSemaine);
        }
        LOGGER.debug("Disponibilités calculées.");
    }

    private void calculer(int noSemaine) throws ControllerException {
        if (!estActive()) {
            LOGGER.debug("Calculateur désactivé, donc sans effet.");
            return;
        }
//        LOGGER.debug("Calcul des disponibilités pour la semaine {} : ", noSemaine);
        for (RessourceHumaineBean ressourceHumaineBean : planChargeBean.getRessourcesHumainesBeans()) {
            calculer(ressourceHumaineBean, noSemaine);
        }
/* Redondant.
        for (ProfilBean profilBean : planChargeBean.getProfilsBeans()) {
            calculer(profilBean, noSemaine);
        }
*/
        LOGGER.debug("Disponibilités calculées pour la semaine {}.", noSemaine);
    }

    public void calculer(@NotNull RessourceHumaineBean ressHumBean) throws ControllerException {
        if (!estActive()) {
            LOGGER.debug("Calculateur désactivé, donc sans effet.");
            return;
        }
//        LOGGER.debug("Calcul des disponibilités pour la ressource {} : ", ressHumBean);
        for (int noSemaine = 1; noSemaine <= PlanChargeIhm.NBR_SEMAINES_PLANIFIEES; noSemaine++) {
            calculer(ressHumBean, noSemaine);
        }
//        LOGGER.debug("Disponibilités calculées pour la ressource {}.", ressHumBean);
    }

    public void calculer(@NotNull RessourceHumaineBean ressHumBean, int noSemaine) throws ControllerException {
        if (!estActive()) {
            LOGGER.debug("Calculateur désactivé, donc sans effet.");
            return;
        }
//        LOGGER.debug("Calcul des disponibilités pour la ressource {} et la semaine n° {} : ", ressHumBean, noSemaine);

        try {
            LocalDate debutPeriode = planChargeBean.dateEtat().plusDays(7 * (noSemaine - 1)); // TODO FDA 2017/08 [issue#26:PeriodeHebdo/Trim]
            LocalDate finPeriode = debutPeriode.plusDays(7); // TODO FDA 2017/08 [issue#26:PeriodeHebdo/Trim]

            LocalDate debutMission = Objects.value(ressHumBean.debutMissionProperty(), ObjectProperty::get);
            LocalDate finMission = Objects.value(ressHumBean.finMissionProperty(), ObjectProperty::get);

            // Nbr de jours ouvrés (calculé) :
            int nbrJoursOuvresPeriode;
            {
                try {
                    nbrJoursOuvresPeriode = disponibilitesService.nbrJoursOuvres(debutPeriode, finPeriode);
                } catch (ServiceException e) {
                    throw new ControllerException("Impossible de calculer le nombre de jours ouvrés.", e);
                }

                if (!getDisponibilitesController().getNbrsJoursOuvresBean().containsKey(debutPeriode)) {
                    getDisponibilitesController().getNbrsJoursOuvresBean().put(debutPeriode, new SimpleIntegerProperty(nbrJoursOuvresPeriode));
                } else {
                    getDisponibilitesController().getNbrsJoursOuvresBean().get(debutPeriode).set(nbrJoursOuvresPeriode);
                }
            }

            // Nbr de jours d'absence (saisi) :
            float nbrsJoursAbsencePeriode;
            {
                NbrsJoursAbsenceBean nbrsJoursAbsenceBean = Collections.any(getDisponibilitesController().getNbrsJoursAbsenceBeans(), bean -> bean.getRessourceHumaineBean().equals(ressHumBean)/*, new IhmException("Impossible de retrouver la ressource humaine '" + rsrcHumBean.getTrigramme() + "'.")*/);
                if (nbrsJoursAbsenceBean == null) {
                    nbrsJoursAbsenceBean = new NbrsJoursAbsenceBean(ressHumBean);
                    getDisponibilitesController().getNbrsJoursAbsenceBeans().add(nbrsJoursAbsenceBean);
                }
                if (!nbrsJoursAbsenceBean.containsKey(debutPeriode)) {
                    nbrsJoursAbsenceBean.put(debutPeriode, new SimpleFloatProperty(DisponibilitesService.NBR_JOURS_ABSENCE_RSRC_DEFAUT));
                }
                nbrsJoursAbsencePeriode = nbrsJoursAbsenceBean.get(debutPeriode).getValue();
            }

            // Nbr de jours de dispo pour le Ministère (calculé) :
            float nbrJoursDispoMinAgriPeriode;
            {
                try {
                    nbrJoursDispoMinAgriPeriode = disponibilitesService.nbrJoursDispoMinAgri(debutPeriode, debutMission, finMission, nbrJoursOuvresPeriode, nbrsJoursAbsencePeriode);
                } catch (ServiceException e) {
                    throw new ControllerException("Impossible de calculer le nombre de jours de disponibilité pour l'entreprise (MinAgri).", e);
                }

                NbrsJoursDispoRsrcBean nbrsJoursDispoMinAgriBean = Collections.any(getDisponibilitesController().getNbrsJoursDispoMinAgriBeans(), bean -> bean.getRessourceHumaineBean().equals(ressHumBean)/*, new IhmException("Impossible de retrouver la ressource humaine '" + ressHumBean.getTrigramme() + "'.")*/);
                if (nbrsJoursDispoMinAgriBean == null) {
                    nbrsJoursDispoMinAgriBean = new NbrsJoursDispoRsrcBean(ressHumBean);
                    getDisponibilitesController().getNbrsJoursDispoMinAgriBeans().add(nbrsJoursDispoMinAgriBean);
                }
                if (!nbrsJoursDispoMinAgriBean.containsKey(debutPeriode)) {
                    nbrsJoursDispoMinAgriBean.put(debutPeriode, new SimpleFloatProperty(nbrJoursDispoMinAgriPeriode));
                } else {
                    nbrsJoursDispoMinAgriBean.get(debutPeriode).set(nbrJoursDispoMinAgriPeriode);
                }
            }

            // % disponibilité CT / rsrc (saisi) :
            Percentage percentageDispoCTPeriode;
            {
                Percentage percentageDispoCTPeriodeParDefaut;
                try {
                    percentageDispoCTPeriodeParDefaut = disponibilitesService.pctageDispoCTParDefaut(debutPeriode, debutMission, finMission);
                } catch (ServiceException e) {
                    throw new ControllerException("Impossible de calculer le pourcentage de disponibilité pour l'équipe (CT du DAL).", e);
                }

                PctagesDispoRsrcBean pctagesDispoCTBean = Collections.any(getDisponibilitesController().getPctagesDispoCTBeans(), bean -> bean.getRessourceHumaineBean().equals(ressHumBean)/*, new IhmException("Impossible de retrouver la ressource humaine '" + ressHumBean.getTrigramme() + "'.")*/);
                if (pctagesDispoCTBean == null) {
                    pctagesDispoCTBean = new PctagesDispoRsrcBean(ressHumBean);
                    getDisponibilitesController().getPctagesDispoCTBeans().add(pctagesDispoCTBean);
                }
                if (!pctagesDispoCTBean.containsKey(debutPeriode)) {
                    percentageDispoCTPeriode = percentageDispoCTPeriodeParDefaut;

                    PercentageProperty percentageDispoCTPeriodeProperty = new PercentageProperty(percentageDispoCTPeriodeParDefaut.floatValue());
                    pctagesDispoCTBean.put(debutPeriode, percentageDispoCTPeriodeProperty);
                } else {
                    percentageDispoCTPeriode = pctagesDispoCTBean.get(debutPeriode).getValue();
                }
            }

            // Nbr de jours de dispo pour la CT (calculé) :
            float nbrJoursDispoCTPeriode;
            {
                try {
                    nbrJoursDispoCTPeriode = disponibilitesService.nbrJoursDispoCT(nbrJoursDispoMinAgriPeriode, percentageDispoCTPeriode);
                } catch (ServiceException e) {
                    throw new ControllerException("Impossible de calculer le nombre de jours de disponibilité pour l'équipe (CT du DAL).", e);
                }

                NbrsJoursDispoRsrcBean nbrsJoursDispoCTBean = Collections.any(getDisponibilitesController().getNbrsJoursDispoCTBeans(), bean -> bean.getRessourceHumaineBean().equals(ressHumBean)/*, new IhmException("Impossible de retrouver la ressource humaine '" + ressHumBean.getTrigramme() + "'.")*/);
                if (nbrsJoursDispoCTBean == null) {
                    nbrsJoursDispoCTBean = new NbrsJoursDispoRsrcBean(ressHumBean);
                    getDisponibilitesController().getNbrsJoursDispoCTBeans().add(nbrsJoursDispoCTBean);
                }
                if (!nbrsJoursDispoCTBean.containsKey(debutPeriode)) {
                    nbrsJoursDispoCTBean.put(debutPeriode, new SimpleFloatProperty(nbrJoursDispoCTPeriode));
                } else {
                    nbrsJoursDispoCTBean.get(debutPeriode).setValue(nbrJoursDispoCTPeriode);
                }
            }

            // % dispo. maxi. / rsrc / profil (saisi) :
            Map<ProfilBean, Percentage> pctagesDispoRsrc = new TreeMap<>(); // TreeMap au lieu de HasMap pour trier, juste afin de faciliter le débogage.
            RECUP_PCTAGES_DISPO_MAX_RSRC_PROFIL:
            {
                // Ajout des beans pour tous les profils manquants,
                // en 1 seule fois (1 'addAll' au lieu de N 'add') pour éviter les recalculs en chaîne, qui sont déclenchés à chaque modif :
                {
                    List<PctagesDispoRsrcProfilBean> pctagesDispoMaxRsrcProfilBeansAAjouter = new ArrayList<>();
                    for (ProfilBean profilBean : planChargeBean.getProfilsBeans()) {
                        PctagesDispoRsrcProfilBean pctagesDispoRsrcProfilBean = Collections.any(getDisponibilitesController().getPctagesDispoMaxRsrcProfilBeans(),
                                pctagesJoursDispoRsrcProfilBean ->
                                        pctagesJoursDispoRsrcProfilBean.getRessourceHumaineBean().equals(ressHumBean)
                                                && pctagesJoursDispoRsrcProfilBean.getProfilBean().equals(profilBean)
                        );
                        if (pctagesDispoRsrcProfilBean == null) {
                            //throw new IhmException("Impossible de retrouver les nombres de jours de dispo. max. pour la resssource '" + ressHumBean.getTrigramme() + "' et le profil '" + profilBean.getCode() + "'.");
                            //LOGGER.error("Impossible de retrouver les pourcentages de dispo. max. pour la ressource '" + ressHumBean.getTrigramme() + "' et le profil '" + profilBean.getCode() + "'.");
                            //break CALCUL_NBRS_JOURS_DISPO_MAX_RSRC_PROFIL;
                            PctagesDispoRsrcProfilBean pctagesDispoMaxRsrcBean = new PctagesDispoRsrcProfilBean(ressHumBean, profilBean);
                            pctagesDispoMaxRsrcProfilBeansAAjouter.add(pctagesDispoMaxRsrcBean);
                        }
                    }
                    if (!pctagesDispoMaxRsrcProfilBeansAAjouter.isEmpty()) {
                        getDisponibilitesController().getPctagesDispoMaxRsrcProfilBeans().addAll(pctagesDispoMaxRsrcProfilBeansAAjouter);
                    }
                }

                for (ProfilBean profilBean : planChargeBean.getProfilsBeans()) {
                    PctagesDispoRsrcProfilBean pctagesDispoMaxRsrcProfilBean = Collections.any(
                            getDisponibilitesController().getPctagesDispoMaxRsrcProfilBeans(),
                            pctagesDispoRsrcProfilBean ->
                                    pctagesDispoRsrcProfilBean.getRessourceHumaineBean().equals(ressHumBean)
                                            && pctagesDispoRsrcProfilBean.getProfilBean().equals(profilBean),
                            new ControllerException("Impossible de retrouver les pourcentages de dispo. max. pour la ressource '" + ressHumBean.getTrigramme() + "' et le profil '" + profilBean.getCode() + "'.")
                    );
                    if (!pctagesDispoMaxRsrcProfilBean.containsKey(debutPeriode)) {
                        pctagesDispoMaxRsrcProfilBean.put(debutPeriode, new PercentageProperty(DisponibilitesService.PCTAGE_DISPO_RSRC_DEFAUT.floatValue()));
                    }
                    PercentageProperty pctageDispoRsrcProfilProperty = pctagesDispoMaxRsrcProfilBean.get(debutPeriode);
                    Percentage pctageDispoRsrcProfil = pctageDispoRsrcProfilProperty.getValue();
                    pctagesDispoRsrc.put(profilBean, pctageDispoRsrcProfil);
                }
            }

            // Nbr de jours de dispo. maxi. / rsrc / profil (calculé) :
            CALCUL_NBRS_JOURS_DISPO_MAX_RSRC_PROFIL:
            {
                // Ajout des beans pour tous les profils manquants,
                // en 1 seule fois (1 'addAll' au lieu de N 'add') pour éviter les recalculs en chaîne, qui sont déclenchés à chaque modif :
                {
                    List<NbrsJoursDispoRsrcProfilBean> nbrsJoursDispoMaxRsrcProfilBeansAAjouter = new ArrayList<>();
                    for (ProfilBean profilBean : planChargeBean.getProfilsBeans()) {
                        NbrsJoursDispoRsrcProfilBean nbrsJoursDispoMaxRsrcProfilBean = Collections.any(getDisponibilitesController().getNbrsJoursDispoMaxRsrcProfilBeans(),
                                nbrsJoursDispoRsrcProfilBean ->
                                        nbrsJoursDispoRsrcProfilBean.getRessourceHumaineBean().equals(ressHumBean)
                                                && nbrsJoursDispoRsrcProfilBean.getProfilBean().equals(profilBean)
                        );
                        if (nbrsJoursDispoMaxRsrcProfilBean == null) {
                            //throw new IhmException("Impossible de retrouver les nombres de jours de dispo. max. pour la resssource '" + ressHumBean.getTrigramme() + "' et le profil '" + profilBean.getCode() + "'.");
                            //LOGGER.error("Impossible de retrouver les pourcentages de dispo. max. pour la ressource '" + ressHumBean.getTrigramme() + "' et le profil '" + profilBean.getCode() + "'.");
                            //break CALCUL_NBRS_JOURS_DISPO_MAX_RSRC_PROFIL;
                            NbrsJoursDispoRsrcProfilBean nbrsJoursDispoMaxRsrcProfilBeanAAjouter = new NbrsJoursDispoRsrcProfilBean(ressHumBean, profilBean);
                            nbrsJoursDispoMaxRsrcProfilBeansAAjouter.add(nbrsJoursDispoMaxRsrcProfilBeanAAjouter);
                        }
                    }
                    if (!nbrsJoursDispoMaxRsrcProfilBeansAAjouter.isEmpty()) {
                        getDisponibilitesController().getNbrsJoursDispoMaxRsrcProfilBeans().addAll(nbrsJoursDispoMaxRsrcProfilBeansAAjouter);
                    }
                }

                List<ProfilBean> profilsConcernes = getDisponibilitesController().getNbrsJoursDispoMaxRsrcProfilBeans().stream()
                        .filter(
                                nbrsJoursDispoRsrcProfilBean ->
                                        nbrsJoursDispoRsrcProfilBean.getRessourceHumaineBean().equals(ressHumBean)
                        )
                        .map(nbrsJoursDispoRsrcProfilBean -> nbrsJoursDispoRsrcProfilBean.getProfilBean())
                        .collect(Collectors.toList());
                for (ProfilBean profilBean : profilsConcernes) {

                    Percentage pctageDispoRsrcProfil = pctagesDispoRsrc.get(profilBean);

                    Float nbrJoursDispoMaxRsrcProfilPeriode;
                    try {
                        nbrJoursDispoMaxRsrcProfilPeriode = disponibilitesService.nbrJoursDispoMaxRsrcProfil(
                                debutPeriode, debutMission, finMission,
                                nbrJoursDispoCTPeriode, pctageDispoRsrcProfil
                        );
                    } catch (ServiceException e) {
                        throw new ControllerException("Impossible de calculer le nombre de jours de disponibilité max. par ressource et profil.", e);
                    }

                    NbrsJoursDispoRsrcProfilBean nbrsJoursDispoMaxRsrcProfilBean = Collections.any(
                            getDisponibilitesController().getNbrsJoursDispoMaxRsrcProfilBeans(),
                            nbrsJoursDispoRsrcProfilBean ->
                                    nbrsJoursDispoRsrcProfilBean.getRessourceHumaineBean().equals(ressHumBean)
                                            && nbrsJoursDispoRsrcProfilBean.getProfilBean().equals(profilBean),
                            new ControllerException("Impossible de retrouver les nombres de jours de dispo. max. pour la resssource '" + ressHumBean.getTrigramme() + "' et le profil '" + profilBean.getCode() + "'.")
                    );

                    if (!nbrsJoursDispoMaxRsrcProfilBean.containsKey(debutPeriode)) {
                        FloatProperty nbrJoursDispoMaxRsrcProfilPeriodeProperty = new SimpleFloatProperty(DisponibilitesService.PCTAGE_DISPO_RSRC_DEFAUT.floatValue());
                        nbrsJoursDispoMaxRsrcProfilBean.put(debutPeriode, nbrJoursDispoMaxRsrcProfilPeriodeProperty);
                    }
                    FloatProperty nbrJoursDispoMaxRsrcProfilPeriodeProperty = nbrsJoursDispoMaxRsrcProfilBean.get(debutPeriode);
                    nbrJoursDispoMaxRsrcProfilPeriodeProperty.setValue(nbrJoursDispoMaxRsrcProfilPeriode);
                }
            }

            // Nbrs de jours de dispo. maxi. / profil (calculé) :
            calculerDispoProfils(debutPeriode, finPeriode);

//        LOGGER.debug("Disponibilités calculées pour la ressource {} et la semaine n° {}.", ressHumBean, noSemaine);
        } catch (BeanException e) {
            throw new ControllerException("Impossible de calculer les disponibilités pour la ressource " + ressHumBean.getTrigramme() + " et la semaine n° " + noSemaine + ".", e);
        }
    }


    public void calculerDispoProfils(@NotNull LocalDate debutPeriode, @NotNull LocalDate finPeriode) throws ControllerException {
        if (!estActive()) {
            LOGGER.debug("Calculateur désactivé, donc sans effet.");
            return;
        }

//        LOGGER.debug("Calcul des disponibilités pour les profils et la période commençant le n° {} : ", debutPeriode.format(DateTimeFormatter.ISO_LOCAL_DATE));

        // Ajout des beans pour tous les profils manquants,
        // en 1 seule fois (1 'addAll' au lieu de N 'add') pour éviter les recalculs en chaîne, qui sont déclenchés à chaque modif :
        {
            List<NbrsJoursDispoProfilBean> nbrsJoursDispoMaxProfilBeansAAjouter = new ArrayList<>();
            for (ProfilBean profilBean : planChargeBean.getProfilsBeans()) {
                NbrsJoursDispoProfilBean nbrsJoursDispoMaxRsrcProfilBean = Collections.any(getDisponibilitesController().getNbrsJoursDispoMaxProfilBeans(),
                        nbrsJoursDispoProfilBean -> nbrsJoursDispoProfilBean.getProfilBean().equals(profilBean)
                );
                if (nbrsJoursDispoMaxRsrcProfilBean == null) {
                    NbrsJoursDispoProfilBean nbrsJoursDispoProfilBeanAAjouter = new NbrsJoursDispoProfilBean(profilBean);
                    nbrsJoursDispoMaxProfilBeansAAjouter.add(nbrsJoursDispoProfilBeanAAjouter);
                }
            }
            if (!nbrsJoursDispoMaxProfilBeansAAjouter.isEmpty()) {
                getDisponibilitesController().getNbrsJoursDispoMaxProfilBeans().addAll(nbrsJoursDispoMaxProfilBeansAAjouter);
            }
        }

        Map<ProfilDTO, Float> nbsrJoursDispoMaxProfil;
        try {
            ObservableList<NbrsJoursDispoRsrcProfilBean> nbrsJoursDispoMaxRsrcProfilBeans = getDisponibilitesController().getNbrsJoursDispoMaxRsrcProfilBeans();
            Map<RessourceHumaineDTO, Map<ProfilDTO, Map<LocalDate, Float>>> nbrsJoursDispoMaxRsrcProfil = NbrsJoursDispoRsrcProfilBeans.toDTO(nbrsJoursDispoMaxRsrcProfilBeans);
            nbsrJoursDispoMaxProfil = disponibilitesService.nbrsJoursDispoMaxProfil(debutPeriode, nbrsJoursDispoMaxRsrcProfil);
        } catch (ServiceException | BeanException  e) {
            throw new ControllerException("Impossible de calculer le nombre de jours de disponibilité max. par profil.", e);
        }
        for (ProfilBean profilBean : planChargeBean.getProfilsBeans()) {

            ProfilDTO profilDTO = profilBean.toDto();
            Float nbrsJoursDispoMaxProfil = nbsrJoursDispoMaxProfil.get(profilDTO);

            NbrsJoursDispoProfilBean nbrsJoursDispoMaxProfilBean = Collections.any(
                    getDisponibilitesController().getNbrsJoursDispoMaxProfilBeans(),
                    nbrsJoursDispoProfilBean ->
                            nbrsJoursDispoProfilBean.getProfilBean().equals(profilBean),
                    new ControllerException("Impossible de retrouver les nombres de jours de dispo. max. pour le profil '" + profilBean.getCode() + "'.")
            );

            if (!nbrsJoursDispoMaxProfilBean.containsKey(debutPeriode)) {
                FloatProperty nbrJoursDispoMaxProfilPeriodeProperty = new SimpleFloatProperty(DisponibilitesService.PCTAGE_DISPO_RSRC_DEFAUT.floatValue());
                nbrsJoursDispoMaxProfilBean.put(debutPeriode, nbrJoursDispoMaxProfilPeriodeProperty);
            }
            FloatProperty nbrJoursDispoMaxProfilPeriodeProperty = nbrsJoursDispoMaxProfilBean.get(debutPeriode);
            nbrJoursDispoMaxProfilPeriodeProperty.setValue(nbrsJoursDispoMaxProfil);
        }
//        LOGGER.debug("Disponibilités calculées pour les profils et la période commençant le n° {} : ", debutPeriode.format(DateTimeFormatter.ISO_LOCAL_DATE));
    }

}
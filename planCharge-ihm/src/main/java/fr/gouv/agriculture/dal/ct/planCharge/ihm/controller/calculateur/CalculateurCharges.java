package fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.calculateur;

import fr.gouv.agriculture.dal.ct.ihm.controller.ControllerException;
import fr.gouv.agriculture.dal.ct.ihm.controller.calculateur.Calculateur;
import fr.gouv.agriculture.dal.ct.ihm.model.BeanException;
import fr.gouv.agriculture.dal.ct.metier.service.ServiceException;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.ChargesController;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.DisponibilitesController;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.charge.*;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.disponibilite.NbrsJoursDispoProfilBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.disponibilite.NbrsJoursDispoRsrcBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.referentiels.ProfilBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.referentiels.RessourceBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.referentiels.RessourceHumaineBean;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dto.TacheDTO;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.charge.Planifications;
import fr.gouv.agriculture.dal.ct.planCharge.metier.service.ChargeService;
import fr.gouv.agriculture.dal.ct.planCharge.util.Collections;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleFloatProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("ClassHasNoToStringMethod")
public class CalculateurCharges extends Calculateur {

    @NotNull
    private static final Logger LOGGER = LoggerFactory.getLogger(CalculateurCharges.class);


    private static CalculateurCharges instance;

    public static final CalculateurCharges  instance() {
        if (instance == null) {
            instance = new CalculateurCharges();
        }
        return instance;
    }


    /*
     La couche métier :
    */

//    @Autowired
    @NotNull
    private final ChargeService chargeService = ChargeService.instance();

    //    @Autowired
    @NotNull
    private final PlanChargeBean planChargeBean = PlanChargeBean.instance();

    /*
     La couche controller :
    */

    @NotNull
    private ChargesController getChargesController() {
        return ChargesController.instance();
    }

    @NotNull
    private DisponibilitesController getDisponibilitesController() {
        return DisponibilitesController.instance();
    }


    // Constructeurs :

    private CalculateurCharges() {
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

        LOGGER.debug("Calcul des charges : ");

//        calculerProvisions();

        calculerNbrsJoursChargeParRessource();
        calculerNbrsJoursChargeParProfil();
        calculerNbrsJoursDispoCTRestanteParRessource();
        calculerNbrsJoursDispoCTMaxRestanteProfil();

        LOGGER.debug("Charges calculées.");
    }

    // Provisions :

    private void calculerProvisions() throws ControllerException {
        LOGGER.debug("Calcul des charges à provisionner : ");
        for (PlanificationTacheBean planificationTacheBean : planChargeBean.getPlanificationsBeans()) {
            calculerProvision(planificationTacheBean);
        }
        LOGGER.debug("Charges à provisionner calculées.");
    }

    public void calculerProvision(@NotNull PlanificationTacheBean planificationTacheBean) throws ControllerException {
        if (!planificationTacheBean.getTacheBean().estProvision()) {
            LOGGER.debug("La tâche {} n'est pas une provision, donc rien à calculer.", planificationTacheBean.noTache());
            return;
        }
        LOGGER.debug("Calcul des charges à provisionner pour la tâche {} : ", planificationTacheBean.noTache());

        for (int noSemaine = 1; noSemaine <= Planifications.NBR_SEMAINES_PLANIFIEES; noSemaine++) {
            calculerProvision(planificationTacheBean, noSemaine);
        }
        LOGGER.debug("Charges à provisionner calculées pour la tâche {} : ", planificationTacheBean.noTache());
    }

    private void calculerProvision(@NotNull PlanificationTacheBean planificationTacheBean, int noSemaine) throws ControllerException {
        TacheDTO tacheDTO = planificationTacheBean.toDto();

        LocalDate dateEtat = planChargeBean.getDateEtat();
        if (dateEtat == null) {
            LOGGER.debug("Pas de date d'état, on ne peut pas calculer les provisions.");
            return;
        }

        LocalDate debutPeriode = dateEtat.plusDays(((long) noSemaine - 1L) * 7L); // TODO FDA 2017/09 [issue#26:PeriodeHebdo/Trim]
        LocalDate finPeriode = debutPeriode.plusDays(7L); // TODO FDA 2017/09 [issue#26:PeriodeHebdo/Trim]

        double provision;
        try {
            provision = chargeService.provision(tacheDTO, debutPeriode, finPeriode, dateEtat);
        } catch (ServiceException e) {
            throw new ControllerException("Impossible de calculer la provision pour la tâche " + planificationTacheBean.getId() + " et la semaine n° " + noSemaine + ".", e);
        }

        planificationTacheBean.majChargePlanifiee(debutPeriode/*, finPeriode*/, provision);
    }

    // Nbrs de jours de charge / ressource :

    private void calculerNbrsJoursChargeParRessource() throws ControllerException {
        for (int noSemaine = 1; noSemaine <= Planifications.NBR_SEMAINES_PLANIFIEES; noSemaine++) {
            calculerNbrsJoursChargeParRessource(noSemaine);
        }
    }

    private void calculerNbrsJoursChargeParRessource(int noSemaine) throws ControllerException {
        for (RessourceBean<?, ?> ressourceBean : planChargeBean.getRessourcesBeans()) {
            calculerNbrsJoursChargeParRessource(ressourceBean, noSemaine);
        }
    }

    private void calculerNbrsJoursChargeParRessource(@NotNull RessourceBean<?, ?> ressourceBean, int noSemaine) throws ControllerException {

        List<PlanificationTacheBean> planificationTacheRessourceBeans = planChargeBean.getPlanificationsBeans().parallelStream()
                .filter(planificationTacheBean -> (planificationTacheBean.getRessource() != null) && planificationTacheBean.getRessource().equals(ressourceBean))
                .collect(Collectors.toList());

        LocalDate dateEtat;
        try {
            dateEtat = planChargeBean.dateEtat();
        } catch (BeanException e) {
            throw new ControllerException("Impossible de calculer les nombres de jours de charge pour la ressource '" + ressourceBean.getCode() + "'.", e);
        }
        LocalDate debutPeriode = dateEtat.plusDays(((long) noSemaine - 1L) * 7L);// TODO FDA 2017/09 [issue#26:PeriodeHebdo/Trim]

        Double chargePlanifieePourLaRessource = 0.0;
        for (PlanificationTacheBean planificationTacheRessourceBean : planificationTacheRessourceBeans) {
            DoubleProperty chargePlanifieeProperty = planificationTacheRessourceBean.chargePlanifieePropertyOuNull(debutPeriode);
            if (chargePlanifieeProperty == null) {
                continue;
            }
            Double chargePlanifiee = chargePlanifieeProperty.getValue();
            if (chargePlanifiee == 0.0) {
                continue;
            }
            chargePlanifieePourLaRessource += chargePlanifiee;
        }

        NbrsJoursParRessourceBean nbrJoursChargePourLaRessourceBean = Collections.any(
                getChargesController().getNbrsJoursChargeRsrcBeans(),
                nbrsJoursChargeBean -> nbrsJoursChargeBean.getRessourceBean().equals(ressourceBean),
                new ControllerException("Impossible de retrouver la ressource '" + ressourceBean.getCode() + "' dans la table des nombres de jours de charge par ressource.")
        );
        if (!nbrJoursChargePourLaRessourceBean.containsKey(debutPeriode)) {
            nbrJoursChargePourLaRessourceBean.put(debutPeriode, new SimpleFloatProperty());
        }
        nbrJoursChargePourLaRessourceBean.get(debutPeriode).setValue(chargePlanifieePourLaRessource);
    }

    // Nbrs de jours de charge / profil :

    private void calculerNbrsJoursChargeParProfil() throws ControllerException {
        for (int noSemaine = 1; noSemaine <= Planifications.NBR_SEMAINES_PLANIFIEES; noSemaine++) {
            calculerNbrsJoursChargeParProfil(noSemaine);
        }
    }

    private void calculerNbrsJoursChargeParProfil(int noSemaine) throws ControllerException {
        for (ProfilBean profilBean : planChargeBean.getProfilsBeans()) {
            calculerNbrsJoursChargeParProfil(profilBean, noSemaine);
        }
    }

    private void calculerNbrsJoursChargeParProfil(@NotNull ProfilBean profilBean, int noSemaine) throws ControllerException {

        List<PlanificationTacheBean> planificationTacheProfilBeans = planChargeBean.getPlanificationsBeans().parallelStream()
                .filter(planificationTacheBean -> (planificationTacheBean.getProfil() != null) && planificationTacheBean.getProfil().equals(profilBean))
                .collect(Collectors.toList());

        LocalDate dateEtat;
        try {
            dateEtat = planChargeBean.dateEtat();
        } catch (BeanException e) {
            throw new ControllerException("Impossible de calculer les nombres de jours de charge pour le profil '" + profilBean.getCode() + "'.", e);
        }
        LocalDate debutPeriode = dateEtat.plusDays(((long) noSemaine - 1L) * 7L);// TODO FDA 2017/09 [issue#26:PeriodeHebdo/Trim]

        Double chargePlanifieePourLaProfil = 0.0;
        for (PlanificationTacheBean planificationTacheProfilBean : planificationTacheProfilBeans) {
            DoubleProperty chargePlanifieeProperty = planificationTacheProfilBean.chargePlanifieePropertyOuNull(debutPeriode);
            if (chargePlanifieeProperty == null) {
                continue;
            }
            Double chargePlanifiee = chargePlanifieeProperty.getValue();
            if (chargePlanifiee == 0.0) {
                continue;
            }
            chargePlanifieePourLaProfil += chargePlanifiee;
        }

        NbrsJoursParProfilBean nbrJoursChargePourLeProfilBean = Collections.any(
                getChargesController().getNbrsJoursChargeProfilBeans(),
                nbrsJoursChargeBean -> nbrsJoursChargeBean.getProfilBean().equals(profilBean)
//                new ControllerException("Impossible de retrouver le profil '" + profilBean.getCode() + "' dans la table des nombres de jours de charge par profil.")
        );
        if (nbrJoursChargePourLeProfilBean == null) {
            nbrJoursChargePourLeProfilBean = new NbrsJoursParProfilBean(profilBean);
            getChargesController().getNbrsJoursChargeProfilBeans().add(nbrJoursChargePourLeProfilBean);
        }
        if (!nbrJoursChargePourLeProfilBean.containsKey(debutPeriode)) {
            nbrJoursChargePourLeProfilBean.put(debutPeriode, new SimpleFloatProperty());
        }
        nbrJoursChargePourLeProfilBean.get(debutPeriode).setValue(chargePlanifieePourLaProfil);
    }

    // Nbrs de jours de dispo CT restante / ressource :

    private void calculerNbrsJoursDispoCTRestanteParRessource() throws ControllerException {
        for (int noSemaine = 1; noSemaine <= Planifications.NBR_SEMAINES_PLANIFIEES; noSemaine++) {
            calculerNbrsJoursDispoCTRestanteParRessource(noSemaine);
        }
    }

    private void calculerNbrsJoursDispoCTRestanteParRessource(int noSemaine) throws ControllerException {
        for (RessourceBean<?, ?> ressourceBean : planChargeBean.getRessourcesBeans()) {
            calculerNbrsJoursDispoCTRestanteParRessource(ressourceBean, noSemaine);
        }
        calculerTotalNbrsJoursDispoCTRestanteParRessource(noSemaine);
    }

    private void calculerTotalNbrsJoursDispoCTRestanteParRessource(int noSemaine) throws ControllerException {

        LocalDate dateEtat;
        try {
            dateEtat = planChargeBean.dateEtat();
        } catch (BeanException e) {
            throw new ControllerException("Impossible de calculer les totaux des nombres de jours de dispo. CT restante par ressource.", e);
        }
        LocalDate debutPeriode = dateEtat.plusDays(((long) noSemaine - 1L) * 7L);// TODO FDA 2017/09 [issue#26:PeriodeHebdo/Trim]

        Double chargeTotalePeriode = 0.0;
        for (RessourceBean<?, ?> ressourceBean : planChargeBean.getRessourcesBeans()) {
            NbrsJoursParRessourceBean nbrJoursChargePourLaRessourceBean = Collections.any(
                    getChargesController().getNbrsJoursDispoCTRestanteRsrcBeans(),
                    nbrsJoursDispoBean -> nbrsJoursDispoBean.getRessourceBean().equals(ressourceBean),
                    new ControllerException("Impossible de retrouver la ressource '" + ressourceBean.getCode() + "' dans la table des nombres de jours de disponibilité restant pour la CT par ressource.")
            );
            Float chargePeriode = nbrJoursChargePourLaRessourceBean.get(debutPeriode).getValue();

            chargeTotalePeriode += chargePeriode;
        }

        TotauxNbrsJoursBean totauxNbrsJoursBean = getChargesController().getTotauxNbrsJoursDispoCTRestanteRsrcBeans().get(0);
        if (!totauxNbrsJoursBean.containsKey(debutPeriode)) {
            totauxNbrsJoursBean.put(debutPeriode, new SimpleFloatProperty());
        }
        totauxNbrsJoursBean.get(debutPeriode).setValue(chargeTotalePeriode);
    }

    private void calculerNbrsJoursDispoCTRestanteParRessource(@NotNull RessourceBean<?, ?> ressourceBean, int noSemaine) throws ControllerException {

        LocalDate dateEtat;
        try {
            dateEtat = planChargeBean.dateEtat();
        } catch (BeanException e) {
            throw new ControllerException("Impossible de calculer les nombres de jours de dispo. CT restante pour la ressource '" + ressourceBean.getCode() + "'.", e);
        }
        LocalDate debutPeriode = dateEtat.plusDays(((long) noSemaine - 1L) * 7L);// TODO FDA 2017/09 [issue#26:PeriodeHebdo/Trim]

        float nbrsJoursDispo;
        boolean estRessourceHumaine;
//        try {
            estRessourceHumaine = ressourceBean.estHumain();
//        } catch (BeanException e) {
//            throw new ControllerException("Impossible de déterminer si la ressource est humaine.", e);
//        }
        if (!estRessourceHumaine) {
            nbrsJoursDispo = 0.0f; // TODO FDA 2017/09 Mettre cette valeur dans un Service.
        } else {
            RessourceHumaineBean ressourceHumaineBean = (RessourceHumaineBean) ressourceBean;
            NbrsJoursDispoRsrcBean nbrsJoursDispoRsrcBean = Collections.any(
                    getDisponibilitesController().getNbrsJoursDispoCTBeans(),
                    nbrsJoursBean -> nbrsJoursBean.getRessourceBean().equals(ressourceBean)
//                    new ControllerException("impossible de retrouver la disponibilité de la ressource " + ressourceBean.getCode() + " pour la semaine " + noSemaine + ".")
            );
            if (nbrsJoursDispoRsrcBean==null)  {
                nbrsJoursDispoRsrcBean = new NbrsJoursDispoRsrcBean(ressourceHumaineBean);
            }
            if (!nbrsJoursDispoRsrcBean.containsKey(debutPeriode)) {
                nbrsJoursDispoRsrcBean.put(debutPeriode, new SimpleFloatProperty(0.0f)); // TODO FDA 2017/09 Mettre cette valeur dans un Service.
            }
            nbrsJoursDispo = nbrsJoursDispoRsrcBean.get(debutPeriode).getValue();
        }

        float nbrJoursCharge;
        NbrsJoursParRessourceBean nbrsJoursChargeRsrcBean = Collections.any(
                getChargesController().getNbrsJoursChargeRsrcBeans(),
                nbrsJoursChargeBean -> nbrsJoursChargeBean.getRessourceBean().equals(ressourceBean),
                new ControllerException("impossible de retrouver la charge totale de la ressource " + ressourceBean.getCode() + " pour la semaine " + noSemaine + ".")
        );
        nbrJoursCharge = nbrsJoursChargeRsrcBean.get(debutPeriode).getValue();

        float nbrJoursDispoCTRestante = nbrsJoursDispo - nbrJoursCharge; // TODO FDA 2017/09 Coder cette RG dans un DTO/Entity/Service.

        NbrsJoursParRessourceBean nbrJoursChargePourLaRessourceBean = Collections.any(
                getChargesController().getNbrsJoursDispoCTRestanteRsrcBeans(),
                nbrsJoursDispoBean -> nbrsJoursDispoBean.getRessourceBean().equals(ressourceBean),
                new ControllerException("Impossible de retrouver la ressource '" + ressourceBean.getCode() + "' dans la table des nombres de jours de disponibilité restant pour la CT par ressource.")
        );
        if (!nbrJoursChargePourLaRessourceBean.containsKey(debutPeriode)) {
            nbrJoursChargePourLaRessourceBean.put(debutPeriode, new SimpleFloatProperty());
        }
        nbrJoursChargePourLaRessourceBean.get(debutPeriode).setValue(nbrJoursDispoCTRestante);
    }

    // Nbrs de jours de dispo CT max restante / profil :

    private void calculerNbrsJoursDispoCTMaxRestanteProfil() throws ControllerException {
        for (int noSemaine = 1; noSemaine <= Planifications.NBR_SEMAINES_PLANIFIEES; noSemaine++) {
            calculerNbrsJoursDispoCTMaxRestanteProfil(noSemaine);
        }
    }

    private void calculerNbrsJoursDispoCTMaxRestanteProfil(int noSemaine) throws ControllerException {
        for (ProfilBean ressourceBean : planChargeBean.getProfilsBeans()) {
            calculerNbrsJoursDispoCTMaxRestanteProfil(ressourceBean, noSemaine);
        }
        calculerTotalNbrsJoursDispoCTMaxRestanteProfil(noSemaine);
    }

    private void calculerTotalNbrsJoursDispoCTMaxRestanteProfil(int noSemaine) throws ControllerException {

        LocalDate dateEtat;
        try {
            dateEtat = planChargeBean.dateEtat();
        } catch (BeanException e) {
            throw new ControllerException("Impossible de calculer les totaux des nombres de jours de dispo. max CT restante par profil.", e);
        }
        LocalDate debutPeriode = dateEtat.plusDays(((long) noSemaine - 1L) * 7L);// TODO FDA 2017/09 [issue#26:PeriodeHebdo/Trim]

        Double chargeTotalePeriode = 0.0;
        for (ProfilBean profilBean : planChargeBean.getProfilsBeans()) {
            NbrsJoursParProfilBean nbrJoursChargePourLeProfilBean = Collections.any(
                    getChargesController().getNbrsJoursDispoCTMaxRestanteProfilBeans(),
                    nbrsJoursDispoBean -> nbrsJoursDispoBean.getProfilBean().equals(profilBean),
                    new ControllerException("Impossible de retrouver le profil '" + profilBean.getCode() + "' dans la table des nombres de jours de disponibilité max. restant pour la CT par profil.")
            );
            Float chargePeriode = nbrJoursChargePourLeProfilBean.get(debutPeriode).getValue();

            chargeTotalePeriode += chargePeriode;
        }

        TotauxNbrsJoursBean totauxNbrsJoursBean = getChargesController().getTotauxNbrsJoursDispoCTMaxRestanteProfilBeans().get(0);
        if (!totauxNbrsJoursBean.containsKey(debutPeriode)) {
            totauxNbrsJoursBean.put(debutPeriode, new SimpleFloatProperty());
        }
        totauxNbrsJoursBean.get(debutPeriode).setValue(chargeTotalePeriode);
    }


    private void calculerNbrsJoursDispoCTMaxRestanteProfil(@NotNull ProfilBean profilBean, int noSemaine) throws ControllerException {

        LocalDate dateEtat;
        try {
            dateEtat = planChargeBean.dateEtat();
        } catch (BeanException e) {
            throw new ControllerException("Impossible de calculer les nombres de jours de dispo. CT restante pour la ressource '" + profilBean.getCode() + "'.", e);
        }
        LocalDate debutPeriode = dateEtat.plusDays(((long) noSemaine - 1L) * 7L);// TODO FDA 2017/09 [issue#26:PeriodeHebdo/Trim]

        float nbrsJoursDispo;
        NbrsJoursDispoProfilBean nbrsJoursDispoRsrcBean = Collections.any(
                getDisponibilitesController().getNbrsJoursDispoMaxProfilBeans(),
                nbrsJoursBean -> nbrsJoursBean.getProfilBean().equals(profilBean)
//                new ControllerException("impossible de retrouver la disponibilité du profil " + profilBean.getCode() + " pour la semaine " + noSemaine + ".")
        );
        if (nbrsJoursDispoRsrcBean == null) {
            nbrsJoursDispoRsrcBean = new NbrsJoursDispoProfilBean(profilBean);
            getDisponibilitesController().getNbrsJoursDispoMaxProfilBeans().add(nbrsJoursDispoRsrcBean);
        }
        if (!nbrsJoursDispoRsrcBean.containsKey(debutPeriode)) {
            nbrsJoursDispoRsrcBean.put(debutPeriode, new SimpleFloatProperty());
        }
        nbrsJoursDispo = nbrsJoursDispoRsrcBean.get(debutPeriode).getValue();

        float nbrJoursCharge;
        {
            NbrsJoursParProfilBean nbrJoursChargeBean = Collections.any(
                    getChargesController().getNbrsJoursChargeProfilBeans(),
                    nbrsJoursBean -> nbrsJoursBean.getProfilBean().equals(profilBean)
//                    new ControllerException("Impossible de retrouver la charge totale du profil " + profilBean.getCode() + " pour la semaine " + noSemaine + ".")
            );
            if (nbrJoursChargeBean ==null) {
                nbrJoursChargeBean = new NbrsJoursParProfilBean(profilBean);
                getChargesController().getNbrsJoursChargeProfilBeans().add(nbrJoursChargeBean);
            }
            if (!nbrJoursChargeBean.containsKey(debutPeriode)) {
                nbrJoursChargeBean.put(debutPeriode, new SimpleFloatProperty());
            }
            nbrJoursCharge = nbrJoursChargeBean.get(debutPeriode).getValue();
        }

        float nbrJoursDispoCTRestante = nbrsJoursDispo - nbrJoursCharge; // TODO FDA 2017/09 Coder cette RG dans un DTO/Entity/Service.

        {
            NbrsJoursParProfilBean nbrJoursChargeBean = Collections.any(
                    getChargesController().getNbrsJoursDispoCTMaxRestanteProfilBeans(),
                    nbrsJoursBean -> nbrsJoursBean.getProfilBean().equals(profilBean)
//                    new ControllerException("Impossible de retrouver le profil '" + profilBean.getCode() + "' dans la table des nombres de jours de disponibilité CT max. restant pour la CT par profil.")
            );
            if (nbrJoursChargeBean == null) {
                nbrJoursChargeBean = new NbrsJoursParProfilBean(profilBean);
                getChargesController().getNbrsJoursDispoCTMaxRestanteProfilBeans().add(nbrJoursChargeBean);
            }
            if (!nbrJoursChargeBean.containsKey(debutPeriode)) {
                nbrJoursChargeBean.put(debutPeriode, new SimpleFloatProperty());
            }
            nbrJoursChargeBean.get(debutPeriode).setValue(nbrJoursDispoCTRestante);
        }
    }

}
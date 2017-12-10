package fr.gouv.agriculture.dal.ct.planCharge.ihm.model.disponibilite;

import fr.gouv.agriculture.dal.ct.ihm.model.BeanException;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.referentiels.ProfilBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.referentiels.RessourceHumaineBean;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dto.ProfilDTO;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dto.RessourceHumaineDTO;
import javafx.collections.ObservableList;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Map;
import java.util.TreeMap;

public final class NbrsJoursDispoParRessourceProfilBeans {

    // Fields :


    // Constructors :

    private NbrsJoursDispoParRessourceProfilBeans() {
        super();
    }

    // Getters/Setters :


    // Implementation of AbstractBean :

    @NotNull
    public static Map<RessourceHumaineDTO, Map<ProfilDTO, Map<LocalDate, Float>>> toDTO(@NotNull ObservableList<NbrsJoursDispoParRessourceProfilBean> nbrsJoursDispoMaxRsrcProfilBeans) throws BeanException {
        Map<RessourceHumaineDTO, Map<ProfilDTO, Map<LocalDate, Float>>> nbrsJoursDispoMaxRsrcProfilDTOs = new TreeMap<>(); // TreeMap au lieu de HashMap pour trier, juste afin de faciliter le débogage.
        for (NbrsJoursDispoParRessourceProfilBean nbrsJoursDispoMaxRsrcProfilBean : nbrsJoursDispoMaxRsrcProfilBeans) {

            RessourceHumaineBean ressourceHumaineBean = nbrsJoursDispoMaxRsrcProfilBean.getRessourceBean();
            if (ressourceHumaineBean.getCode() == null) {
                // Ressource pas encore renseignée suffisamment pour qu'on la considère (en cours de saisie).
                continue;
            }
            RessourceHumaineDTO ressourceHumaineDTO = ressourceHumaineBean.toDto();
            if (!nbrsJoursDispoMaxRsrcProfilDTOs.containsKey(ressourceHumaineDTO)) {
                nbrsJoursDispoMaxRsrcProfilDTOs.put(ressourceHumaineDTO, new TreeMap<>()); // TreeMap au lieu de HashMap pour trier, juste afin de faciliter le débogage.
            }

            ProfilBean profilBean = nbrsJoursDispoMaxRsrcProfilBean.getProfilBean();
            ProfilDTO profilDTO = profilBean.toDto();
            if (!nbrsJoursDispoMaxRsrcProfilDTOs.containsKey(ressourceHumaineDTO)) {
                nbrsJoursDispoMaxRsrcProfilDTOs.put(ressourceHumaineDTO, new TreeMap<>()); // TreeMap au lieu de HashMap pour trier, juste afin de faciliter le débogage.);
            }
            if (!nbrsJoursDispoMaxRsrcProfilDTOs.get(ressourceHumaineDTO).containsKey(profilDTO)) {
                nbrsJoursDispoMaxRsrcProfilDTOs.get(ressourceHumaineDTO).put(profilDTO, new TreeMap<>()); // TreeMap au lieu de HashMap pour trier, juste afin de faciliter le débogage.
            }

            for (LocalDate debutPeriode : nbrsJoursDispoMaxRsrcProfilBean.keySet()) {
                nbrsJoursDispoMaxRsrcProfilDTOs.get(ressourceHumaineDTO).get(profilDTO).put(debutPeriode, nbrsJoursDispoMaxRsrcProfilBean.get(debutPeriode).floatValue());
            }
        }
        return nbrsJoursDispoMaxRsrcProfilDTOs;
    }


    // Utilitie's methods:

}

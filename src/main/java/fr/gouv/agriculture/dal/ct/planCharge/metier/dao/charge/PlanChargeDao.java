package fr.gouv.agriculture.dal.ct.planCharge.metier.dao.charge;

import com.sun.istack.internal.NotNull;
import fr.gouv.agriculture.dal.ct.planCharge.PlanChargeApplication;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dao.AbstractDao;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dao.EntityNotFoundException;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.charge.PlanCharge;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.charge.Planifications;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by frederic.danna on 26/03/2017.
 */
public class PlanChargeDao extends AbstractDao<PlanCharge, LocalDate> {

    private static final Logger LOGGER = LoggerFactory.getLogger(PlanChargeDao.class);

    private static final Map<LocalDate, PlanCharge> CACHE = new HashMap<>();

    @NotNull
    @Autowired
    private PlanChargeApplication application;

    /*
    private ProjetAppliDao projetAppliDao ;
    private ImportanceDao importanceDao;
    private RessourceDao ressourceDao;
    private ProfilDao profilDao;
*/

    @Override
    protected Map<LocalDate, PlanCharge> getCache() {
        return CACHE;
    }

    @Override
    protected PlanCharge newEntity(LocalDate datePlanif) {
        return new PlanCharge(datePlanif, null); // TODO FDA 2017/04 Confirmer qu'on peut passer null pour les planifications.
    }

    @Override
    public PlanCharge load(@NotNull LocalDate date) throws EntityNotFoundException {
        File fichierPlanif = application.getIhm().getFichierPlanificationsCharge(date);
        if (fichierPlanif == null) {
            throw new EntityNotFoundException("Fichier inexistant pour la date du " + date.format(DateTimeFormatter.ofPattern("yyyy/MM/dd")) + ".");
        }
        try {
            Planifications  planifications = planifications(fichierPlanif);
            return new PlanCharge(date, planifications);
        } catch (PlanChargeDaoException e) {
            throw new EntityNotFoundException("", e);
        }
    }

    /**
     * Loads data from the specified file. The current data will be replaced.
     *
     * @param file
     */
    // Cf. http://code.makery.ch/library/javafx-8-tutorial/fr/part5/
    private Planifications planifications(@NotNull File file) throws PlanChargeDaoException {
        try {
            JAXBContext context = JAXBContext.newInstance(PlanChargeWrapper.class);
            Unmarshaller um = context.createUnmarshaller();

            // Reading XML from the file and unmarshalling.
            PlanChargeWrapper wrapper = (PlanChargeWrapper) um.unmarshal(file);

            return wrapper.getPlanifications();

/*
            // Save the file path to the registry.
            setPersonFilePath(file);
*/

        } catch (Exception e) { // catches ANY exception
            throw new PlanChargeDaoException("Impossible de charger le plan de charge depuis le fichier '" + file.getAbsolutePath() + "'.", e);
        }
    }

    public void save(PlanCharge planCharge) throws PlanChargeDaoException {
        LocalDate dateEtat = planCharge.getDateEtat();
        File fichierPlanif = application.getIhm().getFichierPlanificationsCharge(dateEtat);
        savePlanChargeToFile(fichierPlanif, planCharge);
    }

    /**
     * Saves the current person data to the specified file.
     *
     * @param file
     */
    // Cf. http://code.makery.ch/library/javafx-8-tutorial/fr/part5/
    private void savePlanChargeToFile(File file, PlanCharge planCharge) throws PlanChargeDaoException {
        try {
            JAXBContext context = JAXBContext.newInstance(PlanChargeWrapper.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            // Wrapping our person data.
            PlanChargeWrapper wrapper = new PlanChargeWrapper();
            wrapper.setPlanifications(planCharge.getPlanifications());

            // Marshalling and saving XML to the file.
            m.marshal(wrapper, file);

/*
            // Save the file path to the registry.
            setPersonFilePath(file);
*/
        } catch (Exception e) { // catches ANY exception
            throw new PlanChargeDaoException("Impossible de sauver le plan de charge dans le fichier '" + file.getAbsolutePath() + "'.", e);
        }
    }
}

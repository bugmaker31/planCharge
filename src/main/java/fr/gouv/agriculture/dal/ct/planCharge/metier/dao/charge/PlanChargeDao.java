package fr.gouv.agriculture.dal.ct.planCharge.metier.dao.charge;

import com.sun.istack.internal.NotNull;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.PlanChargeIhm;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dao.referentiels.ProfilDao;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dao.referentiels.ProjetAppliDao;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dao.referentiels.RessourceDao;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dao.referentiels.importance.ImportanceDao;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.charge.PlanCharge;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.charge.Planifications;
import javafx.scene.control.Alert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Created by frederic.danna on 26/03/2017.
 */
public class PlanChargeDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(PlanChargeDao.class);

    private PlanChargeIhm application = PlanChargeIhm.APPLICATION();

    private ProjetAppliDao projetAppliDao = new ProjetAppliDao();
    private ImportanceDao importanceDao = new ImportanceDao();
    private RessourceDao ressourceDao = new RessourceDao();
    private ProfilDao profilDao = new ProfilDao();

    public PlanCharge load(@NotNull LocalDate date) throws PlanChargeDaoException {
        File fichierPlanif = application.getFichierPlanificationsCharge(date);
        if (fichierPlanif == null) {
            throw new PlanChargeDaoException("Fichier inexistant pour la date du " + date.format(DateTimeFormatter.ofPattern("yyyy/MM/dd")) + ".");
        }
        Planifications planifications = planifications(fichierPlanif);
        return new PlanCharge(date, planifications);
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

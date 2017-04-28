package fr.gouv.agriculture.dal.ct.planCharge.metier.dao.charge;

import fr.gouv.agriculture.dal.ct.planCharge.metier.dao.AbstractDao;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dao.EntityNotFoundException;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.charge.PlanCharge;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.charge.Planifications;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.IOException;
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
    private String repPersistanceDonnees;

    @NotNull
    private String patronFicPersistanceDonnees;

    /*
    @NotNull
    @Autowired
    private ProjetAppliDao projetAppliDao ;
    @NotNull
    @Autowired
    private ImportanceDao importanceDao;
    @NotNull
    @Autowired
    private RessourceDao ressourceDao;
    @NotNull
    @Autowired
    private ProfilDao profilDao;
*/

    public void setRepPersistance(String repPersistance) {
        this.repPersistanceDonnees = repPersistance;
    }

    public void setPatronFicPersistance(String patronFicPersistance) {
        this.patronFicPersistanceDonnees = patronFicPersistance;
    }

    @Override
    protected Map<LocalDate, PlanCharge> getCache() {
        return CACHE;
    }

    @Override
    protected PlanCharge newEntity(LocalDate dateEtat) {
        return new PlanCharge(dateEtat);
    }

    @Override
    public PlanCharge load(@NotNull LocalDate dateEtat) throws EntityNotFoundException {
        File fichierPlanif = fichierPlanificationsCharge(dateEtat);
        if (fichierPlanif == null) {
            throw new EntityNotFoundException("Fichier inexistant pour la dateEtat du " + dateEtat.format(DateTimeFormatter.ofPattern("yyyy/MM/dd")) + ".");
        }
        try {
            Planifications planifications = planifications(fichierPlanif);
            return new PlanCharge(dateEtat, planifications);
        } catch (PlanChargeDaoException e) {
            throw new EntityNotFoundException("Impossible de charger le plan de charge en dateEtat du " + dateEtat.format(DateTimeFormatter.ofPattern("yyyy/MM/dd")), e);
        }
    }

    @NotNull
    private File fichierPlanificationsCharge(@NotNull LocalDate dateEtat) {
        String nomFic = patronFicPersistanceDonnees
                .replaceAll("\\{dateEtat}", dateEtat.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                + ".xml";
        return new File(repPersistanceDonnees, nomFic);
    }

    /**
     * Loads data from the specified file.
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

            return new Planifications(); // TODO FDA 2017/04 Coder : exploiter le wrapper.

        } catch (Exception e) {
            throw new PlanChargeDaoException("Impossible de dé-sérialiser le plan de charge depuis le fichier XML '" + file.getAbsolutePath() + "'.", e);
        }
    }

    public void sauver(PlanCharge planCharge) throws PlanChargeDaoException {

        LocalDate dateEtat = planCharge.getDateEtat();

        File fichierPlanif = fichierPlanificationsCharge(dateEtat);
        if (fichierPlanif.exists()) {
            fichierPlanif.delete();
        }
        try {
            fichierPlanif.createNewFile();
        } catch (IOException e) {
            throw new PlanChargeDaoException("Impossible de créer le fichier '" + fichierPlanif.getAbsolutePath() + "'.", e);
        }

        serialiserPlanCharge(fichierPlanif, planCharge);
    }

    /**
     * Saves the current data to the specified file.
     *
     * @param file
     */
    // Cf. http://code.makery.ch/library/javafx-8-tutorial/fr/part5/
    private void serialiserPlanCharge(File file, PlanCharge planCharge) throws PlanChargeDaoException {
        try {
            JAXBContext context = JAXBContext.newInstance(PlanChargeWrapper.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_ENCODING, "UTF8");
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            // Wrapping our data.
            PlanChargeWrapper wrapper = new PlanChargeWrapper(planCharge);

            // Marshalling and saving XML to the file.
            m.marshal(wrapper, file);
        } catch (Exception e) {
            throw new PlanChargeDaoException("Impossible de sérialiser le plan de charge dans le fichier XML '" + file.getAbsolutePath() + "'.", e);
        }
    }

}

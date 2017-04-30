package fr.gouv.agriculture.dal.ct.planCharge.metier.dao.charge;

import com.sun.star.frame.XComponentLoader;
import com.sun.star.sheet.XSpreadsheet;
import com.sun.star.sheet.XSpreadsheetDocument;
import com.sun.star.table.XCell;
import fr.gouv.agriculture.dal.ct.libreoffice.Calc;
import fr.gouv.agriculture.dal.ct.libreoffice.LibreOfficeException;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dao.AbstractDao;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dao.DaoException;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dao.EntityNotFoundException;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dao.charge.xml.PlanChargeXmlWrapper;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dao.referentiels.ProfilDao;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dao.referentiels.ProjetAppliDao;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dao.referentiels.RessourceDao;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dao.referentiels.importance.ImportanceDao;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.charge.PlanCharge;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.charge.Planifications;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.*;
import fr.gouv.agriculture.dal.ct.planCharge.util.Dates;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.constraints.NotNull;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
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

    @NotNull
    @Autowired
    private PlanChargeXmlWrapper wrapper;

    @NotNull
    @Autowired
    private ProjetAppliDao projetAppliDao;

    @NotNull
    @Autowired
    private ImportanceDao importanceDao;

    @NotNull
    @Autowired
    private RessourceDao ressourceDao;

    @NotNull
    @Autowired
    private ProfilDao profilDao;

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
            JAXBContext context = JAXBContext.newInstance(PlanChargeXmlWrapper.class);
            Unmarshaller um = context.createUnmarshaller();

            // Reading XML from the file and unmarshalling.
            wrapper = (PlanChargeXmlWrapper) um.unmarshal(file);

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
            JAXBContext context = JAXBContext.newInstance(PlanChargeXmlWrapper.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_ENCODING, "UTF8");
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            // Wrapping our data.
            wrapper.init(planCharge);

            // Marshalling and saving XML to the file.
            m.marshal(wrapper, file);
        } catch (Exception e) {
            throw new PlanChargeDaoException("Impossible de sérialiser le plan de charge dans le fichier XML '" + file.getAbsolutePath() + "'.", e);
        }
    }

    public PlanCharge importerDepuisCalc(@NotNull File ficCalc) throws PlanChargeDaoException {
        PlanCharge planCharge;

        if (!ficCalc.exists()) {
            throw new PlanChargeDaoException("Impossible d'importerPlanCharge le plan ce charge, fichier non trouvé : '" + ficCalc.getAbsolutePath() + "'.");
        }
        XSpreadsheetDocument docCalc = null;
        try {
            // Cf. http://fivedots.coe.psu.ac.th/~ad/jlop/jlop04/04.%20Spreadsheet%20Processing.pdf
            XComponentLoader loader = Calc.getLoader();
            docCalc = Calc.openDoc(ficCalc.getAbsolutePath(), loader);
            if (docCalc == null) {
                throw new PlanChargeDaoException("Document introuvable : '" + ficCalc.getAbsolutePath() + "'.");
            }

            planCharge = importer(docCalc);

        } catch (LibreOfficeException e) {
            throw new PlanChargeDaoException("Impossible d'importerPlanCharge le plan de charge depuis le fichier '" + ficCalc.getAbsolutePath() + "'.", e);
        } finally {
            if (docCalc != null) {
                try {
                    Calc.closeDoc(docCalc);
                } catch (LibreOfficeException e) {
                    throw new PlanChargeDaoException("Impossible de fermer (close) LibreOffice.", e);
                }
            }
        }

        return planCharge;
    }

    private PlanCharge importer(XSpreadsheetDocument calc) throws PlanChargeDaoException, LibreOfficeException {
        PlanCharge planCharge;

        try {
            XSpreadsheet feuilleCharge = Calc.getSheet(calc, "Charge");
            planCharge = importer(feuilleCharge);
        } catch (LibreOfficeException e) {
            throw new PlanChargeDaoException("Impossible d'importer le plan de charge depuis le doc OOCalc.", e);
        }

        return planCharge;
    }

    private PlanCharge importer(XSpreadsheet feuille) throws PlanChargeDaoException, LibreOfficeException {
        PlanCharge planDeCharge;

        final int noLigDateEtat = 1;
        final int noColDateEtat = 4;

        Date dateEtat = Calc.getDate(feuille, noColDateEtat - 1, noLigDateEtat - 1);
        if (dateEtat == null) {
            throw new PlanChargeDaoException("Impossible de retrouver la date d'état.");
        }

        Planifications planifications = importerPlanifications(feuille);

        planDeCharge = new PlanCharge(Dates.asLocalDate(dateEtat), planifications);

        return planDeCharge;
    }

    private Planifications importerPlanifications(XSpreadsheet feuille) throws PlanChargeDaoException, LibreOfficeException {
        Planifications planification;

        final int noLigPeriodes = 1;
        final int noLigDebut = 5;
        final int noColDebut = 12;

        Map<Tache, Map<LocalDate, Double>> matrice = new HashMap<>();
        int cptLig = noLigDebut;
        LIG:
        while (true) {
//            LOGGER.debug("Ligne n°" + cptLig);

            XCell cell = Calc.getCell(feuille, 0, cptLig - 1);

            if (Calc.isEmpty(cell)) {
//                LOGGER.debug("La ligne n°" + cptLig + " commence par une cellule vide, donc il n'y a plus de tâche à parser.");
                break LIG;
            }

            if (!Calc.isNumericValue(cell)) {
                LOGGER.warn("La ligne n°" + cptLig + " ne commence pas par un n° de tâche (entier), donc on la skippe. PI, la 1ère colonne de cette ligne contient '" + Calc.getVal(cell) + "'.");
                cptLig++;
                continue LIG;
            }

            Tache tache = importerTache(feuille, cptLig);

            matrice.put(tache, new HashMap<>());

            int cptCol = noColDebut;
            COL:
            while (true) {
//                LOG.debug("Colonne n°" + cptCol);

                if (Calc.isEmpty(feuille, cptCol - 1, noLigPeriodes - 1)) {
                    break COL;
                }

                Date debutPeriode = Calc.getDate(feuille, cptCol - 1, noLigPeriodes - 1);

                Double chargePlanifiée = (Double) Calc.getVal(feuille, cptCol - 1, cptLig - 1);
                if (chargePlanifiée == null) {
                    chargePlanifiée = 0.0;
                }

                matrice.get(tache).put(Dates.asLocalDate(debutPeriode), chargePlanifiée);

                cptCol++;
            }
            cptLig++;
        }

        planification = new Planifications(matrice);
        return planification;
    }

    private Tache importerTache(XSpreadsheet feuille, int noLig) throws PlanChargeDaoException, LibreOfficeException {
        Tache tache;
        try {
            int cptCol = 1;

            int id = Calc.getInt(feuille, cptCol - 1, noLig - 1);
            cptCol++;

            String noTicketIdal = Calc.getString(feuille, cptCol - 1, noLig - 1);
            cptCol++;

            String description = Calc.getString(feuille, cptCol - 1, noLig - 1);
            cptCol++;

            String codeProjetAppli = Calc.getString(feuille, cptCol - 1, noLig - 1);
            ProjetAppli projetAppli = projetAppliDao.load(codeProjetAppli);
            cptCol++;

            Date debut = (Calc.isEmpty(feuille, cptCol - 1, noLig - 1) ? null : Calc.getDate(feuille, cptCol - 1, noLig - 1));
            cptCol++;

            Date echeance = Calc.getDate(feuille, cptCol - 1, noLig - 1);
            cptCol++;

            String codeImportance = Calc.getString(feuille, cptCol - 1, noLig - 1);
            Importance importance = importanceDao.loadByCode(codeImportance);
            cptCol++;

            double charge = Calc.getDouble(feuille, cptCol - 1, noLig - 1);
            cptCol++;

            String codeRessource = Calc.getString(feuille, cptCol - 1, noLig - 1);
            Ressource ressource = ressourceDao.load(codeRessource);
            cptCol++;

            String codeProfil = Calc.getString(feuille, cptCol - 1, noLig - 1);
            Profil profil = profilDao.load(codeProfil);
            cptCol++;

            tache = new Tache(
                    id, noTicketIdal,
                    description,
                    projetAppli,
                    Dates.asLocalDate(debut), Dates.asLocalDate(echeance),
                    importance,
                    charge,
                    ressource,
                    profil
            );

        } catch (DaoException|LibreOfficeException e) {
            throw new PlanChargeDaoException("Impossible d'importer la tâche.", e);
        }
        return tache;
    }
}

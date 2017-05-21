package fr.gouv.agriculture.dal.ct.planCharge.metier.dao.charge;

import com.sun.star.frame.XComponentLoader;
import com.sun.star.sheet.XSpreadsheet;
import com.sun.star.sheet.XSpreadsheetDocument;
import com.sun.star.table.XCell;
import fr.gouv.agriculture.dal.ct.kernel.KernelException;
import fr.gouv.agriculture.dal.ct.kernel.ParametresApplicatifs;
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
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.ModeleException;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.charge.PlanCharge;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.charge.Planifications;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.*;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.tache.Tache;
import fr.gouv.agriculture.dal.ct.planCharge.util.Dates;
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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by frederic.danna on 26/03/2017.
 */
public class PlanChargeDao extends AbstractDao<PlanCharge, LocalDate> {

    public static final String CLEF_PARAM_REP_PERSISTANCE = "persistance.repertoire";
    public static final String CLEF_PARAM_PATRON_FICHIER = "persistance.patronFichier";

    private static final Logger LOGGER = LoggerFactory.getLogger(PlanChargeDao.class);

    private static final Map<LocalDate, PlanCharge> CACHE = new HashMap<>();

    private static PlanChargeDao instance;

    public static PlanChargeDao instance() {
        if (instance == null) {
            instance = new PlanChargeDao();
        }
        return instance;
    }

    //    @Inject
    @NotNull
    private ParametresApplicatifs params = ParametresApplicatifs.instance();

/*
//    @Inject
//    @Property("persistance.repertoire")
    @NotNull
    private String repPersistanceDonnees;
*/

/*
//    @Inject
//    @Property("persistance.patronFichier")
    @NotNull
    private String patronFicPersistanceDonnees;
*/

    @NotNull
//    @Autowired
    private PlanChargeXmlWrapper wrapper = new PlanChargeXmlWrapper();

    @NotNull
//    @Autowired
    private ProjetAppliDao projetAppliDao = ProjetAppliDao.instance();

    @NotNull
//    @Autowired
    private ImportanceDao importanceDao = ImportanceDao.instance();

    @NotNull
//    @Autowired
    private RessourceDao ressourceDao = RessourceDao.instance();

    @NotNull
//    @Autowired
    private ProfilDao profilDao = ProfilDao.instance();

    // 'private' pour empêcher quiconque d'autre d'instancier cette classe (pattern "Factory").
    private PlanChargeDao() {
        super();
    }

    @NotNull
//    @Override
    public PlanCharge load(@NotNull LocalDate dateEtat) throws EntityNotFoundException, PlanChargeDaoException {
        PlanCharge plan;

        File fichierPlanif = fichierPlanCharge(dateEtat);
        if (fichierPlanif == null) {
            throw new EntityNotFoundException("Fichier inexistant pour la dateEtat du " + dateEtat.format(DateTimeFormatter.ofPattern("yyyy/MM/dd")) + ".");
        }

        try {
            plan = load(fichierPlanif);
        } catch (PlanChargeDaoException e) {
            throw new EntityNotFoundException("Impossible de charger le plan de charge en dateEtat du " + dateEtat.format(DateTimeFormatter.ofPattern("yyyy/MM/dd")), e);
        }

        return plan;
    }

    @NotNull
    public PlanCharge load(@NotNull File ficCalc) throws EntityNotFoundException, PlanChargeDaoException {
        PlanCharge plan;

        try {
            plan = plan(ficCalc);
        } catch (PlanChargeDaoException e) {
            throw new EntityNotFoundException("Impossible de charger le plan de charge depuis le fichier " + ficCalc.getAbsolutePath() + ".", e);
        }

        return plan;
    }

    public void sauver(PlanCharge planCharge) throws PlanChargeDaoException {

        LocalDate dateEtat = planCharge.getDateEtat();

        File fichierPlanif = fichierPlanCharge(dateEtat);
        if (fichierPlanif.exists()) {
            // TODO FDA 2017/04 Demander confirmation à l'utilisateur, avant.
            fichierPlanif.delete();
        }
        try {
            fichierPlanif.createNewFile();
        } catch (IOException e) {
            throw new PlanChargeDaoException("Impossible de créer le fichier '" + fichierPlanif.getAbsolutePath() + "'.", e);
        }

        serialiserPlanCharge(fichierPlanif, planCharge);
    }

    @NotNull
    public File fichierPlanCharge(@NotNull LocalDate dateEtat) throws PlanChargeDaoException {

        final String repPersistanceDonnees;
        try {
            repPersistanceDonnees = params.getParametrage(CLEF_PARAM_REP_PERSISTANCE);
        } catch (KernelException e) {
            throw new PlanChargeDaoException("Impossible de déterminer le répetoire de persistance du plan de charge.", e);
        }

        final String patronFicPersistanceDonnees;
        try {
            patronFicPersistanceDonnees = params.getParametrage(CLEF_PARAM_PATRON_FICHIER);
        } catch (KernelException e) {
            throw new PlanChargeDaoException("Impossible de déterminer le patron du nom du fichier pour persister le plan de charge.", e);
        }

        String nomFic = patronFicPersistanceDonnees
                .replaceAll("\\{dateEtat}", dateEtat.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                + ".xml";

        return new File(repPersistanceDonnees, nomFic);
    }

    /**
     * Loads data from the specified file.
     *
     * @param ficCalc
     */
    // Cf. http://code.makery.ch/library/javafx-8-tutorial/fr/part5/
    private PlanCharge plan(@NotNull File ficCalc) throws PlanChargeDaoException {
        PlanCharge plan;
        try {
            JAXBContext context = JAXBContext.newInstance(PlanChargeXmlWrapper.class);
            Unmarshaller um = context.createUnmarshaller();

            // Reading XML from the file and unmarshalling.
            wrapper = (PlanChargeXmlWrapper) um.unmarshal(ficCalc);

            plan = wrapper.extract();

        } catch (Exception e) {
            throw new PlanChargeDaoException("Impossible de dé-sérialiser le plan de charge depuis le fichier XML '" + ficCalc.getAbsolutePath() + "'.", e);
        }
        return plan;
    }

    /**
     * Saves the current data to the specified file.
     *
     * @param ficCalc
     */
    // Cf. http://code.makery.ch/library/javafx-8-tutorial/fr/part5/
    private void serialiserPlanCharge(File ficCalc, PlanCharge planCharge) throws PlanChargeDaoException {
        try {
            JAXBContext context = JAXBContext.newInstance(PlanChargeXmlWrapper.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_ENCODING, "UTF8");
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            // Wrapping our data.
            wrapper.init(planCharge);

            // Marshalling and saving XML to the file.
            m.marshal(wrapper, ficCalc);
        } catch (Exception e) {
            throw new PlanChargeDaoException("Impossible de sérialiser le plan de charge dans le fichier XML '" + ficCalc.getAbsolutePath() + "'.", e);
        }
    }

    public PlanCharge importerDepuisCalc(@NotNull File ficCalc) throws PlanChargeDaoException {
        PlanCharge planCharge;

        if (!ficCalc.exists()) {
            throw new PlanChargeDaoException("Impossible d'importer le plan ce charge, fichier non trouvé : '" + ficCalc.getAbsolutePath() + "'.");
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
            throw new PlanChargeDaoException("Impossible d'importer le plan de charge depuis le fichier '" + ficCalc.getAbsolutePath() + "'.", e);
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
        final int noLigDebut = 4;
        final int noColDebut = 12;

        Map<Tache, Map<LocalDate, Double>> calendrier = new HashMap<>();
        {
            int cptLig = noLigDebut;
            CategorieTache categorieTache = null;
            SousCategorieTache sousCategorieTache = null;
            LIG:
            while (true) {
                LOGGER.debug("Ligne n°" + cptLig);

                XCell cell = Calc.getCell(feuille, 0, cptLig - 1);

                if (Calc.isEmpty(cell)) {
                    LOGGER.debug("La ligne n°" + cptLig + " commence par une cellule vide, donc il n'y a plus de tâche à parser.");
                    break LIG;
                }

                if (!Calc.isNumericValue(cell)) {
                    String cellStrValue = Calc.getString(cell);
                    CATEGORISATION:
                    {
                        // Catégorie de tâche ?
                        CategorieTache categ = CategorieTache.valeurOuNull(cellStrValue);
                        if (categ != null) {
                            categorieTache = categ;
                            break CATEGORISATION;
                        }

                        // Sous-catégorie de tâche ?
                        SousCategorieTache sousCateg = SousCategorieTache.valeurOuNull(cellStrValue);
                        if (sousCateg != null) {
                            sousCategorieTache = sousCateg;
                            break CATEGORISATION;
                        }

                        // Pas une tâche, ni une catégorie de tâche.
                        throw new PlanChargeDaoException("La ligne n°" + cptLig + " ne commence ni par un n° de tâche (entier), ni par un code d'une catégorie de tâche (Projets, Services, etc.)."
                                + " PI, la 1ère colonne de cette ligne contient '" + Calc.getVal(cell) + "'.");
                    }
                    cptLig++;
                    continue LIG;
                }

                Tache tache = importerTache(feuille, cptLig, categorieTache, sousCategorieTache);

                calendrier.put(tache, new HashMap<>());
                {
                    int cptCol = noColDebut;
                    COL:
                    while (true) {
                        LOGGER.debug("Colonne n°" + cptCol);

                        if (Calc.isEmpty(feuille, cptCol - 1, noLigPeriodes - 1)) {
                            break COL;
                        }

                        Date debutPeriode = Calc.getDate(feuille, cptCol - 1, noLigPeriodes - 1);

                        Double chargePlanifiee = (Double) Calc.getVal(feuille, cptCol - 1, cptLig - 1);
                        if (chargePlanifiee == null) {
                            chargePlanifiee = 0.0;
                        } else {
                            chargePlanifiee = chargeArrondie(chargePlanifiee);
                        }

                        calendrier.get(tache).put(Dates.asLocalDate(debutPeriode), chargePlanifiee);

                        cptCol++;
                    }
                }
                cptLig++;
            }
        }

        planification = new Planifications(calendrier);
        return planification;
    }

    private Double chargeArrondie(Double charge) {
        return Math.round(charge * 8.0) / 8.0;
    }

    private Tache importerTache(XSpreadsheet feuille, int noLig, CategorieTache categorie, SousCategorieTache sousCategorie) throws PlanChargeDaoException, LibreOfficeException {
        Tache tache;
        try {

            int id = Calc.getInt(feuille, 1 - 1, noLig - 1);

            String noTicketIdal = Calc.getString(feuille, 2 - 1, noLig - 1);

            String description = Calc.getString(feuille, 3 - 1, noLig - 1);

            String codeProjetAppli = Calc.getString(feuille, 4 - 1, noLig - 1);
            ProjetAppli projetAppli = projetAppliDao.load(codeProjetAppli);

            int noColDebut = 5;
            Date debut = (Calc.isEmpty(feuille, noColDebut - 1, noLig - 1) ? null : Calc.getDate(feuille, noColDebut - 1, noLig - 1));

            Date echeance = Calc.getDate(feuille, 6 - 1, noLig - 1);

            String codeImportance = Calc.getString(feuille, 7 - 1, noLig - 1);
            Importance importance = importanceDao.load(codeImportance);

            double charge = Calc.getDouble(feuille, 8 - 1, noLig - 1);

            String codeRessource = Calc.getString(feuille, 9 - 1, noLig - 1);
            Ressource ressource = ressourceDao.load(codeRessource);

            String codeProfil = Calc.getString(feuille, 10 - 1, noLig - 1);
            Profil profil = profilDao.load(codeProfil);

            tache = new Tache(
                    id,
                    categorie,
                    sousCategorie,
                    noTicketIdal,
                    description,
                    projetAppli,
                    Dates.asLocalDate(debut), Dates.asLocalDate(echeance),
                    importance,
                    charge,
                    ressource,
                    profil
            );

        } catch (ModeleException | DaoException | LibreOfficeException e) {
            throw new PlanChargeDaoException("Impossible d'importer la tâche.", e);
        }
        return tache;
    }
}

package fr.gouv.agriculture.dal.ct.planCharge.metier.dao.charge;

import com.sun.star.lang.IndexOutOfBoundsException;
import com.sun.star.sheet.XSpreadsheet;
import com.sun.star.sheet.XSpreadsheetDocument;
import com.sun.star.table.CellAddress;
import com.sun.star.table.XCell;
import com.sun.star.table.XCellRange;
import fr.gouv.agriculture.dal.ct.kernel.KernelException;
import fr.gouv.agriculture.dal.ct.kernel.ParametresMetiers;
import fr.gouv.agriculture.dal.ct.libreoffice.Calc;
import fr.gouv.agriculture.dal.ct.libreoffice.LibreOfficeException;
import fr.gouv.agriculture.dal.ct.metier.dao.DaoException;
import fr.gouv.agriculture.dal.ct.metier.dao.DataAcessObject;
import fr.gouv.agriculture.dal.ct.metier.dao.EntityNotFoundException;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dao.charge.xml.PlanChargeXmlWrapper;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dao.referentiels.*;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dao.referentiels.importance.ImportanceDao;
import fr.gouv.agriculture.dal.ct.metier.modele.ModeleException;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.charge.PlanCharge;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.charge.Planifications;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.*;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.tache.Tache;
import fr.gouv.agriculture.dal.ct.planCharge.metier.service.RapportChargementPlanCharge;
import fr.gouv.agriculture.dal.ct.planCharge.metier.service.RapportImportPlanCharge;
import fr.gouv.agriculture.dal.ct.planCharge.metier.service.RapportSauvegarde;
import fr.gouv.agriculture.dal.ct.planCharge.util.Dates;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Created by frederic.danna on 26/03/2017.
 */
public class PlanChargeDao implements DataAcessObject<PlanCharge, LocalDate> {

    public static final String CLEF_PARAM_REP_PERSISTANCE = "persistance.repertoire";
    public static final String CLEF_PARAM_PATRON_FICHIER = "persistance.patronFichier";

    private static final Logger LOGGER = LoggerFactory.getLogger(PlanChargeDao.class);

    private static final Map<LocalDate, PlanCharge> CACHE = new TreeMap<>(); // TreeMap juste pour faciliter le débogage en triant les entrées sur la key.

    private static PlanChargeDao instance;

    public static PlanChargeDao instance() {
        if (instance == null) {
            instance = new PlanChargeDao();
        }
        return instance;
    }

    //    @Inject
    @NotNull
    private ParametresMetiers params = ParametresMetiers.instance();

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
    private StatutDao statutDao = StatutDao.instance();

    @NotNull
//    @Autowired
    private ImportanceDao importanceDao = ImportanceDao.instance();

    @NotNull
//    @Autowired
    private RessourceDao ressourceDao = RessourceDao.instance();
    @NotNull
//    @Autowired
    private RessourceHumaineDao ressourceHumaineDao = RessourceHumaineDao.instance();

    @NotNull
//    @Autowired
    private ProfilDao profilDao = ProfilDao.instance();

    // 'private' pour empêcher quiconque d'autre d'instancier cette classe (pattern "Factory").
    private PlanChargeDao() {
        super();
    }

    @NotNull
//    @Override
    public PlanCharge charger(@NotNull LocalDate dateEtat, @NotNull RapportChargementPlanCharge rapport) throws EntityNotFoundException, PlanChargeDaoException {
        PlanCharge plan;

        File fichierPlanif = fichierPlanCharge(dateEtat);
        if (fichierPlanif == null) {
            throw new EntityNotFoundException("Fichier inexistant pour la date d'état du " + dateEtat.format(DateTimeFormatter.ofPattern("yyyy/MM/dd")) + ".");
        }

        try {
            plan = charger(fichierPlanif, rapport);
        } catch (PlanChargeDaoException e) {
            throw new PlanChargeDaoException("Impossible de charger le plan de charge en date d'état du " + dateEtat.format(DateTimeFormatter.ofPattern("yyyy/MM/dd")), e);
        }

        return plan;
    }

    @NotNull
    public PlanCharge charger(@NotNull File ficCalc, @NotNull RapportChargementPlanCharge rapport) throws PlanChargeDaoException {
        PlanCharge plan;

        try {
            plan = plan(ficCalc, rapport);
        } catch (PlanChargeDaoException e) {
            throw new PlanChargeDaoException("Impossible de charger le plan de charge depuis le fichier '" + ficCalc.getAbsolutePath() + "'.", e);
        }

        return plan;
    }

    public void sauver(@NotNull PlanCharge planCharge, @NotNull RapportSauvegarde rapport) throws PlanChargeDaoException {

        LocalDate dateEtat = planCharge.getDateEtat();

        File fichierPlanif = fichierPlanCharge(dateEtat);
        if (fichierPlanif.exists()) {
            // TODO FDA 2017/04 Demander confirmation à l'utilisateur, avant. (Donc dans la couche View, avant d'appeler la couche métier.)
            boolean isFileDeleted = fichierPlanif.delete();
            if (!isFileDeleted) {
                throw new PlanChargeDaoException("Impossible de suprimer le fichier '" + fichierPlanif.getAbsolutePath() + "'.");
            }
        }

        //noinspection BooleanVariableAlwaysNegated
        boolean isNewFileCreated;
        try {
            isNewFileCreated = fichierPlanif.createNewFile();
        } catch (IOException e) {
            throw new PlanChargeDaoException("Impossible de créer le fichier '" + fichierPlanif.getAbsolutePath() + "'.", e);
        }
        if (!isNewFileCreated) {
            throw new PlanChargeDaoException("Impossible de créer le fichier '" + fichierPlanif.getAbsolutePath() + "'.");
        }

        serialiserPlanCharge(fichierPlanif, planCharge, rapport);
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
     * @param rapport
     */
    // Cf. http://code.makery.ch/library/javafx-8-tutorial/fr/part5/
    private PlanCharge plan(@NotNull File ficCalc, @NotNull RapportChargementPlanCharge rapport) throws PlanChargeDaoException {
        PlanCharge plan;
        try {
            JAXBContext context = JAXBContext.newInstance(PlanChargeXmlWrapper.class);
            Unmarshaller um = context.createUnmarshaller();

            // Reading XML from the file and unmarshalling.
            rapport.setAvancement("Lecture du fichier XML...");
            wrapper = (PlanChargeXmlWrapper) um.unmarshal(ficCalc);

            ctrlVersionsEntreXmlEtAppli(wrapper.getVersionApplication(), wrapper.getVersionFormat());

            rapport.setAvancement("Transformation du XML en objets métier...");
            plan = wrapper.extract();

        } catch (Exception e) {
            throw new PlanChargeDaoException("Impossible de dé-sérialiser le plan de charge depuis le fichier XML '" + ficCalc.getAbsolutePath() + "'.", e);
        }
        return plan;
    }

    private void ctrlVersionsEntreXmlEtAppli(@NotNull String versionApplication, @NotNull String versionFormat) throws PlanChargeDaoException {
        ctrlVersionsCodeEntreXmlEtAppli(versionApplication);
        ctrlVersionsFichierEntreXmlEtAppli(versionFormat);
    }

    // TODO FDA 2017/07 Tester.
    private void ctrlVersionsFichierEntreXmlEtAppli(@NotNull String versionFormatRequis) throws PlanChargeDaoException {
        String versionFormat = PlanChargeXmlWrapper.VERSION_FORMAT;
        if (versionFormat.compareTo(versionFormatRequis) < 0) {
            throw new PlanChargeDaoException("Le fichier XML a un format en version " + versionFormatRequis + ", or l'application gère la version de format " + versionFormat + ".");
        }

    }

    // TODO FDA 2017/07 Tester.
    private void ctrlVersionsCodeEntreXmlEtAppli(@NotNull String versionApplicationRequise) throws PlanChargeDaoException {
        String versionApplication;
        try {
            versionApplication = params.getParametrage("application.version");
        } catch (KernelException e) {
            throw new PlanChargeDaoException("Impossible de déterminer la version de l'application.", e);
        }
        if (versionApplication.compareTo(versionApplicationRequise) < 0) {
            throw new PlanChargeDaoException("Le fichier XML requiert une version de l'application >= " + versionApplicationRequise + ". Or l'application est en version " + versionApplication + ".");
        }
    }

    /**
     * Saves the current data to the specified file.
     *
     * @param ficCalc
     * @param rapport
     */
    // Cf. http://code.makery.ch/library/javafx-8-tutorial/fr/part5/
    private void serialiserPlanCharge(@NotNull File ficCalc, @NotNull PlanCharge planCharge, @NotNull RapportSauvegarde rapport) throws PlanChargeDaoException {
        try {
            JAXBContext context = JAXBContext.newInstance(PlanChargeXmlWrapper.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_ENCODING, "UTF8");
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            // Wrapping our data.
            wrapper.init(planCharge, rapport);

            // Marshalling and saving XML to the file.
            m.marshal(wrapper, ficCalc);

        } catch (Exception e) {
            throw new PlanChargeDaoException("Impossible de sérialiser le plan de charge dans le fichier XML '" + ficCalc.getAbsolutePath() + "'.", e);
        }
    }

    public PlanCharge importerDepuisCalc(@NotNull File ficCalc, @NotNull RapportImportPlanCharge rapport) throws PlanChargeDaoException {
        PlanCharge planCharge;

        if (!ficCalc.exists()) {
            throw new PlanChargeDaoException("Impossible d'importer le plan ce charge, fichier non trouvé : '" + ficCalc.getAbsolutePath() + "'.");
        }
        XSpreadsheetDocument docCalc = null;
        try {

            rapport.setAvancement("Ouverture du fichier Calc...");
            // Cf. http://fivedots.coe.psu.ac.th/~ad/jlop/jlop04/04.%20Spreadsheet%20Processing.pdf
            docCalc = Calc.openDoc(ficCalc.getAbsolutePath());
            if (docCalc == null) {
                throw new PlanChargeDaoException("Document introuvable : '" + ficCalc.getAbsolutePath() + "'.");
            }

            rapport.setAvancement("Import des données...");
            planCharge = importer(docCalc, rapport);

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

    private PlanCharge importer(@NotNull XSpreadsheetDocument calc, @NotNull RapportImportPlanCharge rapport) throws PlanChargeDaoException, LibreOfficeException {
        PlanCharge planCharge;

        try {
            XSpreadsheet feuilleParams = Calc.getSheet(calc, "param");
            XSpreadsheet feuilleCharges = Calc.getSheet(calc, "Charge");
            XSpreadsheet feuilleTaches = Calc.getSheet(calc, "Tâches");
            planCharge = importer(feuilleParams, feuilleCharges, feuilleTaches, rapport);
        } catch (LibreOfficeException e) {
            throw new PlanChargeDaoException("Impossible d'importer le plan de charge depuis le doc OOCalc.", e);
        }

        return planCharge;
    }

    private PlanCharge importer(@NotNull XSpreadsheet feuilleParams, @NotNull XSpreadsheet feuilleCharges, @NotNull XSpreadsheet feuilleTaches, @NotNull RapportImportPlanCharge rapport) throws PlanChargeDaoException, LibreOfficeException {
        PlanCharge planDeCharge;

        final int noLigDateEtat = 1;
        final int noColDateEtat = 4;

        Date dateEtat = Calc.getDate(feuilleCharges, noColDateEtat - 1, noLigDateEtat - 1);
        if (dateEtat == null) {
            throw new PlanChargeDaoException("Impossible de retrouver la date d'état.");
        }

        Referentiels referentiels = importerReferentiels(feuilleParams, rapport);

        // TODO FDA 2017/07 Créer méthode "importerTaches", à appeler avant "importerPlanifications" (+ logique).

        Planifications planifications = importerPlanifications(feuilleCharges, feuilleTaches, rapport);

        planDeCharge = new PlanCharge(Dates.asLocalDate(dateEtat), referentiels, planifications);

        return planDeCharge;
    }

    @NotNull
    private Referentiels importerReferentiels(@NotNull XSpreadsheet feuilleParams, @NotNull RapportImportPlanCharge rapport) throws PlanChargeDaoException {
        rapport.setAvancement("Import des jours fériés...");
        Set<JourFerie> joursferies = importerJoursFeries(feuilleParams);
        rapport.setAvancement("Import des importances...");
        Set<Importance> importances = importerImportances(feuilleParams);
        rapport.setAvancement("Import des profils...");
        Set<Profil> profils = importerProfils(feuilleParams);
        //noinspection HardcodedFileSeparator
        rapport.setAvancement("Import des projets/applis...");
        Set<ProjetAppli> projetsApplis = importerProjetsApplis(feuilleParams);
        rapport.setAvancement("Import des statuts...");
        Set<Statut> statuts = importerStatuts(feuilleParams);
        rapport.setAvancement("Import des ressources...");
        Set<RessourceHumaine> ressourcesHumaines = importerRessourcesHumaines(feuilleParams);
        return new Referentiels(
                joursferies,
                importances,
                profils,
                projetsApplis,
                statuts,
                ressourcesHumaines
        );
    }

    @NotNull
    private Set<JourFerie> importerJoursFeries(@NotNull XSpreadsheet feuilleParams) throws PlanChargeDaoException {
        Set<JourFerie> joursFeries = new TreeSet<>(); // TreeSet (au lieu de hashSet) pour trier, juste pour faciliter le débogage.
        try {
            XCellRange plageRecherche = Calc.getCellRange(feuilleParams, "A1:A300");
            XCell cellule = Calc.findFirst("Congés et fêtes", plageRecherche);
            if (cellule == null) {
                throw new PlanChargeDaoException("Impossible de retrouver les jours fériés.");
            }
            CellAddress adrCell = Calc.getCellAddress(cellule);
            int noLigTitre = adrCell.Row + 1;
            int noColTitre = adrCell.Column + 1;
            assert Calc.getString(feuilleParams, noColTitre - 1, (noLigTitre + 1) - 1).equals("Date");
            assert Calc.getString(feuilleParams, (noColTitre + 1) - 1, (noLigTitre + 1) - 1).equals("Raison");
            int noLig = noLigTitre + 2;
            while (true) {
                XCell dateCell = Calc.getCell(feuilleParams, noColTitre - 1, noLig - 1);
                if (Calc.isEmpty(dateCell)) {
                    break;
                }
                Date date = Calc.getDate(dateCell);
                assert date != null;
                LocalDate dateLocale = Dates.asLocalDate(date);
                assert dateLocale != null;
                String raison = Calc.getString(feuilleParams, (noColTitre + 1) - 1, noLig - 1);
                JourFerie jourFerie = new JourFerie(dateLocale, raison);
                joursFeries.add(jourFerie);

                noLig++;
            }
            return joursFeries;
        } catch (Exception e) {
            throw new PlanChargeDaoException("Impossible d'importer les jours fériés.", e);
        }
    }


    @NotNull
    private Set<Importance> importerImportances(@NotNull XSpreadsheet feuilleParams) throws PlanChargeDaoException {
        Set<Importance> importances = new TreeSet<>(); // TreeSet (au lieu de hashSet) pour trier, juste pour faciliter le débogage.
        try {
            XCellRange plageRecherche = Calc.getCellRange(feuilleParams, "A1:A300");
            XCell cellule = Calc.findFirst("Importances", plageRecherche);
            if (cellule == null) {
                throw new PlanChargeDaoException("Impossible de retrouver les importances.");
            }
            CellAddress adrCell = Calc.getCellAddress(cellule);
            int noLigTitre = adrCell.Row + 1;
            int noColTitre = adrCell.Column + 1;
            assert Calc.getString(feuilleParams, noColTitre - 1, (noLigTitre + 1) - 1).equals("Code");
            assert Calc.getString(feuilleParams, (noColTitre + 1) - 1, (noLigTitre + 1) - 1).equals("Poids");
            int noLig = noLigTitre + 2;
            while (true) {
                XCell codeCell = Calc.getCell(feuilleParams, noColTitre - 1, noLig - 1);
                if (Calc.isEmpty(codeCell)) {
                    break;
                }
                String codeInterne = Calc.getString(codeCell);
                Importance importance = new Importance(codeInterne);
                importances.add(importance);
                importanceDao.createOrUpdate(importance);

                noLig++;
            }
            return importances;
        } catch (Exception e) {
            throw new PlanChargeDaoException("Impossible d'importer les importances.", e);
        }
    }

    @NotNull
    private Set<Profil> importerProfils(@NotNull XSpreadsheet feuilleParams) throws PlanChargeDaoException {
        Set<Profil> profils = new TreeSet<>(); // TreeSet (au lieu de hashSet) pour trier, juste pour faciliter le débogage.
        try {
            XCellRange plageRecherche = Calc.getCellRange(feuilleParams, "A1:A300");
            XCell cellule = Calc.findFirst("Profils", plageRecherche);
            if (cellule == null) {
                throw new PlanChargeDaoException("Impossible de retrouver les profils.");
            }
            CellAddress adrCell = Calc.getCellAddress(cellule);
            int noLigTitre = adrCell.Row + 1;
            int noColTitre = adrCell.Column + 1;
            assert Calc.getString(feuilleParams, noColTitre - 1, (noLigTitre + 1) - 1).equals("Code");
            assert Calc.getString(feuilleParams, (noColTitre + 1) - 1, (noLigTitre + 1) - 1).equals("Libellé");
//            assert Calc.getString(feuilleParams, (noColTitre + 2) - 1, (noLigTitre + 1) - 1).equals("Rôle CT");
            int noLig = noLigTitre + 2;
            while (true) {
                XCell codeCell = Calc.getCell(feuilleParams, noColTitre - 1, noLig - 1);
                if (Calc.isEmpty(codeCell)) {
                    break;
                }
                String code = Calc.getString(codeCell);
                Profil profil = new Profil(code);
                profils.add(profil);
                profilDao.createOrUpdate(profil);

                noLig++;
            }
            return profils;
        } catch (Exception e) {
            throw new PlanChargeDaoException("Impossible d'importer les profils.", e);
        }
    }

    @NotNull
    private Set<ProjetAppli> importerProjetsApplis(@NotNull XSpreadsheet feuilleParams) throws PlanChargeDaoException {
        Set<ProjetAppli> projetsApplis = new TreeSet<>(); // TreeSet (au lieu de hashSet) pour trier, juste pour faciliter le débogage.
        try {
            XCellRange plageRecherche = Calc.getCellRange(feuilleParams, "A1:A300");
            //noinspection HardcodedFileSeparator
            XCell cellule = Calc.findFirst("Projets / Applications", plageRecherche);
            if (cellule == null) {
                //noinspection HardcodedFileSeparator
                throw new PlanChargeDaoException("Impossible de retrouver les projets/applications.");
            }
            CellAddress adrCell = Calc.getCellAddress(cellule);
            int noLigTitre = adrCell.Row + 1;
            int noColTitre = adrCell.Column + 1;
            assert Calc.getString(feuilleParams, noColTitre - 1, (noLigTitre + 1) - 1).equals("Code");
            assert Calc.getString(feuilleParams, (noColTitre + 1) - 1, (noLigTitre + 1) - 1).equals("Nom");
            int noLig = noLigTitre + 2;
            while (true) {
                XCell codeCell = Calc.getCell(feuilleParams, noColTitre - 1, noLig - 1);
                if (Calc.isEmpty(codeCell)) {
                    break;
                }
                String code = Calc.getString(codeCell);
                ProjetAppli projetAppli = new ProjetAppli(code);
                projetsApplis.add(projetAppli);
                projetAppliDao.createOrUpdate(projetAppli);

                noLig++;
            }
            return projetsApplis;
        } catch (Exception e) {
            //noinspection HardcodedFileSeparator
            throw new PlanChargeDaoException("Impossible d'importer les projets/applis.", e);
        }
    }

    @NotNull
    private Set<Statut> importerStatuts(@NotNull XSpreadsheet feuilleParams) throws PlanChargeDaoException {
        Set<Statut> statuts = new TreeSet<>(); // TreeSet (au lieu de hashSet) pour trier, juste pour faciliter le débogage.
        try {
            XCellRange plageRecherche = Calc.getCellRange(feuilleParams, "A1:A300");
            XCell cellule = Calc.findFirst("Statuts", plageRecherche);
            if (cellule == null) {
                throw new PlanChargeDaoException("Impossible de retrouver les statuts.");
            }
            CellAddress adrCell = Calc.getCellAddress(cellule);
            int noLigTitre = adrCell.Row + 1;
            int noColTitre = adrCell.Column + 1;
            assert Calc.getString(feuilleParams, noColTitre - 1, (noLigTitre + 1) - 1).equals("Code");
            assert Calc.getString(feuilleParams, (noColTitre + 1) - 1, (noLigTitre + 1) - 1).equals("Description");
            int noLig = noLigTitre + 2;
            while (true) {
                XCell codeCell = Calc.getCell(feuilleParams, noColTitre - 1, noLig - 1);
                if (Calc.isEmpty(codeCell)) {
                    break;
                }
                String code = Calc.getString(codeCell);
                Statut statut = new Statut(code);
                statuts.add(statut);
                statutDao.createOrUpdate(statut);

                noLig++;
            }
            return statuts;
        } catch (Exception e) {
            throw new PlanChargeDaoException("Impossible d'importer les statuts.", e);
        }
    }

    @NotNull
    private Set<RessourceHumaine> importerRessourcesHumaines(XSpreadsheet feuilleParams) throws PlanChargeDaoException {
        Set<RessourceHumaine> ressourcesHumaines = new TreeSet<>(); // TreeSet (au lieu de hashSet) pour trier, juste pour faciliter le débogage.
        try {
            XCellRange plageRecherche = Calc.getCellRange(feuilleParams, "A1:A300");
            XCell cellule = Calc.findFirst("Ressources", plageRecherche);
            if (cellule == null) {
                throw new PlanChargeDaoException("Impossible de retrouver les ressources.");
            }
            CellAddress adrCell = Calc.getCellAddress(cellule);
            int noLigTitre = adrCell.Row + 1;
            int noColTitre = adrCell.Column + 1;
            assert Calc.getString(feuilleParams, noColTitre - 1, (noLigTitre + 1) - 1).equals("Trigramme");
            assert Calc.getString(feuilleParams, (noColTitre + 1) - 1, (noLigTitre + 1) - 1).equals("Prénom");
            assert Calc.getString(feuilleParams, (noColTitre + 2) - 1, (noLigTitre + 1) - 1).equals("Nom");
            assert Calc.getString(feuilleParams, (noColTitre + 3) - 1, (noLigTitre + 1) - 1).equals("Société");
            assert Calc.getString(feuilleParams, (noColTitre + 4) - 1, (noLigTitre + 1) - 1).equals("Début");
            assert Calc.getString(feuilleParams, (noColTitre + 5) - 1, (noLigTitre + 1) - 1).equals("Fin");
            int noLig = noLigTitre + 2;
            while (true) {
                XCell trigrammeCell = Calc.getCell(feuilleParams, noColTitre - 1, noLig - 1);
                if (Calc.isEmpty(trigrammeCell)) {
                    break;
                }
                String trigramme = Calc.getString(trigrammeCell);
                String prenom = Calc.getString(feuilleParams, (noColTitre + 1) - 1, noLig - 1);
                String nom = Calc.getString(feuilleParams, (noColTitre + 2) - 1, noLig - 1);
                String societe = Calc.getString(feuilleParams, (noColTitre + 3) - 1, noLig - 1);
                //noinspection HardcodedFileSeparator
                Date debutMission = (
                        Calc.getString(feuilleParams, (noColTitre + 4) - 1, noLig - 1).equals("N/A") ? null
                                : Calc.getDate(feuilleParams, (noColTitre + 4) - 1, noLig - 1)
                );
                //noinspection HardcodedFileSeparator
                Date finMission = (
                        Calc.getString(feuilleParams, (noColTitre + 5) - 1, noLig - 1).equals("N/A") ? null
                                : Calc.getDate(feuilleParams, (noColTitre + 5) - 1, noLig - 1)
                );
                LocalDate debutMissionLocale = Dates.asLocalDate(debutMission);
                LocalDate finMissionLocale = Dates.asLocalDate(finMission);
                RessourceHumaine ressource = new RessourceHumaine(trigramme, nom, prenom, societe, debutMissionLocale, finMissionLocale);
                ressourcesHumaines.add(ressource);
                ressourceHumaineDao.createOrUpdate(ressource);

                noLig++;
            }
            return ressourcesHumaines;
        } catch (Exception e) {
            throw new PlanChargeDaoException("Impossible d'importer les ressources humaines.", e);
        }
    }

    private Planifications importerPlanifications(@NotNull XSpreadsheet feuilleCharges, @NotNull XSpreadsheet feuilleTaches, @NotNull RapportImportPlanCharge rapport) throws PlanChargeDaoException, LibreOfficeException {
        Planifications planification;

        final int noLigPeriodes = 1;
        final int noLigDebut = 4;
        final int noColDebut = 12;

        Map<Tache, Map<LocalDate, Double>> calendrier = new TreeMap<>(); // TreeMap juste pour faciliter le débogage en triant les entrées sur la key.
        {
            int cptLig = noLigDebut;
            CategorieTache categorieTache = null;
            SousCategorieTache sousCategorieTache = null;
            LIG:
            while (true) {
                LOGGER.debug("Ligne n°" + cptLig);
                rapport.setAvancement("Import de la ligne " + cptLig + "...");

                XCell cell = Calc.getCell(feuilleCharges, 0, cptLig - 1);

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

                Tache tache = importerTache(feuilleCharges, feuilleTaches, cptLig, categorieTache, sousCategorieTache);

                calendrier.put(tache, new TreeMap<>()); // TreeMap juste pour faciliter le débogage en triant les entrées sur la key.
                {
                    int cptCol = noColDebut;
                    COL:
                    while (true) {
                        LOGGER.debug("Colonne n°" + cptCol);

                        if (Calc.isEmpty(feuilleCharges, cptCol - 1, noLigPeriodes - 1)) {
                            break COL;
                        }

                        Date debutPeriode = Calc.getDate(feuilleCharges, cptCol - 1, noLigPeriodes - 1);

                        Double chargePlanifiee = (Double) Calc.getVal(feuilleCharges, cptCol - 1, cptLig - 1);
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

    private Double chargeArrondie(@NotNull Double charge) {
        return Math.round(charge * 8.0) / 8.0;
    }

    private Tache importerTache(@NotNull XSpreadsheet feuilleCharges, @NotNull XSpreadsheet feuilleTaches, int noLig, @NotNull CategorieTache categorie, @Null SousCategorieTache sousCategorie) throws PlanChargeDaoException, LibreOfficeException {
        Tache tache;
        try {

            int id = Calc.getInt(feuilleCharges, 1 - 1, noLig - 1);

            String noTicketIdal = Calc.getString(feuilleCharges, 2 - 1, noLig - 1);

            String description = Calc.getString(feuilleCharges, 3 - 1, noLig - 1);

            String codeProjetAppli = Calc.getString(feuilleCharges, 4 - 1, noLig - 1);
            ProjetAppli projetAppli = projetAppliDao.load(codeProjetAppli);

            String codeStatut = codeStatut(id, feuilleTaches);
            Statut statut = statutDao.load(codeStatut);

            int noColDebut = 5;
            Date debut = (Calc.isEmpty(feuilleCharges, noColDebut - 1, noLig - 1) ? null : Calc.getDate(feuilleCharges, noColDebut - 1, noLig - 1));

            Date echeance = Calc.getDate(feuilleCharges, 6 - 1, noLig - 1);

            String codeImportance = Calc.getString(feuilleCharges, 7 - 1, noLig - 1);
            Importance importance = importanceDao.load(codeImportance);

            double charge = Calc.getDouble(feuilleCharges, 8 - 1, noLig - 1);

            String codeRessource = Calc.getString(feuilleCharges, 9 - 1, noLig - 1);
            Ressource ressource = ressourceDao.loadAny(codeRessource);

            String codeProfil = Calc.getString(feuilleCharges, 10 - 1, noLig - 1);
            Profil profil = (
                    codeProfil.equals(Profil.TOUS.getCode()) ? Profil.TOUS :
                            profilDao.load(codeProfil)
            );

            tache = new Tache(
                    id,
                    categorie,
                    sousCategorie,
                    noTicketIdal,
                    description,
                    projetAppli,
                    statut,
                    Dates.asLocalDate(debut), Dates.asLocalDate(echeance),
                    importance,
                    charge,
                    ressource,
                    profil
            );

        } catch (/*ModeleException |*/ DaoException | LibreOfficeException e) {
            throw new PlanChargeDaoException("Impossible d'importer la tâche.", e);
        }
        return tache;
    }

    @NotNull
    private String codeStatut(int idTache, @NotNull XSpreadsheet feuilleTaches) throws PlanChargeDaoException {

        final int noColIndexes = 1;
        final int noColStatuts = 13;

        final int noLigDebut = 4;
        final int noLigFin = 200;

        try {

            // Recherche de la tâche, identifiée par son ID :
            Integer noLigTache = null;
            for (int cptLig = noLigDebut; cptLig <= noLigFin; cptLig++) {
                XCell idCell = feuilleTaches.getCellByPosition(noColIndexes - 1, cptLig - 1);
                Integer idCourant = Calc.getInt(idCell);
                if (idCourant.equals(idTache)) {
                    noLigTache = cptLig;
                    break;
                }
            }
            if (noLigTache == null) {
                throw new PlanChargeDaoException("impossible de trouver la tâche n°" + idTache + " dans la feuille Calc.");
            }
            assert noLigTache != null;

            // Recherche du statut de la tâche :
            String codeStatut = Calc.getString(feuilleTaches, noColStatuts - 1, noLigTache - 1);

            return codeStatut;

        } catch (IndexOutOfBoundsException | LibreOfficeException e) {
            throw new PlanChargeDaoException("Impossible de retrouver le statut de la tâche n°" + idTache + ".", e);
        }
    }
}

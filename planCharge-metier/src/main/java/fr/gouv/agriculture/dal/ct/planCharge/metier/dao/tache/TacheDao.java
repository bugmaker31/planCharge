package fr.gouv.agriculture.dal.ct.planCharge.metier.dao.tache;

import com.sun.star.sheet.XSpreadsheet;
import com.sun.star.sheet.XSpreadsheetDocument;
import com.sun.star.table.XCell;
import fr.gouv.agriculture.dal.ct.kernel.ParametresMetiers;
import fr.gouv.agriculture.dal.ct.libreoffice.Calc;
import fr.gouv.agriculture.dal.ct.libreoffice.LibreOfficeException;
import fr.gouv.agriculture.dal.ct.metier.dao.DaoException;
import fr.gouv.agriculture.dal.ct.metier.dao.DataAcessObject;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dao.referentiels.ProfilDao;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dao.referentiels.ProjetAppliDao;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dao.referentiels.RessourceDao;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dao.referentiels.StatutDao;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dao.referentiels.importance.ImportanceDao;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.*;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.tache.Tache;
import fr.gouv.agriculture.dal.ct.planCharge.metier.service.RapportImportTaches;
import fr.gouv.agriculture.dal.ct.planCharge.util.Dates;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import java.io.File;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by frederic.danna on 26/03/2017.
 */
public class TacheDao implements DataAcessObject<Tache, Integer> {

    private static final Logger LOGGER = LoggerFactory.getLogger(TacheDao.class);

    private static TacheDao instance;

    public static TacheDao instance() {
        if (instance == null) {
            instance = new TacheDao();
        }
        return instance;
    }

    //    @Inject
    @NotNull
    private ParametresMetiers params = ParametresMetiers.instance();

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
    private ProfilDao profilDao = ProfilDao.instance();


    // 'private' pour empêcher quiconque d'autre d'instancier cette classe (pattern "Factory").
    private TacheDao() {
        super();
    }


    public Set<Tache> importerDepuisCalc(@NotNull File ficCalc, @NotNull RapportImportTaches rapport) throws TacheDaoException {
        Set<Tache> taches;

        if (!ficCalc.exists()) {
            throw new TacheDaoException("Impossible d'importer les tâches, fichier non trouvé : '" + ficCalc.getAbsolutePath() + "'.");
        }
        XSpreadsheetDocument docCalc = null;
        try {

            // Cf. http://fivedots.coe.psu.ac.th/~ad/jlop/jlop04/04.%20Spreadsheet%20Processing.pdf
            rapport.setAvancement("Ouverture du fichier Calc...");
            docCalc = Calc.openDoc(ficCalc.getAbsolutePath());
            if (docCalc == null) {
                throw new TacheDaoException("Document introuvable : '" + ficCalc.getAbsolutePath() + "'.");
            }

            rapport.setAvancement("Import des tâches...");
            taches = importer(docCalc, rapport);

        } catch (LibreOfficeException e) {
            throw new TacheDaoException("Impossible d'importer les tâches depuis le fichier '" + ficCalc.getAbsolutePath() + "'.", e);
        } finally {
            if (docCalc != null) {
                try {
                    Calc.closeDoc(docCalc);
                } catch (LibreOfficeException e) {
                    throw new TacheDaoException("Impossible de fermer (close) LibreOffice.", e);
                }
            }
        }

        return taches;
    }

    private Set<Tache> importer(XSpreadsheetDocument calc, @NotNull RapportImportTaches rapport) throws TacheDaoException, LibreOfficeException {
        Set<Tache> taches;

        try {
            XSpreadsheet feuilleCharge = Calc.getSheet(calc, "Tâches");
            taches = importerTaches(feuilleCharge, rapport);
        } catch (LibreOfficeException e) {
            throw new TacheDaoException("Impossible d'importer les tâches depuis le document OOCalc.", e);
        }

        return taches;
    }

    private Set<Tache> importerTaches(XSpreadsheet feuille, @NotNull RapportImportTaches rapport) throws TacheDaoException, LibreOfficeException {
        Set<Tache> taches = new HashSet<>();

        //noinspection TooBroadScope
        int noLigDebut = 4;

        {
            int cptLig = noLigDebut;
            LIG:
            while (true) {
                LOGGER.debug("Ligne n°" + cptLig);
                rapport.setAvancement("Import de la ligne n°" + cptLig + "...");
                rapport.incrNbrTachesImportees();
                PARSE_LIG:
                {
                    XCell cell = Calc.getCell(feuille, 0, cptLig - 1);

                    if (Calc.isEmpty(cell)) {
                        LOGGER.debug("La ligne n°" + cptLig + " commence par une cellule vide, donc il n'y a plus de tâche à parser.");
                        break LIG;
                    }

                    String statutTache = Calc.getString(feuille, 13 - 1, cptLig - 1);
                    if (statutTache.compareTo("85-Reportée") >= 0) {
                        LOGGER.debug("Tâche avec statut '" + statutTache + "' (< \"85-Reportée\"), skippée.");
                        break PARSE_LIG;
                    }

                    if (!Calc.isNumericValue(cell)) {
                        // Pas une tâche, ni une catégorie de tâche.
                        throw new TacheDaoException("La ligne n°" + cptLig + " ne commence ni par un n° de tâche (entier), ni par un code d'une catégorie de tâche (Projets, Services, etc.)."
                                + " PI, la 1ère colonne de cette ligne contient '" + Calc.getVal(cell) + "'.");
                    }

                    Tache tache = importerTache(feuille, cptLig);

                    taches.add(tache);
                    rapport.incrNbrTachesPlanifiees();
                }
                cptLig++;
            }
        }

        return taches;
    }

    @SuppressWarnings({"UseOfObsoleteDateTimeApi", "MagicNumber"})
    private Tache importerTache(XSpreadsheet feuille, int noLig) throws TacheDaoException, LibreOfficeException {
        Tache tache;
        try {
            //noinspection PointlessArithmeticExpression,LocalVariableNamingConvention
            int id = Calc.getInt(feuille, 1 - 1, noLig - 1);

            String noTicketIdal = Calc.getString(feuille, 2 - 1, noLig - 1);

            String description = Calc.getString(feuille, 3 - 1, noLig - 1);

            String codeProfil = Calc.getString(feuille, 6 - 1, noLig - 1);
            Profil profil = profilDao.load(codeProfil);

            String codeProjetAppli = Calc.getString(feuille, 12 - 1, noLig - 1);
            ProjetAppli projetAppli = projetAppliDao.load(codeProjetAppli);

            String codeStatut = Calc.getString(feuille, 13 - 1, noLig - 1);
            Statut statut = statutDao.createOrUpdate(new Statut(codeStatut));

            int noColDebut = 8;
            Date debut = (Calc.isEmpty(feuille, noColDebut - 1, noLig - 1) ? null : Calc.getDate(feuille, noColDebut - 1, noLig - 1));

            Date echeance = Calc.getDate(feuille, 9 - 1, noLig - 1);
            LocalDate echeanceLocale = Dates.asLocalDate(echeance);
            assert echeanceLocale != null;

            String codeImportance = Calc.getString(feuille, 11 - 1, noLig - 1);
            Importance importance = importanceDao.load(codeImportance);

            double charge = Calc.getDouble(feuille, 18 - 1, noLig - 1);

            String codeRessource = Calc.getString(feuille, 7 - 1, noLig - 1);
            Ressource<?> ressource = ressourceDao.load(codeRessource);

            tache = new Tache(
                    id,
                    CategorieTache.SERVICE, // FIXME FDA 2017/05 La catégorie n'est pas TOUJOURS "service". Mais on n'a pas cette info dans le fichier Calc...
                    null, // FIXME FDA 2017/05 La sous-catégorie n'est pas TOUJOURS vide. Mais on n'a pas cette info dans le fichier Calc...
                    noTicketIdal,
                    description,
                    projetAppli,
                    statut,
                    Dates.asLocalDate(debut),
                    echeanceLocale,
                    importance,
                    charge,
                    ressource,
                    profil
            );

        } catch (/*ModeleException | */DaoException | LibreOfficeException e) {
            throw new TacheDaoException("Impossible d'importer la tâche de la ligne n°" + noLig + ".", e);
        }
        return tache;
    }
}

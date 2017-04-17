package fr.gouv.agriculture.dal.ct.planCharge.metier.dao.charge;

import fr.gouv.agriculture.dal.ct.planCharge.metier.dao.referentiels.ImportanceDao;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dao.referentiels.ProfilDao;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dao.referentiels.ProjetAppliDao;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dao.referentiels.RessourceDao;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.charge.PlanCharge;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.charge.Planifications;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.Tache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by frederic.danna on 26/03/2017.
 */
public class PlanChargeDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(PlanChargeDao.class);

    private ProjetAppliDao projetAppliDao = new ProjetAppliDao();
    private ImportanceDao importanceDao = new ImportanceDao();
    private RessourceDao ressourceDao = new RessourceDao();
    private ProfilDao profilDao = new ProfilDao();

    public PlanCharge load(LocalDate date) {

        // TODO FDA 2017/03 Débouchonner.
        Map<Tache, Map<LocalDate, Double>> matrice = new HashMap<>();

        Planifications planifications = new Planifications(matrice);
        // >>> Partie générée, ne pas modifier à la main
        {
            Tache tache = new Tache(2001, "IDAL0001", "Tâche n°1", projetAppliDao.load("ProjetA"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("BPE"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), 1.0);
            planif.put(LocalDate.of(2017, 4, 24), 1.0);
            planif.put(LocalDate.of(2017, 5, 1), 1.0);
            planif.put(LocalDate.of(2017, 5, 8), .0);
            planif.put(LocalDate.of(2017, 5, 15), .0);
            planif.put(LocalDate.of(2017, 5, 22), .0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .0);
            planif.put(LocalDate.of(2017, 6, 12), .0);
            planif.put(LocalDate.of(2017, 6, 19), .0);
            planif.put(LocalDate.of(2017, 6, 26), .0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2002, "IDAL0002", "Tâche n°2", projetAppliDao.load("ProjetA"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("BPE"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), .0);
            planif.put(LocalDate.of(2017, 4, 24), .0);
            planif.put(LocalDate.of(2017, 5, 1), 2.0);
            planif.put(LocalDate.of(2017, 5, 8), 2.0);
            planif.put(LocalDate.of(2017, 5, 15), .0);
            planif.put(LocalDate.of(2017, 5, 22), .0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .0);
            planif.put(LocalDate.of(2017, 6, 12), .0);
            planif.put(LocalDate.of(2017, 6, 19), .0);
            planif.put(LocalDate.of(2017, 6, 26), .0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2003, "IDAL0003", "Tâche n°3", projetAppliDao.load("ProjetA"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("ITA"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), .0);
            planif.put(LocalDate.of(2017, 4, 24), .3);
            planif.put(LocalDate.of(2017, 5, 1), .0);
            planif.put(LocalDate.of(2017, 5, 8), .0);
            planif.put(LocalDate.of(2017, 5, 15), .0);
            planif.put(LocalDate.of(2017, 5, 22), .0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .0);
            planif.put(LocalDate.of(2017, 6, 12), .0);
            planif.put(LocalDate.of(2017, 6, 19), .0);
            planif.put(LocalDate.of(2017, 6, 26), .0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2004, "IDAL0004", "Tâche n°4", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("ITA"), profilDao.load("Dev"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), .0);
            planif.put(LocalDate.of(2017, 4, 24), .0);
            planif.put(LocalDate.of(2017, 5, 1), .0);
            planif.put(LocalDate.of(2017, 5, 8), .0);
            planif.put(LocalDate.of(2017, 5, 15), .0);
            planif.put(LocalDate.of(2017, 5, 22), .0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .0);
            planif.put(LocalDate.of(2017, 6, 12), .0);
            planif.put(LocalDate.of(2017, 6, 19), .0);
            planif.put(LocalDate.of(2017, 6, 26), .0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2005, "IDAL0005", "Tâche n°5", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("RVA"), profilDao.load("GC"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), .0);
            planif.put(LocalDate.of(2017, 4, 24), 2.0);
            planif.put(LocalDate.of(2017, 5, 1), 2.0);
            planif.put(LocalDate.of(2017, 5, 8), 2.0);
            planif.put(LocalDate.of(2017, 5, 15), .0);
            planif.put(LocalDate.of(2017, 5, 22), .0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .0);
            planif.put(LocalDate.of(2017, 6, 12), .0);
            planif.put(LocalDate.of(2017, 6, 19), .0);
            planif.put(LocalDate.of(2017, 6, 26), .0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2006, "IDAL0006", "Tâche n°6", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("EGR"), profilDao.load("TestPerf"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), .1);
            planif.put(LocalDate.of(2017, 4, 24), .0);
            planif.put(LocalDate.of(2017, 5, 1), .0);
            planif.put(LocalDate.of(2017, 5, 8), .0);
            planif.put(LocalDate.of(2017, 5, 15), .0);
            planif.put(LocalDate.of(2017, 5, 22), .0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .0);
            planif.put(LocalDate.of(2017, 6, 12), .0);
            planif.put(LocalDate.of(2017, 6, 19), .0);
            planif.put(LocalDate.of(2017, 6, 26), .0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2007, "IDAL0007", "Tâche n°7", projetAppliDao.load("ProjetC"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("FDA"), profilDao.load("Archi"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), .3);
            planif.put(LocalDate.of(2017, 4, 24), .0);
            planif.put(LocalDate.of(2017, 5, 1), .0);
            planif.put(LocalDate.of(2017, 5, 8), .0);
            planif.put(LocalDate.of(2017, 5, 15), .0);
            planif.put(LocalDate.of(2017, 5, 22), .0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .0);
            planif.put(LocalDate.of(2017, 6, 12), .0);
            planif.put(LocalDate.of(2017, 6, 19), .0);
            planif.put(LocalDate.of(2017, 6, 26), .0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2008, "IDAL0008", "Tâche n°8", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("FDA"), profilDao.load("Dev"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), 1.0);
            planif.put(LocalDate.of(2017, 4, 24), .0);
            planif.put(LocalDate.of(2017, 5, 1), .0);
            planif.put(LocalDate.of(2017, 5, 8), .0);
            planif.put(LocalDate.of(2017, 5, 15), .0);
            planif.put(LocalDate.of(2017, 5, 22), .0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .0);
            planif.put(LocalDate.of(2017, 6, 12), .0);
            planif.put(LocalDate.of(2017, 6, 19), .0);
            planif.put(LocalDate.of(2017, 6, 26), .0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2009, "IDAL0009", "Tâche n°9", projetAppliDao.load("ProjetA"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("FDA"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), 2.0);
            planif.put(LocalDate.of(2017, 4, 24), .0);
            planif.put(LocalDate.of(2017, 5, 1), .0);
            planif.put(LocalDate.of(2017, 5, 8), .0);
            planif.put(LocalDate.of(2017, 5, 15), .0);
            planif.put(LocalDate.of(2017, 5, 22), .0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .0);
            planif.put(LocalDate.of(2017, 6, 12), .0);
            planif.put(LocalDate.of(2017, 6, 19), .0);
            planif.put(LocalDate.of(2017, 6, 26), .0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2010, "IDAL00010", "Tâche n°10", projetAppliDao.load("*"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("FDA"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), .0);
            planif.put(LocalDate.of(2017, 4, 24), .0);
            planif.put(LocalDate.of(2017, 5, 1), 1.0);
            planif.put(LocalDate.of(2017, 5, 8), 1.0);
            planif.put(LocalDate.of(2017, 5, 15), 1.0);
            planif.put(LocalDate.of(2017, 5, 22), .0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .0);
            planif.put(LocalDate.of(2017, 6, 12), .0);
            planif.put(LocalDate.of(2017, 6, 19), .0);
            planif.put(LocalDate.of(2017, 6, 26), .0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2011, "IDAL00011", "Tâche n°11", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("FBO"), profilDao.load("Dev"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), .0);
            planif.put(LocalDate.of(2017, 4, 24), .0);
            planif.put(LocalDate.of(2017, 5, 1), .0);
            planif.put(LocalDate.of(2017, 5, 8), .0);
            planif.put(LocalDate.of(2017, 5, 15), 2.0);
            planif.put(LocalDate.of(2017, 5, 22), 2.0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .0);
            planif.put(LocalDate.of(2017, 6, 12), .0);
            planif.put(LocalDate.of(2017, 6, 19), .0);
            planif.put(LocalDate.of(2017, 6, 26), .0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2012, "IDAL00012", "Tâche n°12", projetAppliDao.load("ProjetC"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("FBO"), profilDao.load("Archi"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), .0);
            planif.put(LocalDate.of(2017, 4, 24), .0);
            planif.put(LocalDate.of(2017, 5, 1), .0);
            planif.put(LocalDate.of(2017, 5, 8), .3);
            planif.put(LocalDate.of(2017, 5, 15), .0);
            planif.put(LocalDate.of(2017, 5, 22), .0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .0);
            planif.put(LocalDate.of(2017, 6, 12), .0);
            planif.put(LocalDate.of(2017, 6, 19), .0);
            planif.put(LocalDate.of(2017, 6, 26), .0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2013, "IDAL00013", "Tâche n°13", projetAppliDao.load("ProjetC"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("BPE"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), .0);
            planif.put(LocalDate.of(2017, 4, 24), .0);
            planif.put(LocalDate.of(2017, 5, 1), .0);
            planif.put(LocalDate.of(2017, 5, 8), .0);
            planif.put(LocalDate.of(2017, 5, 15), .0);
            planif.put(LocalDate.of(2017, 5, 22), .0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .0);
            planif.put(LocalDate.of(2017, 6, 12), .0);
            planif.put(LocalDate.of(2017, 6, 19), .0);
            planif.put(LocalDate.of(2017, 6, 26), .0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2014, "IDAL00014", "Tâche n°14", projetAppliDao.load("ProjetC"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("BPE"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), .0);
            planif.put(LocalDate.of(2017, 4, 24), .0);
            planif.put(LocalDate.of(2017, 5, 1), .0);
            planif.put(LocalDate.of(2017, 5, 8), 2.0);
            planif.put(LocalDate.of(2017, 5, 15), 2.0);
            planif.put(LocalDate.of(2017, 5, 22), 2.0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .0);
            planif.put(LocalDate.of(2017, 6, 12), .0);
            planif.put(LocalDate.of(2017, 6, 19), .0);
            planif.put(LocalDate.of(2017, 6, 26), .0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2015, "IDAL00015", "Tâche n°15", projetAppliDao.load("ProjetC"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("ITA"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), .0);
            planif.put(LocalDate.of(2017, 4, 24), .0);
            planif.put(LocalDate.of(2017, 5, 1), .3);
            planif.put(LocalDate.of(2017, 5, 8), .0);
            planif.put(LocalDate.of(2017, 5, 15), .0);
            planif.put(LocalDate.of(2017, 5, 22), .0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .0);
            planif.put(LocalDate.of(2017, 6, 12), .0);
            planif.put(LocalDate.of(2017, 6, 19), .0);
            planif.put(LocalDate.of(2017, 6, 26), .0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2016, "IDAL00016", "Tâche n°16", projetAppliDao.load("*"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("ITA"), profilDao.load("Dev"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), .0);
            planif.put(LocalDate.of(2017, 4, 24), .1);
            planif.put(LocalDate.of(2017, 5, 1), .0);
            planif.put(LocalDate.of(2017, 5, 8), .0);
            planif.put(LocalDate.of(2017, 5, 15), .0);
            planif.put(LocalDate.of(2017, 5, 22), .0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .0);
            planif.put(LocalDate.of(2017, 6, 12), .0);
            planif.put(LocalDate.of(2017, 6, 19), .0);
            planif.put(LocalDate.of(2017, 6, 26), .0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2017, "IDAL00017", "Tâche n°17", projetAppliDao.load("*"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("RVA"), profilDao.load("GC"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), .3);
            planif.put(LocalDate.of(2017, 4, 24), .0);
            planif.put(LocalDate.of(2017, 5, 1), .0);
            planif.put(LocalDate.of(2017, 5, 8), .0);
            planif.put(LocalDate.of(2017, 5, 15), .0);
            planif.put(LocalDate.of(2017, 5, 22), .0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .0);
            planif.put(LocalDate.of(2017, 6, 12), .0);
            planif.put(LocalDate.of(2017, 6, 19), .0);
            planif.put(LocalDate.of(2017, 6, 26), .0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2018, "IDAL00018", "Tâche n°18", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("EGR"), profilDao.load("TestPerf"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), .0);
            planif.put(LocalDate.of(2017, 4, 24), .0);
            planif.put(LocalDate.of(2017, 5, 1), .0);
            planif.put(LocalDate.of(2017, 5, 8), .0);
            planif.put(LocalDate.of(2017, 5, 15), .0);
            planif.put(LocalDate.of(2017, 5, 22), .0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .0);
            planif.put(LocalDate.of(2017, 6, 12), .0);
            planif.put(LocalDate.of(2017, 6, 19), .0);
            planif.put(LocalDate.of(2017, 6, 26), .0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2019, "IDAL00019", "Tâche n°19", projetAppliDao.load("ProjetC"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("FDA"), profilDao.load("Archi"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), .0);
            planif.put(LocalDate.of(2017, 4, 24), .0);
            planif.put(LocalDate.of(2017, 5, 1), .0);
            planif.put(LocalDate.of(2017, 5, 8), .0);
            planif.put(LocalDate.of(2017, 5, 15), .0);
            planif.put(LocalDate.of(2017, 5, 22), .0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .0);
            planif.put(LocalDate.of(2017, 6, 12), 2.0);
            planif.put(LocalDate.of(2017, 6, 19), 2.0);
            planif.put(LocalDate.of(2017, 6, 26), .0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2020, "IDAL00020", "Tâche n°20", projetAppliDao.load("ProjetA"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("FDA"), profilDao.load("Dev"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), .0);
            planif.put(LocalDate.of(2017, 4, 24), .0);
            planif.put(LocalDate.of(2017, 5, 1), .0);
            planif.put(LocalDate.of(2017, 5, 8), .0);
            planif.put(LocalDate.of(2017, 5, 15), .0);
            planif.put(LocalDate.of(2017, 5, 22), .0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .3);
            planif.put(LocalDate.of(2017, 6, 12), .0);
            planif.put(LocalDate.of(2017, 6, 19), .0);
            planif.put(LocalDate.of(2017, 6, 26), .0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2021, "IDAL00021", "Tâche n°21", projetAppliDao.load("ProjetC"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("FDA"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), .0);
            planif.put(LocalDate.of(2017, 4, 24), .0);
            planif.put(LocalDate.of(2017, 5, 1), .0);
            planif.put(LocalDate.of(2017, 5, 8), .0);
            planif.put(LocalDate.of(2017, 5, 15), .0);
            planif.put(LocalDate.of(2017, 5, 22), .0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .0);
            planif.put(LocalDate.of(2017, 6, 12), .0);
            planif.put(LocalDate.of(2017, 6, 19), .0);
            planif.put(LocalDate.of(2017, 6, 26), .0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2022, "IDAL00022", "Tâche n°22", projetAppliDao.load("ProjetA"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("FDA"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), .0);
            planif.put(LocalDate.of(2017, 4, 24), .0);
            planif.put(LocalDate.of(2017, 5, 1), .0);
            planif.put(LocalDate.of(2017, 5, 8), .0);
            planif.put(LocalDate.of(2017, 5, 15), .0);
            planif.put(LocalDate.of(2017, 5, 22), .0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), 2.0);
            planif.put(LocalDate.of(2017, 6, 12), 2.0);
            planif.put(LocalDate.of(2017, 6, 19), 2.0);
            planif.put(LocalDate.of(2017, 6, 26), .0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2023, "IDAL00023", "Tâche n°23", projetAppliDao.load("ProjetC"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("BPE"), profilDao.load("Archi"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), .0);
            planif.put(LocalDate.of(2017, 4, 24), .0);
            planif.put(LocalDate.of(2017, 5, 1), .1);
            planif.put(LocalDate.of(2017, 5, 8), .0);
            planif.put(LocalDate.of(2017, 5, 15), .0);
            planif.put(LocalDate.of(2017, 5, 22), .0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .0);
            planif.put(LocalDate.of(2017, 6, 12), .0);
            planif.put(LocalDate.of(2017, 6, 19), .0);
            planif.put(LocalDate.of(2017, 6, 26), .0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2024, "IDAL00024", "Tâche n°24", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("BPE"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), .0);
            planif.put(LocalDate.of(2017, 4, 24), .3);
            planif.put(LocalDate.of(2017, 5, 1), .0);
            planif.put(LocalDate.of(2017, 5, 8), .0);
            planif.put(LocalDate.of(2017, 5, 15), .0);
            planif.put(LocalDate.of(2017, 5, 22), .0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .0);
            planif.put(LocalDate.of(2017, 6, 12), .0);
            planif.put(LocalDate.of(2017, 6, 19), .0);
            planif.put(LocalDate.of(2017, 6, 26), .0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2025, "IDAL00025", "Tâche n°25", projetAppliDao.load("ProjetC"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("HLE"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), .3);
            planif.put(LocalDate.of(2017, 4, 24), .0);
            planif.put(LocalDate.of(2017, 5, 1), .0);
            planif.put(LocalDate.of(2017, 5, 8), .0);
            planif.put(LocalDate.of(2017, 5, 15), .0);
            planif.put(LocalDate.of(2017, 5, 22), .0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .0);
            planif.put(LocalDate.of(2017, 6, 12), .0);
            planif.put(LocalDate.of(2017, 6, 19), .0);
            planif.put(LocalDate.of(2017, 6, 26), .0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2026, "IDAL00026", "Tâche n°26", projetAppliDao.load("ProjetA"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("FBO"), profilDao.load("Dev"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), .0);
            planif.put(LocalDate.of(2017, 4, 24), .0);
            planif.put(LocalDate.of(2017, 5, 1), .0);
            planif.put(LocalDate.of(2017, 5, 8), .1);
            planif.put(LocalDate.of(2017, 5, 15), .0);
            planif.put(LocalDate.of(2017, 5, 22), .0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .0);
            planif.put(LocalDate.of(2017, 6, 12), .0);
            planif.put(LocalDate.of(2017, 6, 19), .0);
            planif.put(LocalDate.of(2017, 6, 26), .0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2027, "IDAL00027", "Tâche n°27", projetAppliDao.load("ProjetA"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("FBO"), profilDao.load("Archi"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), .0);
            planif.put(LocalDate.of(2017, 4, 24), .0);
            planif.put(LocalDate.of(2017, 5, 1), .0);
            planif.put(LocalDate.of(2017, 5, 8), .0);
            planif.put(LocalDate.of(2017, 5, 15), .0);
            planif.put(LocalDate.of(2017, 5, 22), .0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .0);
            planif.put(LocalDate.of(2017, 6, 12), .0);
            planif.put(LocalDate.of(2017, 6, 19), .0);
            planif.put(LocalDate.of(2017, 6, 26), .0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2028, "IDAL00028", "Tâche n°28", projetAppliDao.load("ProjetA"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("BPE"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), .0);
            planif.put(LocalDate.of(2017, 4, 24), .0);
            planif.put(LocalDate.of(2017, 5, 1), .0);
            planif.put(LocalDate.of(2017, 5, 8), .0);
            planif.put(LocalDate.of(2017, 5, 15), .0);
            planif.put(LocalDate.of(2017, 5, 22), .0);
            planif.put(LocalDate.of(2017, 5, 29), .1);
            planif.put(LocalDate.of(2017, 6, 5), .0);
            planif.put(LocalDate.of(2017, 6, 12), .0);
            planif.put(LocalDate.of(2017, 6, 19), .0);
            planif.put(LocalDate.of(2017, 6, 26), .0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2029, "IDAL00029", "Tâche n°29", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("BPE"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), .0);
            planif.put(LocalDate.of(2017, 4, 24), .1);
            planif.put(LocalDate.of(2017, 5, 1), .0);
            planif.put(LocalDate.of(2017, 5, 8), .0);
            planif.put(LocalDate.of(2017, 5, 15), .0);
            planif.put(LocalDate.of(2017, 5, 22), .0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .0);
            planif.put(LocalDate.of(2017, 6, 12), .0);
            planif.put(LocalDate.of(2017, 6, 19), .0);
            planif.put(LocalDate.of(2017, 6, 26), .0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2030, "IDAL00030", "Tâche n°30", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("ITA"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), .0);
            planif.put(LocalDate.of(2017, 4, 24), .0);
            planif.put(LocalDate.of(2017, 5, 1), .0);
            planif.put(LocalDate.of(2017, 5, 8), .0);
            planif.put(LocalDate.of(2017, 5, 15), .0);
            planif.put(LocalDate.of(2017, 5, 22), .0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .0);
            planif.put(LocalDate.of(2017, 6, 12), .0);
            planif.put(LocalDate.of(2017, 6, 19), .0);
            planif.put(LocalDate.of(2017, 6, 26), .0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2031, "IDAL00031", "Tâche n°31", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("ITA"), profilDao.load("Dev"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), .0);
            planif.put(LocalDate.of(2017, 4, 24), .0);
            planif.put(LocalDate.of(2017, 5, 1), .0);
            planif.put(LocalDate.of(2017, 5, 8), .0);
            planif.put(LocalDate.of(2017, 5, 15), .0);
            planif.put(LocalDate.of(2017, 5, 22), .0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .0);
            planif.put(LocalDate.of(2017, 6, 12), .0);
            planif.put(LocalDate.of(2017, 6, 19), .0);
            planif.put(LocalDate.of(2017, 6, 26), .0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2032, "IDAL00032", "Tâche n°32", projetAppliDao.load("ProjetC"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("RVA"), profilDao.load("GC"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), .0);
            planif.put(LocalDate.of(2017, 4, 24), .0);
            planif.put(LocalDate.of(2017, 5, 1), .0);
            planif.put(LocalDate.of(2017, 5, 8), .0);
            planif.put(LocalDate.of(2017, 5, 15), .0);
            planif.put(LocalDate.of(2017, 5, 22), .0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .0);
            planif.put(LocalDate.of(2017, 6, 12), 1.0);
            planif.put(LocalDate.of(2017, 6, 19), 1.0);
            planif.put(LocalDate.of(2017, 6, 26), 1.0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2033, "IDAL00033", "Tâche n°33", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("EGR"), profilDao.load("TestPerf"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), .0);
            planif.put(LocalDate.of(2017, 4, 24), .0);
            planif.put(LocalDate.of(2017, 5, 1), .0);
            planif.put(LocalDate.of(2017, 5, 8), .0);
            planif.put(LocalDate.of(2017, 5, 15), .0);
            planif.put(LocalDate.of(2017, 5, 22), .0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .0);
            planif.put(LocalDate.of(2017, 6, 12), .0);
            planif.put(LocalDate.of(2017, 6, 19), .0);
            planif.put(LocalDate.of(2017, 6, 26), 2.0);
            planif.put(LocalDate.of(2017, 7, 3), 2.0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2034, "IDAL00034", "Tâche n°34", projetAppliDao.load("ProjetA"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("FDA"), profilDao.load("Archi"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), .0);
            planif.put(LocalDate.of(2017, 4, 24), .0);
            planif.put(LocalDate.of(2017, 5, 1), .0);
            planif.put(LocalDate.of(2017, 5, 8), .0);
            planif.put(LocalDate.of(2017, 5, 15), .0);
            planif.put(LocalDate.of(2017, 5, 22), .0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .0);
            planif.put(LocalDate.of(2017, 6, 12), .0);
            planif.put(LocalDate.of(2017, 6, 19), .3);
            planif.put(LocalDate.of(2017, 6, 26), .0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2035, "IDAL00035", "Tâche n°35", projetAppliDao.load("*"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("FDA"), profilDao.load("Dev"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), .0);
            planif.put(LocalDate.of(2017, 4, 24), .0);
            planif.put(LocalDate.of(2017, 5, 1), .0);
            planif.put(LocalDate.of(2017, 5, 8), .0);
            planif.put(LocalDate.of(2017, 5, 15), .0);
            planif.put(LocalDate.of(2017, 5, 22), .0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .0);
            planif.put(LocalDate.of(2017, 6, 12), .0);
            planif.put(LocalDate.of(2017, 6, 19), .0);
            planif.put(LocalDate.of(2017, 6, 26), .0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2036, "IDAL00036", "Tâche n°36", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("FDA"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), .0);
            planif.put(LocalDate.of(2017, 4, 24), .0);
            planif.put(LocalDate.of(2017, 5, 1), .0);
            planif.put(LocalDate.of(2017, 5, 8), .0);
            planif.put(LocalDate.of(2017, 5, 15), .0);
            planif.put(LocalDate.of(2017, 5, 22), .0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .0);
            planif.put(LocalDate.of(2017, 6, 12), .0);
            planif.put(LocalDate.of(2017, 6, 19), 2.0);
            planif.put(LocalDate.of(2017, 6, 26), 2.0);
            planif.put(LocalDate.of(2017, 7, 3), 2.0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2037, "IDAL00037", "Tâche n°37", projetAppliDao.load("ProjetC"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("FDA"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), 1.0);
            planif.put(LocalDate.of(2017, 4, 24), 1.0);
            planif.put(LocalDate.of(2017, 5, 1), 1.0);
            planif.put(LocalDate.of(2017, 5, 8), .0);
            planif.put(LocalDate.of(2017, 5, 15), .0);
            planif.put(LocalDate.of(2017, 5, 22), .0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .0);
            planif.put(LocalDate.of(2017, 6, 12), .0);
            planif.put(LocalDate.of(2017, 6, 19), .0);
            planif.put(LocalDate.of(2017, 6, 26), .0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2038, "IDAL00038", "Tâche n°38", projetAppliDao.load("ProjetC"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("BPE"), profilDao.load("Archi"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), .0);
            planif.put(LocalDate.of(2017, 4, 24), .0);
            planif.put(LocalDate.of(2017, 5, 1), 2.0);
            planif.put(LocalDate.of(2017, 5, 8), 2.0);
            planif.put(LocalDate.of(2017, 5, 15), .0);
            planif.put(LocalDate.of(2017, 5, 22), .0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .0);
            planif.put(LocalDate.of(2017, 6, 12), .0);
            planif.put(LocalDate.of(2017, 6, 19), .0);
            planif.put(LocalDate.of(2017, 6, 26), .0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2039, "IDAL00039", "Tâche n°39", projetAppliDao.load("ProjetC"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("BPE"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), .0);
            planif.put(LocalDate.of(2017, 4, 24), .3);
            planif.put(LocalDate.of(2017, 5, 1), .0);
            planif.put(LocalDate.of(2017, 5, 8), .0);
            planif.put(LocalDate.of(2017, 5, 15), .0);
            planif.put(LocalDate.of(2017, 5, 22), .0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .0);
            planif.put(LocalDate.of(2017, 6, 12), .0);
            planif.put(LocalDate.of(2017, 6, 19), .0);
            planif.put(LocalDate.of(2017, 6, 26), .0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2040, "IDAL00040", "Tâche n°40", projetAppliDao.load("ProjetC"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("HLE"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), .0);
            planif.put(LocalDate.of(2017, 4, 24), .0);
            planif.put(LocalDate.of(2017, 5, 1), .0);
            planif.put(LocalDate.of(2017, 5, 8), .0);
            planif.put(LocalDate.of(2017, 5, 15), .0);
            planif.put(LocalDate.of(2017, 5, 22), .0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .0);
            planif.put(LocalDate.of(2017, 6, 12), .0);
            planif.put(LocalDate.of(2017, 6, 19), .0);
            planif.put(LocalDate.of(2017, 6, 26), .0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2041, "IDAL00041", "Tâche n°41", projetAppliDao.load("*"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("FBO"), profilDao.load("Dev"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), .0);
            planif.put(LocalDate.of(2017, 4, 24), 2.0);
            planif.put(LocalDate.of(2017, 5, 1), 2.0);
            planif.put(LocalDate.of(2017, 5, 8), 2.0);
            planif.put(LocalDate.of(2017, 5, 15), .0);
            planif.put(LocalDate.of(2017, 5, 22), .0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .0);
            planif.put(LocalDate.of(2017, 6, 12), .0);
            planif.put(LocalDate.of(2017, 6, 19), .0);
            planif.put(LocalDate.of(2017, 6, 26), .0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2042, "IDAL00042", "Tâche n°42", projetAppliDao.load("*"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("FBO"), profilDao.load("Archi"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), .1);
            planif.put(LocalDate.of(2017, 4, 24), .0);
            planif.put(LocalDate.of(2017, 5, 1), .0);
            planif.put(LocalDate.of(2017, 5, 8), .0);
            planif.put(LocalDate.of(2017, 5, 15), .0);
            planif.put(LocalDate.of(2017, 5, 22), .0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .0);
            planif.put(LocalDate.of(2017, 6, 12), .0);
            planif.put(LocalDate.of(2017, 6, 19), .0);
            planif.put(LocalDate.of(2017, 6, 26), .0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2043, "IDAL00043", "Tâche n°43", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("BPE"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), .3);
            planif.put(LocalDate.of(2017, 4, 24), .0);
            planif.put(LocalDate.of(2017, 5, 1), .0);
            planif.put(LocalDate.of(2017, 5, 8), .0);
            planif.put(LocalDate.of(2017, 5, 15), .0);
            planif.put(LocalDate.of(2017, 5, 22), .0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .0);
            planif.put(LocalDate.of(2017, 6, 12), .0);
            planif.put(LocalDate.of(2017, 6, 19), .0);
            planif.put(LocalDate.of(2017, 6, 26), .0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2044, "IDAL00044", "Tâche n°44", projetAppliDao.load("ProjetC"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("BPE"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), 1.0);
            planif.put(LocalDate.of(2017, 4, 24), .0);
            planif.put(LocalDate.of(2017, 5, 1), .0);
            planif.put(LocalDate.of(2017, 5, 8), .0);
            planif.put(LocalDate.of(2017, 5, 15), .0);
            planif.put(LocalDate.of(2017, 5, 22), .0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .0);
            planif.put(LocalDate.of(2017, 6, 12), .0);
            planif.put(LocalDate.of(2017, 6, 19), .0);
            planif.put(LocalDate.of(2017, 6, 26), .0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2045, "IDAL00045", "Tâche n°45", projetAppliDao.load("ProjetA"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("ITA"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), 2.0);
            planif.put(LocalDate.of(2017, 4, 24), .0);
            planif.put(LocalDate.of(2017, 5, 1), .0);
            planif.put(LocalDate.of(2017, 5, 8), .0);
            planif.put(LocalDate.of(2017, 5, 15), .0);
            planif.put(LocalDate.of(2017, 5, 22), .0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .0);
            planif.put(LocalDate.of(2017, 6, 12), .0);
            planif.put(LocalDate.of(2017, 6, 19), .0);
            planif.put(LocalDate.of(2017, 6, 26), .0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2046, "IDAL00046", "Tâche n°46", projetAppliDao.load("ProjetC"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("ITA"), profilDao.load("Dev"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), .0);
            planif.put(LocalDate.of(2017, 4, 24), .0);
            planif.put(LocalDate.of(2017, 5, 1), 1.0);
            planif.put(LocalDate.of(2017, 5, 8), 1.0);
            planif.put(LocalDate.of(2017, 5, 15), 1.0);
            planif.put(LocalDate.of(2017, 5, 22), .0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .0);
            planif.put(LocalDate.of(2017, 6, 12), .0);
            planif.put(LocalDate.of(2017, 6, 19), .0);
            planif.put(LocalDate.of(2017, 6, 26), .0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2047, "IDAL00047", "Tâche n°47", projetAppliDao.load("ProjetA"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("RVA"), profilDao.load("GC"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), .0);
            planif.put(LocalDate.of(2017, 4, 24), .0);
            planif.put(LocalDate.of(2017, 5, 1), .0);
            planif.put(LocalDate.of(2017, 5, 8), .0);
            planif.put(LocalDate.of(2017, 5, 15), 2.0);
            planif.put(LocalDate.of(2017, 5, 22), 2.0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .0);
            planif.put(LocalDate.of(2017, 6, 12), .0);
            planif.put(LocalDate.of(2017, 6, 19), .0);
            planif.put(LocalDate.of(2017, 6, 26), .0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2048, "IDAL00048", "Tâche n°48", projetAppliDao.load("ProjetC"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("EGR"), profilDao.load("TestPerf"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), .0);
            planif.put(LocalDate.of(2017, 4, 24), .0);
            planif.put(LocalDate.of(2017, 5, 1), .0);
            planif.put(LocalDate.of(2017, 5, 8), .3);
            planif.put(LocalDate.of(2017, 5, 15), .0);
            planif.put(LocalDate.of(2017, 5, 22), .0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .0);
            planif.put(LocalDate.of(2017, 6, 12), .0);
            planif.put(LocalDate.of(2017, 6, 19), .0);
            planif.put(LocalDate.of(2017, 6, 26), .0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2049, "IDAL00049", "Tâche n°49", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("FDA"), profilDao.load("Archi"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), .0);
            planif.put(LocalDate.of(2017, 4, 24), .0);
            planif.put(LocalDate.of(2017, 5, 1), .0);
            planif.put(LocalDate.of(2017, 5, 8), .0);
            planif.put(LocalDate.of(2017, 5, 15), .0);
            planif.put(LocalDate.of(2017, 5, 22), .0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .0);
            planif.put(LocalDate.of(2017, 6, 12), .0);
            planif.put(LocalDate.of(2017, 6, 19), .0);
            planif.put(LocalDate.of(2017, 6, 26), .0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2050, "IDAL00050", "Tâche n°50", projetAppliDao.load("ProjetC"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("FDA"), profilDao.load("Dev"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), .0);
            planif.put(LocalDate.of(2017, 4, 24), .0);
            planif.put(LocalDate.of(2017, 5, 1), .0);
            planif.put(LocalDate.of(2017, 5, 8), 2.0);
            planif.put(LocalDate.of(2017, 5, 15), 2.0);
            planif.put(LocalDate.of(2017, 5, 22), 2.0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .0);
            planif.put(LocalDate.of(2017, 6, 12), .0);
            planif.put(LocalDate.of(2017, 6, 19), .0);
            planif.put(LocalDate.of(2017, 6, 26), .0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2051, "IDAL00051", "Tâche n°51", projetAppliDao.load("ProjetA"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("FDA"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), .0);
            planif.put(LocalDate.of(2017, 4, 24), .0);
            planif.put(LocalDate.of(2017, 5, 1), .3);
            planif.put(LocalDate.of(2017, 5, 8), .0);
            planif.put(LocalDate.of(2017, 5, 15), .0);
            planif.put(LocalDate.of(2017, 5, 22), .0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .0);
            planif.put(LocalDate.of(2017, 6, 12), .0);
            planif.put(LocalDate.of(2017, 6, 19), .0);
            planif.put(LocalDate.of(2017, 6, 26), .0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2052, "IDAL00052", "Tâche n°52", projetAppliDao.load("ProjetA"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("FDA"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), .0);
            planif.put(LocalDate.of(2017, 4, 24), .1);
            planif.put(LocalDate.of(2017, 5, 1), .0);
            planif.put(LocalDate.of(2017, 5, 8), .0);
            planif.put(LocalDate.of(2017, 5, 15), .0);
            planif.put(LocalDate.of(2017, 5, 22), .0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .0);
            planif.put(LocalDate.of(2017, 6, 12), .0);
            planif.put(LocalDate.of(2017, 6, 19), .0);
            planif.put(LocalDate.of(2017, 6, 26), .0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2053, "IDAL00053", "Tâche n°53", projetAppliDao.load("ProjetA"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("BPE"), profilDao.load("Archi"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), .3);
            planif.put(LocalDate.of(2017, 4, 24), .0);
            planif.put(LocalDate.of(2017, 5, 1), .0);
            planif.put(LocalDate.of(2017, 5, 8), .0);
            planif.put(LocalDate.of(2017, 5, 15), .0);
            planif.put(LocalDate.of(2017, 5, 22), .0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .0);
            planif.put(LocalDate.of(2017, 6, 12), .0);
            planif.put(LocalDate.of(2017, 6, 19), .0);
            planif.put(LocalDate.of(2017, 6, 26), .0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2054, "IDAL00054", "Tâche n°54", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("BPE"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), .0);
            planif.put(LocalDate.of(2017, 4, 24), .0);
            planif.put(LocalDate.of(2017, 5, 1), .0);
            planif.put(LocalDate.of(2017, 5, 8), .0);
            planif.put(LocalDate.of(2017, 5, 15), .0);
            planif.put(LocalDate.of(2017, 5, 22), .0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .0);
            planif.put(LocalDate.of(2017, 6, 12), .0);
            planif.put(LocalDate.of(2017, 6, 19), .0);
            planif.put(LocalDate.of(2017, 6, 26), .0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2055, "IDAL00055", "Tâche n°55", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("HLE"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), .0);
            planif.put(LocalDate.of(2017, 4, 24), .0);
            planif.put(LocalDate.of(2017, 5, 1), .0);
            planif.put(LocalDate.of(2017, 5, 8), .0);
            planif.put(LocalDate.of(2017, 5, 15), .0);
            planif.put(LocalDate.of(2017, 5, 22), .0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .0);
            planif.put(LocalDate.of(2017, 6, 12), 2.0);
            planif.put(LocalDate.of(2017, 6, 19), 2.0);
            planif.put(LocalDate.of(2017, 6, 26), .0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2056, "IDAL00056", "Tâche n°56", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("FBO"), profilDao.load("Dev"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), .0);
            planif.put(LocalDate.of(2017, 4, 24), .0);
            planif.put(LocalDate.of(2017, 5, 1), .0);
            planif.put(LocalDate.of(2017, 5, 8), .0);
            planif.put(LocalDate.of(2017, 5, 15), .0);
            planif.put(LocalDate.of(2017, 5, 22), .0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .3);
            planif.put(LocalDate.of(2017, 6, 12), .0);
            planif.put(LocalDate.of(2017, 6, 19), .0);
            planif.put(LocalDate.of(2017, 6, 26), .0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2057, "IDAL00057", "Tâche n°57", projetAppliDao.load("ProjetC"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("FBO"), profilDao.load("Archi"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), .0);
            planif.put(LocalDate.of(2017, 4, 24), .0);
            planif.put(LocalDate.of(2017, 5, 1), .0);
            planif.put(LocalDate.of(2017, 5, 8), .0);
            planif.put(LocalDate.of(2017, 5, 15), .0);
            planif.put(LocalDate.of(2017, 5, 22), .0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .0);
            planif.put(LocalDate.of(2017, 6, 12), .0);
            planif.put(LocalDate.of(2017, 6, 19), .0);
            planif.put(LocalDate.of(2017, 6, 26), .0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2058, "IDAL00058", "Tâche n°58", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("BPE"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), .0);
            planif.put(LocalDate.of(2017, 4, 24), .0);
            planif.put(LocalDate.of(2017, 5, 1), .0);
            planif.put(LocalDate.of(2017, 5, 8), .0);
            planif.put(LocalDate.of(2017, 5, 15), .0);
            planif.put(LocalDate.of(2017, 5, 22), .0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), 2.0);
            planif.put(LocalDate.of(2017, 6, 12), 2.0);
            planif.put(LocalDate.of(2017, 6, 19), 2.0);
            planif.put(LocalDate.of(2017, 6, 26), .0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2059, "IDAL00059", "Tâche n°59", projetAppliDao.load("ProjetA"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("BPE"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), .0);
            planif.put(LocalDate.of(2017, 4, 24), .0);
            planif.put(LocalDate.of(2017, 5, 1), .1);
            planif.put(LocalDate.of(2017, 5, 8), .0);
            planif.put(LocalDate.of(2017, 5, 15), .0);
            planif.put(LocalDate.of(2017, 5, 22), .0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .0);
            planif.put(LocalDate.of(2017, 6, 12), .0);
            planif.put(LocalDate.of(2017, 6, 19), .0);
            planif.put(LocalDate.of(2017, 6, 26), .0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2060, "IDAL00060", "Tâche n°60", projetAppliDao.load("*"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("ITA"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), .0);
            planif.put(LocalDate.of(2017, 4, 24), .3);
            planif.put(LocalDate.of(2017, 5, 1), .0);
            planif.put(LocalDate.of(2017, 5, 8), .0);
            planif.put(LocalDate.of(2017, 5, 15), .0);
            planif.put(LocalDate.of(2017, 5, 22), .0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .0);
            planif.put(LocalDate.of(2017, 6, 12), .0);
            planif.put(LocalDate.of(2017, 6, 19), .0);
            planif.put(LocalDate.of(2017, 6, 26), .0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2061, "IDAL00061", "Tâche n°61", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("ITA"), profilDao.load("Dev"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), .3);
            planif.put(LocalDate.of(2017, 4, 24), .0);
            planif.put(LocalDate.of(2017, 5, 1), .0);
            planif.put(LocalDate.of(2017, 5, 8), .0);
            planif.put(LocalDate.of(2017, 5, 15), .0);
            planif.put(LocalDate.of(2017, 5, 22), .0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .0);
            planif.put(LocalDate.of(2017, 6, 12), .0);
            planif.put(LocalDate.of(2017, 6, 19), .0);
            planif.put(LocalDate.of(2017, 6, 26), .0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2062, "IDAL00062", "Tâche n°62", projetAppliDao.load("ProjetC"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("RVA"), profilDao.load("GC"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), .0);
            planif.put(LocalDate.of(2017, 4, 24), .0);
            planif.put(LocalDate.of(2017, 5, 1), .0);
            planif.put(LocalDate.of(2017, 5, 8), .1);
            planif.put(LocalDate.of(2017, 5, 15), .0);
            planif.put(LocalDate.of(2017, 5, 22), .0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .0);
            planif.put(LocalDate.of(2017, 6, 12), .0);
            planif.put(LocalDate.of(2017, 6, 19), .0);
            planif.put(LocalDate.of(2017, 6, 26), .0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2063, "IDAL00063", "Tâche n°63", projetAppliDao.load("ProjetC"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("EGR"), profilDao.load("TestPerf"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), .0);
            planif.put(LocalDate.of(2017, 4, 24), .0);
            planif.put(LocalDate.of(2017, 5, 1), .0);
            planif.put(LocalDate.of(2017, 5, 8), .0);
            planif.put(LocalDate.of(2017, 5, 15), .0);
            planif.put(LocalDate.of(2017, 5, 22), .0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .0);
            planif.put(LocalDate.of(2017, 6, 12), .0);
            planif.put(LocalDate.of(2017, 6, 19), .0);
            planif.put(LocalDate.of(2017, 6, 26), .0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2064, "IDAL00064", "Tâche n°64", projetAppliDao.load("ProjetC"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("FDA"), profilDao.load("Archi"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), .0);
            planif.put(LocalDate.of(2017, 4, 24), .0);
            planif.put(LocalDate.of(2017, 5, 1), .0);
            planif.put(LocalDate.of(2017, 5, 8), .0);
            planif.put(LocalDate.of(2017, 5, 15), .0);
            planif.put(LocalDate.of(2017, 5, 22), .0);
            planif.put(LocalDate.of(2017, 5, 29), .1);
            planif.put(LocalDate.of(2017, 6, 5), .0);
            planif.put(LocalDate.of(2017, 6, 12), .0);
            planif.put(LocalDate.of(2017, 6, 19), .0);
            planif.put(LocalDate.of(2017, 6, 26), .0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2065, "IDAL00065", "Tâche n°65", projetAppliDao.load("ProjetC"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("FDA"), profilDao.load("Dev"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), .0);
            planif.put(LocalDate.of(2017, 4, 24), .1);
            planif.put(LocalDate.of(2017, 5, 1), .0);
            planif.put(LocalDate.of(2017, 5, 8), .0);
            planif.put(LocalDate.of(2017, 5, 15), .0);
            planif.put(LocalDate.of(2017, 5, 22), .0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .0);
            planif.put(LocalDate.of(2017, 6, 12), .0);
            planif.put(LocalDate.of(2017, 6, 19), .0);
            planif.put(LocalDate.of(2017, 6, 26), .0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2066, "IDAL00066", "Tâche n°66", projetAppliDao.load("*"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("FDA"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), .0);
            planif.put(LocalDate.of(2017, 4, 24), .0);
            planif.put(LocalDate.of(2017, 5, 1), .0);
            planif.put(LocalDate.of(2017, 5, 8), .0);
            planif.put(LocalDate.of(2017, 5, 15), .0);
            planif.put(LocalDate.of(2017, 5, 22), .0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .0);
            planif.put(LocalDate.of(2017, 6, 12), .0);
            planif.put(LocalDate.of(2017, 6, 19), .0);
            planif.put(LocalDate.of(2017, 6, 26), .0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2067, "IDAL00067", "Tâche n°67", projetAppliDao.load("*"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("FDA"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), .0);
            planif.put(LocalDate.of(2017, 4, 24), .0);
            planif.put(LocalDate.of(2017, 5, 1), .0);
            planif.put(LocalDate.of(2017, 5, 8), .0);
            planif.put(LocalDate.of(2017, 5, 15), .0);
            planif.put(LocalDate.of(2017, 5, 22), .0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .0);
            planif.put(LocalDate.of(2017, 6, 12), .0);
            planif.put(LocalDate.of(2017, 6, 19), .0);
            planif.put(LocalDate.of(2017, 6, 26), .0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2068, "IDAL00068", "Tâche n°68", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("BPE"), profilDao.load("Archi"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), .0);
            planif.put(LocalDate.of(2017, 4, 24), .0);
            planif.put(LocalDate.of(2017, 5, 1), .0);
            planif.put(LocalDate.of(2017, 5, 8), .0);
            planif.put(LocalDate.of(2017, 5, 15), .0);
            planif.put(LocalDate.of(2017, 5, 22), .0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .0);
            planif.put(LocalDate.of(2017, 6, 12), 1.0);
            planif.put(LocalDate.of(2017, 6, 19), 1.0);
            planif.put(LocalDate.of(2017, 6, 26), 1.0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2069, "IDAL00069", "Tâche n°69", projetAppliDao.load("ProjetC"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("BPE"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), .0);
            planif.put(LocalDate.of(2017, 4, 24), .0);
            planif.put(LocalDate.of(2017, 5, 1), .0);
            planif.put(LocalDate.of(2017, 5, 8), .0);
            planif.put(LocalDate.of(2017, 5, 15), .0);
            planif.put(LocalDate.of(2017, 5, 22), .0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .0);
            planif.put(LocalDate.of(2017, 6, 12), .0);
            planif.put(LocalDate.of(2017, 6, 19), .0);
            planif.put(LocalDate.of(2017, 6, 26), 2.0);
            planif.put(LocalDate.of(2017, 7, 3), 2.0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2070, "IDAL00070", "Tâche n°70", projetAppliDao.load("ProjetA"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("HLE"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), .0);
            planif.put(LocalDate.of(2017, 4, 24), .0);
            planif.put(LocalDate.of(2017, 5, 1), .0);
            planif.put(LocalDate.of(2017, 5, 8), .0);
            planif.put(LocalDate.of(2017, 5, 15), .0);
            planif.put(LocalDate.of(2017, 5, 22), .0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .0);
            planif.put(LocalDate.of(2017, 6, 12), .0);
            planif.put(LocalDate.of(2017, 6, 19), .3);
            planif.put(LocalDate.of(2017, 6, 26), .0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2071, "IDAL00071", "Tâche n°71", projetAppliDao.load("ProjetC"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("FBO"), profilDao.load("Dev"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), .0);
            planif.put(LocalDate.of(2017, 4, 24), .0);
            planif.put(LocalDate.of(2017, 5, 1), .0);
            planif.put(LocalDate.of(2017, 5, 8), .0);
            planif.put(LocalDate.of(2017, 5, 15), .0);
            planif.put(LocalDate.of(2017, 5, 22), .0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .0);
            planif.put(LocalDate.of(2017, 6, 12), .0);
            planif.put(LocalDate.of(2017, 6, 19), .0);
            planif.put(LocalDate.of(2017, 6, 26), .0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2072, "IDAL00072", "Tâche n°72", projetAppliDao.load("ProjetA"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("FBO"), profilDao.load("Archi"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), .0);
            planif.put(LocalDate.of(2017, 4, 24), .0);
            planif.put(LocalDate.of(2017, 5, 1), .0);
            planif.put(LocalDate.of(2017, 5, 8), .0);
            planif.put(LocalDate.of(2017, 5, 15), .0);
            planif.put(LocalDate.of(2017, 5, 22), .0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .0);
            planif.put(LocalDate.of(2017, 6, 12), .0);
            planif.put(LocalDate.of(2017, 6, 19), 2.0);
            planif.put(LocalDate.of(2017, 6, 26), 2.0);
            planif.put(LocalDate.of(2017, 7, 3), 2.0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2073, "IDAL00073", "Tâche n°73", projetAppliDao.load("ProjetC"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("BPE"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), 1.0);
            planif.put(LocalDate.of(2017, 4, 24), 1.0);
            planif.put(LocalDate.of(2017, 5, 1), 1.0);
            planif.put(LocalDate.of(2017, 5, 8), .0);
            planif.put(LocalDate.of(2017, 5, 15), .0);
            planif.put(LocalDate.of(2017, 5, 22), .0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .0);
            planif.put(LocalDate.of(2017, 6, 12), .0);
            planif.put(LocalDate.of(2017, 6, 19), .0);
            planif.put(LocalDate.of(2017, 6, 26), .0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2074, "IDAL00074", "Tâche n°74", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("BPE"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), .0);
            planif.put(LocalDate.of(2017, 4, 24), .0);
            planif.put(LocalDate.of(2017, 5, 1), 2.0);
            planif.put(LocalDate.of(2017, 5, 8), 2.0);
            planif.put(LocalDate.of(2017, 5, 15), .0);
            planif.put(LocalDate.of(2017, 5, 22), .0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .0);
            planif.put(LocalDate.of(2017, 6, 12), .0);
            planif.put(LocalDate.of(2017, 6, 19), .0);
            planif.put(LocalDate.of(2017, 6, 26), .0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2075, "IDAL00075", "Tâche n°75", projetAppliDao.load("ProjetC"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("ITA"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), .0);
            planif.put(LocalDate.of(2017, 4, 24), .3);
            planif.put(LocalDate.of(2017, 5, 1), .0);
            planif.put(LocalDate.of(2017, 5, 8), .0);
            planif.put(LocalDate.of(2017, 5, 15), .0);
            planif.put(LocalDate.of(2017, 5, 22), .0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .0);
            planif.put(LocalDate.of(2017, 6, 12), .0);
            planif.put(LocalDate.of(2017, 6, 19), .0);
            planif.put(LocalDate.of(2017, 6, 26), .0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2076, "IDAL00076", "Tâche n°76", projetAppliDao.load("ProjetA"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("ITA"), profilDao.load("Dev"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), .0);
            planif.put(LocalDate.of(2017, 4, 24), .0);
            planif.put(LocalDate.of(2017, 5, 1), .0);
            planif.put(LocalDate.of(2017, 5, 8), .0);
            planif.put(LocalDate.of(2017, 5, 15), .0);
            planif.put(LocalDate.of(2017, 5, 22), .0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .0);
            planif.put(LocalDate.of(2017, 6, 12), .0);
            planif.put(LocalDate.of(2017, 6, 19), .0);
            planif.put(LocalDate.of(2017, 6, 26), .0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2077, "IDAL00077", "Tâche n°77", projetAppliDao.load("ProjetA"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("RVA"), profilDao.load("GC"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), .0);
            planif.put(LocalDate.of(2017, 4, 24), 2.0);
            planif.put(LocalDate.of(2017, 5, 1), 2.0);
            planif.put(LocalDate.of(2017, 5, 8), 2.0);
            planif.put(LocalDate.of(2017, 5, 15), .0);
            planif.put(LocalDate.of(2017, 5, 22), .0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .0);
            planif.put(LocalDate.of(2017, 6, 12), .0);
            planif.put(LocalDate.of(2017, 6, 19), .0);
            planif.put(LocalDate.of(2017, 6, 26), .0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2078, "IDAL00078", "Tâche n°78", projetAppliDao.load("ProjetA"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("EGR"), profilDao.load("TestPerf"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), .1);
            planif.put(LocalDate.of(2017, 4, 24), .0);
            planif.put(LocalDate.of(2017, 5, 1), .0);
            planif.put(LocalDate.of(2017, 5, 8), .0);
            planif.put(LocalDate.of(2017, 5, 15), .0);
            planif.put(LocalDate.of(2017, 5, 22), .0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .0);
            planif.put(LocalDate.of(2017, 6, 12), .0);
            planif.put(LocalDate.of(2017, 6, 19), .0);
            planif.put(LocalDate.of(2017, 6, 26), .0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2079, "IDAL00079", "Tâche n°79", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("FDA"), profilDao.load("Archi"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), .3);
            planif.put(LocalDate.of(2017, 4, 24), .0);
            planif.put(LocalDate.of(2017, 5, 1), .0);
            planif.put(LocalDate.of(2017, 5, 8), .0);
            planif.put(LocalDate.of(2017, 5, 15), .0);
            planif.put(LocalDate.of(2017, 5, 22), .0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .0);
            planif.put(LocalDate.of(2017, 6, 12), .0);
            planif.put(LocalDate.of(2017, 6, 19), .0);
            planif.put(LocalDate.of(2017, 6, 26), .0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2080, "IDAL00080", "Tâche n°80", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("FDA"), profilDao.load("Dev"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), 1.0);
            planif.put(LocalDate.of(2017, 4, 24), .0);
            planif.put(LocalDate.of(2017, 5, 1), .0);
            planif.put(LocalDate.of(2017, 5, 8), .0);
            planif.put(LocalDate.of(2017, 5, 15), .0);
            planif.put(LocalDate.of(2017, 5, 22), .0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .0);
            planif.put(LocalDate.of(2017, 6, 12), .0);
            planif.put(LocalDate.of(2017, 6, 19), .0);
            planif.put(LocalDate.of(2017, 6, 26), .0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2081, "IDAL00081", "Tâche n°81", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("FDA"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), 2.0);
            planif.put(LocalDate.of(2017, 4, 24), .0);
            planif.put(LocalDate.of(2017, 5, 1), .0);
            planif.put(LocalDate.of(2017, 5, 8), .0);
            planif.put(LocalDate.of(2017, 5, 15), .0);
            planif.put(LocalDate.of(2017, 5, 22), .0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .0);
            planif.put(LocalDate.of(2017, 6, 12), .0);
            planif.put(LocalDate.of(2017, 6, 19), .0);
            planif.put(LocalDate.of(2017, 6, 26), .0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2082, "IDAL00082", "Tâche n°82", projetAppliDao.load("ProjetC"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("FDA"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), .0);
            planif.put(LocalDate.of(2017, 4, 24), .0);
            planif.put(LocalDate.of(2017, 5, 1), 1.0);
            planif.put(LocalDate.of(2017, 5, 8), 1.0);
            planif.put(LocalDate.of(2017, 5, 15), 1.0);
            planif.put(LocalDate.of(2017, 5, 22), .0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .0);
            planif.put(LocalDate.of(2017, 6, 12), .0);
            planif.put(LocalDate.of(2017, 6, 19), .0);
            planif.put(LocalDate.of(2017, 6, 26), .0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2083, "IDAL00083", "Tâche n°83", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("BPE"), profilDao.load("Archi"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), .0);
            planif.put(LocalDate.of(2017, 4, 24), .0);
            planif.put(LocalDate.of(2017, 5, 1), .0);
            planif.put(LocalDate.of(2017, 5, 8), .0);
            planif.put(LocalDate.of(2017, 5, 15), 2.0);
            planif.put(LocalDate.of(2017, 5, 22), 2.0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .0);
            planif.put(LocalDate.of(2017, 6, 12), .0);
            planif.put(LocalDate.of(2017, 6, 19), .0);
            planif.put(LocalDate.of(2017, 6, 26), .0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2084, "IDAL00084", "Tâche n°84", projetAppliDao.load("ProjetA"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("BPE"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), .0);
            planif.put(LocalDate.of(2017, 4, 24), .0);
            planif.put(LocalDate.of(2017, 5, 1), .0);
            planif.put(LocalDate.of(2017, 5, 8), .3);
            planif.put(LocalDate.of(2017, 5, 15), .0);
            planif.put(LocalDate.of(2017, 5, 22), .0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .0);
            planif.put(LocalDate.of(2017, 6, 12), .0);
            planif.put(LocalDate.of(2017, 6, 19), .0);
            planif.put(LocalDate.of(2017, 6, 26), .0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2085, "IDAL00085", "Tâche n°85", projetAppliDao.load("*"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("HLE"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), .0);
            planif.put(LocalDate.of(2017, 4, 24), .0);
            planif.put(LocalDate.of(2017, 5, 1), .0);
            planif.put(LocalDate.of(2017, 5, 8), .0);
            planif.put(LocalDate.of(2017, 5, 15), .0);
            planif.put(LocalDate.of(2017, 5, 22), .0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .0);
            planif.put(LocalDate.of(2017, 6, 12), .0);
            planif.put(LocalDate.of(2017, 6, 19), .0);
            planif.put(LocalDate.of(2017, 6, 26), .0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2086, "IDAL00086", "Tâche n°86", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("FBO"), profilDao.load("Dev"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), .0);
            planif.put(LocalDate.of(2017, 4, 24), .0);
            planif.put(LocalDate.of(2017, 5, 1), .0);
            planif.put(LocalDate.of(2017, 5, 8), 2.0);
            planif.put(LocalDate.of(2017, 5, 15), 2.0);
            planif.put(LocalDate.of(2017, 5, 22), 2.0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .0);
            planif.put(LocalDate.of(2017, 6, 12), .0);
            planif.put(LocalDate.of(2017, 6, 19), .0);
            planif.put(LocalDate.of(2017, 6, 26), .0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2087, "IDAL00087", "Tâche n°87", projetAppliDao.load("ProjetC"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("FBO"), profilDao.load("Archi"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), .0);
            planif.put(LocalDate.of(2017, 4, 24), .0);
            planif.put(LocalDate.of(2017, 5, 1), .3);
            planif.put(LocalDate.of(2017, 5, 8), .0);
            planif.put(LocalDate.of(2017, 5, 15), .0);
            planif.put(LocalDate.of(2017, 5, 22), .0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .0);
            planif.put(LocalDate.of(2017, 6, 12), .0);
            planif.put(LocalDate.of(2017, 6, 19), .0);
            planif.put(LocalDate.of(2017, 6, 26), .0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2088, "IDAL00088", "Tâche n°88", projetAppliDao.load("ProjetC"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("BPE"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), .0);
            planif.put(LocalDate.of(2017, 4, 24), .1);
            planif.put(LocalDate.of(2017, 5, 1), .0);
            planif.put(LocalDate.of(2017, 5, 8), .0);
            planif.put(LocalDate.of(2017, 5, 15), .0);
            planif.put(LocalDate.of(2017, 5, 22), .0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .0);
            planif.put(LocalDate.of(2017, 6, 12), .0);
            planif.put(LocalDate.of(2017, 6, 19), .0);
            planif.put(LocalDate.of(2017, 6, 26), .0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2089, "IDAL00089", "Tâche n°89", projetAppliDao.load("ProjetC"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("BPE"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), .3);
            planif.put(LocalDate.of(2017, 4, 24), .0);
            planif.put(LocalDate.of(2017, 5, 1), .0);
            planif.put(LocalDate.of(2017, 5, 8), .0);
            planif.put(LocalDate.of(2017, 5, 15), .0);
            planif.put(LocalDate.of(2017, 5, 22), .0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .0);
            planif.put(LocalDate.of(2017, 6, 12), .0);
            planif.put(LocalDate.of(2017, 6, 19), .0);
            planif.put(LocalDate.of(2017, 6, 26), .0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2090, "IDAL00090", "Tâche n°90", projetAppliDao.load("ProjetC"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("ITA"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), .0);
            planif.put(LocalDate.of(2017, 4, 24), .0);
            planif.put(LocalDate.of(2017, 5, 1), .0);
            planif.put(LocalDate.of(2017, 5, 8), .0);
            planif.put(LocalDate.of(2017, 5, 15), .0);
            planif.put(LocalDate.of(2017, 5, 22), .0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .0);
            planif.put(LocalDate.of(2017, 6, 12), .0);
            planif.put(LocalDate.of(2017, 6, 19), .0);
            planif.put(LocalDate.of(2017, 6, 26), .0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2091, "IDAL00091", "Tâche n°91", projetAppliDao.load("*"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("ITA"), profilDao.load("Dev"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), .0);
            planif.put(LocalDate.of(2017, 4, 24), .0);
            planif.put(LocalDate.of(2017, 5, 1), .0);
            planif.put(LocalDate.of(2017, 5, 8), .0);
            planif.put(LocalDate.of(2017, 5, 15), .0);
            planif.put(LocalDate.of(2017, 5, 22), .0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .0);
            planif.put(LocalDate.of(2017, 6, 12), 2.0);
            planif.put(LocalDate.of(2017, 6, 19), 2.0);
            planif.put(LocalDate.of(2017, 6, 26), .0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2092, "IDAL00092", "Tâche n°92", projetAppliDao.load("*"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("RVA"), profilDao.load("GC"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), .0);
            planif.put(LocalDate.of(2017, 4, 24), .0);
            planif.put(LocalDate.of(2017, 5, 1), .0);
            planif.put(LocalDate.of(2017, 5, 8), .0);
            planif.put(LocalDate.of(2017, 5, 15), .0);
            planif.put(LocalDate.of(2017, 5, 22), .0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .3);
            planif.put(LocalDate.of(2017, 6, 12), .0);
            planif.put(LocalDate.of(2017, 6, 19), .0);
            planif.put(LocalDate.of(2017, 6, 26), .0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2093, "IDAL00093", "Tâche n°93", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("EGR"), profilDao.load("TestPerf"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), .0);
            planif.put(LocalDate.of(2017, 4, 24), .0);
            planif.put(LocalDate.of(2017, 5, 1), .0);
            planif.put(LocalDate.of(2017, 5, 8), .0);
            planif.put(LocalDate.of(2017, 5, 15), .0);
            planif.put(LocalDate.of(2017, 5, 22), .0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .0);
            planif.put(LocalDate.of(2017, 6, 12), .0);
            planif.put(LocalDate.of(2017, 6, 19), .0);
            planif.put(LocalDate.of(2017, 6, 26), .0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2094, "IDAL00094", "Tâche n°94", projetAppliDao.load("ProjetC"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("FDA"), profilDao.load("Archi"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), .0);
            planif.put(LocalDate.of(2017, 4, 24), .0);
            planif.put(LocalDate.of(2017, 5, 1), .0);
            planif.put(LocalDate.of(2017, 5, 8), .0);
            planif.put(LocalDate.of(2017, 5, 15), .0);
            planif.put(LocalDate.of(2017, 5, 22), .0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), 2.0);
            planif.put(LocalDate.of(2017, 6, 12), 2.0);
            planif.put(LocalDate.of(2017, 6, 19), 2.0);
            planif.put(LocalDate.of(2017, 6, 26), .0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2095, "IDAL00095", "Tâche n°95", projetAppliDao.load("ProjetA"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("FDA"), profilDao.load("Dev"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), .0);
            planif.put(LocalDate.of(2017, 4, 24), .0);
            planif.put(LocalDate.of(2017, 5, 1), .1);
            planif.put(LocalDate.of(2017, 5, 8), .0);
            planif.put(LocalDate.of(2017, 5, 15), .0);
            planif.put(LocalDate.of(2017, 5, 22), .0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .0);
            planif.put(LocalDate.of(2017, 6, 12), .0);
            planif.put(LocalDate.of(2017, 6, 19), .0);
            planif.put(LocalDate.of(2017, 6, 26), .0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2096, "IDAL00096", "Tâche n°96", projetAppliDao.load("ProjetC"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("FDA"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), .0);
            planif.put(LocalDate.of(2017, 4, 24), .3);
            planif.put(LocalDate.of(2017, 5, 1), .0);
            planif.put(LocalDate.of(2017, 5, 8), .0);
            planif.put(LocalDate.of(2017, 5, 15), .0);
            planif.put(LocalDate.of(2017, 5, 22), .0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .0);
            planif.put(LocalDate.of(2017, 6, 12), .0);
            planif.put(LocalDate.of(2017, 6, 19), .0);
            planif.put(LocalDate.of(2017, 6, 26), .0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2097, "IDAL00097", "Tâche n°97", projetAppliDao.load("ProjetA"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("FDA"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), .3);
            planif.put(LocalDate.of(2017, 4, 24), .0);
            planif.put(LocalDate.of(2017, 5, 1), .0);
            planif.put(LocalDate.of(2017, 5, 8), .0);
            planif.put(LocalDate.of(2017, 5, 15), .0);
            planif.put(LocalDate.of(2017, 5, 22), .0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .0);
            planif.put(LocalDate.of(2017, 6, 12), .0);
            planif.put(LocalDate.of(2017, 6, 19), .0);
            planif.put(LocalDate.of(2017, 6, 26), .0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2098, "IDAL00098", "Tâche n°98", projetAppliDao.load("ProjetC"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("BPE"), profilDao.load("Archi"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), .0);
            planif.put(LocalDate.of(2017, 4, 24), .0);
            planif.put(LocalDate.of(2017, 5, 1), .0);
            planif.put(LocalDate.of(2017, 5, 8), .1);
            planif.put(LocalDate.of(2017, 5, 15), .0);
            planif.put(LocalDate.of(2017, 5, 22), .0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .0);
            planif.put(LocalDate.of(2017, 6, 12), .0);
            planif.put(LocalDate.of(2017, 6, 19), .0);
            planif.put(LocalDate.of(2017, 6, 26), .0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2099, "IDAL00099", "Tâche n°99", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("BPE"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), .0);
            planif.put(LocalDate.of(2017, 4, 24), .0);
            planif.put(LocalDate.of(2017, 5, 1), .0);
            planif.put(LocalDate.of(2017, 5, 8), .0);
            planif.put(LocalDate.of(2017, 5, 15), .0);
            planif.put(LocalDate.of(2017, 5, 22), .0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .0);
            planif.put(LocalDate.of(2017, 6, 12), .0);
            planif.put(LocalDate.of(2017, 6, 19), .0);
            planif.put(LocalDate.of(2017, 6, 26), .0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2100, "IDAL000100", "Tâche n°100", projetAppliDao.load("ProjetC"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("HLE"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), .0);
            planif.put(LocalDate.of(2017, 4, 24), .0);
            planif.put(LocalDate.of(2017, 5, 1), .0);
            planif.put(LocalDate.of(2017, 5, 8), .0);
            planif.put(LocalDate.of(2017, 5, 15), .0);
            planif.put(LocalDate.of(2017, 5, 22), .0);
            planif.put(LocalDate.of(2017, 5, 29), .1);
            planif.put(LocalDate.of(2017, 6, 5), .0);
            planif.put(LocalDate.of(2017, 6, 12), .0);
            planif.put(LocalDate.of(2017, 6, 19), .0);
            planif.put(LocalDate.of(2017, 6, 26), .0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2101, "IDAL000101", "Tâche n°101", projetAppliDao.load("ProjetA"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("FBO"), profilDao.load("Dev"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), .0);
            planif.put(LocalDate.of(2017, 4, 24), .1);
            planif.put(LocalDate.of(2017, 5, 1), .0);
            planif.put(LocalDate.of(2017, 5, 8), .0);
            planif.put(LocalDate.of(2017, 5, 15), .0);
            planif.put(LocalDate.of(2017, 5, 22), .0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .0);
            planif.put(LocalDate.of(2017, 6, 12), .0);
            planif.put(LocalDate.of(2017, 6, 19), .0);
            planif.put(LocalDate.of(2017, 6, 26), .0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2102, "IDAL000102", "Tâche n°102", projetAppliDao.load("ProjetA"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("FBO"), profilDao.load("Archi"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), .0);
            planif.put(LocalDate.of(2017, 4, 24), .0);
            planif.put(LocalDate.of(2017, 5, 1), .0);
            planif.put(LocalDate.of(2017, 5, 8), .0);
            planif.put(LocalDate.of(2017, 5, 15), .0);
            planif.put(LocalDate.of(2017, 5, 22), .0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .0);
            planif.put(LocalDate.of(2017, 6, 12), .0);
            planif.put(LocalDate.of(2017, 6, 19), .0);
            planif.put(LocalDate.of(2017, 6, 26), .0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2103, "IDAL000103", "Tâche n°103", projetAppliDao.load("ProjetA"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("BPE"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), .0);
            planif.put(LocalDate.of(2017, 4, 24), .0);
            planif.put(LocalDate.of(2017, 5, 1), .0);
            planif.put(LocalDate.of(2017, 5, 8), .0);
            planif.put(LocalDate.of(2017, 5, 15), .0);
            planif.put(LocalDate.of(2017, 5, 22), .0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .0);
            planif.put(LocalDate.of(2017, 6, 12), .0);
            planif.put(LocalDate.of(2017, 6, 19), .0);
            planif.put(LocalDate.of(2017, 6, 26), .0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2104, "IDAL000104", "Tâche n°104", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("BPE"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), .0);
            planif.put(LocalDate.of(2017, 4, 24), .0);
            planif.put(LocalDate.of(2017, 5, 1), .0);
            planif.put(LocalDate.of(2017, 5, 8), .0);
            planif.put(LocalDate.of(2017, 5, 15), .0);
            planif.put(LocalDate.of(2017, 5, 22), .0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .0);
            planif.put(LocalDate.of(2017, 6, 12), 1.0);
            planif.put(LocalDate.of(2017, 6, 19), 1.0);
            planif.put(LocalDate.of(2017, 6, 26), 1.0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2105, "IDAL000105", "Tâche n°105", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("ITA"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), .0);
            planif.put(LocalDate.of(2017, 4, 24), .0);
            planif.put(LocalDate.of(2017, 5, 1), .0);
            planif.put(LocalDate.of(2017, 5, 8), .0);
            planif.put(LocalDate.of(2017, 5, 15), .0);
            planif.put(LocalDate.of(2017, 5, 22), .0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .0);
            planif.put(LocalDate.of(2017, 6, 12), .0);
            planif.put(LocalDate.of(2017, 6, 19), .0);
            planif.put(LocalDate.of(2017, 6, 26), 2.0);
            planif.put(LocalDate.of(2017, 7, 3), 2.0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2106, "IDAL000106", "Tâche n°106", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("ITA"), profilDao.load("Dev"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), .0);
            planif.put(LocalDate.of(2017, 4, 24), .0);
            planif.put(LocalDate.of(2017, 5, 1), .0);
            planif.put(LocalDate.of(2017, 5, 8), .0);
            planif.put(LocalDate.of(2017, 5, 15), .0);
            planif.put(LocalDate.of(2017, 5, 22), .0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .0);
            planif.put(LocalDate.of(2017, 6, 12), .0);
            planif.put(LocalDate.of(2017, 6, 19), .3);
            planif.put(LocalDate.of(2017, 6, 26), .0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2107, "IDAL000107", "Tâche n°107", projetAppliDao.load("ProjetC"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("RVA"), profilDao.load("GC"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), .0);
            planif.put(LocalDate.of(2017, 4, 24), .0);
            planif.put(LocalDate.of(2017, 5, 1), .0);
            planif.put(LocalDate.of(2017, 5, 8), .0);
            planif.put(LocalDate.of(2017, 5, 15), .0);
            planif.put(LocalDate.of(2017, 5, 22), .0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .0);
            planif.put(LocalDate.of(2017, 6, 12), .0);
            planif.put(LocalDate.of(2017, 6, 19), .0);
            planif.put(LocalDate.of(2017, 6, 26), .0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2108, "IDAL000108", "Tâche n°108", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("EGR"), profilDao.load("TestPerf"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), .0);
            planif.put(LocalDate.of(2017, 4, 24), .0);
            planif.put(LocalDate.of(2017, 5, 1), .0);
            planif.put(LocalDate.of(2017, 5, 8), .0);
            planif.put(LocalDate.of(2017, 5, 15), .0);
            planif.put(LocalDate.of(2017, 5, 22), .0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .0);
            planif.put(LocalDate.of(2017, 6, 12), .0);
            planif.put(LocalDate.of(2017, 6, 19), 2.0);
            planif.put(LocalDate.of(2017, 6, 26), 2.0);
            planif.put(LocalDate.of(2017, 7, 3), 2.0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2109, "IDAL000109", "Tâche n°109", projetAppliDao.load("ProjetA"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("FDA"), profilDao.load("Archi"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), 1.0);
            planif.put(LocalDate.of(2017, 4, 24), 1.0);
            planif.put(LocalDate.of(2017, 5, 1), 1.0);
            planif.put(LocalDate.of(2017, 5, 8), .0);
            planif.put(LocalDate.of(2017, 5, 15), .0);
            planif.put(LocalDate.of(2017, 5, 22), .0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .0);
            planif.put(LocalDate.of(2017, 6, 12), .0);
            planif.put(LocalDate.of(2017, 6, 19), .0);
            planif.put(LocalDate.of(2017, 6, 26), .0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2110, "IDAL000110", "Tâche n°110", projetAppliDao.load("*"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("FDA"), profilDao.load("Dev"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), .0);
            planif.put(LocalDate.of(2017, 4, 24), .0);
            planif.put(LocalDate.of(2017, 5, 1), 2.0);
            planif.put(LocalDate.of(2017, 5, 8), 2.0);
            planif.put(LocalDate.of(2017, 5, 15), .0);
            planif.put(LocalDate.of(2017, 5, 22), .0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .0);
            planif.put(LocalDate.of(2017, 6, 12), .0);
            planif.put(LocalDate.of(2017, 6, 19), .0);
            planif.put(LocalDate.of(2017, 6, 26), .0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2111, "IDAL000111", "Tâche n°111", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("FDA"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), .0);
            planif.put(LocalDate.of(2017, 4, 24), .3);
            planif.put(LocalDate.of(2017, 5, 1), .0);
            planif.put(LocalDate.of(2017, 5, 8), .0);
            planif.put(LocalDate.of(2017, 5, 15), .0);
            planif.put(LocalDate.of(2017, 5, 22), .0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .0);
            planif.put(LocalDate.of(2017, 6, 12), .0);
            planif.put(LocalDate.of(2017, 6, 19), .0);
            planif.put(LocalDate.of(2017, 6, 26), .0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2112, "IDAL000112", "Tâche n°112", projetAppliDao.load("ProjetC"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("FDA"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), .0);
            planif.put(LocalDate.of(2017, 4, 24), .0);
            planif.put(LocalDate.of(2017, 5, 1), .0);
            planif.put(LocalDate.of(2017, 5, 8), .0);
            planif.put(LocalDate.of(2017, 5, 15), .0);
            planif.put(LocalDate.of(2017, 5, 22), .0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .0);
            planif.put(LocalDate.of(2017, 6, 12), .0);
            planif.put(LocalDate.of(2017, 6, 19), .0);
            planif.put(LocalDate.of(2017, 6, 26), .0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2113, "IDAL000113", "Tâche n°113", projetAppliDao.load("ProjetC"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("BPE"), profilDao.load("Archi"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), .0);
            planif.put(LocalDate.of(2017, 4, 24), 2.0);
            planif.put(LocalDate.of(2017, 5, 1), 2.0);
            planif.put(LocalDate.of(2017, 5, 8), 2.0);
            planif.put(LocalDate.of(2017, 5, 15), .0);
            planif.put(LocalDate.of(2017, 5, 22), .0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .0);
            planif.put(LocalDate.of(2017, 6, 12), .0);
            planif.put(LocalDate.of(2017, 6, 19), .0);
            planif.put(LocalDate.of(2017, 6, 26), .0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2114, "IDAL000114", "Tâche n°114", projetAppliDao.load("ProjetC"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("BPE"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), .1);
            planif.put(LocalDate.of(2017, 4, 24), .0);
            planif.put(LocalDate.of(2017, 5, 1), .0);
            planif.put(LocalDate.of(2017, 5, 8), .0);
            planif.put(LocalDate.of(2017, 5, 15), .0);
            planif.put(LocalDate.of(2017, 5, 22), .0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .0);
            planif.put(LocalDate.of(2017, 6, 12), .0);
            planif.put(LocalDate.of(2017, 6, 19), .0);
            planif.put(LocalDate.of(2017, 6, 26), .0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2115, "IDAL000115", "Tâche n°115", projetAppliDao.load("ProjetC"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("HLE"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), .3);
            planif.put(LocalDate.of(2017, 4, 24), .0);
            planif.put(LocalDate.of(2017, 5, 1), .0);
            planif.put(LocalDate.of(2017, 5, 8), .0);
            planif.put(LocalDate.of(2017, 5, 15), .0);
            planif.put(LocalDate.of(2017, 5, 22), .0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .0);
            planif.put(LocalDate.of(2017, 6, 12), .0);
            planif.put(LocalDate.of(2017, 6, 19), .0);
            planif.put(LocalDate.of(2017, 6, 26), .0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2116, "IDAL000116", "Tâche n°116", projetAppliDao.load("*"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("FBO"), profilDao.load("Dev"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), 1.0);
            planif.put(LocalDate.of(2017, 4, 24), .0);
            planif.put(LocalDate.of(2017, 5, 1), .0);
            planif.put(LocalDate.of(2017, 5, 8), .0);
            planif.put(LocalDate.of(2017, 5, 15), .0);
            planif.put(LocalDate.of(2017, 5, 22), .0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .0);
            planif.put(LocalDate.of(2017, 6, 12), .0);
            planif.put(LocalDate.of(2017, 6, 19), .0);
            planif.put(LocalDate.of(2017, 6, 26), .0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2117, "IDAL000117", "Tâche n°117", projetAppliDao.load("*"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("FBO"), profilDao.load("Archi"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), 2.0);
            planif.put(LocalDate.of(2017, 4, 24), .0);
            planif.put(LocalDate.of(2017, 5, 1), .0);
            planif.put(LocalDate.of(2017, 5, 8), .0);
            planif.put(LocalDate.of(2017, 5, 15), .0);
            planif.put(LocalDate.of(2017, 5, 22), .0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .0);
            planif.put(LocalDate.of(2017, 6, 12), .0);
            planif.put(LocalDate.of(2017, 6, 19), .0);
            planif.put(LocalDate.of(2017, 6, 26), .0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2118, "IDAL000118", "Tâche n°118", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("BPE"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), .0);
            planif.put(LocalDate.of(2017, 4, 24), .0);
            planif.put(LocalDate.of(2017, 5, 1), 1.0);
            planif.put(LocalDate.of(2017, 5, 8), 1.0);
            planif.put(LocalDate.of(2017, 5, 15), 1.0);
            planif.put(LocalDate.of(2017, 5, 22), .0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .0);
            planif.put(LocalDate.of(2017, 6, 12), .0);
            planif.put(LocalDate.of(2017, 6, 19), .0);
            planif.put(LocalDate.of(2017, 6, 26), .0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2119, "IDAL000119", "Tâche n°119", projetAppliDao.load("ProjetC"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("BPE"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), .0);
            planif.put(LocalDate.of(2017, 4, 24), .0);
            planif.put(LocalDate.of(2017, 5, 1), .0);
            planif.put(LocalDate.of(2017, 5, 8), .0);
            planif.put(LocalDate.of(2017, 5, 15), 2.0);
            planif.put(LocalDate.of(2017, 5, 22), 2.0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .0);
            planif.put(LocalDate.of(2017, 6, 12), .0);
            planif.put(LocalDate.of(2017, 6, 19), .0);
            planif.put(LocalDate.of(2017, 6, 26), .0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2120, "IDAL000120", "Tâche n°120", projetAppliDao.load("ProjetA"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("ITA"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), .0);
            planif.put(LocalDate.of(2017, 4, 24), .0);
            planif.put(LocalDate.of(2017, 5, 1), .0);
            planif.put(LocalDate.of(2017, 5, 8), .3);
            planif.put(LocalDate.of(2017, 5, 15), .0);
            planif.put(LocalDate.of(2017, 5, 22), .0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .0);
            planif.put(LocalDate.of(2017, 6, 12), .0);
            planif.put(LocalDate.of(2017, 6, 19), .0);
            planif.put(LocalDate.of(2017, 6, 26), .0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2121, "IDAL000121", "Tâche n°121", projetAppliDao.load("ProjetC"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("ITA"), profilDao.load("Dev"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), .0);
            planif.put(LocalDate.of(2017, 4, 24), .0);
            planif.put(LocalDate.of(2017, 5, 1), .0);
            planif.put(LocalDate.of(2017, 5, 8), .0);
            planif.put(LocalDate.of(2017, 5, 15), .0);
            planif.put(LocalDate.of(2017, 5, 22), .0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .0);
            planif.put(LocalDate.of(2017, 6, 12), .0);
            planif.put(LocalDate.of(2017, 6, 19), .0);
            planif.put(LocalDate.of(2017, 6, 26), .0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2122, "IDAL000122", "Tâche n°122", projetAppliDao.load("ProjetA"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("RVA"), profilDao.load("GC"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), .0);
            planif.put(LocalDate.of(2017, 4, 24), .0);
            planif.put(LocalDate.of(2017, 5, 1), .0);
            planif.put(LocalDate.of(2017, 5, 8), 2.0);
            planif.put(LocalDate.of(2017, 5, 15), 2.0);
            planif.put(LocalDate.of(2017, 5, 22), 2.0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .0);
            planif.put(LocalDate.of(2017, 6, 12), .0);
            planif.put(LocalDate.of(2017, 6, 19), .0);
            planif.put(LocalDate.of(2017, 6, 26), .0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2123, "IDAL000123", "Tâche n°123", projetAppliDao.load("ProjetC"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("EGR"), profilDao.load("TestPerf"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), .0);
            planif.put(LocalDate.of(2017, 4, 24), .0);
            planif.put(LocalDate.of(2017, 5, 1), .3);
            planif.put(LocalDate.of(2017, 5, 8), .0);
            planif.put(LocalDate.of(2017, 5, 15), .0);
            planif.put(LocalDate.of(2017, 5, 22), .0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .0);
            planif.put(LocalDate.of(2017, 6, 12), .0);
            planif.put(LocalDate.of(2017, 6, 19), .0);
            planif.put(LocalDate.of(2017, 6, 26), .0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2124, "IDAL000124", "Tâche n°124", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("FDA"), profilDao.load("Archi"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), .0);
            planif.put(LocalDate.of(2017, 4, 24), .1);
            planif.put(LocalDate.of(2017, 5, 1), .0);
            planif.put(LocalDate.of(2017, 5, 8), .0);
            planif.put(LocalDate.of(2017, 5, 15), .0);
            planif.put(LocalDate.of(2017, 5, 22), .0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .0);
            planif.put(LocalDate.of(2017, 6, 12), .0);
            planif.put(LocalDate.of(2017, 6, 19), .0);
            planif.put(LocalDate.of(2017, 6, 26), .0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2125, "IDAL000125", "Tâche n°125", projetAppliDao.load("ProjetC"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("FDA"), profilDao.load("Dev"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), .3);
            planif.put(LocalDate.of(2017, 4, 24), .0);
            planif.put(LocalDate.of(2017, 5, 1), .0);
            planif.put(LocalDate.of(2017, 5, 8), .0);
            planif.put(LocalDate.of(2017, 5, 15), .0);
            planif.put(LocalDate.of(2017, 5, 22), .0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .0);
            planif.put(LocalDate.of(2017, 6, 12), .0);
            planif.put(LocalDate.of(2017, 6, 19), .0);
            planif.put(LocalDate.of(2017, 6, 26), .0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2126, "IDAL000126", "Tâche n°126", projetAppliDao.load("ProjetA"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("FDA"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), .0);
            planif.put(LocalDate.of(2017, 4, 24), .0);
            planif.put(LocalDate.of(2017, 5, 1), .0);
            planif.put(LocalDate.of(2017, 5, 8), .0);
            planif.put(LocalDate.of(2017, 5, 15), .0);
            planif.put(LocalDate.of(2017, 5, 22), .0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .0);
            planif.put(LocalDate.of(2017, 6, 12), .0);
            planif.put(LocalDate.of(2017, 6, 19), .0);
            planif.put(LocalDate.of(2017, 6, 26), .0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2127, "IDAL000127", "Tâche n°127", projetAppliDao.load("ProjetA"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("FDA"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), .0);
            planif.put(LocalDate.of(2017, 4, 24), .0);
            planif.put(LocalDate.of(2017, 5, 1), .0);
            planif.put(LocalDate.of(2017, 5, 8), .0);
            planif.put(LocalDate.of(2017, 5, 15), .0);
            planif.put(LocalDate.of(2017, 5, 22), .0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .0);
            planif.put(LocalDate.of(2017, 6, 12), 2.0);
            planif.put(LocalDate.of(2017, 6, 19), 2.0);
            planif.put(LocalDate.of(2017, 6, 26), .0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2128, "IDAL000128", "Tâche n°128", projetAppliDao.load("ProjetA"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("BPE"), profilDao.load("Archi"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), .0);
            planif.put(LocalDate.of(2017, 4, 24), .0);
            planif.put(LocalDate.of(2017, 5, 1), .0);
            planif.put(LocalDate.of(2017, 5, 8), .0);
            planif.put(LocalDate.of(2017, 5, 15), .0);
            planif.put(LocalDate.of(2017, 5, 22), .0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .3);
            planif.put(LocalDate.of(2017, 6, 12), .0);
            planif.put(LocalDate.of(2017, 6, 19), .0);
            planif.put(LocalDate.of(2017, 6, 26), .0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2129, "IDAL000129", "Tâche n°129", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("BPE"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), .0);
            planif.put(LocalDate.of(2017, 4, 24), .0);
            planif.put(LocalDate.of(2017, 5, 1), .0);
            planif.put(LocalDate.of(2017, 5, 8), .0);
            planif.put(LocalDate.of(2017, 5, 15), .0);
            planif.put(LocalDate.of(2017, 5, 22), .0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .0);
            planif.put(LocalDate.of(2017, 6, 12), .0);
            planif.put(LocalDate.of(2017, 6, 19), .0);
            planif.put(LocalDate.of(2017, 6, 26), .0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2130, "IDAL000130", "Tâche n°130", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("HLE"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), .0);
            planif.put(LocalDate.of(2017, 4, 24), .0);
            planif.put(LocalDate.of(2017, 5, 1), .0);
            planif.put(LocalDate.of(2017, 5, 8), .0);
            planif.put(LocalDate.of(2017, 5, 15), .0);
            planif.put(LocalDate.of(2017, 5, 22), .0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), 2.0);
            planif.put(LocalDate.of(2017, 6, 12), 2.0);
            planif.put(LocalDate.of(2017, 6, 19), 2.0);
            planif.put(LocalDate.of(2017, 6, 26), .0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2131, "IDAL000131", "Tâche n°131", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("FBO"), profilDao.load("Dev"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), .0);
            planif.put(LocalDate.of(2017, 4, 24), .0);
            planif.put(LocalDate.of(2017, 5, 1), .1);
            planif.put(LocalDate.of(2017, 5, 8), .0);
            planif.put(LocalDate.of(2017, 5, 15), .0);
            planif.put(LocalDate.of(2017, 5, 22), .0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .0);
            planif.put(LocalDate.of(2017, 6, 12), .0);
            planif.put(LocalDate.of(2017, 6, 19), .0);
            planif.put(LocalDate.of(2017, 6, 26), .0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2132, "IDAL000132", "Tâche n°132", projetAppliDao.load("ProjetC"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("FBO"), profilDao.load("Archi"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), .0);
            planif.put(LocalDate.of(2017, 4, 24), .3);
            planif.put(LocalDate.of(2017, 5, 1), .0);
            planif.put(LocalDate.of(2017, 5, 8), .0);
            planif.put(LocalDate.of(2017, 5, 15), .0);
            planif.put(LocalDate.of(2017, 5, 22), .0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .0);
            planif.put(LocalDate.of(2017, 6, 12), .0);
            planif.put(LocalDate.of(2017, 6, 19), .0);
            planif.put(LocalDate.of(2017, 6, 26), .0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2133, "IDAL000133", "Tâche n°133", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("BPE"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), .3);
            planif.put(LocalDate.of(2017, 4, 24), .0);
            planif.put(LocalDate.of(2017, 5, 1), .0);
            planif.put(LocalDate.of(2017, 5, 8), .0);
            planif.put(LocalDate.of(2017, 5, 15), .0);
            planif.put(LocalDate.of(2017, 5, 22), .0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .0);
            planif.put(LocalDate.of(2017, 6, 12), .0);
            planif.put(LocalDate.of(2017, 6, 19), .0);
            planif.put(LocalDate.of(2017, 6, 26), .0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2134, "IDAL000134", "Tâche n°134", projetAppliDao.load("ProjetA"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("BPE"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), .0);
            planif.put(LocalDate.of(2017, 4, 24), .0);
            planif.put(LocalDate.of(2017, 5, 1), .0);
            planif.put(LocalDate.of(2017, 5, 8), .1);
            planif.put(LocalDate.of(2017, 5, 15), .0);
            planif.put(LocalDate.of(2017, 5, 22), .0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .0);
            planif.put(LocalDate.of(2017, 6, 12), .0);
            planif.put(LocalDate.of(2017, 6, 19), .0);
            planif.put(LocalDate.of(2017, 6, 26), .0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2135, "IDAL000135", "Tâche n°135", projetAppliDao.load("*"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("ITA"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), .0);
            planif.put(LocalDate.of(2017, 4, 24), .0);
            planif.put(LocalDate.of(2017, 5, 1), .0);
            planif.put(LocalDate.of(2017, 5, 8), .0);
            planif.put(LocalDate.of(2017, 5, 15), .0);
            planif.put(LocalDate.of(2017, 5, 22), .0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .0);
            planif.put(LocalDate.of(2017, 6, 12), .0);
            planif.put(LocalDate.of(2017, 6, 19), .0);
            planif.put(LocalDate.of(2017, 6, 26), .0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2136, "IDAL000136", "Tâche n°136", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("ITA"), profilDao.load("Dev"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), .0);
            planif.put(LocalDate.of(2017, 4, 24), .0);
            planif.put(LocalDate.of(2017, 5, 1), .0);
            planif.put(LocalDate.of(2017, 5, 8), .0);
            planif.put(LocalDate.of(2017, 5, 15), .0);
            planif.put(LocalDate.of(2017, 5, 22), .0);
            planif.put(LocalDate.of(2017, 5, 29), .1);
            planif.put(LocalDate.of(2017, 6, 5), .0);
            planif.put(LocalDate.of(2017, 6, 12), .0);
            planif.put(LocalDate.of(2017, 6, 19), .0);
            planif.put(LocalDate.of(2017, 6, 26), .0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2137, "IDAL000137", "Tâche n°137", projetAppliDao.load("ProjetC"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("RVA"), profilDao.load("GC"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), .0);
            planif.put(LocalDate.of(2017, 4, 24), .1);
            planif.put(LocalDate.of(2017, 5, 1), .0);
            planif.put(LocalDate.of(2017, 5, 8), .0);
            planif.put(LocalDate.of(2017, 5, 15), .0);
            planif.put(LocalDate.of(2017, 5, 22), .0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .0);
            planif.put(LocalDate.of(2017, 6, 12), .0);
            planif.put(LocalDate.of(2017, 6, 19), .0);
            planif.put(LocalDate.of(2017, 6, 26), .0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2138, "IDAL000138", "Tâche n°138", projetAppliDao.load("ProjetC"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("EGR"), profilDao.load("TestPerf"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), .0);
            planif.put(LocalDate.of(2017, 4, 24), .0);
            planif.put(LocalDate.of(2017, 5, 1), .0);
            planif.put(LocalDate.of(2017, 5, 8), .0);
            planif.put(LocalDate.of(2017, 5, 15), .0);
            planif.put(LocalDate.of(2017, 5, 22), .0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .0);
            planif.put(LocalDate.of(2017, 6, 12), .0);
            planif.put(LocalDate.of(2017, 6, 19), .0);
            planif.put(LocalDate.of(2017, 6, 26), .0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2139, "IDAL000139", "Tâche n°139", projetAppliDao.load("ProjetC"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("FDA"), profilDao.load("Archi"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), .0);
            planif.put(LocalDate.of(2017, 4, 24), .0);
            planif.put(LocalDate.of(2017, 5, 1), .0);
            planif.put(LocalDate.of(2017, 5, 8), .0);
            planif.put(LocalDate.of(2017, 5, 15), .0);
            planif.put(LocalDate.of(2017, 5, 22), .0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .0);
            planif.put(LocalDate.of(2017, 6, 12), .0);
            planif.put(LocalDate.of(2017, 6, 19), .0);
            planif.put(LocalDate.of(2017, 6, 26), .0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2140, "IDAL000140", "Tâche n°140", projetAppliDao.load("ProjetC"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("FDA"), profilDao.load("Dev"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), .0);
            planif.put(LocalDate.of(2017, 4, 24), .0);
            planif.put(LocalDate.of(2017, 5, 1), .0);
            planif.put(LocalDate.of(2017, 5, 8), .0);
            planif.put(LocalDate.of(2017, 5, 15), .0);
            planif.put(LocalDate.of(2017, 5, 22), .0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .0);
            planif.put(LocalDate.of(2017, 6, 12), 1.0);
            planif.put(LocalDate.of(2017, 6, 19), 1.0);
            planif.put(LocalDate.of(2017, 6, 26), 1.0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2141, "IDAL000141", "Tâche n°141", projetAppliDao.load("*"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("FDA"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), .0);
            planif.put(LocalDate.of(2017, 4, 24), .0);
            planif.put(LocalDate.of(2017, 5, 1), .0);
            planif.put(LocalDate.of(2017, 5, 8), .0);
            planif.put(LocalDate.of(2017, 5, 15), .0);
            planif.put(LocalDate.of(2017, 5, 22), .0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .0);
            planif.put(LocalDate.of(2017, 6, 12), .0);
            planif.put(LocalDate.of(2017, 6, 19), .0);
            planif.put(LocalDate.of(2017, 6, 26), 2.0);
            planif.put(LocalDate.of(2017, 7, 3), 2.0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2142, "IDAL000142", "Tâche n°142", projetAppliDao.load("*"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("FDA"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), .0);
            planif.put(LocalDate.of(2017, 4, 24), .0);
            planif.put(LocalDate.of(2017, 5, 1), .0);
            planif.put(LocalDate.of(2017, 5, 8), .0);
            planif.put(LocalDate.of(2017, 5, 15), .0);
            planif.put(LocalDate.of(2017, 5, 22), .0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .0);
            planif.put(LocalDate.of(2017, 6, 12), .0);
            planif.put(LocalDate.of(2017, 6, 19), .3);
            planif.put(LocalDate.of(2017, 6, 26), .0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2143, "IDAL000143", "Tâche n°143", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("BPE"), profilDao.load("Archi"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), .0);
            planif.put(LocalDate.of(2017, 4, 24), .0);
            planif.put(LocalDate.of(2017, 5, 1), .0);
            planif.put(LocalDate.of(2017, 5, 8), .0);
            planif.put(LocalDate.of(2017, 5, 15), .0);
            planif.put(LocalDate.of(2017, 5, 22), .0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .0);
            planif.put(LocalDate.of(2017, 6, 12), .0);
            planif.put(LocalDate.of(2017, 6, 19), .0);
            planif.put(LocalDate.of(2017, 6, 26), .0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2144, "IDAL000144", "Tâche n°144", projetAppliDao.load("ProjetC"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("BPE"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), .0);
            planif.put(LocalDate.of(2017, 4, 24), .0);
            planif.put(LocalDate.of(2017, 5, 1), .0);
            planif.put(LocalDate.of(2017, 5, 8), .0);
            planif.put(LocalDate.of(2017, 5, 15), .0);
            planif.put(LocalDate.of(2017, 5, 22), .0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .0);
            planif.put(LocalDate.of(2017, 6, 12), .0);
            planif.put(LocalDate.of(2017, 6, 19), 2.0);
            planif.put(LocalDate.of(2017, 6, 26), 2.0);
            planif.put(LocalDate.of(2017, 7, 3), 2.0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2145, "IDAL000145", "Tâche n°145", projetAppliDao.load("ProjetA"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("HLE"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), 1.0);
            planif.put(LocalDate.of(2017, 4, 24), 1.0);
            planif.put(LocalDate.of(2017, 5, 1), 1.0);
            planif.put(LocalDate.of(2017, 5, 8), .0);
            planif.put(LocalDate.of(2017, 5, 15), .0);
            planif.put(LocalDate.of(2017, 5, 22), .0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .0);
            planif.put(LocalDate.of(2017, 6, 12), .0);
            planif.put(LocalDate.of(2017, 6, 19), .0);
            planif.put(LocalDate.of(2017, 6, 26), .0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2146, "IDAL000146", "Tâche n°146", projetAppliDao.load("ProjetC"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("FBO"), profilDao.load("Dev"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), .0);
            planif.put(LocalDate.of(2017, 4, 24), .0);
            planif.put(LocalDate.of(2017, 5, 1), 2.0);
            planif.put(LocalDate.of(2017, 5, 8), 2.0);
            planif.put(LocalDate.of(2017, 5, 15), .0);
            planif.put(LocalDate.of(2017, 5, 22), .0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .0);
            planif.put(LocalDate.of(2017, 6, 12), .0);
            planif.put(LocalDate.of(2017, 6, 19), .0);
            planif.put(LocalDate.of(2017, 6, 26), .0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2147, "IDAL000147", "Tâche n°147", projetAppliDao.load("ProjetA"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("FBO"), profilDao.load("Archi"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), .0);
            planif.put(LocalDate.of(2017, 4, 24), .3);
            planif.put(LocalDate.of(2017, 5, 1), .0);
            planif.put(LocalDate.of(2017, 5, 8), .0);
            planif.put(LocalDate.of(2017, 5, 15), .0);
            planif.put(LocalDate.of(2017, 5, 22), .0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .0);
            planif.put(LocalDate.of(2017, 6, 12), .0);
            planif.put(LocalDate.of(2017, 6, 19), .0);
            planif.put(LocalDate.of(2017, 6, 26), .0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2148, "IDAL000148", "Tâche n°148", projetAppliDao.load("ProjetC"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("BPE"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), .0);
            planif.put(LocalDate.of(2017, 4, 24), .0);
            planif.put(LocalDate.of(2017, 5, 1), .0);
            planif.put(LocalDate.of(2017, 5, 8), .0);
            planif.put(LocalDate.of(2017, 5, 15), .0);
            planif.put(LocalDate.of(2017, 5, 22), .0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .0);
            planif.put(LocalDate.of(2017, 6, 12), .0);
            planif.put(LocalDate.of(2017, 6, 19), .0);
            planif.put(LocalDate.of(2017, 6, 26), .0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2149, "IDAL000149", "Tâche n°149", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("BPE"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), .0);
            planif.put(LocalDate.of(2017, 4, 24), 2.0);
            planif.put(LocalDate.of(2017, 5, 1), 2.0);
            planif.put(LocalDate.of(2017, 5, 8), 2.0);
            planif.put(LocalDate.of(2017, 5, 15), .0);
            planif.put(LocalDate.of(2017, 5, 22), .0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .0);
            planif.put(LocalDate.of(2017, 6, 12), .0);
            planif.put(LocalDate.of(2017, 6, 19), .0);
            planif.put(LocalDate.of(2017, 6, 26), .0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2150, "IDAL000150", "Tâche n°150", projetAppliDao.load("ProjetC"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("ITA"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), .1);
            planif.put(LocalDate.of(2017, 4, 24), .0);
            planif.put(LocalDate.of(2017, 5, 1), .0);
            planif.put(LocalDate.of(2017, 5, 8), .0);
            planif.put(LocalDate.of(2017, 5, 15), .0);
            planif.put(LocalDate.of(2017, 5, 22), .0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .0);
            planif.put(LocalDate.of(2017, 6, 12), .0);
            planif.put(LocalDate.of(2017, 6, 19), .0);
            planif.put(LocalDate.of(2017, 6, 26), .0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2151, "IDAL000151", "Tâche n°151", projetAppliDao.load("ProjetA"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("ITA"), profilDao.load("Dev"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), .3);
            planif.put(LocalDate.of(2017, 4, 24), .0);
            planif.put(LocalDate.of(2017, 5, 1), .0);
            planif.put(LocalDate.of(2017, 5, 8), .0);
            planif.put(LocalDate.of(2017, 5, 15), .0);
            planif.put(LocalDate.of(2017, 5, 22), .0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .0);
            planif.put(LocalDate.of(2017, 6, 12), .0);
            planif.put(LocalDate.of(2017, 6, 19), .0);
            planif.put(LocalDate.of(2017, 6, 26), .0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2152, "IDAL000152", "Tâche n°152", projetAppliDao.load("ProjetA"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("RVA"), profilDao.load("GC"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), 1.0);
            planif.put(LocalDate.of(2017, 4, 24), .0);
            planif.put(LocalDate.of(2017, 5, 1), .0);
            planif.put(LocalDate.of(2017, 5, 8), .0);
            planif.put(LocalDate.of(2017, 5, 15), .0);
            planif.put(LocalDate.of(2017, 5, 22), .0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .0);
            planif.put(LocalDate.of(2017, 6, 12), .0);
            planif.put(LocalDate.of(2017, 6, 19), .0);
            planif.put(LocalDate.of(2017, 6, 26), .0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2153, "IDAL000153", "Tâche n°153", projetAppliDao.load("ProjetA"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("EGR"), profilDao.load("TestPerf"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), 2.0);
            planif.put(LocalDate.of(2017, 4, 24), .0);
            planif.put(LocalDate.of(2017, 5, 1), .0);
            planif.put(LocalDate.of(2017, 5, 8), .0);
            planif.put(LocalDate.of(2017, 5, 15), .0);
            planif.put(LocalDate.of(2017, 5, 22), .0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .0);
            planif.put(LocalDate.of(2017, 6, 12), .0);
            planif.put(LocalDate.of(2017, 6, 19), .0);
            planif.put(LocalDate.of(2017, 6, 26), .0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2154, "IDAL000154", "Tâche n°154", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("FDA"), profilDao.load("Archi"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), .0);
            planif.put(LocalDate.of(2017, 4, 24), .0);
            planif.put(LocalDate.of(2017, 5, 1), 1.0);
            planif.put(LocalDate.of(2017, 5, 8), 1.0);
            planif.put(LocalDate.of(2017, 5, 15), 1.0);
            planif.put(LocalDate.of(2017, 5, 22), .0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .0);
            planif.put(LocalDate.of(2017, 6, 12), .0);
            planif.put(LocalDate.of(2017, 6, 19), .0);
            planif.put(LocalDate.of(2017, 6, 26), .0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2155, "IDAL000155", "Tâche n°155", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("FDA"), profilDao.load("Dev"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), .0);
            planif.put(LocalDate.of(2017, 4, 24), .0);
            planif.put(LocalDate.of(2017, 5, 1), .0);
            planif.put(LocalDate.of(2017, 5, 8), .0);
            planif.put(LocalDate.of(2017, 5, 15), 2.0);
            planif.put(LocalDate.of(2017, 5, 22), 2.0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .0);
            planif.put(LocalDate.of(2017, 6, 12), .0);
            planif.put(LocalDate.of(2017, 6, 19), .0);
            planif.put(LocalDate.of(2017, 6, 26), .0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2156, "IDAL000156", "Tâche n°156", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("FDA"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), .0);
            planif.put(LocalDate.of(2017, 4, 24), .0);
            planif.put(LocalDate.of(2017, 5, 1), .0);
            planif.put(LocalDate.of(2017, 5, 8), .3);
            planif.put(LocalDate.of(2017, 5, 15), .0);
            planif.put(LocalDate.of(2017, 5, 22), .0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .0);
            planif.put(LocalDate.of(2017, 6, 12), .0);
            planif.put(LocalDate.of(2017, 6, 19), .0);
            planif.put(LocalDate.of(2017, 6, 26), .0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2157, "IDAL000157", "Tâche n°157", projetAppliDao.load("ProjetC"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("FDA"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), .0);
            planif.put(LocalDate.of(2017, 4, 24), .0);
            planif.put(LocalDate.of(2017, 5, 1), .0);
            planif.put(LocalDate.of(2017, 5, 8), .0);
            planif.put(LocalDate.of(2017, 5, 15), .0);
            planif.put(LocalDate.of(2017, 5, 22), .0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .0);
            planif.put(LocalDate.of(2017, 6, 12), .0);
            planif.put(LocalDate.of(2017, 6, 19), .0);
            planif.put(LocalDate.of(2017, 6, 26), .0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2158, "IDAL000158", "Tâche n°158", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("BPE"), profilDao.load("Archi"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), .0);
            planif.put(LocalDate.of(2017, 4, 24), .0);
            planif.put(LocalDate.of(2017, 5, 1), .0);
            planif.put(LocalDate.of(2017, 5, 8), 2.0);
            planif.put(LocalDate.of(2017, 5, 15), 2.0);
            planif.put(LocalDate.of(2017, 5, 22), 2.0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .0);
            planif.put(LocalDate.of(2017, 6, 12), .0);
            planif.put(LocalDate.of(2017, 6, 19), .0);
            planif.put(LocalDate.of(2017, 6, 26), .0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2159, "IDAL000159", "Tâche n°159", projetAppliDao.load("ProjetA"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("BPE"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), .0);
            planif.put(LocalDate.of(2017, 4, 24), .0);
            planif.put(LocalDate.of(2017, 5, 1), .3);
            planif.put(LocalDate.of(2017, 5, 8), .0);
            planif.put(LocalDate.of(2017, 5, 15), .0);
            planif.put(LocalDate.of(2017, 5, 22), .0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .0);
            planif.put(LocalDate.of(2017, 6, 12), .0);
            planif.put(LocalDate.of(2017, 6, 19), .0);
            planif.put(LocalDate.of(2017, 6, 26), .0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2160, "IDAL000160", "Tâche n°160", projetAppliDao.load("*"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("HLE"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), .0);
            planif.put(LocalDate.of(2017, 4, 24), .1);
            planif.put(LocalDate.of(2017, 5, 1), .0);
            planif.put(LocalDate.of(2017, 5, 8), .0);
            planif.put(LocalDate.of(2017, 5, 15), .0);
            planif.put(LocalDate.of(2017, 5, 22), .0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .0);
            planif.put(LocalDate.of(2017, 6, 12), .0);
            planif.put(LocalDate.of(2017, 6, 19), .0);
            planif.put(LocalDate.of(2017, 6, 26), .0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2161, "IDAL000161", "Tâche n°161", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("FBO"), profilDao.load("Dev"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), .3);
            planif.put(LocalDate.of(2017, 4, 24), .0);
            planif.put(LocalDate.of(2017, 5, 1), .0);
            planif.put(LocalDate.of(2017, 5, 8), .0);
            planif.put(LocalDate.of(2017, 5, 15), .0);
            planif.put(LocalDate.of(2017, 5, 22), .0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .0);
            planif.put(LocalDate.of(2017, 6, 12), .0);
            planif.put(LocalDate.of(2017, 6, 19), .0);
            planif.put(LocalDate.of(2017, 6, 26), .0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2162, "IDAL000162", "Tâche n°162", projetAppliDao.load("ProjetC"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("FBO"), profilDao.load("Archi"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), .0);
            planif.put(LocalDate.of(2017, 4, 24), .0);
            planif.put(LocalDate.of(2017, 5, 1), .0);
            planif.put(LocalDate.of(2017, 5, 8), .0);
            planif.put(LocalDate.of(2017, 5, 15), .0);
            planif.put(LocalDate.of(2017, 5, 22), .0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .0);
            planif.put(LocalDate.of(2017, 6, 12), .0);
            planif.put(LocalDate.of(2017, 6, 19), .0);
            planif.put(LocalDate.of(2017, 6, 26), .0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2163, "IDAL000163", "Tâche n°163", projetAppliDao.load("ProjetC"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("BPE"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), .0);
            planif.put(LocalDate.of(2017, 4, 24), .0);
            planif.put(LocalDate.of(2017, 5, 1), .0);
            planif.put(LocalDate.of(2017, 5, 8), .0);
            planif.put(LocalDate.of(2017, 5, 15), .0);
            planif.put(LocalDate.of(2017, 5, 22), .0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .0);
            planif.put(LocalDate.of(2017, 6, 12), 2.0);
            planif.put(LocalDate.of(2017, 6, 19), 2.0);
            planif.put(LocalDate.of(2017, 6, 26), .0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2164, "IDAL000164", "Tâche n°164", projetAppliDao.load("ProjetC"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("BPE"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), .0);
            planif.put(LocalDate.of(2017, 4, 24), .0);
            planif.put(LocalDate.of(2017, 5, 1), .0);
            planif.put(LocalDate.of(2017, 5, 8), .0);
            planif.put(LocalDate.of(2017, 5, 15), .0);
            planif.put(LocalDate.of(2017, 5, 22), .0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .3);
            planif.put(LocalDate.of(2017, 6, 12), .0);
            planif.put(LocalDate.of(2017, 6, 19), .0);
            planif.put(LocalDate.of(2017, 6, 26), .0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2165, "IDAL000165", "Tâche n°165", projetAppliDao.load("ProjetC"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("ITA"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), .0);
            planif.put(LocalDate.of(2017, 4, 24), .0);
            planif.put(LocalDate.of(2017, 5, 1), .0);
            planif.put(LocalDate.of(2017, 5, 8), .0);
            planif.put(LocalDate.of(2017, 5, 15), .0);
            planif.put(LocalDate.of(2017, 5, 22), .0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .0);
            planif.put(LocalDate.of(2017, 6, 12), .0);
            planif.put(LocalDate.of(2017, 6, 19), .0);
            planif.put(LocalDate.of(2017, 6, 26), .0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2166, "IDAL000166", "Tâche n°166", projetAppliDao.load("*"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("ITA"), profilDao.load("Dev"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), .0);
            planif.put(LocalDate.of(2017, 4, 24), .0);
            planif.put(LocalDate.of(2017, 5, 1), .0);
            planif.put(LocalDate.of(2017, 5, 8), .0);
            planif.put(LocalDate.of(2017, 5, 15), .0);
            planif.put(LocalDate.of(2017, 5, 22), .0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), 2.0);
            planif.put(LocalDate.of(2017, 6, 12), 2.0);
            planif.put(LocalDate.of(2017, 6, 19), 2.0);
            planif.put(LocalDate.of(2017, 6, 26), .0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        {
            Tache tache = new Tache(2167, "IDAL000167", "Tâche n°167", projetAppliDao.load("*"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("RVA"), profilDao.load("GC"));
            Map<LocalDate, Double> planif = new HashMap<>();
            planif.put(LocalDate.of(2017, 4, 17), .0);
            planif.put(LocalDate.of(2017, 4, 24), .0);
            planif.put(LocalDate.of(2017, 5, 1), .1);
            planif.put(LocalDate.of(2017, 5, 8), .0);
            planif.put(LocalDate.of(2017, 5, 15), .0);
            planif.put(LocalDate.of(2017, 5, 22), .0);
            planif.put(LocalDate.of(2017, 5, 29), .0);
            planif.put(LocalDate.of(2017, 6, 5), .0);
            planif.put(LocalDate.of(2017, 6, 12), .0);
            planif.put(LocalDate.of(2017, 6, 19), .0);
            planif.put(LocalDate.of(2017, 6, 26), .0);
            planif.put(LocalDate.of(2017, 7, 3), .0);
            ;
            matrice.put(tache, planif);
        }
        // <<< Fin de la partie générée
        return new PlanCharge(date, planifications);
    }

}

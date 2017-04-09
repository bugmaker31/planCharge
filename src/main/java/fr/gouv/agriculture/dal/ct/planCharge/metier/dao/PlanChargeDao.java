package fr.gouv.agriculture.dal.ct.planCharge.metier.dao;

import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.PlanCharge;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.Planification;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.Tache;
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
        {
            Tache tache = new Tache(1526, "(pas de ticket)", "Formation Orion - Part 1/2  – RGO", projetAppliDao.load("*"), LocalDate.parse("2016-08-25"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("RGO"), profilDao.load("RéfTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2001, "IDAL000001", "Formation Orion - Part 2/2  – RGO", projetAppliDao.load("*"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("RGO"), profilDao.load("RéfTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2002, "IDAL000002", "Tâche n°2", projetAppliDao.load("ProjetA"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("?"), profilDao.load("RéfTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2003, "IDAL000003", "Tâche n°3", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("FDA"), profilDao.load("Archi"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2003, "IDAL000004", "Tâche n°4", projetAppliDao.load("ProjetC"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("BPE"), profilDao.load("Dev"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2004, "IDAL0004", "Tâche n°4", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("FDA"), profilDao.load("Archi"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2005, "IDAL0005", "Tâche n°5", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("FDA"), profilDao.load("Dev"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2006, "IDAL0006", "Tâche n°6", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("FDA"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2007, "IDAL0007", "Tâche n°7", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("FDA"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2008, "IDAL0008", "Tâche n°8", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("BPE"), profilDao.load("Archi"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2009, "IDAL0009", "Tâche n°9", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("BPE"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2010, "IDAL00010", "Tâche n°10", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("HLE"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2011, "IDAL00011", "Tâche n°11", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("FBO"), profilDao.load("Dev"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2012, "IDAL00012", "Tâche n°12", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("FBO"), profilDao.load("Archi"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2013, "IDAL00013", "Tâche n°13", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("BPE"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2014, "IDAL00014", "Tâche n°14", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("BPE"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2015, "IDAL00015", "Tâche n°15", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("ITA"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2016, "IDAL00016", "Tâche n°16", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("ITA"), profilDao.load("Dev"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2017, "IDAL00017", "Tâche n°17", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("RVA"), profilDao.load("GC"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2018, "IDAL00018", "Tâche n°18", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("EGR"), profilDao.load("TestPerf"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2019, "IDAL00019", "Tâche n°19", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("FDA"), profilDao.load("Archi"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2020, "IDAL00020", "Tâche n°20", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("FDA"), profilDao.load("Dev"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2021, "IDAL00021", "Tâche n°21", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("FDA"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2022, "IDAL00022", "Tâche n°22", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("FDA"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2023, "IDAL00023", "Tâche n°23", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("BPE"), profilDao.load("Archi"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2024, "IDAL00024", "Tâche n°24", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("BPE"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2025, "IDAL00025", "Tâche n°25", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("HLE"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2026, "IDAL00026", "Tâche n°26", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("FBO"), profilDao.load("Dev"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2027, "IDAL00027", "Tâche n°27", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("FBO"), profilDao.load("Archi"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2028, "IDAL00028", "Tâche n°28", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("BPE"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2029, "IDAL00029", "Tâche n°29", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("BPE"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2030, "IDAL00030", "Tâche n°30", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("ITA"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2031, "IDAL00031", "Tâche n°31", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("ITA"), profilDao.load("Dev"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2032, "IDAL00032", "Tâche n°32", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("RVA"), profilDao.load("GC"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2033, "IDAL00033", "Tâche n°33", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("EGR"), profilDao.load("TestPerf"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2034, "IDAL00034", "Tâche n°34", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("FDA"), profilDao.load("Archi"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2035, "IDAL00035", "Tâche n°35", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("FDA"), profilDao.load("Dev"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2036, "IDAL00036", "Tâche n°36", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("FDA"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2037, "IDAL00037", "Tâche n°37", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("FDA"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2038, "IDAL00038", "Tâche n°38", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("BPE"), profilDao.load("Archi"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2039, "IDAL00039", "Tâche n°39", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("BPE"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2040, "IDAL00040", "Tâche n°40", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("HLE"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2041, "IDAL00041", "Tâche n°41", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("FBO"), profilDao.load("Dev"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2042, "IDAL00042", "Tâche n°42", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("FBO"), profilDao.load("Archi"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2043, "IDAL00043", "Tâche n°43", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("BPE"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2044, "IDAL00044", "Tâche n°44", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("BPE"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2045, "IDAL00045", "Tâche n°45", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("ITA"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2046, "IDAL00046", "Tâche n°46", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("ITA"), profilDao.load("Dev"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2047, "IDAL00047", "Tâche n°47", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("RVA"), profilDao.load("GC"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2048, "IDAL00048", "Tâche n°48", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("EGR"), profilDao.load("TestPerf"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2049, "IDAL00049", "Tâche n°49", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("FDA"), profilDao.load("Archi"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2050, "IDAL00050", "Tâche n°50", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("FDA"), profilDao.load("Dev"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2051, "IDAL00051", "Tâche n°51", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("FDA"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2052, "IDAL00052", "Tâche n°52", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("FDA"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2053, "IDAL00053", "Tâche n°53", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("BPE"), profilDao.load("Archi"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2054, "IDAL00054", "Tâche n°54", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("BPE"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2055, "IDAL00055", "Tâche n°55", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("HLE"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2056, "IDAL00056", "Tâche n°56", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("FBO"), profilDao.load("Dev"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2057, "IDAL00057", "Tâche n°57", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("FBO"), profilDao.load("Archi"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2058, "IDAL00058", "Tâche n°58", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("BPE"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2059, "IDAL00059", "Tâche n°59", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("BPE"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2060, "IDAL00060", "Tâche n°60", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("ITA"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2061, "IDAL00061", "Tâche n°61", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("ITA"), profilDao.load("Dev"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2062, "IDAL00062", "Tâche n°62", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("RVA"), profilDao.load("GC"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2063, "IDAL00063", "Tâche n°63", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("EGR"), profilDao.load("TestPerf"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2064, "IDAL00064", "Tâche n°64", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("FDA"), profilDao.load("Archi"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2065, "IDAL00065", "Tâche n°65", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("FDA"), profilDao.load("Dev"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2066, "IDAL00066", "Tâche n°66", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("FDA"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2067, "IDAL00067", "Tâche n°67", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("FDA"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2068, "IDAL00068", "Tâche n°68", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("BPE"), profilDao.load("Archi"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2069, "IDAL00069", "Tâche n°69", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("BPE"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2070, "IDAL00070", "Tâche n°70", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("HLE"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2071, "IDAL00071", "Tâche n°71", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("FBO"), profilDao.load("Dev"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2072, "IDAL00072", "Tâche n°72", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("FBO"), profilDao.load("Archi"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2073, "IDAL00073", "Tâche n°73", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("BPE"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2074, "IDAL00074", "Tâche n°74", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("BPE"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2075, "IDAL00075", "Tâche n°75", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("ITA"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2076, "IDAL00076", "Tâche n°76", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("ITA"), profilDao.load("Dev"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2077, "IDAL00077", "Tâche n°77", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("RVA"), profilDao.load("GC"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2078, "IDAL00078", "Tâche n°78", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("EGR"), profilDao.load("TestPerf"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2079, "IDAL00079", "Tâche n°79", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("FDA"), profilDao.load("Archi"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2080, "IDAL00080", "Tâche n°80", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("FDA"), profilDao.load("Dev"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2081, "IDAL00081", "Tâche n°81", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("FDA"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2082, "IDAL00082", "Tâche n°82", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("FDA"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2083, "IDAL00083", "Tâche n°83", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("BPE"), profilDao.load("Archi"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2084, "IDAL00084", "Tâche n°84", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("BPE"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2085, "IDAL00085", "Tâche n°85", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("HLE"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2086, "IDAL00086", "Tâche n°86", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("FBO"), profilDao.load("Dev"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2087, "IDAL00087", "Tâche n°87", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("FBO"), profilDao.load("Archi"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2088, "IDAL00088", "Tâche n°88", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("BPE"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2089, "IDAL00089", "Tâche n°89", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("BPE"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2090, "IDAL00090", "Tâche n°90", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("ITA"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2091, "IDAL00091", "Tâche n°91", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("ITA"), profilDao.load("Dev"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2092, "IDAL00092", "Tâche n°92", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("RVA"), profilDao.load("GC"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2093, "IDAL00093", "Tâche n°93", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("EGR"), profilDao.load("TestPerf"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2094, "IDAL00094", "Tâche n°94", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("FDA"), profilDao.load("Archi"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2095, "IDAL00095", "Tâche n°95", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("FDA"), profilDao.load("Dev"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2096, "IDAL00096", "Tâche n°96", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("FDA"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2097, "IDAL00097", "Tâche n°97", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("FDA"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2098, "IDAL00098", "Tâche n°98", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("BPE"), profilDao.load("Archi"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2099, "IDAL00099", "Tâche n°99", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("BPE"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2100, "IDAL000100", "Tâche n°100", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("HLE"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2101, "IDAL000101", "Tâche n°101", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("FBO"), profilDao.load("Dev"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2102, "IDAL000102", "Tâche n°102", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("FBO"), profilDao.load("Archi"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2103, "IDAL000103", "Tâche n°103", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("BPE"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2104, "IDAL000104", "Tâche n°104", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("BPE"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2105, "IDAL000105", "Tâche n°105", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("ITA"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2106, "IDAL000106", "Tâche n°106", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("ITA"), profilDao.load("Dev"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2107, "IDAL000107", "Tâche n°107", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("RVA"), profilDao.load("GC"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2108, "IDAL000108", "Tâche n°108", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("EGR"), profilDao.load("TestPerf"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2109, "IDAL000109", "Tâche n°109", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("FDA"), profilDao.load("Archi"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2110, "IDAL000110", "Tâche n°110", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("FDA"), profilDao.load("Dev"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2111, "IDAL000111", "Tâche n°111", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("FDA"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2112, "IDAL000112", "Tâche n°112", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("FDA"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2113, "IDAL000113", "Tâche n°113", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("BPE"), profilDao.load("Archi"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2114, "IDAL000114", "Tâche n°114", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("BPE"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2115, "IDAL000115", "Tâche n°115", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("HLE"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2116, "IDAL000116", "Tâche n°116", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("FBO"), profilDao.load("Dev"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2117, "IDAL000117", "Tâche n°117", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("FBO"), profilDao.load("Archi"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2118, "IDAL000118", "Tâche n°118", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("BPE"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2119, "IDAL000119", "Tâche n°119", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("BPE"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2120, "IDAL000120", "Tâche n°120", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("ITA"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2121, "IDAL000121", "Tâche n°121", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("ITA"), profilDao.load("Dev"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2122, "IDAL000122", "Tâche n°122", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("RVA"), profilDao.load("GC"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2123, "IDAL000123", "Tâche n°123", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("EGR"), profilDao.load("TestPerf"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2124, "IDAL000124", "Tâche n°124", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("FDA"), profilDao.load("Archi"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2125, "IDAL000125", "Tâche n°125", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("FDA"), profilDao.load("Dev"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2126, "IDAL000126", "Tâche n°126", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("FDA"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2127, "IDAL000127", "Tâche n°127", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("FDA"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2128, "IDAL000128", "Tâche n°128", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("BPE"), profilDao.load("Archi"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2129, "IDAL000129", "Tâche n°129", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("BPE"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2130, "IDAL000130", "Tâche n°130", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("HLE"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2131, "IDAL000131", "Tâche n°131", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("FBO"), profilDao.load("Dev"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2132, "IDAL000132", "Tâche n°132", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("FBO"), profilDao.load("Archi"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2133, "IDAL000133", "Tâche n°133", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("BPE"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2134, "IDAL000134", "Tâche n°134", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("BPE"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2135, "IDAL000135", "Tâche n°135", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("ITA"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2136, "IDAL000136", "Tâche n°136", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("ITA"), profilDao.load("Dev"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2137, "IDAL000137", "Tâche n°137", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("RVA"), profilDao.load("GC"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2138, "IDAL000138", "Tâche n°138", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("EGR"), profilDao.load("TestPerf"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2139, "IDAL000139", "Tâche n°139", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("FDA"), profilDao.load("Archi"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2140, "IDAL000140", "Tâche n°140", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("FDA"), profilDao.load("Dev"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2141, "IDAL000141", "Tâche n°141", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("FDA"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2142, "IDAL000142", "Tâche n°142", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("FDA"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2143, "IDAL000143", "Tâche n°143", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("BPE"), profilDao.load("Archi"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2144, "IDAL000144", "Tâche n°144", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("BPE"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2145, "IDAL000145", "Tâche n°145", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("HLE"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2146, "IDAL000146", "Tâche n°146", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("FBO"), profilDao.load("Dev"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2147, "IDAL000147", "Tâche n°147", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("FBO"), profilDao.load("Archi"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2148, "IDAL000148", "Tâche n°148", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("BPE"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2149, "IDAL000149", "Tâche n°149", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("BPE"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2150, "IDAL000150", "Tâche n°150", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("ITA"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2151, "IDAL000151", "Tâche n°151", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("ITA"), profilDao.load("Dev"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2152, "IDAL000152", "Tâche n°152", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("RVA"), profilDao.load("GC"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2153, "IDAL000153", "Tâche n°153", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("EGR"), profilDao.load("TestPerf"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2154, "IDAL000154", "Tâche n°154", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("FDA"), profilDao.load("Archi"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2155, "IDAL000155", "Tâche n°155", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("FDA"), profilDao.load("Dev"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2156, "IDAL000156", "Tâche n°156", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("FDA"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2157, "IDAL000157", "Tâche n°157", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("FDA"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2158, "IDAL000158", "Tâche n°158", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("BPE"), profilDao.load("Archi"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2159, "IDAL000159", "Tâche n°159", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("BPE"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2160, "IDAL000160", "Tâche n°160", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("HLE"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2161, "IDAL000161", "Tâche n°161", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("FBO"), profilDao.load("Dev"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2162, "IDAL000162", "Tâche n°162", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("FBO"), profilDao.load("Archi"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2163, "IDAL000163", "Tâche n°163", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("BPE"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2164, "IDAL000164", "Tâche n°164", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("BPE"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2165, "IDAL000165", "Tâche n°165", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("ITA"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2166, "IDAL000166", "Tâche n°166", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("ITA"), profilDao.load("Dev"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2167, "IDAL000167", "Tâche n°167", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("RVA"), profilDao.load("GC"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2168, "IDAL000168", "Tâche n°168", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("EGR"), profilDao.load("TestPerf"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2169, "IDAL000169", "Tâche n°169", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("FDA"), profilDao.load("Archi"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2170, "IDAL000170", "Tâche n°170", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("FDA"), profilDao.load("Dev"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2171, "IDAL000171", "Tâche n°171", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("FDA"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2172, "IDAL000172", "Tâche n°172", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("FDA"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2173, "IDAL000173", "Tâche n°173", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("BPE"), profilDao.load("Archi"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2174, "IDAL000174", "Tâche n°174", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("BPE"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2175, "IDAL000175", "Tâche n°175", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("HLE"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2176, "IDAL000176", "Tâche n°176", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("FBO"), profilDao.load("Dev"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2177, "IDAL000177", "Tâche n°177", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("FBO"), profilDao.load("Archi"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2178, "IDAL000178", "Tâche n°178", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("BPE"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2179, "IDAL000179", "Tâche n°179", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("BPE"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2180, "IDAL000180", "Tâche n°180", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("ITA"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2181, "IDAL000181", "Tâche n°181", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("ITA"), profilDao.load("Dev"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2182, "IDAL000182", "Tâche n°182", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("RVA"), profilDao.load("GC"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2183, "IDAL000183", "Tâche n°183", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("FDA"), profilDao.load("Archi"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2184, "IDAL000184", "Tâche n°184", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("FDA"), profilDao.load("Dev"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2185, "IDAL000185", "Tâche n°185", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("FDA"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2186, "IDAL000186", "Tâche n°186", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("FDA"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2187, "IDAL000187", "Tâche n°187", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("BPE"), profilDao.load("Archi"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2188, "IDAL000188", "Tâche n°188", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("BPE"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2189, "IDAL000189", "Tâche n°189", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("HLE"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2190, "IDAL000190", "Tâche n°190", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("FBO"), profilDao.load("Dev"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2191, "IDAL000191", "Tâche n°191", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("FBO"), profilDao.load("Archi"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2192, "IDAL000192", "Tâche n°192", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("BPE"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2193, "IDAL000193", "Tâche n°193", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("BPE"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2194, "IDAL000194", "Tâche n°194", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("ITA"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2195, "IDAL000195", "Tâche n°195", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("ITA"), profilDao.load("Dev"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2196, "IDAL000196", "Tâche n°196", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("RVA"), profilDao.load("GC"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2197, "IDAL000197", "Tâche n°197", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("EGR"), profilDao.load("TestPerf"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2198, "IDAL000198", "Tâche n°198", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("FDA"), profilDao.load("Archi"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2199, "IDAL000199", "Tâche n°199", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("FDA"), profilDao.load("Dev"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2200, "IDAL000200", "Tâche n°200", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("FDA"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2201, "IDAL000201", "Tâche n°201", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("FDA"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2202, "IDAL000202", "Tâche n°202", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("BPE"), profilDao.load("Archi"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2203, "IDAL000203", "Tâche n°203", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("BPE"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2204, "IDAL000204", "Tâche n°204", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("HLE"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2205, "IDAL000205", "Tâche n°205", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("FBO"), profilDao.load("Dev"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2206, "IDAL000206", "Tâche n°206", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("FBO"), profilDao.load("Archi"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2207, "IDAL000207", "Tâche n°207", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("BPE"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2208, "IDAL000208", "Tâche n°208", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("BPE"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2209, "IDAL000209", "Tâche n°209", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("ITA"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2210, "IDAL000210", "Tâche n°210", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("ITA"), profilDao.load("Dev"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2211, "IDAL000211", "Tâche n°211", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("RVA"), profilDao.load("GC"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2212, "IDAL000212", "Tâche n°212", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("FDA"), profilDao.load("TestPerf"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2213, "IDAL000213", "Tâche n°213", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("FDA"), profilDao.load("Archi"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2214, "IDAL000214", "Tâche n°214", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("FDA"), profilDao.load("Dev"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2215, "IDAL000215", "Tâche n°215", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("FDA"), profilDao.load("Archi"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2216, "IDAL000216", "Tâche n°216", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("BPE"), profilDao.load("Dev"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2217, "IDAL000217", "Tâche n°217", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("BPE"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2218, "IDAL000218", "Tâche n°218", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("HLE"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2219, "IDAL000219", "Tâche n°219", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("FBO"), profilDao.load("Archi"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2220, "IDAL000220", "Tâche n°220", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("FBO"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2221, "IDAL000221", "Tâche n°221", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("BPE"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2222, "IDAL000222", "Tâche n°222", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("BPE"), profilDao.load("Dev"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2223, "IDAL000223", "Tâche n°223", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("ITA"), profilDao.load("Archi"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2224, "IDAL000224", "Tâche n°224", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("ITA"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2225, "IDAL000225", "Tâche n°225", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("RVA"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2226, "IDAL000226", "Tâche n°226", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("EGR"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2227, "IDAL000227", "Tâche n°227", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("FDA"), profilDao.load("Dev"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2228, "IDAL000228", "Tâche n°228", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("FDA"), profilDao.load("GC"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2229, "IDAL000229", "Tâche n°229", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("FDA"), profilDao.load("TestPerf"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2230, "IDAL000230", "Tâche n°230", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("FDA"), profilDao.load("Archi"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2231, "IDAL000231", "Tâche n°231", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("BPE"), profilDao.load("Dev"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2232, "IDAL000232", "Tâche n°232", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("BPE"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2233, "IDAL000233", "Tâche n°233", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("HLE"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2234, "IDAL000234", "Tâche n°234", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("FBO"), profilDao.load("Archi"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2235, "IDAL000235", "Tâche n°235", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("FBO"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2236, "IDAL000236", "Tâche n°236", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("BPE"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2237, "IDAL000237", "Tâche n°237", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("BPE"), profilDao.load("Dev"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2238, "IDAL000238", "Tâche n°238", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("ITA"), profilDao.load("Archi"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2239, "IDAL000239", "Tâche n°239", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("ITA"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2240, "IDAL000240", "Tâche n°240", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("FDA"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2241, "IDAL000241", "Tâche n°241", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("FDA"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2242, "IDAL000242", "Tâche n°242", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("FDA"), profilDao.load("Dev"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2243, "IDAL000243", "Tâche n°243", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("FDA"), profilDao.load("GC"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2244, "IDAL000244", "Tâche n°244", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("BPE"), profilDao.load("TestPerf"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2245, "IDAL000245", "Tâche n°245", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("BPE"), profilDao.load("Archi"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2246, "IDAL000246", "Tâche n°246", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("HLE"), profilDao.load("Dev"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2247, "IDAL000247", "Tâche n°247", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("FBO"), profilDao.load("Archi"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2248, "IDAL000248", "Tâche n°248", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("FBO"), profilDao.load("Dev"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2249, "IDAL000249", "Tâche n°249", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("BPE"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2250, "IDAL000250", "Tâche n°250", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("BPE"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2251, "IDAL000251", "Tâche n°251", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("ITA"), profilDao.load("Archi"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2252, "IDAL000252", "Tâche n°252", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("ITA"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2253, "IDAL000253", "Tâche n°253", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("RVA"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2254, "IDAL000254", "Tâche n°254", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("EGR"), profilDao.load("Dev"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2255, "IDAL000255", "Tâche n°255", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("FDA"), profilDao.load("Archi"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2256, "IDAL000256", "Tâche n°256", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("FDA"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2257, "IDAL000257", "Tâche n°257", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("FDA"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2258, "IDAL000258", "Tâche n°258", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("FDA"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2259, "IDAL000259", "Tâche n°259", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("BPE"), profilDao.load("Dev"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2260, "IDAL000260", "Tâche n°260", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("BPE"), profilDao.load("GC"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2261, "IDAL000261", "Tâche n°261", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("HLE"), profilDao.load("TestPerf"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2262, "IDAL000262", "Tâche n°262", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("FBO"), profilDao.load("Archi"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2263, "IDAL000263", "Tâche n°263", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("FBO"), profilDao.load("Dev"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2264, "IDAL000264", "Tâche n°264", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("BPE"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2265, "IDAL000265", "Tâche n°265", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("BPE"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2266, "IDAL000266", "Tâche n°266", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("ITA"), profilDao.load("Archi"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2267, "IDAL000267", "Tâche n°267", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("ITA"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2268, "IDAL000268", "Tâche n°268", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("FDA"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2269, "IDAL000269", "Tâche n°269", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("FDA"), profilDao.load("Dev"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2270, "IDAL000270", "Tâche n°270", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("FDA"), profilDao.load("Archi"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2271, "IDAL000271", "Tâche n°271", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("FDA"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2272, "IDAL000272", "Tâche n°272", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("BPE"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2273, "IDAL000273", "Tâche n°273", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("BPE"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2274, "IDAL000274", "Tâche n°274", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("HLE"), profilDao.load("Dev"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2275, "IDAL000275", "Tâche n°275", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("FBO"), profilDao.load("GC"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2276, "IDAL000276", "Tâche n°276", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("FBO"), profilDao.load("TestPerf"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2277, "IDAL000277", "Tâche n°277", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("BPE"), profilDao.load("Archi"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2278, "IDAL000278", "Tâche n°278", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("BPE"), profilDao.load("Dev"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2279, "IDAL000279", "Tâche n°279", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("ITA"), profilDao.load("Archi"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2280, "IDAL000280", "Tâche n°280", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("ITA"), profilDao.load("Dev"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2281, "IDAL000281", "Tâche n°281", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("RVA"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2282, "IDAL000282", "Tâche n°282", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("EGR"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2283, "IDAL000283", "Tâche n°283", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("FDA"), profilDao.load("Archi"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2284, "IDAL000284", "Tâche n°284", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("FDA"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2285, "IDAL000285", "Tâche n°285", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("FDA"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2286, "IDAL000286", "Tâche n°286", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("FDA"), profilDao.load("Dev"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2287, "IDAL000287", "Tâche n°287", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("BPE"), profilDao.load("Archi"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2288, "IDAL000288", "Tâche n°288", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("BPE"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2289, "IDAL000289", "Tâche n°289", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("HLE"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2290, "IDAL000290", "Tâche n°290", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("FBO"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2291, "IDAL000291", "Tâche n°291", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("FBO"), profilDao.load("Dev"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2292, "IDAL000292", "Tâche n°292", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("BPE"), profilDao.load("GC"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2293, "IDAL000293", "Tâche n°293", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("BPE"), profilDao.load("TestPerf"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2294, "IDAL000294", "Tâche n°294", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("ITA"), profilDao.load("Archi"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2295, "IDAL000295", "Tâche n°295", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("ITA"), profilDao.load("Dev"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2296, "IDAL000296", "Tâche n°296", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("FDA"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2297, "IDAL000297", "Tâche n°297", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("FDA"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2298, "IDAL000298", "Tâche n°298", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("FDA"), profilDao.load("Archi"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2299, "IDAL000299", "Tâche n°299", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("FDA"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2300, "IDAL000300", "Tâche n°300", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("BPE"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2301, "IDAL000301", "Tâche n°301", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("BPE"), profilDao.load("Dev"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2302, "IDAL000302", "Tâche n°302", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("HLE"), profilDao.load("Archi"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2303, "IDAL000303", "Tâche n°303", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("FBO"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2304, "IDAL000304", "Tâche n°304", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("FBO"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2305, "IDAL000305", "Tâche n°305", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("BPE"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2306, "IDAL000306", "Tâche n°306", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("BPE"), profilDao.load("Dev"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2307, "IDAL000307", "Tâche n°307", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("ITA"), profilDao.load("GC"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2308, "IDAL000308", "Tâche n°308", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("ITA"), profilDao.load("TestPerf"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2309, "IDAL000309", "Tâche n°309", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("RVA"), profilDao.load("Archi"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2310, "IDAL000310", "Tâche n°310", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("EGR"), profilDao.load("Dev"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2311, "IDAL000311", "Tâche n°311", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("FDA"), profilDao.load("Archi"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2312, "IDAL000312", "Tâche n°312", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("FDA"), profilDao.load("Dev"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2313, "IDAL000313", "Tâche n°313", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("FDA"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2314, "IDAL000314", "Tâche n°314", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("FDA"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2315, "IDAL000315", "Tâche n°315", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("BPE"), profilDao.load("Archi"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2316, "IDAL000316", "Tâche n°316", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("BPE"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2317, "IDAL000317", "Tâche n°317", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("HLE"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2318, "IDAL000318", "Tâche n°318", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("FBO"), profilDao.load("Dev"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2319, "IDAL000319", "Tâche n°319", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("FBO"), profilDao.load("Archi"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2320, "IDAL000320", "Tâche n°320", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("BPE"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2321, "IDAL000321", "Tâche n°321", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("BPE"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2322, "IDAL000322", "Tâche n°322", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("ITA"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2323, "IDAL000323", "Tâche n°323", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("ITA"), profilDao.load("Dev"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2324, "IDAL000324", "Tâche n°324", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("FDA"), profilDao.load("GC"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2325, "IDAL000325", "Tâche n°325", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("FDA"), profilDao.load("TestPerf"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2326, "IDAL000326", "Tâche n°326", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("FDA"), profilDao.load("Archi"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2327, "IDAL000327", "Tâche n°327", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("FDA"), profilDao.load("Dev"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2328, "IDAL000328", "Tâche n°328", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("BPE"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2329, "IDAL000329", "Tâche n°329", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("BPE"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2330, "IDAL000330", "Tâche n°330", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("HLE"), profilDao.load("Archi"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2331, "IDAL000331", "Tâche n°331", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("FBO"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2332, "IDAL000332", "Tâche n°332", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("FBO"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2333, "IDAL000333", "Tâche n°333", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("BPE"), profilDao.load("Dev"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2334, "IDAL000334", "Tâche n°334", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("BPE"), profilDao.load("Archi"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2335, "IDAL000335", "Tâche n°335", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("ITA"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2336, "IDAL000336", "Tâche n°336", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("ITA"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2337, "IDAL000337", "Tâche n°337", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("RVA"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2338, "IDAL000338", "Tâche n°338", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("EGR"), profilDao.load("Dev"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2339, "IDAL000339", "Tâche n°339", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("FDA"), profilDao.load("GC"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2340, "IDAL000340", "Tâche n°340", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("FDA"), profilDao.load("TestPerf"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2341, "IDAL000341", "Tâche n°341", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("FDA"), profilDao.load("Archi"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2342, "IDAL000342", "Tâche n°342", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("FDA"), profilDao.load("Dev"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2343, "IDAL000343", "Tâche n°343", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("BPE"), profilDao.load("Archi"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2344, "IDAL000344", "Tâche n°344", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("BPE"), profilDao.load("Dev"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2345, "IDAL000345", "Tâche n°345", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("HLE"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2346, "IDAL000346", "Tâche n°346", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("FBO"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2347, "IDAL000347", "Tâche n°347", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("FBO"), profilDao.load("Archi"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2348, "IDAL000348", "Tâche n°348", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("BPE"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2349, "IDAL000349", "Tâche n°349", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("BPE"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2350, "IDAL000350", "Tâche n°350", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("ITA"), profilDao.load("Dev"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2351, "IDAL000351", "Tâche n°351", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("ITA"), profilDao.load("Archi"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2352, "IDAL000352", "Tâche n°352", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("FDA"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2353, "IDAL000353", "Tâche n°353", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("FDA"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2354, "IDAL000354", "Tâche n°354", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("FDA"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2355, "IDAL000355", "Tâche n°355", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("FDA"), profilDao.load("Dev"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2356, "IDAL000356", "Tâche n°356", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("BPE"), profilDao.load("GC"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2357, "IDAL000357", "Tâche n°357", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("BPE"), profilDao.load("TestPerf"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2358, "IDAL000358", "Tâche n°358", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("HLE"), profilDao.load("Archi"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2359, "IDAL000359", "Tâche n°359", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("FBO"), profilDao.load("Dev"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2360, "IDAL000360", "Tâche n°360", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("FBO"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2361, "IDAL000361", "Tâche n°361", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("BPE"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2362, "IDAL000362", "Tâche n°362", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("BPE"), profilDao.load("Archi"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2363, "IDAL000363", "Tâche n°363", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("ITA"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2364, "IDAL000364", "Tâche n°364", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("ITA"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2365, "IDAL000365", "Tâche n°365", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("RVA"), profilDao.load("Dev"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2366, "IDAL000366", "Tâche n°366", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("EGR"), profilDao.load("Archi"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2367, "IDAL000367", "Tâche n°367", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("FDA"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2368, "IDAL000368", "Tâche n°368", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("FDA"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2369, "IDAL000369", "Tâche n°369", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("FDA"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2370, "IDAL000370", "Tâche n°370", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("FDA"), profilDao.load("Dev"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2371, "IDAL000371", "Tâche n°371", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("BPE"), profilDao.load("GC"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2372, "IDAL000372", "Tâche n°372", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("BPE"), profilDao.load("TestPerf"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2373, "IDAL000373", "Tâche n°373", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("HLE"), profilDao.load("Archi"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2374, "IDAL000374", "Tâche n°374", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("FBO"), profilDao.load("Dev"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2375, "IDAL000375", "Tâche n°375", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("FBO"), profilDao.load("Archi"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2376, "IDAL000376", "Tâche n°376", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("BPE"), profilDao.load("Dev"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2377, "IDAL000377", "Tâche n°377", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("BPE"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2378, "IDAL000378", "Tâche n°378", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("ITA"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2379, "IDAL000379", "Tâche n°379", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("ITA"), profilDao.load("Archi"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2380, "IDAL000380", "Tâche n°380", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("FDA"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2381, "IDAL000381", "Tâche n°381", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("FDA"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2382, "IDAL000382", "Tâche n°382", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("FDA"), profilDao.load("Dev"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2383, "IDAL000383", "Tâche n°383", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("FDA"), profilDao.load("Archi"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2384, "IDAL000384", "Tâche n°384", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("BPE"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2385, "IDAL000385", "Tâche n°385", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("BPE"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2386, "IDAL000386", "Tâche n°386", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("HLE"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2387, "IDAL000387", "Tâche n°387", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("FBO"), profilDao.load("Dev"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2388, "IDAL000388", "Tâche n°388", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("FBO"), profilDao.load("GC"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2389, "IDAL000389", "Tâche n°389", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("BPE"), profilDao.load("TestPerf"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2390, "IDAL000390", "Tâche n°390", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("BPE"), profilDao.load("Archi"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2391, "IDAL000391", "Tâche n°391", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("ITA"), profilDao.load("Dev"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2392, "IDAL000392", "Tâche n°392", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("ITA"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2393, "IDAL000393", "Tâche n°393", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("RVA"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2394, "IDAL000394", "Tâche n°394", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("EGR"), profilDao.load("Archi"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2395, "IDAL000395", "Tâche n°395", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("FDA"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2396, "IDAL000396", "Tâche n°396", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("FDA"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2397, "IDAL000397", "Tâche n°397", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("FDA"), profilDao.load("Dev"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2398, "IDAL000398", "Tâche n°398", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("FDA"), profilDao.load("Archi"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2399, "IDAL000399", "Tâche n°399", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("BPE"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2400, "IDAL000400", "Tâche n°400", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("BPE"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2401, "IDAL000401", "Tâche n°401", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("HLE"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2402, "IDAL000402", "Tâche n°402", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("FBO"), profilDao.load("Dev"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2403, "IDAL000403", "Tâche n°403", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("FBO"), profilDao.load("GC"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2404, "IDAL000404", "Tâche n°404", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("BPE"), profilDao.load("TestPerf"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2405, "IDAL000405", "Tâche n°405", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("BPE"), profilDao.load("Archi"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2406, "IDAL000406", "Tâche n°406", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("ITA"), profilDao.load("Dev"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2407, "IDAL000407", "Tâche n°407", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("ITA"), profilDao.load("Archi"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2408, "IDAL000408", "Tâche n°408", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("FDA"), profilDao.load("Dev"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2409, "IDAL000409", "Tâche n°409", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("FDA"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2410, "IDAL000410", "Tâche n°410", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("FDA"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2411, "IDAL000411", "Tâche n°411", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("FDA"), profilDao.load("Archi"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2412, "IDAL000412", "Tâche n°412", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("BPE"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2413, "IDAL000413", "Tâche n°413", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("BPE"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2414, "IDAL000414", "Tâche n°414", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("HLE"), profilDao.load("Dev"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2415, "IDAL000415", "Tâche n°415", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("FBO"), profilDao.load("Archi"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2416, "IDAL000416", "Tâche n°416", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("FBO"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2417, "IDAL000417", "Tâche n°417", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("BPE"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2418, "IDAL000418", "Tâche n°418", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("BPE"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2419, "IDAL000419", "Tâche n°419", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("ITA"), profilDao.load("Dev"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2420, "IDAL000420", "Tâche n°420", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("ITA"), profilDao.load("GC"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2421, "IDAL000421", "Tâche n°421", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("RVA"), profilDao.load("TestPerf"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2422, "IDAL000422", "Tâche n°422", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("EGR"), profilDao.load("Archi"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2423, "IDAL000423", "Tâche n°423", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("FDA"), profilDao.load("Dev"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2424, "IDAL000424", "Tâche n°424", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("FDA"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2425, "IDAL000425", "Tâche n°425", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("FDA"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2426, "IDAL000426", "Tâche n°426", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("FDA"), profilDao.load("Archi"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2427, "IDAL000427", "Tâche n°427", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("BPE"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2428, "IDAL000428", "Tâche n°428", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("BPE"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2429, "IDAL000429", "Tâche n°429", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("HLE"), profilDao.load("Dev"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2430, "IDAL000430", "Tâche n°430", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("FBO"), profilDao.load("Archi"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2431, "IDAL000431", "Tâche n°431", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("FBO"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2432, "IDAL000432", "Tâche n°432", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("BPE"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2433, "IDAL000433", "Tâche n°433", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("BPE"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2434, "IDAL000434", "Tâche n°434", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("ITA"), profilDao.load("Dev"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2435, "IDAL000435", "Tâche n°435", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("ITA"), profilDao.load("GC"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2436, "IDAL000436", "Tâche n°436", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("FDA"), profilDao.load("TestPerf"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2437, "IDAL000437", "Tâche n°437", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("FDA"), profilDao.load("Archi"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2438, "IDAL000438", "Tâche n°438", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("FDA"), profilDao.load("Dev"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2439, "IDAL000439", "Tâche n°439", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("FDA"), profilDao.load("Archi"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2440, "IDAL000440", "Tâche n°440", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("BPE"), profilDao.load("Dev"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2441, "IDAL000441", "Tâche n°441", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("BPE"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2442, "IDAL000442", "Tâche n°442", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("HLE"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2443, "IDAL000443", "Tâche n°443", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("FBO"), profilDao.load("Archi"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2444, "IDAL000444", "Tâche n°444", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("FBO"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2445, "IDAL000445", "Tâche n°445", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("BPE"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2446, "IDAL000446", "Tâche n°446", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("BPE"), profilDao.load("Dev"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2447, "IDAL000447", "Tâche n°447", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("ITA"), profilDao.load("Archi"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2448, "IDAL000448", "Tâche n°448", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("ITA"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2449, "IDAL000449", "Tâche n°449", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("RVA"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2450, "IDAL000450", "Tâche n°450", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("EGR"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2451, "IDAL000451", "Tâche n°451", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("FDA"), profilDao.load("Dev"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2452, "IDAL000452", "Tâche n°452", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("FDA"), profilDao.load("GC"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2453, "IDAL000453", "Tâche n°453", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("FDA"), profilDao.load("TestPerf"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2454, "IDAL000454", "Tâche n°454", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("FDA"), profilDao.load("Archi"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2455, "IDAL000455", "Tâche n°455", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("BPE"), profilDao.load("Dev"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2456, "IDAL000456", "Tâche n°456", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("BPE"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2457, "IDAL000457", "Tâche n°457", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("HLE"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2458, "IDAL000458", "Tâche n°458", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("FBO"), profilDao.load("Archi"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2459, "IDAL000459", "Tâche n°459", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("FBO"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2460, "IDAL000460", "Tâche n°460", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("BPE"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2461, "IDAL000461", "Tâche n°461", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("BPE"), profilDao.load("Dev"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2462, "IDAL000462", "Tâche n°462", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("ITA"), profilDao.load("Archi"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2463, "IDAL000463", "Tâche n°463", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("ITA"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2464, "IDAL000464", "Tâche n°464", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("FDA"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2465, "IDAL000465", "Tâche n°465", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("FDA"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2466, "IDAL000466", "Tâche n°466", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("FDA"), profilDao.load("Dev"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2467, "IDAL000467", "Tâche n°467", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("FDA"), profilDao.load("GC"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2468, "IDAL000468", "Tâche n°468", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("BPE"), profilDao.load("TestPerf"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2469, "IDAL000469", "Tâche n°469", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("BPE"), profilDao.load("Archi"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2470, "IDAL000470", "Tâche n°470", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("HLE"), profilDao.load("Dev"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2471, "IDAL000471", "Tâche n°471", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("FBO"), profilDao.load("Archi"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2472, "IDAL000472", "Tâche n°472", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("FBO"), profilDao.load("Dev"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2473, "IDAL000473", "Tâche n°473", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("BPE"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2474, "IDAL000474", "Tâche n°474", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("BPE"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2475, "IDAL000475", "Tâche n°475", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("ITA"), profilDao.load("Archi"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2476, "IDAL000476", "Tâche n°476", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("ITA"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2477, "IDAL000477", "Tâche n°477", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("RVA"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2478, "IDAL000478", "Tâche n°478", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("EGR"), profilDao.load("Dev"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2479, "IDAL000479", "Tâche n°479", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("90_Maximale"), 3.0, ressourceDao.load("FDA"), profilDao.load("Archi"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2480, "IDAL000480", "Tâche n°480", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("70_Haute"), 3.0, ressourceDao.load("FDA"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2481, "IDAL000481", "Tâche n°481", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("FDA"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2482, "IDAL000482", "Tâche n°482", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("FDA"), profilDao.load("RefTech"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }
        {
            Tache tache = new Tache(2483, "IDAL000483", "Tâche n°483", projetAppliDao.load("ProjetB"), LocalDate.parse("2016-11-13"), LocalDate.parse("2016-11-30"), importanceDao.load("50_Normale"), 3.0, ressourceDao.load("BPE"), profilDao.load("Dev"));
            Map<LocalDate, Double> planifT1 = new HashMap<>();
            planifT1.put(LocalDate.of(2016, 11, 13), 7.0);
            matrice.put(tache, planifT1);
        }

        Planification planification = new Planification(matrice);

        return new PlanCharge(date, planification);
    }

}

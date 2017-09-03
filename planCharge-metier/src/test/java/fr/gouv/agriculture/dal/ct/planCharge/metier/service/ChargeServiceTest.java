package fr.gouv.agriculture.dal.ct.planCharge.metier.service;

import fr.gouv.agriculture.dal.ct.planCharge.metier.dto.*;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

import static org.junit.Assert.assertEquals;

@SuppressWarnings({"MagicNumber", "OverlyBroadThrowsClause", "InstanceMethodNamingConvention"})
public class ChargeServiceTest {

    @SuppressWarnings("NullableProblems")
    @NotNull
    private ChargeService chargeService;

    @SuppressWarnings("NullableProblems")
    @NotNull
    private TacheDTO tacheDTO;

    @SuppressWarnings("NullableProblems")
    @NotNull
    private LocalDate dateEtat;


    @org.junit.Before
    public void setUp() throws Exception {

        chargeService = ChargeService.instance();

        LocalDate debutTache = LocalDate.of(2017, 9, 11);
        LocalDate finTache = LocalDate.of(2017, 9, 15);
        double chargeTache = 5.0;
        tacheDTO = new TacheDTO(7, CategorieTacheDTO.PROJET, null, "T0007", "Tâche de TU n°7", new ProjetAppliDTO("*"), StatutDTO.PROVISION, debutTache, finTache, new ImportanceDTO("10-Nouveau"), chargeTache, RessourceGeneriqueDTO.NIMPORTE_QUI, ProfilDTO.TOUS);

        dateEtat = LocalDate.of(2017, 9, 4);
    }

    @org.junit.After
    public void tearDown() throws Exception {
    }


    @org.junit.Test
    public void provision_AvantDebut_Ouvre_MilieuSemaine() throws Exception {
        LocalDate debutPeriode = LocalDate.of(2017, 9, 1); // Un jour ouvré.
        LocalDate finPeriode = debutPeriode.plusDays(7L);
        double provision = chargeService.provision(tacheDTO, debutPeriode, finPeriode, dateEtat);
        assertEquals(0.0, provision, 0.0);
    }

    @org.junit.Test
    public void provision_AvantDebut_NonOuvre() throws Exception {
        LocalDate debutPeriode = LocalDate.of(2017, 9, 3); // Un dimanche.
        LocalDate finPeriode = debutPeriode.plusDays(7L);
        double provision = chargeService.provision(tacheDTO, debutPeriode, finPeriode, dateEtat);
        assertEquals(0.0, provision, 0);
    }

    @org.junit.Test
    public void provision_PendantPeriode_Ouvre_DebutSemaine() throws Exception {
        LocalDate debutPeriode = LocalDate.of(2017, 9, 11);
        LocalDate finPeriode = debutPeriode.plusDays(7L);
        double provision = chargeService.provision(tacheDTO, debutPeriode, finPeriode, dateEtat);
        assertEquals(5.0, provision, 0.0);
    }

    @org.junit.Test
    public void provision_PendantPeriode_Ouvre_UnSeulJour() throws Exception {
        LocalDate debutPeriode = LocalDate.of(2017, 9, 15);
        LocalDate finPeriode = debutPeriode.plusDays(1L);
        double provision = chargeService.provision(tacheDTO, debutPeriode, finPeriode, dateEtat);
        assertEquals(1.0, provision, 0.0);
    }

    // Après la fin demandée :
    @org.junit.Test
    public void provision_ApresEcheance_NonOuvre() throws Exception {
        LocalDate debutPeriode = LocalDate.of(2017, 9, 16); // Un jour non ouvré (samedi).
        LocalDate finPeriode = debutPeriode.plusDays(7L);
        double provision = chargeService.provision(tacheDTO, debutPeriode, finPeriode, dateEtat);
        assertEquals(0.0, provision, 0.0);
    }

    @org.junit.Test
    public void provision_ApresEcheance_Ouvre() throws Exception {
        LocalDate debutPeriode = LocalDate.of(2017, 9, 18); // Un jour ouvré (lundi).
        LocalDate finPeriode = debutPeriode.plusDays(7);
        double provision = chargeService.provision(tacheDTO, debutPeriode, finPeriode, dateEtat);
        assertEquals(0.0, provision, 0.0);
    }
}
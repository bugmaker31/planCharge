package fr.gouv.agriculture.dal.ct.planCharge.metier.service;

import fr.gouv.agriculture.dal.ct.metier.service.AbstractService;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dto.StatutDTO;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dto.TacheDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

public class TacheService extends AbstractService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TacheService.class);


    private static TacheService instance;

    public static TacheService instance() {
        if (instance == null) {
            instance = new TacheService();
        }
        return instance;
    }


    // Méthodes :

    public boolean estATraiter(@NotNull TacheDTO tache) {
        return (tache.getStatut() == null) // Par défaut, la tâche est à traiter. TODO FDA 2017/08 Confirmer.
                || (tache.getStatut().compareTo(StatutDTO.REPORTE) < 0);
    }

    @NotNull
    public List<TacheDTO> tachesATraiter(@NotNull List<TacheDTO> tacheDTOs) {
        return tacheDTOs.parallelStream()
                .filter(this::estATraiter)
                .collect(Collectors.toList());
    }
}

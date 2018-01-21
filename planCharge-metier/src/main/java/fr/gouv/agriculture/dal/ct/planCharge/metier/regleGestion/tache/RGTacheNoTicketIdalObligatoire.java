package fr.gouv.agriculture.dal.ct.planCharge.metier.regleGestion.tache;

import fr.gouv.agriculture.dal.ct.metier.regleGestion.RegleGestion;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dto.TacheDTO;

import javax.validation.constraints.NotNull;

public final class RGTacheNoTicketIdalObligatoire extends RegleGestion<TacheDTO> {

    public static final RGTacheNoTicketIdalObligatoire INSTANCE = new RGTacheNoTicketIdalObligatoire();

    private RGTacheNoTicketIdalObligatoire() {
        super("RG_Tache_NoTicketIdalObligatoire_TODO", "N° de ticket IDAL non défini", tache -> "Le n° de ticket IDAL est requis pour la tâche " + tache.noTache() + ".");
    }

    @Override
    public boolean estApplicable(@NotNull TacheDTO tache) {
        return true; // Toujours applicable.
    }

    @Override
    public boolean estValide(@NotNull TacheDTO tache) {
        return tache.getNoTicketIdal() != null;
    }
}

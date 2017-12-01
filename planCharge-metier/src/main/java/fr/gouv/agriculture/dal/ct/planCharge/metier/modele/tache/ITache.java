package fr.gouv.agriculture.dal.ct.planCharge.metier.modele.tache;

import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.revision.Revisable;

@SuppressWarnings("InterfaceNamingConvention")
public interface ITache<T extends ITache<T>> extends Comparable<T>, Revisable {

    String noTache();

    boolean estProvision();

}

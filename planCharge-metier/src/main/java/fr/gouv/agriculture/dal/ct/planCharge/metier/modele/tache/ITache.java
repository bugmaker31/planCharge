package fr.gouv.agriculture.dal.ct.planCharge.metier.modele.tache;

@SuppressWarnings("InterfaceNamingConvention")
public interface ITache<T extends ITache<T>> extends Comparable<T> {

    String noTache();

    boolean estProvision();

}

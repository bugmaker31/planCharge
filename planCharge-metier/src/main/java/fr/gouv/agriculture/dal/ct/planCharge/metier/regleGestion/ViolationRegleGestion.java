package fr.gouv.agriculture.dal.ct.planCharge.metier.regleGestion;

import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.AbstractEntity;

import javax.validation.constraints.NotNull;

public class ViolationRegleGestion<T extends AbstractEntity> {

    @NotNull
    private RegleGestion<T> regle;
    @NotNull
    private final T entity;

    public ViolationRegleGestion(@NotNull RegleGestion<T> regle, @NotNull T entity) {
        this.regle = regle;
        this.entity = entity;
    }

    @NotNull
    public RegleGestion<T> getRegle() {
        return regle;
    }

    @NotNull
    public T getEntity() {
        return entity;
    }
}

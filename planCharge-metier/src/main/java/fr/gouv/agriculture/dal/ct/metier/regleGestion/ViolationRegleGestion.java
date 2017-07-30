package fr.gouv.agriculture.dal.ct.metier.regleGestion;

import fr.gouv.agriculture.dal.ct.metier.dto.AbstractDTO;

import javax.validation.constraints.NotNull;

public class ViolationRegleGestion<T extends AbstractDTO> {

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

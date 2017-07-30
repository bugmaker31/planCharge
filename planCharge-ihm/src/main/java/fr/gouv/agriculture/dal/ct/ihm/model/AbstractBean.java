package fr.gouv.agriculture.dal.ct.ihm.model;

import fr.gouv.agriculture.dal.ct.metier.dto.AbstractDTO;

import javax.validation.constraints.NotNull;

public abstract class AbstractBean<E extends AbstractDTO, T extends AbstractBean<E, T>> {

    @NotNull
    public abstract E toDto() throws BeanException;

    @NotNull
    public abstract T fromDto(@NotNull E dto) throws BeanException;
}

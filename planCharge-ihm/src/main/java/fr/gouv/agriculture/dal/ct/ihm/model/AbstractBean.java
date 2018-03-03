package fr.gouv.agriculture.dal.ct.ihm.model;

import fr.gouv.agriculture.dal.ct.metier.dto.AbstractDTO;

import javax.validation.constraints.NotNull;

public abstract class AbstractBean<D extends AbstractDTO, B extends AbstractBean<D, B>> {

    @NotNull
    public abstract D toDto() throws BeanException;

    @NotNull
    public abstract B fromDto(@NotNull D dto) throws BeanException;
}

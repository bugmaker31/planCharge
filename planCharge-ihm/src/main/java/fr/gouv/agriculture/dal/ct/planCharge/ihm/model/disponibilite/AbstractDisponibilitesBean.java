package fr.gouv.agriculture.dal.ct.planCharge.ihm.model.disponibilite;

import fr.gouv.agriculture.dal.ct.ihm.model.AbstractBean;
import fr.gouv.agriculture.dal.ct.ihm.model.BeanException;
import fr.gouv.agriculture.dal.ct.metier.dto.AbstractDTO;
import javafx.beans.property.Property;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public abstract class AbstractDisponibilitesBean<D extends AbstractDTO, B extends AbstractDisponibilitesBean<D, B, P>, P extends Property> extends AbstractBean<D, B> {


    // Fields :

    @NotNull
    private final Map<LocalDate, P> calendrier;


    // Constructors:

    AbstractDisponibilitesBean() {
        super();
        calendrier = new TreeMap<>(); // TreeMap, au lieu de HashMap, juste pour faciliter le débogage en gardant le tri sur la Key (date).
    }

    AbstractDisponibilitesBean(@NotNull Map<LocalDate, P> calendrier) {
        super();
        this.calendrier = calendrier;
    }


    // Getters/Setters:


    // Delegation to #calendrier :

    public int size() {
        return calendrier.size();
    }

    public boolean isEmpty() {
        return calendrier.isEmpty();
    }

    public boolean containsKey(LocalDate key) {
        return calendrier.containsKey(key);
    }

    public P get(LocalDate key) {
        return calendrier.get(key);
    }

    public P put(LocalDate key, P value) {
        return calendrier.put(key, value);
    }

    public P remove(LocalDate key) {
        return calendrier.remove(key);
    }

    public void putAll(Map<? extends LocalDate, ? extends P> m) {
        calendrier.putAll(m);
    }

    public void clear() {
        calendrier.clear();
    }

    public Set<LocalDate> keySet() {
        return calendrier.keySet();
    }

    public Collection<P> values() {
        return calendrier.values();
    }

    public Set<Map.Entry<LocalDate, P>> entrySet() {
        return calendrier.entrySet();
    }

    @Override
    public boolean equals(Object o) {
        return calendrier.equals(o);
    }

    @Override
    public int hashCode() {
        return calendrier.hashCode();
    }

    public P getOrDefault(LocalDate key, P defaultValue) {
        return calendrier.getOrDefault(key, defaultValue);
    }

    public void forEach(BiConsumer<? super LocalDate, ? super P> action) {
        calendrier.forEach(action);
    }

    public void replaceAll(BiFunction<? super LocalDate, ? super P, ? extends P> function) {
        calendrier.replaceAll(function);
    }

    public P putIfAbsent(LocalDate key, P value) {
        return calendrier.putIfAbsent(key, value);
    }

    public boolean remove(LocalDate key, P value) {
        return calendrier.remove(key, value);
    }

    public boolean replace(LocalDate key, P oldValue, P newValue) {
        return calendrier.replace(key, oldValue, newValue);
    }

    public P replace(LocalDate key, P value) {
        return calendrier.replace(key, value);
    }

    public P computeIfAbsent(LocalDate key, Function<? super LocalDate, ? extends P> mappingFunction) {
        return calendrier.computeIfAbsent(key, mappingFunction);
    }

    public P computeIfPresent(LocalDate key, BiFunction<? super LocalDate, ? super P, ? extends P> remappingFunction) {
        return calendrier.computeIfPresent(key, remappingFunction);
    }

    public P compute(LocalDate key, BiFunction<? super LocalDate, ? super P, ? extends P> remappingFunction) {
        return calendrier.compute(key, remappingFunction);
    }

    public P merge(LocalDate key, P value, BiFunction<? super P, ? super P, ? extends P> remappingFunction) {
        return calendrier.merge(key, value, remappingFunction);
    }


    // Implementation of AbstractBean :

    @NotNull
    @Override
    public D toDto() throws BeanException {
        return null; // FIXME FDA 2017/08
    }

    @NotNull
    @Override
    public B fromDto(@NotNull D dto) throws BeanException {
        return null; // FIXME FDA 2017/08
    }


    // Utilitie's methods:

    @Override
    public String toString() {
        return calendrier.toString();
    }
}

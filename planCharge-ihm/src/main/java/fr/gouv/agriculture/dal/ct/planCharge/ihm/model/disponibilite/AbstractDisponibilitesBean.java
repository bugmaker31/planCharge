package fr.gouv.agriculture.dal.ct.planCharge.ihm.model.disponibilite;

import fr.gouv.agriculture.dal.ct.ihm.model.AbstractBean;
import fr.gouv.agriculture.dal.ct.ihm.model.BeanException;
import fr.gouv.agriculture.dal.ct.metier.dto.AbstractDTO;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public abstract class AbstractDisponibilitesBean<D extends AbstractDTO, B extends AbstractDisponibilitesBean<D, B, T>, T> extends AbstractBean<D, B> {


    // Fields :

    @NotNull
    final Map<LocalDate, T> calendrier;


    // Constructors:

    public AbstractDisponibilitesBean() {
        super();
        calendrier = new TreeMap<>(); // TreeMap, au lieu de HashMap, juste pour faciliter le débogage en gardant le tri sur la Key (date).
    }

    public AbstractDisponibilitesBean(@NotNull Map<LocalDate, T> calendrier) {
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

    public T get(LocalDate key) {
        return calendrier.get(key);
    }

    public T put(LocalDate key, T value) {
        return calendrier.put(key, value);
    }

    public T remove(LocalDate key) {
        return calendrier.remove(key);
    }

    public void putAll(Map<? extends LocalDate, ? extends T> m) {
        calendrier.putAll(m);
    }

    public void clear() {
        calendrier.clear();
    }

    public Set<LocalDate> keySet() {
        return calendrier.keySet();
    }

    public Collection<T> values() {
        return calendrier.values();
    }

    public Set<Map.Entry<LocalDate, T>> entrySet() {
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

    public T getOrDefault(LocalDate key, T defaultValue) {
        return calendrier.getOrDefault(key, defaultValue);
    }

    public void forEach(BiConsumer<? super LocalDate, ? super T> action) {
        calendrier.forEach(action);
    }

    public void replaceAll(BiFunction<? super LocalDate, ? super T, ? extends T> function) {
        calendrier.replaceAll(function);
    }

    public T putIfAbsent(LocalDate key, T value) {
        return calendrier.putIfAbsent(key, value);
    }

    public boolean remove(LocalDate key, T value) {
        return calendrier.remove(key, value);
    }

    public boolean replace(LocalDate key, T oldValue, T newValue) {
        return calendrier.replace(key, oldValue, newValue);
    }

    public T replace(LocalDate key, T value) {
        return calendrier.replace(key, value);
    }

    public T computeIfAbsent(LocalDate key, Function<? super LocalDate, ? extends T> mappingFunction) {
        return calendrier.computeIfAbsent(key, mappingFunction);
    }

    public T computeIfPresent(LocalDate key, BiFunction<? super LocalDate, ? super T, ? extends T> remappingFunction) {
        return calendrier.computeIfPresent(key, remappingFunction);
    }

    public T compute(LocalDate key, BiFunction<? super LocalDate, ? super T, ? extends T> remappingFunction) {
        return calendrier.compute(key, remappingFunction);
    }

    public T merge(LocalDate key, T value, BiFunction<? super T, ? super T, ? extends T> remappingFunction) {
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

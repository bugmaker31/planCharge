package fr.gouv.agriculture.dal.ct.planCharge.ihm.model.disponibilite;

import fr.gouv.agriculture.dal.ct.ihm.model.AbstractBean;
import fr.gouv.agriculture.dal.ct.ihm.model.BeanException;
import fr.gouv.agriculture.dal.ct.metier.dto.AbstractDTO;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.referentiels.RessourceHumaineBean;
import javafx.beans.property.IntegerProperty;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public class NbrsJoursDAbsenceBean extends AbstractBean<AbstractDTO, NbrsJoursDAbsenceBean> {


    // Fields :

    @NotNull
    private RessourceHumaineBean ressourceHumaineBean;

    @NotNull
    private Map<LocalDate, IntegerProperty> calendrier;


    // Constructors :

    private NbrsJoursDAbsenceBean() {
        super();
        ressourceHumaineBean = new RessourceHumaineBean();
        calendrier = new TreeMap<>(); // TreeMap, au lieu de HashMap, juste pour faciliter le débogage en gardant le tri sur la Key (date).
    }

    public NbrsJoursDAbsenceBean(@NotNull RessourceHumaineBean ressourceHumaineBean, @NotNull Map<LocalDate, IntegerProperty> calendrier) {
        super();
        this.ressourceHumaineBean = ressourceHumaineBean;
        this.calendrier = calendrier;
    }


    // Getters/Setters :

    @NotNull
    public RessourceHumaineBean getRessourceHumaineBean() {
        return ressourceHumaineBean;
    }


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

    public IntegerProperty get(LocalDate key) {
        return calendrier.get(key);
    }

    public IntegerProperty put(LocalDate key, IntegerProperty value) {
        return calendrier.put(key, value);
    }

    public IntegerProperty remove(LocalDate key) {
        return calendrier.remove(key);
    }

    public void putAll(Map<? extends LocalDate, ? extends IntegerProperty> m) {
        calendrier.putAll(m);
    }

    public void clear() {
        calendrier.clear();
    }

    public Set<LocalDate> keySet() {
        return calendrier.keySet();
    }

    public Collection<IntegerProperty> values() {
        return calendrier.values();
    }

    public Set<Map.Entry<LocalDate, IntegerProperty>> entrySet() {
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

    public IntegerProperty getOrDefault(LocalDate key, IntegerProperty defaultValue) {
        return calendrier.getOrDefault(key, defaultValue);
    }

    public void forEach(BiConsumer<? super LocalDate, ? super IntegerProperty> action) {
        calendrier.forEach(action);
    }

    public void replaceAll(BiFunction<? super LocalDate, ? super IntegerProperty, ? extends IntegerProperty> function) {
        calendrier.replaceAll(function);
    }

    public IntegerProperty putIfAbsent(LocalDate key, IntegerProperty value) {
        return calendrier.putIfAbsent(key, value);
    }

    public boolean remove(LocalDate key, IntegerProperty value) {
        return calendrier.remove(key, value);
    }

    public boolean replace(LocalDate key, IntegerProperty oldValue, IntegerProperty newValue) {
        return calendrier.replace(key, oldValue, newValue);
    }

    public IntegerProperty replace(LocalDate key, IntegerProperty value) {
        return calendrier.replace(key, value);
    }

    public IntegerProperty computeIfAbsent(LocalDate key, Function<? super LocalDate, ? extends IntegerProperty> mappingFunction) {
        return calendrier.computeIfAbsent(key, mappingFunction);
    }

    public IntegerProperty computeIfPresent(LocalDate key, BiFunction<? super LocalDate, ? super IntegerProperty, ? extends IntegerProperty> remappingFunction) {
        return calendrier.computeIfPresent(key, remappingFunction);
    }

    public IntegerProperty compute(LocalDate key, BiFunction<? super LocalDate, ? super IntegerProperty, ? extends IntegerProperty> remappingFunction) {
        return calendrier.compute(key, remappingFunction);
    }

    public IntegerProperty merge(LocalDate key, IntegerProperty value, BiFunction<? super IntegerProperty, ? super IntegerProperty, ? extends IntegerProperty> remappingFunction) {
        return calendrier.merge(key, value, remappingFunction);
    }


    // Implementation of AbstractBean :

    @NotNull
    @Override
    public AbstractDTO toDto() throws BeanException {
        return null; // FIXME FDA 2017/08
    }

    @NotNull
    @Override
    public NbrsJoursDAbsenceBean fromDto(@NotNull AbstractDTO dto) throws BeanException {
        return null; // FIXME FDA 2017/08
    }
}

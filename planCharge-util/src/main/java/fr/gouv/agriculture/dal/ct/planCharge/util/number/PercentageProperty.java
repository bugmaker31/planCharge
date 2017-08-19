package fr.gouv.agriculture.dal.ct.planCharge.util.number;

import javafx.beans.InvalidationListener;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class PercentageProperty implements Property<Percentage> {


    // Fields:

    private final Property<Number> floatProperty = new SimpleFloatProperty();


    // Constructors:

    public PercentageProperty(float v) {
        super();
        floatProperty.setValue(v);
    }


    // Implementation of Property<Percentage>:

    public void bind(ObservableValue observable) {
        floatProperty.bind(observable);
    }

    @Override
    public void unbind() {
        floatProperty.unbind();
    }

    @Override
    public boolean isBound() {
        return floatProperty.isBound();
    }

    public void bindBidirectional(Property other) {
        floatProperty.bindBidirectional(other);
    }

    public void unbindBidirectional(Property other) {
        floatProperty.unbindBidirectional(other);
    }

    @Override
    public Object getBean() {
        return floatProperty.getBean();
    }

    @Override
    public String getName() {
        return floatProperty.getName();
    }

    public void addListener(ChangeListener listener) {
        floatProperty.addListener(listener);
    }

    public void removeListener(ChangeListener listener) {
        floatProperty.removeListener(listener);
    }

    @Override
    public Percentage getValue() {
        return new Percentage(floatProperty.getValue().floatValue());
    }

    @Override
    public void addListener(InvalidationListener listener) {
        floatProperty.addListener(listener);
    }

    @Override
    public void removeListener(InvalidationListener listener) {
        floatProperty.removeListener(listener);
    }

    public void setValue(Percentage value) {
        floatProperty.setValue(value);
    }


    // Juste pour faciliter le débogage :
    @Override
    public String toString() {
        return floatProperty.getValue() + "";
    }
}

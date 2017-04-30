/*
package fr.gouv.agriculture.dal.ct;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

*/
/**
 * Cf. https://dzone.com/articles/how-to-inject-property-file-properties-with-cdi
 * <p/>
 * Created by frederic.danna on 30/04/2017.
 *//*

public class PropertyProducer {

    private static final String PROPERTIES_FILE_RSRC_NAME = "/metier-conf.properties";

    private Properties properties;

    private String getPropertyValue(@NotNull String key) {
        if (!this.properties.containsKey(key)) {
            throw new RuntimeException("No property '" + key + "' in resource '" + PROPERTIES_FILE_RSRC_NAME + "'.");
        }
        return this.properties.getProperty(key);
    }

    @Property
    @Produces
    public String produceString(final InjectionPoint ip) {
        return getPropertyValue(getKey(ip));
    }

    @Property
    @Produces
    public int produceInt(final InjectionPoint ip) {
        return Integer.valueOf(getPropertyValue(getKey(ip)));
    }

    @Property
    @Produces
    public boolean produceBoolean(final InjectionPoint ip) {
        return Boolean.valueOf(getPropertyValue(getKey(ip)));
    }

    private String getKey(final InjectionPoint ip) {
        return (ip.getAnnotated().isAnnotationPresent(Property.class)
                &&
                !ip.getAnnotated().getAnnotation(Property.class).value().isEmpty()
        ) ?
                ip.getAnnotated().getAnnotation(Property.class).value()
                : ip.getMember().getName();
    }

    @PostConstruct
    public void init() {
        this.properties = new Properties();
        final InputStream stream = PropertyProducer.class.getResourceAsStream(PROPERTIES_FILE_RSRC_NAME);
        if (stream == null) {
            throw new RuntimeException("No property file named '" + PROPERTIES_FILE_RSRC_NAME + "' in classpath.");
        }
        try {
            this.properties.load(stream);
        } catch (final IOException e) {
            throw new RuntimeException("Can't load properties from resource '" + PROPERTIES_FILE_RSRC_NAME + "'.", e);
        }
    }
}
*/

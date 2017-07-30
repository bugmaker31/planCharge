package fr.gouv.agriculture.dal.ct.kernel;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by frederic.danna on 30/04/2017.
 */
public class ParametresMetiers {

    @SuppressWarnings("HardcodedFileSeparator")
    private static final String PROPERTIES_FILE_RSRC_NAME = "/metier-conf.properties";

    private static ParametresMetiers instance;

    public static ParametresMetiers instance() {
        if (instance == null) {
            instance = new ParametresMetiers();
        }
        return instance;
    }

    private Properties properties= new Properties();

    // 'private' pour empêcher quiconque d'autre d'instancier cette classe (pattern "Factory").
    private ParametresMetiers() {
        super();
    }

    public void init() throws KernelException {
        final InputStream stream = this.getClass().getResourceAsStream(PROPERTIES_FILE_RSRC_NAME);
        if (stream == null) {
            throw new KernelException("No property file named '" + PROPERTIES_FILE_RSRC_NAME + "' in classpath.");
        }
        try {
            this.properties.load(stream);
        } catch (IOException e) {
            throw new KernelException("Can't load properties from resource '" + PROPERTIES_FILE_RSRC_NAME + "'.", e);
        }
    }

    @NotNull
    public String getParametrage(@NotNull String clef) throws KernelException {
        if (!this.properties.containsKey(clef)) {
            throw new KernelException("No property '" + clef + "' in resource '" + PROPERTIES_FILE_RSRC_NAME + "'.");
        }
        if (this.properties.getProperty(clef) == null) {
            throw new KernelException("Property '" + clef + "' is null (in resource '" + PROPERTIES_FILE_RSRC_NAME + "').");
        }
        return this.properties.getProperty(clef);
    }

    @NotNull
    public String getParametrage(@NotNull String clef, @NotNull String defaut) {
        if (!this.properties.containsKey(clef)) {
            return defaut;
        }
        if (this.properties.getProperty(clef) == null) {
            return defaut;
        }
        return this.properties.getProperty(clef);
    }
}

package fr.gouv.agriculture.dal.ct.kernel;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by frederic.danna on 30/04/2017.
 */
public class ParametresApplicatifs {

    private static final String PROPERTIES_FILE_RSRC_NAME = "/metier-conf.properties";

    private static ParametresApplicatifs instance;

    public static ParametresApplicatifs instance() {
        if (instance == null) {
            instance = new ParametresApplicatifs();
        }
        return instance;
    }

    private Properties properties= new Properties();

    // 'private' pour empêcher quiconque d'autre d'instancier cette classe (pattern "Factory").
    private ParametresApplicatifs() {
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

    public String getParametrage(String clef) {
        if (!this.properties.containsKey(clef)) {
            throw new RuntimeException("No property '" + clef + "' in resource '" + PROPERTIES_FILE_RSRC_NAME + "'.");
        }
        if (this.properties.getProperty(clef) == null) {
            throw new RuntimeException("Property '" + clef + "' is null (in resource '" + PROPERTIES_FILE_RSRC_NAME + "').");
        }
        return this.properties.getProperty(clef);
    }
}

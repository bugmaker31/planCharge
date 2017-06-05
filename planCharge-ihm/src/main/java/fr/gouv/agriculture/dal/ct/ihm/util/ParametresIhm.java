package fr.gouv.agriculture.dal.ct.ihm.util;

import fr.gouv.agriculture.dal.ct.kernel.KernelException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by frederic.danna on 30/04/2017.
 */
public class ParametresIhm {

    private static final String PROPERTIES_FILE_RSRC_NAME = "/ihm-conf.properties";

    private static ParametresIhm instance;

    public static ParametresIhm instance() {
        if (instance == null) {
            instance = new ParametresIhm();
        }
        return instance;
    }

    private Properties properties= new Properties();

    // 'private' pour empêcher quiconque d'autre d'instancier cette classe (pattern "Factory").
    private ParametresIhm() {
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

    public String getParametrage(String clef) throws KernelException {
        if (!this.properties.containsKey(clef)) {
            throw new KernelException("No property '" + clef + "' in resource '" + PROPERTIES_FILE_RSRC_NAME + "'.");
        }
        if (this.properties.getProperty(clef) == null) {
            throw new KernelException("Property '" + clef + "' is null (in resource '" + PROPERTIES_FILE_RSRC_NAME + "').");
        }
        return this.properties.getProperty(clef);
    }
}

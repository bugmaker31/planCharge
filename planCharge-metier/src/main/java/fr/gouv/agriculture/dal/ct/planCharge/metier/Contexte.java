package fr.gouv.agriculture.dal.ct.planCharge.metier;

/**
 * Created by frederic.danna on 27/04/2017.
 */
public class Contexte {

    private static Contexte instance;

    public static Contexte instance() {
        if (instance == null) {
            instance = new Contexte();
        }
        return instance;
    }

    private String applicationVersion;

    public String getApplicationVersion() {
        return applicationVersion;
    }

    public void setApplicationVersion(String applicationVersion) {
        this.applicationVersion = applicationVersion;
    }

    // 'private' pour empêcher quiconque d'autre d'instancier cette classe (pattern "Factory").
    private Contexte() {
        super();
    }

}

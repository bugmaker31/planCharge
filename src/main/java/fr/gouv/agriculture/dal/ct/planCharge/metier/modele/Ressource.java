package fr.gouv.agriculture.dal.ct.planCharge.metier.modele;

/**
 * Created by frederic.danna on 25/03/2017.
 */
public class Ressource extends AbstractEntity<String> {

    private final String trigramme;

    public Ressource(String trigramme) {
        this.trigramme = trigramme;
    }

    public String getTrigramme() {
        return trigramme;
    }

    @Override
    public String getIdentity() {
        return getTrigramme();
    }
}

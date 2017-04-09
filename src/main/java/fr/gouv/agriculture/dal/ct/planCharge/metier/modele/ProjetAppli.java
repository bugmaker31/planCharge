package fr.gouv.agriculture.dal.ct.planCharge.metier.modele;

/**
 * Created by frederic.danna on 25/03/2017.
 */
public class ProjetAppli extends AbstractEntity<String> {

    private final String code;

    public ProjetAppli(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }


    @Override
    public String getIdentity() {
        return getCode();
    }
}

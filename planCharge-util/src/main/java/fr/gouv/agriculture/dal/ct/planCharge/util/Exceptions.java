package fr.gouv.agriculture.dal.ct.planCharge.util;

/**
 * Created by frederic.danna on 19/05/2017.
 *
 * @author frederic.danna
 */
public class Exceptions {

    public static Throwable causeOriginelle(Throwable e) {
        return ((e.getCause() == null) ? e : causeOriginelle(e.getCause()));
    }
}

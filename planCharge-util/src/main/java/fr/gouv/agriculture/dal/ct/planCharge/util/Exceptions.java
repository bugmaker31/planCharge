package fr.gouv.agriculture.dal.ct.planCharge.util;

/**
 * Created by Fred on 19/05/2017.
 *
 * @author Fred
 */
public class Exceptions {

    public static Throwable causeOriginelle(Throwable e) {
        return ((e.getCause() == null) ? e : causeOriginelle(e.getCause()));
    }
}

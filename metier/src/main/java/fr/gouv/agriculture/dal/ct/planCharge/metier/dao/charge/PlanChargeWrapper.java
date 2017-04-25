package fr.gouv.agriculture.dal.ct.planCharge.metier.dao.charge;

import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.charge.Planifications;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by frederic.danna on 22/04/2017.
 */
// Cf. http://code.makery.ch/library/javafx-8-tutorial/fr/part5/
@XmlRootElement(name = "planCharge")
public class PlanChargeWrapper {

    private Planifications planifications;

    @XmlElement(name = "planifications")
    public Planifications getPlanifications() {
        return planifications;
    }

    public void setPlanifications(Planifications planifications) {
        this.planifications = planifications;
    }

}

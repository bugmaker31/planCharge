@XmlJavaTypeAdapter(type = LocalDate.class, value = LocalDateXmlAdapter.class)

package fr.gouv.agriculture.dal.ct.planCharge.metier.dao.tache.xml;

import fr.gouv.agriculture.dal.ct.planCharge.util.xml.LocalDateXmlAdapter;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDate;
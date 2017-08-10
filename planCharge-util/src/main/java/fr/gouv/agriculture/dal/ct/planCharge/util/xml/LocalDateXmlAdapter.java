package fr.gouv.agriculture.dal.ct.planCharge.util.xml;

//Cf. https://stackoverflow.com/questions/36156741/marshalling-localdate-using-jaxb

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.time.LocalDate;

public class LocalDateXmlAdapter extends XmlAdapter<String, LocalDate> {

    public LocalDate unmarshal(String v) throws Exception {
        return LocalDate.parse(v);
    }

    public String marshal(LocalDate v) throws Exception {
        return v.toString();
    }
}

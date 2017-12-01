package fr.gouv.agriculture.dal.ct.planCharge.metier.constante;

public interface ResponsablePriorisation {

    enum ResponsablesPriorisation implements ResponsablePriorisation {

        // Titulaire :
        EAU,
        // Adjoint du titulaire :
        XPO,
        // Remplaçants :
        SCR,
        NBR,
        TSA;

        public String getTrigramme() {
            return name();
        }
    }

    String getTrigramme();
}

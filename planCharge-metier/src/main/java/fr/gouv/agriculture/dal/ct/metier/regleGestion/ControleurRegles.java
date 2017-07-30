package fr.gouv.agriculture.dal.ct.metier.regleGestion;

import fr.gouv.agriculture.dal.ct.metier.MetierException;
import fr.gouv.agriculture.dal.ct.metier.modele.ModeleException;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dto.PlanChargeDTO;
import fr.gouv.agriculture.dal.ct.planCharge.metier.regleGestion.RegleGestionPackage;
import fr.gouv.agriculture.dal.ct.planCharge.util.reflect.InheritageFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;

import javax.validation.constraints.NotNull;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ControleurRegles {

    private static final Logger LOGGER = LoggerFactory.getLogger(ControleurRegles.class);

    private static boolean REGLES_INITIALISEES = false;

    private static Set<BeanDefinition> reglesGestionBDs = null;

    public static List<ViolationRegleGestion> violations(@NotNull PlanChargeDTO planChargeDTO) throws MetierException {
        List<ViolationRegleGestion> violationsRegles = new ArrayList<>();

        // TODO FDA 2017/07 Optimiser pour ne plus avoir  : passer un Observable plutôt ?
        initReglesGestion(planChargeDTO);

/*
        List<Controlable> instancesControlables = instancesControlables(planCharge);
        for (Controlable instanceControlable : instancesControlables) {
            violationsRegles.addAll(instanceControlable.controlerReglesGestion());
        }
*/
        violationsRegles = planChargeDTO.controlerReglesGestion();

        return violationsRegles;
    }

    private static void initReglesGestion(@NotNull PlanChargeDTO planChargeDTO) throws ModeleException {
        LOGGER.debug("Chargement des règles de gestion :");
//        List<RegleGestion> reglesGestion = new ArrayList<>();
        try {
            Set<BeanDefinition> reglesGestionBDs = getReglesGestionBDs();
            LOGGER.debug(reglesGestionBDs.size() + " règles de gestion trouvées.");

            for (BeanDefinition regleGestionBD : reglesGestionBDs) {
                LOGGER.debug("- Règle de gestion : '" + regleGestionBD.getBeanClassName() + "'");
                Class<? extends RegleGestion> classeRegleGestion =
                        (Class<? extends RegleGestion>) Class.forName(regleGestionBD.getBeanClassName());

                Field instanceField = classeRegleGestion.getDeclaredField(RegleGestion.INSTANCE_FIELD_NAME);
                if (instanceField == null) {
                    throw new ModeleException("La règle de gestion '" + regleGestionBD.getBeanClassName() + "' ne possède pas d'attribut nommé '" + RegleGestion.INSTANCE_FIELD_NAME + "'.");
                }

                RegleGestion regleGestion = (RegleGestion) instanceField.get(null);
                regleGestion.setPlanCharge(planChargeDTO);

//                reglesGestion.add(regleGestion);
            }
        } catch (Exception e) {
            throw new ModeleException("Impossible de déterminer dynamiquement les règles de gestion.", e);
        }
        LOGGER.debug("Règles de gestion chargées.");
//        return reglesGestion;
    }

    private static Set<BeanDefinition> getReglesGestionBDs() {
        if (reglesGestionBDs == null) {
            ClassPathScanningCandidateComponentProvider regleGestionCPSCPP = new ClassPathScanningCandidateComponentProvider(false);
            // On considère toutes les classes qui héritent de RegleGestion :
            regleGestionCPSCPP.addIncludeFilter(new InheritageFilter(RegleGestion.class));
            Package pack = RegleGestionPackage.class.getPackage();
            reglesGestionBDs = regleGestionCPSCPP.findCandidateComponents(pack.getName());
            LOGGER.debug(reglesGestionBDs.size() + " règles de gestion trouvées.");
        }
        return reglesGestionBDs;
    }

/*
    @NotNull
    private static List<Controlable> instancesControlables(@NotNull PlanCharge planCharge) throws MetierException {
        List<Controlable> instancesControlables = new ArrayList<>();
        instancesControlables.addAll(instancesControlables((Object) planCharge, MetierException.class.getPackage()));
        return instancesControlables;
    }

    private static Collection<? extends Controlable> instancesControlables(Object o, @NotNull Package packageFilter) throws MetierException {
        List<Controlable> instancesControlables = new ArrayList<>();

*/
/*
        Class<?> cls = o.getClass();
        if (!cls.getPackage().getName().startsWith(packageFilter.getName() + ".")) {
            return instancesControlables;
        }
*//*


        if (o instanceof Controlable) {
            Controlable c = (Controlable) o;
            instancesControlables.add(c);
        }

        if (o == null) {
            return instancesControlables;
        }

        Class<?> cls = o.getClass();

        if (Collection.class.isAssignableFrom(cls)) {
            Collection coll = (Collection) o;
            for (Object elt : coll) {
                Collection<? extends Controlable> instContrElt = instancesControlables(elt, packageFilter);
                instancesControlables.addAll(instContrElt);
            }

        } else if (Map.class.isAssignableFrom(cls)) {
            Map map = (Map) o;
            for (Object value : map.values()) {
                Collection<? extends Controlable> instContrElt = instancesControlables(value, packageFilter);
                instancesControlables.addAll(instContrElt);
            }

        } else {

            Method[] methods = cls.getDeclaredMethods();
            for (Method method : methods) {
                // Seulement les Getters :
                if (!method.getName().startsWith("get") || (method.getParameterCount() != 0)) {
                    continue;
                }
                // Seulement les méthodes (getters) accessibles :
                int getterModifiers = method.getModifiers();
                if (Modifier.isAbstract(getterModifiers)
                        || Modifier.isProtected(getterModifiers)
                        || Modifier.isPrivate(getterModifiers)
                        ) {
                    continue;
                }

                Collection<? extends Controlable> instContrGetterField;
                try {
                    Object child = method.invoke(o, (Object[]) null);
                    instContrGetterField = instancesControlables(child, packageFilter);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new MetierException("Impossible de déterminer les instances controlables de l'objet.", e);
                }
                instancesControlables.addAll(instContrGetterField);
            }
        }

        return instancesControlables;
    }
*/

}

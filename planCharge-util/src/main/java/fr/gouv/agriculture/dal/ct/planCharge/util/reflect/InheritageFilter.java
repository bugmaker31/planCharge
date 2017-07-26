package fr.gouv.agriculture.dal.ct.planCharge.util.reflect;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.filter.AbstractTypeHierarchyTraversingFilter;
import org.springframework.util.ClassUtils;

import java.lang.annotation.Annotation;

public class InheritageFilter<AC extends Annotation> extends AbstractTypeHierarchyTraversingFilter {

    private static final Logger LOG = LoggerFactory.getLogger(InheritageFilter.class);

    private Class<?> motherClass;
    private Class<AC> annotationClass;
//    private Object[] filteredAnnotationTypeClasses;

    public InheritageFilter(Class<?> motherClass) {
        super(true, false);
        this.motherClass = motherClass;
    }

    public InheritageFilter(Class<?> motherClass, Class<AC> annotationClass/*, Object[] filteredAnnotationTypeClasses*/) {
        this(motherClass);
        this.annotationClass = annotationClass;
//        this.filteredAnnotationTypeClasses = filteredAnnotationTypeClasses;
    }

    @Override
    protected boolean matchSelf(MetadataReader metadataReader) {
        ClassMetadata classMetadata = metadataReader.getClassMetadata();
        String className = classMetadata.getClassName();
        return inherits(className) && (annotationClass == null ? true : hasAnnotation(className));
    }

    @Override
    protected Boolean matchSuperClass(String superClassName) {
        return inherits(superClassName) && (annotationClass == null ? true : hasAnnotation(superClassName));
    }

    @Override
    protected Boolean matchInterface(String interfaceName) {
        return inherits(interfaceName) && (annotationClass == null ? true : hasAnnotation(interfaceName));
    }

    private Boolean inherits(String typeName) {
        try {
            Class<?> typeClazz = ClassUtils.forName(typeName, getClass().getClassLoader());
            return motherClass.isAssignableFrom(typeClazz);
        } catch (Throwable ex) {
            LOG.error("Class not regularly loadable - can't determine a match that way", ex);
            return false;
        }
    }

    private Boolean hasAnnotation(String typeName) {
        try {
            Class<?> typeClazz = ClassUtils.forName(typeName, getClass().getClassLoader());
            AC annotation = typeClazz.getAnnotation(annotationClass);
            if (annotation == null) {
                return false;
            }
//            FamilleDeControle famille = annotation.famille();
//            for (Object filteredAnnotationTypeClass : filteredAnnotationTypeClasses) {
//                if (filteredAnnotationTypeClass.equals(famille)) {
//                    return false;
//                }
//            }
            return true;
        } catch (Throwable ex) {
            LOG.error("Class not regularly loadable - can't determine a match that way", ex);
            return false;
        }
    }

}

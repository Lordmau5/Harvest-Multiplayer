package com.lordmau5.harvest.shared.serialization.annotations;

import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;

public abstract class AnnotationHelper {
    public static int getClassId(Class<?> clazz) {
        Annotation annotation = clazz.getAnnotation(ClassId.class);
        if (annotation != null)
            return ((ClassId) annotation).id();
        return 0;
    }

    public static boolean hasAnnotation(AccessibleObject member, Class<? extends Annotation> annotationClass) {
        Annotation annotation = member.getAnnotation(annotationClass);
        return annotation != null;
    }
}

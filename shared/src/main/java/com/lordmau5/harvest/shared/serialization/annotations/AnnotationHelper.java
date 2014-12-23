package com.lordmau5.harvest.shared.serialization.annotations;

import java.lang.annotation.Annotation;

public abstract class AnnotationHelper {
    public static int getClassId(Class<?> clazz) {
        Annotation annotation = clazz.getAnnotation(ClassId.class);
        if (annotation != null)
            return ((ClassId) annotation).id();
        return 0;
    }
}
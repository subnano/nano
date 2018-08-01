package io.nano.core.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Mark Wardell
 */
public final class TestUtil {

    private TestUtil() {
        // can't touch this
    }

    public static void assertUtilityClassWellDefined(final Class<?> clazz) throws Exception {
        assertClassFinal(clazz);
        assertClassHasOnlyOneConstructor(clazz);
        assertConstructorIsPrivate(clazz);
        assertClassHasOnlyStaticMethods(clazz);
    }

    private static void assertClassFinal(Class<?> clazz) {
        assertThat(Modifier.isFinal(clazz.getModifiers())).as("Class must be final").isTrue();
    }

    private static void assertClassHasOnlyOneConstructor(Class<?> clazz) {
        assertThat(clazz.getDeclaredConstructors().length).as("There must be only one constructor").isEqualTo(1);
    }

    private static void assertConstructorIsPrivate(Class<?> clazz) throws Exception {
        final Constructor<?> constructor = clazz.getDeclaredConstructor();
        if (constructor.isAccessible() || !Modifier.isPrivate(constructor.getModifiers())) {
            throw new IllegalArgumentException("Constructor is not private");
        }
        constructor.setAccessible(true);
        constructor.newInstance();
        constructor.setAccessible(false);
    }

    private static void assertClassHasOnlyStaticMethods(Class<?> clazz) {
        for (final Method method : clazz.getMethods()) {
            if (!Modifier.isStatic(method.getModifiers())
                    && method.getDeclaringClass().equals(clazz)) {
                throw new IllegalArgumentException("Class contains a non-static method:" + method);
            }
        }
    }
}

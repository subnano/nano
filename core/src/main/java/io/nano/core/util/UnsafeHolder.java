package io.nano.core.util;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * @author Mark Wardell
 */
public final class UnsafeHolder {

    public static final Unsafe UNSAFE;

    static {
        try {
            Field unsafeField = Unsafe.class.getDeclaredField("theUnsafe");
            unsafeField.setAccessible(true);
            UNSAFE = (Unsafe)unsafeField.get(null);
        } catch (Exception e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private UnsafeHolder() {
        // can't touch this
    }

}

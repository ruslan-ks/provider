package com.provider.functions;

import java.util.Arrays;

public class Util {
    private Util() {}

    public static boolean contains(String[] array, String value) {
        if (array == null) {
            return false;
        }
        return Arrays.asList(array).contains(value);
    }
}

package com.provider.constants;

public class Regex {
    private Regex() {}

    public static final String REGULAR_TEXT = "^[a-zA-Zа-яА-Я0-9 .,;!?-]+$";

    public static final String FILE_NAME = "^[a-zA-Z0-9_.]+$";

    public static final String NAME = "^[A-Za-z0-9-]+$";

    public static final String PHONE = "^([1-9][0-9])?[0-9]{6,10}$";

    public static final String LOGIN = "^[a-zA-Z0-9_]{4,}$";
}

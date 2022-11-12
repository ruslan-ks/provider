package com.provider.constants;

public class Regex {
    private Regex() {}

    /**
     * Any kind of regular text regex
     */
    public static final String REGULAR_TEXT = "^[a-zA-Zа-яА-ЯіїґєІЇҐЄ0-9 .,;!?-]+$";

    /**
     * File name regex - used when saving the file on the server side
     */
    public static final String FILE_NAME = "^[a-zA-Z0-9_.]+$";

    /**
     * User name regex
     */
    public static final String NAME = "^[a-zA-Zа-яА-ЯіїґєІЇҐЄ0-9-]+$";

    /**
     * User phone regex
     */
    public static final String PHONE = "^([1-9][0-9])?[0-9]{6,10}$";

    /**
     * User login regex
     */
    public static final String LOGIN = "^[a-zA-Z0-9_]{4,}$";
}

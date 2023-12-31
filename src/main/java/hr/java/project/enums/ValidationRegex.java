package hr.java.project.enums;

/**
 * Predstavlja regularne izraze za provjeru valjanosti.
 */
public enum ValidationRegex {
    VALID_WEB_ADDRESS("www\\.[A-Za-z0-9]+\\.[A-Za-z]+"),
    VALID_POSTAL_CODE("[0-9]+"),

    VALID_LOCAL_DATE_REGEX ("dd.MM.yyyy."),
    VALID_LOCAL_DATE_TIME_REGEX ("dd.MM.yyyy HH:mm:ss"),

    VALID_MEMBER_ID_REGEX ("\\d{5}");

    private final String regex;

    ValidationRegex(String regex) {
        this.regex = regex;
    }

    public String getRegex() {
        return regex;
    }
}
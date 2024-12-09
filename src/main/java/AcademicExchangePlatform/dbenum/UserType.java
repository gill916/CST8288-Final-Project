package AcademicExchangePlatform.dbenum;

/**
 * Enum representing the type of user.
 */
public enum UserType {
    PROFESSIONAL("Professional"),
    INSTITUTION("Institution"),
    ADMIN("Administrator");

    private final String value;

    UserType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static UserType fromValue(String value) {
        for (UserType type : UserType.values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid user type: " + value);
    }

    @Override
    public String toString() {
        return name();
    }
}

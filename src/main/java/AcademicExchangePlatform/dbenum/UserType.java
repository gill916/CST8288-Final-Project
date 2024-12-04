package AcademicExchangePlatform.dbenum;


public enum UserType {
    ADMIN("Admin"),
    INSTITUTION("Institution"),
    PROFESSIONAL("Professional");

    private final String value;

    UserType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static UserType fromValue(String value) {
        for (UserType userType : UserType.values()) {
            if (userType.getValue().equalsIgnoreCase(value)) {
                return userType;
            }
        }
        throw new IllegalArgumentException("Invalid UserType: " + value);
    }
}

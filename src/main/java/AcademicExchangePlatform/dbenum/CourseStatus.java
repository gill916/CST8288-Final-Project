package AcademicExchangePlatform.dbenum;

public enum CourseStatus {
    ACTIVE("Active"),
    INACTIVE("Inactive"),
    FILLED("Filled"),
    EXPIRED("Expired");

    private final String value;

    CourseStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return name();
    }
} 
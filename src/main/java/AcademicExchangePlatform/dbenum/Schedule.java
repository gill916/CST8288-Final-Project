package AcademicExchangePlatform.dbenum;

public enum Schedule {
    MORNING,
    AFTERNOON,
    EVENING;

    @Override
    public String toString() {
        return name();
    }
}

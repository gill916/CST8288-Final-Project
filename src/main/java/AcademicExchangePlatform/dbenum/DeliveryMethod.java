package AcademicExchangePlatform.dbenum;

public enum DeliveryMethod {
    IN_PERSON,
    REMOTE,
    HYBRID;

    @Override
    public String toString() {
        return name();
    }
}

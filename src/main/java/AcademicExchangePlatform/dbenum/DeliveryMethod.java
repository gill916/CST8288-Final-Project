/**
 * Author: Jiajun Cai
 * Student Number: 041127296
 */
package AcademicExchangePlatform.dbenum;

/**
 * Enum representing the method of delivery for a course.
 */
public enum DeliveryMethod {
    IN_PERSON,
    REMOTE,
    HYBRID;

    @Override
    public String toString() {
        return name();
    }
}

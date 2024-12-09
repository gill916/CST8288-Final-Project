/**
 * Author: Jiajun Cai
 * Student Number: 041127296
 */
package AcademicExchangePlatform.dbenum;

/**
 * Enum representing the schedule of a course.
 */
public enum Schedule {
    MORNING,
    AFTERNOON,
    EVENING;

    @Override
    public String toString() {
        return name();
    }
}

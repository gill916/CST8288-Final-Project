/**
 * Author: Jiajun Cai
 * Student Number: 041127296
 */
package AcademicExchangePlatform.dbenum;

/**
 * Enum representing the status of a course application.
 */
public enum ApplicationStatus {
    
    PENDING,
    ACCEPTED,
    REJECTED,
    WITHDRAWN;
    
    /**
     * Returns the string representation of the application status.
     * @return The string representation of the application status
     */
    @Override
    public String toString() {
        return name();
    }
} 
/**
 * Author: Jiajun Cai
 * Student Number: 041127296
 */
package AcademicExchangePlatform.dao;

import AcademicExchangePlatform.model.CourseApplication;
import AcademicExchangePlatform.dbenum.ApplicationStatus;
import java.util.List;

/**
 * Data Access Object interface for managing course applications in the Academic Exchange Platform.
 * Provides methods to create, update, retrieve, and manage course applications.
 */
public interface CourseApplicationDAO {
    /**
     * Creates a new course application in the database.
     * @param application The CourseApplication object containing application details
     * @return true if creation was successful, false otherwise
     */
    boolean createApplication(CourseApplication application);

    /**
     * Updates the status of an existing course application.
     * @param applicationId The ID of the application to update
     * @param status The new ApplicationStatus to set
     * @param institutionId The ID of the institution making the update
     * @return true if update was successful, false otherwise
     */
    boolean updateApplicationStatus(int applicationId, ApplicationStatus status, int institutionId);

    /**
     * Retrieves a specific course application by its ID.
     * @param applicationId The ID of the application to retrieve
     * @return The CourseApplication object if found, null otherwise
     */
    CourseApplication getApplicationById(int applicationId);

    /**
     * Retrieves all applications for a specific course.
     * @param courseId The ID of the course
     * @return List of CourseApplication objects
     */
    List<CourseApplication> getApplicationsByCourse(int courseId);

    /**
     * Retrieves all applications submitted by a specific professional.
     * @param professionalId The ID of the academic professional
     * @return List of CourseApplication objects
     */
    List<CourseApplication> getApplicationsByProfessional(int professionalId);

    /**
     * Withdraws a course application.
     * @param applicationId The ID of the application to withdraw
     * @return true if withdrawal was successful, false otherwise
     */
    boolean withdrawApplication(int applicationId);

    /**
     * Retrieves all applications for courses at a specific institution.
     * @param institutionId The ID of the academic institution
     * @return List of CourseApplication objects
     */
    List<CourseApplication> getAllInstitutionApplications(int institutionId);

    /**
     * Checks if a professional has an existing application for a course.
     * @param professionalId The ID of the academic professional
     * @param courseId The ID of the course
     * @return true if an application exists, false otherwise
     */
    boolean hasExistingApplication(int professionalId, int courseId);

    /**
     * Gets the total number of applications for a specific course.
     * @param courseId The ID of the course
     * @return The number of applications
     */
    int getApplicationCount(int courseId);

    /**
     * Updates an existing course application.
     * @param application The CourseApplication object containing updated details
     * @return true if update was successful, false otherwise
     */
    boolean updateApplication(CourseApplication application);
} 
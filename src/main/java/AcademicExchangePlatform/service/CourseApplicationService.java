/**
 * Author: Jiajun Cai
 * Student Number: 041127296
 */
package AcademicExchangePlatform.service;

import AcademicExchangePlatform.dao.CourseApplicationDAO;
import AcademicExchangePlatform.dao.CourseApplicationDAOImpl;
import AcademicExchangePlatform.dao.CourseDAO;
import AcademicExchangePlatform.dao.CourseDAOImpl;
import AcademicExchangePlatform.model.Course;
import AcademicExchangePlatform.model.CourseApplication;
import AcademicExchangePlatform.model.AcademicProfessional;
import AcademicExchangePlatform.dbenum.ApplicationStatus;
import AcademicExchangePlatform.dbenum.CourseStatus;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * Service class for managing course applications.
 * Provides methods for applying for courses, withdrawing applications, updating application status,
 * retrieving applications by course or professional, and checking application eligibility.
 */
public class CourseApplicationService {
    private static CourseApplicationService instance;
    private final CourseApplicationDAO applicationDAO;
    private final CourseDAO courseDAO;

    // Private constructor for Singleton pattern
    private CourseApplicationService() {
        this.applicationDAO = new CourseApplicationDAOImpl();
        this.courseDAO = new CourseDAOImpl();
    }

    // Singleton instance
    public static CourseApplicationService getInstance() {
        if (instance == null) {
            instance = new CourseApplicationService();
        }
        return instance;
    }

    /**
     * Allows an Academic Professional to apply for a course.
     * @param application The CourseApplication object containing application details.
     * @param professional The AcademicProfessional applying for the course.
     * @return true if the application was successful, false otherwise.
     */
    public boolean applyForCourse(CourseApplication application, AcademicProfessional professional) {
        // Check if a pending application already exists
        List<CourseApplication> existingApplications =
                applicationDAO.getApplicationsByProfessional(professional.getUserId());

        boolean hasExistingApplication = existingApplications.stream()
                .anyMatch(app -> app.getCourseId() == application.getCourseId() &&
                        app.getStatus() == ApplicationStatus.PENDING);

        if (hasExistingApplication) {
            return false;
        }

        Course course = courseDAO.getCourseById(application.getCourseId());
        if (!canApplyToCourse(professional, course)) {
            if (!professional.isProfileComplete()) {
                NotificationService.getInstance().notifyProfileIncomplete(professional.getUserId());
            }
            return false;
        }

        boolean created = applicationDAO.createApplication(application);
        if (created) {
            NotificationService.getInstance().notifyNewApplication(application, course);
        }
        return created;
    }

    /**
     * Allows an Academic Professional to withdraw their application.
     * @param applicationId The ID of the application to withdraw.
     * @param professionalId The ID of the Academic Professional withdrawing the application.
     * @return true if the withdrawal was successful, false otherwise.
     */
    public boolean withdrawApplication(int applicationId, int professionalId) {
        CourseApplication application = applicationDAO.getApplicationById(applicationId);
        if (application == null || application.getProfessionalId() != professionalId ||
                application.getStatus() != ApplicationStatus.PENDING) {
            return false;
        }

        return applicationDAO.withdrawApplication(applicationId);
    }

    /**
     * Updates the status of a course application (e.g., ACCEPTED, REJECTED).
     * @param applicationId The ID of the application.
     * @param status The new status to set.
     * @param institutionId The ID of the institution processing the application.
     * @return true if the update was successful, false otherwise.
     */
    public boolean updateApplicationStatus(int applicationId, ApplicationStatus status, int institutionId) {
        CourseApplication application = applicationDAO.getApplicationById(applicationId);
        if (application == null) {
            return false;
        }

        Course course = courseDAO.getCourseById(application.getCourseId());
        if (course == null || course.getInstitutionId() != institutionId) {
            return false;
        }

        // Set the decision date when updating status
        application.setDecisionDate(new Date());
        application.setStatus(status);

        return applicationDAO.updateApplication(application);
    }

    /**
     * Retrieves all applications for a specific course.
     * @param courseId The ID of the course.
     * @param institutionId The ID of the institution managing the course.
     * @return A list of CourseApplication objects, or null if validation fails.
     */
    public List<CourseApplication> getApplicationsByCourse(int courseId, int institutionId) {
        Course course = courseDAO.getCourseById(courseId);
        if (course == null || course.getInstitutionId() != institutionId) {
            return null;
        }
        return applicationDAO.getApplicationsByCourse(courseId);
    }

    /**
     * Retrieves all applications submitted by a specific Academic Professional.
     * @param professionalId The ID of the Academic Professional.
     * @return A list of CourseApplication objects.
     */
    public List<CourseApplication> getApplicationsByProfessional(int professionalId) {
        return applicationDAO.getApplicationsByProfessional(professionalId);
    }

    /**
     * Checks if an Academic Professional can apply to a course.
     * @param professional The AcademicProfessional object.
     * @param course The Course object.
     * @return true if the application is allowed, false otherwise.
     */
    private boolean canApplyToCourse(AcademicProfessional professional, Course course) {
        if (course == null || !professional.isProfileComplete()) {
            return false;
        }

        Date now = new Date();
        return course.getStatus() == CourseStatus.ACTIVE &&
                course.getApplicationDeadline().after(now);
    }

    /**
     * Retrieves a course application by its ID.
     * @param applicationId The ID of the application.
     * @return The CourseApplication object, or null if not found.
     */
    public CourseApplication getApplicationById(int applicationId) {
        return applicationDAO.getApplicationById(applicationId);
    }

    /**
     * Retrieves all applications for an institution's courses.
     * @param institutionId The ID of the institution.
     * @return A list of CourseApplication objects.
     */
    public List<CourseApplication> getAllInstitutionApplications(int institutionId) {
        if (institutionId <= 0) {
            return null;
        }
        return applicationDAO.getAllInstitutionApplications(institutionId);
    }

    public Map<String, Integer> getApplicationStatistics(int professionalId) {
        List<CourseApplication> applications = applicationDAO.getApplicationsByProfessional(professionalId);
        Map<String, Integer> stats = new HashMap<>();
        
        stats.put("pending", 0);
        stats.put("accepted", 0);
        stats.put("rejected", 0);
        
        for (CourseApplication app : applications) {
            switch (app.getStatus()) {
                case PENDING:
                    stats.put("pending", stats.get("pending") + 1);
                    break;
                case ACCEPTED:
                    stats.put("accepted", stats.get("accepted") + 1);
                    break;
                case REJECTED:
                    stats.put("rejected", stats.get("rejected") + 1);
                    break;
            }
        }
        
        return stats;
    }
}

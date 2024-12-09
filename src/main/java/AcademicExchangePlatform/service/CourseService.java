/**
 * Author: Jiajun Cai
 * Student Number: 041127296
 */
package AcademicExchangePlatform.service;

import AcademicExchangePlatform.dao.CourseDAO;
import AcademicExchangePlatform.dao.CourseDAOImpl;
import AcademicExchangePlatform.model.Course;
import AcademicExchangePlatform.model.AcademicProfessional;
import AcademicExchangePlatform.dbenum.CourseStatus;
import AcademicExchangePlatform.dbenum.DeliveryMethod;
import AcademicExchangePlatform.model.CourseSearchCriteria;
import AcademicExchangePlatform.service.UserService;
import AcademicExchangePlatform.model.NotificationObserverImpl;

import java.util.List;
import java.util.Date;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Set;

/**
 * Service class for managing course operations.
 * Handles business logic for course creation, updates, and search functionality.
 * Implements the Singleton pattern and manages course-related notifications.
 */
public class CourseService {
    private static CourseService instance;
    private final CourseDAO courseDAO;
    private final NotificationService notificationService;

    private CourseService() {
        this.courseDAO = new CourseDAOImpl();
        this.notificationService = NotificationService.getInstance();
        notificationService.registerObserver(new NotificationObserverImpl("NEW_COURSE"));
    }

    /**
     * Gets the singleton instance of CourseService.
     * @return The CourseService instance
     */
    public static CourseService getInstance() {
        if (instance == null) {
            instance = new CourseService();
        }
        return instance;
    }

    /**
     * Creates a new course and notifies potential applicants.
     * @param course The Course object to create
     * @return true if creation was successful, false otherwise
     */
    public boolean createCourse(Course course) {
        try {
            if (!validateCourse(course)) {
                return false;
            }
            course.setStatus(CourseStatus.ACTIVE);
            courseDAO.createCourse(course);
            List<AcademicProfessional> professionals = UserService.getInstance().getAllProfessionals();
            for (AcademicProfessional professional : professionals) {
                notificationService.notifyObservers(
                    "New course available: " + course.getCourseTitle(),
                    professional.getUserId(),
                    "NEW_COURSE",
                    String.valueOf(course.getCourseId())
                );
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Updates an existing course's information.
     * @param course The Course object containing updated information
     * @return true if update was successful, false otherwise
     */
    public boolean updateCourse(Course course) {
        try {
            if (!validateCourse(course)) {
                return false;
            }
            courseDAO.updateCourse(course);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Course> getCoursesByInstitution(int institutionId) {
        System.out.println("DEBUG: Fetching courses for institution ID: " + institutionId);
        try {
            return courseDAO.getCoursesByInstitution(institutionId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Course getCourseById(int courseId) {
        try {
            return courseDAO.getCourseById(courseId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Validates course fields for completeness and correctness.
     * @param course The Course object to validate
     * @return true if all required fields are valid, false otherwise
     */
    private boolean validateCourse(Course course) {
        return course != null &&
               course.getCourseTitle() != null && !course.getCourseTitle().isEmpty() &&
               course.getCourseCode() != null && !course.getCourseCode().isEmpty() &&
               course.getTerm() != null && !course.getTerm().isEmpty() &&
               course.getSchedule() != null &&
               course.getDeliveryMethod() != null &&
               course.getOutline() != null && !course.getOutline().isEmpty() &&
               course.getCompensation() != null && course.getCompensation().compareTo(BigDecimal.ZERO) > 0 &&
               course.getApplicationDeadline() != null;
    }

    /**
     * Updates a course's status and sends notifications.
     * @param courseId The ID of the course
     * @param status The new CourseStatus to set
     * @return true if update was successful, false otherwise
     */
    public boolean updateCourseStatus(int courseId, CourseStatus status) {
        Course course = courseDAO.getCourseById(courseId);
        if (course == null) return false;
        
        String oldStatus = course.getStatus().toString();
        course.setStatus(status);
        boolean updated = courseDAO.updateCourse(course);
        
        if (updated) {
            NotificationService.getInstance().notifyCourseStatusChange(
                course, 
                oldStatus, 
                status.toString()
            );
        }
        return updated;
    }

    /**
     * Checks if a professional can apply to a specific course.
     * Validates profile completion and application deadline.
     * @param professional The AcademicProfessional object
     * @param course The Course object
     * @return true if application is allowed, false otherwise
     */
    public boolean canApplyToCourse(AcademicProfessional professional, Course course) {
        System.out.println("Debug - Checking if can apply to course");
        System.out.println("Debug - Professional complete: " + professional.isProfileComplete());
        System.out.println("Debug - Course status: " + course.getStatus());
        System.out.println("Debug - Application deadline: " + course.getApplicationDeadline());
        
        if (!professional.isProfileComplete()) {
            return false;
        }
        
        Date now = new Date();
        return course.getStatus() == CourseStatus.ACTIVE && 
               course.getApplicationDeadline().after(now);
    }

    /**
     * Searches for courses based on provided criteria.
     * @param criteria The CourseSearchCriteria object containing search parameters
     * @return List of courses matching the criteria
     */
    public List<Course> searchCourses(CourseSearchCriteria criteria) {
        // Validate criteria
        if (criteria == null) {
            criteria = new CourseSearchCriteria();
        }

        // Perform search
        return courseDAO.searchCourses(criteria);
    }

    public Set<String> getAvailableTerms() {
        return courseDAO.getAvailableTerms();
    }

    /**
     * Gets recommended courses for a professional based on their expertise.
     * @param professionalId The ID of the professional
     * @return List of recommended courses
     */
    public List<Course> getRecommendedCourses(int professionalId) {
        // Get professional's expertise and qualifications
        AcademicProfessional professional = UserService.getInstance().getProfessionalById(professionalId);
        if (professional == null) {
            return Collections.emptyList();
        }

        // Create search criteria based on professional's profile
        CourseSearchCriteria criteria = new CourseSearchCriteria();
        criteria.setActiveOnly(true);
        criteria.setOpenOnly(true);
        
        // Search using expertise keywords
        String expertiseKeywords = String.join(" ", professional.getExpertise());
        criteria.setKeyword(expertiseKeywords);
        
        return courseDAO.searchCourses(criteria);
    }
}

package AcademicExchangePlatform.dao;
import AcademicExchangePlatform.dbenum.CourseStatus;
import AcademicExchangePlatform.model.Course;
import AcademicExchangePlatform.model.CourseSearchCriteria;
import java.util.List;
import java.util.Set;

/**
 * Data Access Object interface for managing courses in the Academic Exchange Platform.
 * Provides methods to create, update, retrieve, and manage courses.
 */
public interface CourseDAO {
    /**
     * Creates a new course in the database.
     * @param course The Course object to create
     * @return true if creation was successful, false otherwise
     */
    boolean createCourse(Course course);

    /**
     * Updates an existing course.
     * @param course The Course object containing updated details
     * @return true if update was successful, false otherwise
     */
    boolean updateCourse(Course course);

    /**
     * Deletes a specific course.
     * @param courseId The ID of the course to delete
     * @return true if deletion was successful, false otherwise
     */
    boolean deleteCourse(int courseId);

    /**
     * Retrieves a specific course by its ID.
     * @param courseId The ID of the course
     * @return The Course object if found, null otherwise
     */
    Course getCourseById(int courseId);

    /**
     * Retrieves all courses in the system.
     * @return List of all Course objects
     */
    List<Course> getAllCourses();

    /**
     * Retrieves all courses for a specific institution.
     * @param institutionId The ID of the institution
     * @return List of Course objects
     */
    List<Course> getCoursesByInstitution(int institutionId);

    /**
     * Updates the status of a specific course.
     * @param courseId The ID of the course
     * @param status The new CourseStatus to set
     * @return true if update was successful, false otherwise
     */
    boolean updateCourseStatus(int courseId, CourseStatus status);

    /**
     * Searches for courses based on specified criteria.
     * @param criteria The CourseSearchCriteria object containing search parameters
     * @return List of matching Course objects
     */
    List<Course> searchCourses(CourseSearchCriteria criteria);

    /**
     * Retrieves all available academic terms.
     * @return Set of unique term strings
     */
    Set<String> getAvailableTerms();
}

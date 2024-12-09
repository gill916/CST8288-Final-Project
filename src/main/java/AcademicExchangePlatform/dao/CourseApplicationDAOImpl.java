package AcademicExchangePlatform.dao;

import AcademicExchangePlatform.model.CourseApplication;
import AcademicExchangePlatform.model.DatabaseConnection;
import AcademicExchangePlatform.dbenum.ApplicationStatus;
import AcademicExchangePlatform.model.Course;
import AcademicExchangePlatform.model.AcademicProfessional;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the CourseApplicationDAO interface.
 * Provides methods to create, update, retrieve, and manage course applications.
 */
public class CourseApplicationDAOImpl implements CourseApplicationDAO {

    /**
     * Creates a new course application.
     * @param application The CourseApplication object to create
     * @return true if creation was successful, false otherwise
     */
    @Override
    public boolean createApplication(CourseApplication application) {
        String query = "INSERT INTO courseapplications (courseId, professionalId, coverLetter, " +
                      "additionalDocuments, applicationDate, status) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, application.getCourseId());
            stmt.setInt(2, application.getProfessionalId());
            stmt.setString(3, application.getCoverLetter());
            stmt.setString(4, application.getAdditionalDocuments());
            stmt.setTimestamp(5, new Timestamp(application.getApplicationDate().getTime()));
            stmt.setString(6, application.getStatus().toString());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Updates the status of a course application.
     * @param applicationId The ID of the course application
     * @param status The new status of the application
     * @param institutionId The ID of the institution
     * @return true if update was successful, false otherwise
     */
    @Override
    public boolean updateApplicationStatus(int applicationId, ApplicationStatus status, int institutionId) {
        String query = "UPDATE courseapplications SET status = ?, decisionDate = ? " +
                      "WHERE applicationId = ? AND EXISTS (" +
                      "SELECT 1 FROM courses c WHERE c.courseId = courseapplications.courseId " +
                      "AND c.institutionId = ?)";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, status.name());
            stmt.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
            stmt.setInt(3, applicationId);
            stmt.setInt(4, institutionId);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Retrieves a course application by its ID.
     * @param applicationId The ID of the course application
     * @return The CourseApplication object, or null if not found
     */
    @Override
    public CourseApplication getApplicationById(int applicationId) {
        String query = "SELECT * FROM courseapplications WHERE applicationId = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, applicationId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extractApplicationFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Retrieves all applications for a specific course.
     * @param courseId The ID of the course
     * @return List of CourseApplication objects
     */
    @Override
    public List<CourseApplication> getApplicationsByCourse(int courseId) {
        List<CourseApplication> applications = new ArrayList<>();
        String query = "SELECT * FROM courseapplications WHERE courseId = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, courseId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    applications.add(extractApplicationFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return applications;
    }

    /**
     * Retrieves all applications for a specific professional.
     * @param professionalId The ID of the academic professional
     * @return List of CourseApplication objects
     */
    @Override
    public List<CourseApplication> getApplicationsByProfessional(int professionalId) {
        List<CourseApplication> applications = new ArrayList<>();
        String query = "SELECT ca.*, c.courseTitle, c.courseCode, c.institutionId, ai.institutionName " +
                      "FROM courseapplications ca " +
                      "JOIN courses c ON ca.courseId = c.courseId " +
                      "JOIN academicinstitutions ai ON c.institutionId = ai.userId " +
                      "WHERE ca.professionalId = ?";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, professionalId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    CourseApplication application = extractApplicationFromResultSet(rs);
                    Course course = new Course();
                    course.setCourseId(rs.getInt("courseId"));
                    course.setCourseTitle(rs.getString("courseTitle"));
                    course.setCourseCode(rs.getString("courseCode"));
                    course.setInstitutionId(rs.getInt("institutionId"));
                    course.setInstitutionName(rs.getString("institutionName"));
                    application.setCourse(course);
                    applications.add(application);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return applications;
    }

    /**
     * Withdraws a course application.
     * @param applicationId The ID of the course application
     * @return true if withdrawal was successful, false otherwise
     */
    @Override
    public boolean withdrawApplication(int applicationId) {
        String query = "UPDATE courseapplications SET status = ?, decisionDate = ? WHERE applicationId = ?";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, ApplicationStatus.REJECTED.name());
            stmt.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
            stmt.setInt(3, applicationId);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Retrieves all applications for courses at a specific institution.
     * @param institutionId The ID of the academic institution
     * @return List of CourseApplication objects
     */
    @Override
    public List<CourseApplication> getAllInstitutionApplications(int institutionId) {
        List<CourseApplication> applications = new ArrayList<>();
        String query = "SELECT ca.*, c.courseTitle, c.courseCode, " +
                      "ap.firstName, ap.lastName, ap.userId as professionalId " +
                      "FROM courseapplications ca " +
                      "JOIN courses c ON ca.courseId = c.courseId " +
                      "JOIN academicprofessionals ap ON ca.professionalId = ap.userId " +
                      "WHERE c.institutionId = ?";
                      
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, institutionId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    CourseApplication application = new CourseApplication();
                    application.setApplicationId(rs.getInt("applicationId"));
                    application.setCourseId(rs.getInt("courseId"));
                    application.setProfessionalId(rs.getInt("professionalId"));
                    application.setStatus(ApplicationStatus.valueOf(rs.getString("status")));
                    application.setApplicationDate(rs.getTimestamp("applicationDate"));
                    
                    // Set course information
                    Course course = new Course();
                    course.setCourseId(rs.getInt("courseId"));
                    course.setCourseTitle(rs.getString("courseTitle"));
                    course.setCourseCode(rs.getString("courseCode"));
                    application.setCourse(course);
                    
                    // Set professional information
                    AcademicProfessional professional = new AcademicProfessional();
                    professional.setUserId(rs.getInt("professionalId"));
                    professional.setFirstName(rs.getString("firstName"));
                    professional.setLastName(rs.getString("lastName"));
                    application.setProfessional(professional);
                    
                    applications.add(application);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return applications;
    }

    /**
     * Checks if a professional has an existing application for a course.
     * @param professionalId The ID of the academic professional
     * @param courseId The ID of the course
     * @return true if an application exists, false otherwise
     */
    @Override
    public boolean hasExistingApplication(int professionalId, int courseId) {
        String query = "SELECT COUNT(*) FROM courseapplications " +
                      "WHERE professionalId = ? AND courseId = ? AND status = ?";
                      
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, professionalId);
            stmt.setInt(2, courseId);
            stmt.setString(3, ApplicationStatus.PENDING.toString());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Retrieves the total number of applications for a specific course.
     * @param courseId The ID of the course
     * @return The number of applications
     */
    @Override
    public int getApplicationCount(int courseId) {
        String query = "SELECT COUNT(*) FROM courseapplications WHERE courseId = ?";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, courseId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Extracts a CourseApplication object from a ResultSet.
     * @param rs The ResultSet containing the application data
     * @return The CourseApplication object
     * @throws SQLException If there is an error accessing the ResultSet
     */
    private CourseApplication extractApplicationFromResultSet(ResultSet rs) throws SQLException {
        CourseApplication application = new CourseApplication();
        application.setApplicationId(rs.getInt("applicationId"));
        application.setCourseId(rs.getInt("courseId"));
        application.setProfessionalId(rs.getInt("professionalId"));
        application.setCoverLetter(rs.getString("coverLetter"));
        application.setAdditionalDocuments(rs.getString("additionalDocuments"));
        application.setApplicationDate(rs.getTimestamp("applicationDate"));
        application.setDecisionDate(rs.getTimestamp("decisionDate"));
        application.setStatus(ApplicationStatus.valueOf(rs.getString("status")));
        return application;
    }

    /**
     * Updates an existing course application.
     * @param application The CourseApplication object containing updated details
     * @return true if update was successful, false otherwise
     */
    @Override
    public boolean updateApplication(CourseApplication application) {
        String sql = "UPDATE courseapplications SET status = ?, decisionDate = ? WHERE applicationId = ?";

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, application.getStatus().name());
            statement.setTimestamp(2, application.getDecisionDate() != null
                    ? new Timestamp(application.getDecisionDate().getTime()) : null);
            statement.setInt(3, application.getApplicationId());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

} 
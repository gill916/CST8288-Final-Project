/**
 * Author: Jiajun Cai
 * Student Number: 041127296
 */
package AcademicExchangePlatform.dao;

import AcademicExchangePlatform.dbenum.CourseStatus;
import AcademicExchangePlatform.dbenum.DeliveryMethod;
import AcademicExchangePlatform.dbenum.Schedule;
import AcademicExchangePlatform.model.Course;
import AcademicExchangePlatform.model.CourseSearchCriteria;
import AcademicExchangePlatform.model.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Implementation of the CourseDAO interface.
 * Provides methods to create, update, retrieve, and manage courses.
 */
public class CourseDAOImpl implements CourseDAO {

    /**
     * Creates a new course.
     * @param course The Course object to create
     * @return true if creation was successful, false otherwise
     */
    @Override
    public boolean createCourse(Course course) {
        String query = "INSERT INTO courses (courseTitle, courseCode, term, schedule, deliveryMethod, outline, " +
                "preferredQualifications, compensation, institutionId, status, applicationDeadline) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, course.getCourseTitle());
            statement.setString(2, course.getCourseCode());
            statement.setString(3, course.getTerm());
            statement.setString(4, course.getSchedule().name());
            statement.setString(5, course.getDeliveryMethod().name());
            statement.setString(6, course.getOutline());
            statement.setString(7, course.getPreferredQualifications());
            statement.setBigDecimal(8, course.getCompensation());
            statement.setInt(9, course.getInstitutionId());
            statement.setString(10, course.getStatus().name());
            statement.setDate(11, new java.sql.Date(course.getApplicationDeadline().getTime()));
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Updates an existing course.
     * @param course The Course object containing updated details
     * @return true if update was successful, false otherwise
     */
    @Override
    public boolean updateCourse(Course course) {
        String query = "UPDATE courses SET courseTitle = ?, courseCode = ?, term = ?, schedule = ?, " +
                "deliveryMethod = ?, outline = ?, preferredQualifications = ?, compensation = ?, " +
                "status = ?, applicationDeadline = ? WHERE courseId = ?";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, course.getCourseTitle());
            statement.setString(2, course.getCourseCode());
            statement.setString(3, course.getTerm());
            statement.setString(4, course.getSchedule().name());
            statement.setString(5, course.getDeliveryMethod().name());
            statement.setString(6, course.getOutline());
            statement.setString(7, course.getPreferredQualifications());
            statement.setBigDecimal(8, course.getCompensation());
            statement.setString(9, course.getStatus().name());
            statement.setDate(10, new java.sql.Date(course.getApplicationDeadline().getTime()));
            statement.setInt(11, course.getCourseId());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Deletes a course.
     * @param courseId The ID of the course to delete
     * @return true if deletion was successful, false otherwise
     */
    @Override
    public boolean deleteCourse(int courseId) {
        String query = "DELETE FROM courses WHERE courseId = ?";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, courseId);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Retrieves all courses for a specific institution.
     * @param institutionId The ID of the institution
     * @return List of Course objects
     */
    @Override
    public List<Course> getCoursesByInstitution(int institutionId) {
        List<Course> courses = new ArrayList<>();
        String query = "SELECT * FROM courses WHERE institutionId = ?";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, institutionId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                courses.add(extractCourseFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return courses;
    }

    /**
     * Updates the status of a specific course.
     * @param courseId The ID of the course
     * @param status The new CourseStatus to set
     * @return true if update was successful, false otherwise
     */
    @Override
    public boolean updateCourseStatus(int courseId, CourseStatus status) {
        String query = "UPDATE courses SET status = ? WHERE courseId = ?";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, status.name());
            statement.setInt(2, courseId);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Retrieves a specific course by its ID.
     * @param courseId The ID of the course
     * @return The Course object if found, null otherwise
     */
    @Override
    public Course getCourseById(int courseId) {
        String query = "SELECT * FROM courses WHERE courseId = ?";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, courseId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return extractCourseFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Retrieves all courses in the system.
     * @return List of all Course objects
     */
    @Override
    public List<Course> getAllCourses() {
        List<Course> courses = new ArrayList<>();
        String query = "SELECT * FROM courses";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                courses.add(extractCourseFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return courses;
    }

    /**
     * Searches for courses based on specified criteria.
     * @param criteria The CourseSearchCriteria object containing search parameters
     * @return List of matching Course objects
     */
    @Override
    public List<Course> searchCourses(CourseSearchCriteria criteria) {
        List<Course> results = new ArrayList<>();
        List<Object> params = new ArrayList<>();
        
        StringBuilder query = new StringBuilder(
            "SELECT * FROM courses WHERE 1=1"
        );

        // Keyword search
        if (criteria.getKeyword() != null && !criteria.getKeyword().trim().isEmpty()) {
            query.append(" AND (courseTitle LIKE ? OR courseCode LIKE ? OR outline LIKE ?)");
            String keyword = "%" + criteria.getKeyword() + "%";
            params.add(keyword);
            params.add(keyword);
            params.add(keyword);
        }

        // Schedule filter
        if (criteria.getSchedule() != null) {
            query.append(" AND schedule = ?");
            params.add(criteria.getSchedule().name());
        }

        // Delivery method filter
        if (criteria.getDeliveryMethod() != null) {
            query.append(" AND deliveryMethod = ?");
            params.add(criteria.getDeliveryMethod().name());
        }

        // Term filter
        if (criteria.getTerm() != null && !criteria.getTerm().trim().isEmpty()) {
            query.append(" AND term = ?");
            params.add(criteria.getTerm());
        }

        // Active courses only
        if (criteria.isActiveOnly()) {
            query.append(" AND status = 'ACTIVE'");
        }

        // Open for applications
        if (criteria.isOpenOnly()) {
            query.append(" AND status = 'ACTIVE' AND applicationDeadline > NOW()");
        }

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query.toString())) {
            
            // Set parameters
            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                results.add(extractCourseFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return results;
    }

    /**
     * Retrieves all available academic terms.
     * @return Set of unique term strings
     */
    @Override
    public Set<String> getAvailableTerms() {
        Set<String> terms = new TreeSet<>();
        String query = "SELECT DISTINCT term FROM courses";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                terms.add(resultSet.getString("term"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return terms;
    }

    /**
     * Extracts a Course object from a ResultSet.
     * @param rs The ResultSet containing the course data
     * @return The Course object
     * @throws SQLException If there is an error accessing the ResultSet
     */
    private Course extractCourseFromResultSet(ResultSet rs) throws SQLException {
        Course course = new Course();
        course.setCourseId(rs.getInt("courseId"));
        course.setCourseTitle(rs.getString("courseTitle"));
        course.setCourseCode(rs.getString("courseCode"));
        course.setTerm(rs.getString("term"));
        course.setSchedule(Schedule.valueOf(rs.getString("schedule")));
        course.setDeliveryMethod(DeliveryMethod.valueOf(rs.getString("deliveryMethod")));
        course.setOutline(rs.getString("outline"));
        course.setPreferredQualifications(rs.getString("preferredQualifications"));
        course.setCompensation(rs.getBigDecimal("compensation"));
        course.setInstitutionId(rs.getInt("institutionId"));
        course.setStatus(CourseStatus.valueOf(rs.getString("status")));
        course.setApplicationDeadline(rs.getDate("applicationDeadline"));
        return course;
    }
}

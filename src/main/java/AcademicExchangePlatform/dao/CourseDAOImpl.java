package AcademicExchangePlatform.dao;

import AcademicExchangePlatform.dbenum.DeliveryMethod;
import AcademicExchangePlatform.dbenum.Schedule;
import AcademicExchangePlatform.model.Course;
import AcademicExchangePlatform.model.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CourseDAOImpl implements CourseDAO {

    @Override
    public void createCourse(Course course) {
        String query = "INSERT INTO courses (courseTitle, courseCode, term, schedule, deliveryMethod, outline, preferredQualifications, compensation, institutionId) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, course.getCourseTitle());
            statement.setString(2, course.getCourseCode());
            statement.setString(3, course.getTerm());
            statement.setString(4, course.getSchedule().name()); // Use enum's name
            statement.setString(5, course.getDeliveryMethod().name()); // Use enum's name
            statement.setString(6, course.getOutline());
            statement.setString(7, course.getPreferredQualifications());
            statement.setBigDecimal(8, course.getCompensation());
            statement.setInt(9, course.getInstitutionId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateCourse(Course course) {
        String query = "UPDATE courses SET courseTitle = ?, courseCode = ?, term = ?, schedule = ?, deliveryMethod = ?, " +
                "outline = ?, preferredQualifications = ?, compensation = ?, institutionId = ? WHERE courseId = ?";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, course.getCourseTitle());
            statement.setString(2, course.getCourseCode());
            statement.setString(3, course.getTerm());
            statement.setString(4, course.getSchedule().name()); // Use enum's name
            statement.setString(5, course.getDeliveryMethod().name()); // Use enum's name
            statement.setString(6, course.getOutline());
            statement.setString(7, course.getPreferredQualifications());
            statement.setBigDecimal(8, course.getCompensation());
            statement.setInt(9, course.getInstitutionId());
            statement.setInt(10, course.getCourseId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteCourse(int courseId) {
        String query = "DELETE FROM courses WHERE courseId = ?";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, courseId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Course getCourseById(int courseId) {
        String query = "SELECT * FROM courses WHERE courseId = ?";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, courseId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Course course = new Course();
                course.setCourseId(resultSet.getInt("courseId"));
                course.setCourseTitle(resultSet.getString("courseTitle"));
                course.setCourseCode(resultSet.getString("courseCode"));
                course.setTerm(resultSet.getString("term"));
                course.setSchedule(Schedule.valueOf(resultSet.getString("schedule"))); // Convert back to enum
                course.setDeliveryMethod(DeliveryMethod.valueOf(resultSet.getString("deliveryMethod"))); // Convert back to enum
                course.setOutline(resultSet.getString("outline"));
                course.setPreferredQualifications(resultSet.getString("preferredQualifications"));
                course.setCompensation(resultSet.getBigDecimal("compensation"));
                course.setInstitutionId(resultSet.getInt("institutionId"));
                return course;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Course> getAllCourses() {
        List<Course> courses = new ArrayList<>();
        String query = "SELECT * FROM courses";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                Course course = new Course();
                course.setCourseId(resultSet.getInt("courseId"));
                course.setCourseTitle(resultSet.getString("courseTitle"));
                course.setCourseCode(resultSet.getString("courseCode"));
                course.setTerm(resultSet.getString("term"));
                course.setSchedule(Schedule.valueOf(resultSet.getString("schedule"))); // Convert back to enum
                course.setDeliveryMethod(DeliveryMethod.valueOf(resultSet.getString("deliveryMethod"))); // Convert back to enum
                course.setOutline(resultSet.getString("outline"));
                course.setPreferredQualifications(resultSet.getString("preferredQualifications"));
                course.setCompensation(resultSet.getBigDecimal("compensation"));
                course.setInstitutionId(resultSet.getInt("institutionId"));
                courses.add(course);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return courses;
    }
}

package AcademicExchangePlatform.dao;

import AcademicExchangePlatform.dbenum.CourseStatus;
import AcademicExchangePlatform.dbenum.DeliveryMethod;
import AcademicExchangePlatform.dbenum.Schedule;
import AcademicExchangePlatform.model.Course;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class CourseDAOImplTest {
    private CourseDAO courseDAO;

    @Before
    public void setUp() throws SQLException {
        TestDatabaseConnection.setupMockConnection();
        courseDAO = new CourseDAOImpl();
    }

    @Test
    public void testCreateCourse() throws SQLException {
        Course course = createSampleCourse();
        when(TestDatabaseConnection.getPreparedStatement().executeUpdate()).thenReturn(1);

        boolean result = courseDAO.createCourse(course);

        assertTrue(result);
    }

    @Test
    public void testGetCourseById() throws SQLException {
        when(TestDatabaseConnection.getResultSet().next()).thenReturn(true);
        setupMockResultSetForCourse();

        Course result = courseDAO.getCourseById(1);

        assertNotNull(result);
        assertEquals("Test Course", result.getCourseTitle());
        assertEquals(CourseStatus.ACTIVE, result.getStatus());
    }

    @Test
    public void testUpdateCourseStatus() throws SQLException {
        when(TestDatabaseConnection.getPreparedStatement().executeUpdate()).thenReturn(1);

        boolean result = courseDAO.updateCourseStatus(1, CourseStatus.FILLED);

        assertTrue(result);
    }

    private Course createSampleCourse() {
        Course course = new Course();
        course.setCourseTitle("Test Course");
        course.setCourseCode("TEST101");
        course.setTerm("Fall 2023");
        course.setSchedule(Schedule.MORNING);
        course.setDeliveryMethod(DeliveryMethod.IN_PERSON);
        course.setOutline("Test outline");
        course.setCompensation(new BigDecimal("1000.00"));
        course.setInstitutionId(1);
        course.setStatus(CourseStatus.ACTIVE);
        course.setApplicationDeadline(new Date(System.currentTimeMillis()));
        return course;
    }

    private void setupMockResultSetForCourse() throws SQLException {
        when(TestDatabaseConnection.getResultSet().getString("courseTitle")).thenReturn("Test Course");
        when(TestDatabaseConnection.getResultSet().getString("courseCode")).thenReturn("TEST101");
        when(TestDatabaseConnection.getResultSet().getString("term")).thenReturn("Fall 2023");
        when(TestDatabaseConnection.getResultSet().getString("schedule")).thenReturn(Schedule.MORNING.name());
        when(TestDatabaseConnection.getResultSet().getString("deliveryMethod")).thenReturn(DeliveryMethod.IN_PERSON.name());
        when(TestDatabaseConnection.getResultSet().getString("outline")).thenReturn("Test outline");
        when(TestDatabaseConnection.getResultSet().getBigDecimal("compensation")).thenReturn(new BigDecimal("1000.00"));
        when(TestDatabaseConnection.getResultSet().getInt("institutionId")).thenReturn(1);
        when(TestDatabaseConnection.getResultSet().getString("status")).thenReturn(CourseStatus.ACTIVE.name());
        when(TestDatabaseConnection.getResultSet().getDate("applicationDeadline")).thenReturn(new Date(System.currentTimeMillis()));
    }
}
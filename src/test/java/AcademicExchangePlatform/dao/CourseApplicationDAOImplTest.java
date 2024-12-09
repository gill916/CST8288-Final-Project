package AcademicExchangePlatform.dao;

import AcademicExchangePlatform.model.CourseApplication;
import AcademicExchangePlatform.dbenum.ApplicationStatus;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class CourseApplicationDAOImplTest {
    private CourseApplicationDAO courseApplicationDAO;

    @Before
    public void setUp() throws SQLException {
        TestDatabaseConnection.setupMockConnection();
        courseApplicationDAO = new CourseApplicationDAOImpl();
    }

    @Test
    public void testCreateApplication() throws SQLException {
        CourseApplication application = new CourseApplication();
        application.setCourseId(1);
        application.setProfessionalId(1);
        application.setCoverLetter("Test cover letter");
        application.setAdditionalDocuments("Test documents");
        application.setApplicationDate(new Date());
        application.setStatus(ApplicationStatus.PENDING);

        when(TestDatabaseConnection.getPreparedStatement().executeUpdate()).thenReturn(1);

        boolean result = courseApplicationDAO.createApplication(application);

        assertTrue(result);
    }

    @Test
    public void testGetApplicationById() throws SQLException {
        when(TestDatabaseConnection.getResultSet().next()).thenReturn(true);
        when(TestDatabaseConnection.getResultSet().getInt("applicationId")).thenReturn(1);
        when(TestDatabaseConnection.getResultSet().getString("status")).thenReturn("PENDING");
        when(TestDatabaseConnection.getResultSet().getTimestamp("applicationDate"))
            .thenReturn(new Timestamp(System.currentTimeMillis()));

        CourseApplication result = courseApplicationDAO.getApplicationById(1);

        assertNotNull(result);
        assertEquals(ApplicationStatus.PENDING, result.getStatus());
    }

    @Test
    public void testUpdateApplicationStatus() throws SQLException {
        when(TestDatabaseConnection.getPreparedStatement().executeUpdate()).thenReturn(1);

        boolean result = courseApplicationDAO.updateApplicationStatus(1, ApplicationStatus.ACCEPTED, 1);

        assertTrue(result);
    }

    @Test
    public void testWithdrawApplication() throws SQLException {
        when(TestDatabaseConnection.getPreparedStatement().executeUpdate()).thenReturn(1);

        boolean result = courseApplicationDAO.withdrawApplication(1);

        assertTrue(result);
    }

    @Test
    public void testHasExistingApplication() throws SQLException {
        when(TestDatabaseConnection.getResultSet().next()).thenReturn(true);
        when(TestDatabaseConnection.getResultSet().getInt(1)).thenReturn(1);

        boolean result = courseApplicationDAO.hasExistingApplication(1, 1);

        assertTrue(result);
    }
} 
package AcademicExchangePlatform.dao;

import AcademicExchangePlatform.model.Notification;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class NotificationDAOImplTest {
    private NotificationDAO notificationDAO;

    @Before
    public void setUp() throws SQLException {
        TestDatabaseConnection.setupMockConnection();
        notificationDAO = NotificationDAOImpl.getInstance();
    }

    @Test
    public void testCreateNotification() throws SQLException {
        Notification notification = new Notification();
        notification.setUserId(1);
        notification.setTitle("Test Title");
        notification.setMessage("Test Message");
        notification.setType("SYSTEM");
        notification.setDateSent(new Date());

        when(TestDatabaseConnection.getPreparedStatement().executeUpdate()).thenReturn(1);

        boolean result = notificationDAO.createNotification(notification);

        assertTrue(result);
    }

    @Test
    public void testGetUnreadCount() throws SQLException {
        when(TestDatabaseConnection.getResultSet().next()).thenReturn(true);
        when(TestDatabaseConnection.getResultSet().getInt(1)).thenReturn(5);

        int result = notificationDAO.getUnreadCount(1);

        assertEquals(5, result);
    }

    @Test
    public void testMarkAllAsRead() throws SQLException {
        when(TestDatabaseConnection.getPreparedStatement().executeUpdate()).thenReturn(1);

        boolean result = notificationDAO.markAllAsRead(1);

        assertTrue(result);
    }

    @Test
    public void testMarkAsRead() throws SQLException {
        when(TestDatabaseConnection.getPreparedStatement().executeUpdate()).thenReturn(1);

        boolean result = notificationDAO.markAsRead(1);

        assertTrue(result);
    }

    @Test
    public void testDeleteNotification() throws SQLException {
        when(TestDatabaseConnection.getPreparedStatement().executeUpdate()).thenReturn(1);

        boolean result = notificationDAO.deleteNotification(1);

        assertTrue(result);
    }
}
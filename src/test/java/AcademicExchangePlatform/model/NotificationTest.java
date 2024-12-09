/**
 * Author: Jiajun Cai
 *
 * Student Number: 041127296
 */
package AcademicExchangePlatform.model;

import org.junit.Test;
import java.util.Date;
import static org.junit.Assert.*;

public class NotificationTest {
    @Test
    public void testCreateNotification() {
        Notification notification = new Notification(1, "Test Title", "Test Message", "SYSTEM", "REF1");
        
        assertEquals(1, notification.getUserId());
        assertEquals("Test Title", notification.getTitle());
        assertEquals("Test Message", notification.getMessage());
        assertEquals("SYSTEM", notification.getType());
        assertFalse(notification.isRead());
        assertNotNull(notification.getDateSent());
    }
} 
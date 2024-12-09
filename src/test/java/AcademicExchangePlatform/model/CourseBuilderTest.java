package AcademicExchangePlatform.model;

import AcademicExchangePlatform.dbenum.DeliveryMethod;
import AcademicExchangePlatform.dbenum.Schedule;
import org.junit.Test;
import java.math.BigDecimal;
import static org.junit.Assert.*;

public class CourseBuilderTest {
    @Test
    public void testBuildCourse() {
        Course course = new CourseBuilder()
            .setCourseId(1)
            .setCourseTitle("Test Course")
            .setCourseCode("TEST101")
            .setTerm("Fall 2023")
            .setSchedule(Schedule.MORNING)
            .setDeliveryMethod(DeliveryMethod.IN_PERSON)
            .setOutline("Test outline")
            .setCompensation(new BigDecimal("1000.00"))
            .setInstitutionId(1)
            .build();

        assertNotNull(course);
        assertEquals("Test Course", course.getCourseTitle());
        assertEquals("TEST101", course.getCourseCode());
        assertEquals(Schedule.MORNING, course.getSchedule());
    }
} 
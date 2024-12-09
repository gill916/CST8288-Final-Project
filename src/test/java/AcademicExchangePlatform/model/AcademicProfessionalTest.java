package AcademicExchangePlatform.model;

import org.junit.Test;
import java.util.Arrays;
import static org.junit.Assert.*;

public class AcademicProfessionalTest {
    @Test
    public void testCompleteProfile() {
        AcademicProfessional professional = new AcademicProfessional();
        professional.completeProfile(
            "Professor",
            "Test University",
            "PhD in Computer Science",
            Arrays.asList("Java", "Python")
        );

        assertTrue(professional.isProfileComplete());
        assertEquals("Professor", professional.getPosition());
        assertEquals("Test University", professional.getCurrentInstitution());
        assertEquals(2, professional.getExpertise().size());
    }
} 
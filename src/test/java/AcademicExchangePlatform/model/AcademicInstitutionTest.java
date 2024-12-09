/**
 * Author: Jiajun Cai
 * Student Number: 041127296
 */
package AcademicExchangePlatform.model;

import org.junit.Test;
import static org.junit.Assert.*;

public class AcademicInstitutionTest {
    @Test
    public void testCompleteProfile() {
        AcademicInstitution institution = new AcademicInstitution();
        institution.setInstitutionName("Test University");
        institution.completeProfile(
            "123 Test St",
            "contact@test.edu",
            "123-456-7890",
            "www.test.edu",
            "Test Description"
        );

        assertTrue(institution.isProfileComplete());
        assertEquals("123 Test St", institution.getAddress());
        assertEquals("contact@test.edu", institution.getContactEmail());
    }
} 
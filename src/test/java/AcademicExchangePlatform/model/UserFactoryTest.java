/**
 * Author: Jiajun Cai
 * Student Number: 041127296
 */
package AcademicExchangePlatform.model;

import AcademicExchangePlatform.dbenum.UserType;
import org.junit.Test;
import static org.junit.Assert.*;

public class UserFactoryTest {
    @Test
    public void testCreateProfessionalUser() {
        User user = UserFactory.createUser(UserType.PROFESSIONAL);
        assertTrue(user instanceof AcademicProfessional);
        AcademicProfessional professional = (AcademicProfessional) user;
        professional.setUserType(UserType.PROFESSIONAL);
        assertEquals(UserType.PROFESSIONAL, professional.getUserType());
    }

    @Test
    public void testCreateInstitutionUser() {
        User user = UserFactory.createUser(UserType.INSTITUTION);
        assertTrue(user instanceof AcademicInstitution);
        AcademicInstitution institution = (AcademicInstitution) user;
        institution.setUserType(UserType.INSTITUTION);
        assertEquals(UserType.INSTITUTION, institution.getUserType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateInvalidUser() {
        UserFactory.createUser(UserType.ADMIN);
    }
}
package AcademicExchangePlatform.model;

import AcademicExchangePlatform.model.AcademicInstitution;
import AcademicExchangePlatform.model.AcademicProfessional;
import AcademicExchangePlatform.model.User;

public class UserFactory {
    public static User createUser(String userType) {
        switch (userType) {
            case "AcademicProfessional":
                return new AcademicProfessional();
            case "AcademicInstitution":
                return new AcademicInstitution();
            default:
                throw new IllegalArgumentException("Invalid user type: " + userType);
        }
    }
}

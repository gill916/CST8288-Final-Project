package AcademicExchangePlatform.model;
import AcademicExchangePlatform.dbenum.UserType;

/**
 * Factory class for creating user objects.
 * Implements the Factory pattern for user object creation.
 * Provides centralized user object instantiation based on user type.
 */
public class UserFactory {
    public static User createUser(UserType type) {
        switch (type) {
            case PROFESSIONAL:
                return new AcademicProfessional();
            case INSTITUTION:
                return new AcademicInstitution();
            default:
                throw new IllegalArgumentException("Invalid user type");
        }
    }
}

package AcademicExchangePlatform.dao;

import AcademicExchangePlatform.model.User;
import AcademicExchangePlatform.model.AcademicInstitution;
import AcademicExchangePlatform.model.AcademicProfessional;
import java.util.List;
/**
 * Data Access Object interface for managing courses in the Academic Exchange Platform.
 * Provides methods to create, update, retrieve, and manage courses.
 */
public interface UserDAO {
        /**
     * Creates a new course in the database.
     * @param course The Course object to create
     * @return true if creation was successful, false otherwise
     */
    boolean addUser(User user);

    /**
     * Retrieves a user by their email address.
     * @param email The email address to search for
     * @return User object if found, null otherwise
     */
    User getUserByEmail(String email);
    
    /**
     * Updates an existing user.
     * @param user The User object containing updated details
     * @return true if update was successful, false otherwise
     */
    boolean updateUser(User user);

    /**
     * Retrieves all institutions in the system.
     * @return List of all AcademicInstitution objects
     */
    List<AcademicInstitution> getAllInstitutions();
    User getUserById(int userId);

    /**
     * Retrieves all professionals in the system.
     * @return List of all AcademicProfessional objects
     */
    List<AcademicProfessional> getAllProfessionals();
}

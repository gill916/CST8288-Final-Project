/**
 * Author: Jiajun Cai
 * Student Number: 041127296
 */
package AcademicExchangePlatform.service;

import AcademicExchangePlatform.dao.UserDAO;
import AcademicExchangePlatform.dao.UserDAOImpl;
import AcademicExchangePlatform.model.User;
import AcademicExchangePlatform.model.DatabaseConnection;
import AcademicExchangePlatform.model.AcademicInstitution;
import AcademicExchangePlatform.model.AcademicProfessional;
import java.util.Date;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collections;

/**
 * Service class for managing user operations.
 * Handles user registration, login, profile updates, and data retrieval.
 * Implements the Singleton pattern for centralized user management.
 */
public class UserService {
    private static UserService instance;
    
    /**
     * Private constructor for singleton pattern.
     * Initializes userDAO.
     */
    private UserService() {
        this.userDAO = UserDAOImpl.getInstance();
    }
    
    /**
     * Gets the singleton instance of UserService.
     * @return The UserService instance
     */
    public static UserService getInstance() {
        if (instance == null) {
            synchronized(UserService.class) {
                if (instance == null) {
                    instance = new UserService();
                }
            }
        }
        return instance;
    }

    private final UserDAO userDAO;

    /**
     * Registers a new user in the system.
     * @param user The user to register
     * @return True if registration is successful, false otherwise
     */
    public boolean register(User user) {
        if (!validateUserFields(user)) {
            return false;
        }

        if (userDAO.getUserByEmail(user.getEmail()) != null) {
            return false;
        }

        return userDAO.addUser(user);
    }

    /**
     * Logs in a user with the given email and password.
     * @param email The email of the user
     * @param password The password of the user
     * @return The logged-in user or null if login fails
     */
    public User login(String email, String password) {
        User user = userDAO.getUserByEmail(email);
        if (user != null && user.getPassword().equals(password) && "ACTIVE".equals(user.getStatus())) {
            user.setLastLogin(new Date());
            userDAO.updateUser(user);
            
            if (user instanceof AcademicProfessional) {
                return getProfessionalById(user.getUserId());
            }
            return user;
        }
        return null;
    }

    /**
     * Updates the profile of a user.
     * @param user The user to update
     * @return True if the update is successful, false otherwise
     */
    public boolean updateProfile(User user) {
        if (user instanceof AcademicProfessional) {
            AcademicProfessional professional = (AcademicProfessional) user;
            String sql = "UPDATE academicprofessionals SET " +
                        "position = ?, " +
                        "currentInstitution = ?, " +
                        "educationBackground = ?, " +
                        "expertise = ?, " +
                        "isProfileComplete = ? " +
                        "WHERE userId = ?";
            
            try (Connection conn = DatabaseConnection.getInstance().getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                
                stmt.setString(1, professional.getPosition());
                stmt.setString(2, professional.getCurrentInstitution());
                stmt.setString(3, professional.getEducationBackground());
                stmt.setString(4, professional.getExpertise() != null ? 
                                String.join(",", professional.getExpertise()) : "");
                stmt.setBoolean(5, professional.isProfileComplete());
                stmt.setInt(6, professional.getUserId());
                
                return stmt.executeUpdate() > 0;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    /**
     * Retrieves all institutions from the database.
     * @return List of all institutions
     */
    public List<AcademicInstitution> getAllInstitutions() {
        return userDAO.getAllInstitutions();
    }

    /**
     * Validates the fields of a user.
     * @param user The user to validate
     * @return True if the user is valid, false otherwise
     */
    private boolean validateUserFields(User user) {
        if (user == null || user.getEmail() == null || user.getPassword() == null) {
            return false;
        }

        if (user instanceof AcademicProfessional) {
            return validateProfessional((AcademicProfessional) user);
        } else if (user instanceof AcademicInstitution) {
            return validateInstitution((AcademicInstitution) user);
        }
        return false;
    }

    private boolean validateProfessional(AcademicProfessional professional) {
        return professional.getFirstName() != null && !professional.getFirstName().isEmpty() &&
               professional.getLastName() != null && !professional.getLastName().isEmpty() &&
               professional.getCurrentInstitution() != null && !professional.getCurrentInstitution().isEmpty() &&
               professional.getPosition() != null && !professional.getPosition().isEmpty();
    }

    private boolean validateInstitution(AcademicInstitution institution) {
        return institution.getInstitutionName() != null && !institution.getInstitutionName().isEmpty();
    }

    /**
     * Retrieves a professional by their user ID.
     * @param userId The ID of the professional to retrieve
     * @return The professional or null if not found
     */
    public AcademicProfessional getProfessionalById(int userId) {
        User user = userDAO.getUserById(userId);
        if (user instanceof AcademicProfessional) {
            return (AcademicProfessional) user;
        }
        return null;
    }

    /**
     * Updates a user's profile in the database.
     * @param user The user to update
     * @return True if the update is successful, false otherwise
     */
    public boolean updateUser(User user) {
        if (user instanceof AcademicProfessional) {
            AcademicProfessional professional = (AcademicProfessional) user;
            String sql = "UPDATE academicprofessionals SET " +
                        "isProfileComplete = ?, " +
                        "position = ?, " +
                        "currentInstitution = ?, " +
                        "educationBackground = ?, " +
                        "expertise = ?, " +
                        "firstName = ?, " +
                        "lastName = ? " +
                        "WHERE userId = ?";
            
            try (Connection conn = DatabaseConnection.getInstance().getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                
                stmt.setBoolean(1, professional.isProfileComplete());
                stmt.setString(2, professional.getPosition());
                stmt.setString(3, professional.getCurrentInstitution());
                stmt.setString(4, professional.getEducationBackground());
                stmt.setString(5, professional.getExpertise() != null ? 
                                String.join(",", professional.getExpertise()) : "");
                stmt.setString(6, professional.getFirstName());
                stmt.setString(7, professional.getLastName());
                stmt.setInt(8, professional.getUserId());
                
                return stmt.executeUpdate() > 0;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    /**
     * Retrieves all professionals from the database.
     * @return List of all professionals
     */
    public List<AcademicProfessional> getAllProfessionals() {
        try {
            return userDAO.getAllProfessionals();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}

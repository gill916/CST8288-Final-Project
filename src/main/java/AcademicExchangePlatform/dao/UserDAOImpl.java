/**
 * Author: Jiajun Cai
 * Student Number: 041127296
 */
package AcademicExchangePlatform.dao;

import AcademicExchangePlatform.dao.UserDAO;
import AcademicExchangePlatform.dbenum.UserType;
import AcademicExchangePlatform.model.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.sql.Timestamp;
import java.util.Arrays;

/**
 * Implementation of the UserDAO interface.
 * Provides methods to create, update, retrieve, and manage users.
 */
public class UserDAOImpl implements UserDAO {
    private static UserDAOImpl instance;
    
    private UserDAOImpl() {} // Private constructor
    
    /**
     * Retrieves the singleton instance of UserDAOImpl.
     * @return The singleton instance of UserDAOImpl
     */
    public static UserDAOImpl getInstance() {
        if (instance == null) {
            instance = new UserDAOImpl();
        }
        return instance;
    }

    /**
     * Adds a new user to the database.
     * @param user The User object to add
     * @return true if addition was successful, false otherwise
     */
    @Override
    public boolean addUser(User user) {
        Connection connection = null;
        PreparedStatement userStmt = null;
        PreparedStatement detailsStmt = null;
        
        try {
            connection = DatabaseConnection.getInstance().getConnection();
            connection.setAutoCommit(false);  // Start transaction

            // Insert into Users table
            String userQuery = "INSERT INTO Users (email, password, userType, status, createdAt, lastLogin) VALUES (?, ?, ?, 'ACTIVE', NOW(), NULL)";
            userStmt = connection.prepareStatement(userQuery, Statement.RETURN_GENERATED_KEYS);
            userStmt.setString(1, user.getEmail());
            userStmt.setString(2, user.getPassword());
            userStmt.setString(3, user.getUserType().getValue());
            
            int rowsAffected = userStmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Creating user failed, no rows affected.");
            }

            // Get the generated user ID
            ResultSet generatedKeys = userStmt.getGeneratedKeys();
            if (!generatedKeys.next()) {
                throw new SQLException("Creating user failed, no ID obtained.");
            }
            int userId = generatedKeys.getInt(1);
            user.setUserId(userId);

            // Insert additional details based on user type
            if (user instanceof AcademicProfessional) {
                addProfessionalDetails(connection, (AcademicProfessional) user);
            } else if (user instanceof AcademicInstitution) {
                addInstitutionDetails(connection, (AcademicInstitution) user);
            }

            connection.commit();  // Commit transaction
            return true;

        } catch (SQLException e) {
            try {
                if (connection != null) {
                    connection.rollback();  // Rollback on error
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (userStmt != null) userStmt.close();
                if (detailsStmt != null) detailsStmt.close();
                if (connection != null) {
                    connection.setAutoCommit(true);
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Adds professional details to the database.
     * @param connection The database connection
     * @param professional The AcademicProfessional object containing details to add
     * @throws SQLException If there is an error accessing the database
     */
    private void addProfessionalDetails(Connection connection, AcademicProfessional professional) throws SQLException {
        String query = "INSERT INTO AcademicProfessionals (userId, firstName, lastName, " +
                      "currentInstitution, position, educationBackground) " +
                      "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, professional.getUserId());
            stmt.setString(2, professional.getFirstName());
            stmt.setString(3, professional.getLastName());
            stmt.setString(4, professional.getCurrentInstitution());
            stmt.setString(5, professional.getPosition());
            stmt.setString(6, professional.getEducationBackground());
            stmt.executeUpdate();
        }
    }

    /**
     * Adds institution details to the database.
     * @param connection The database connection
     * @param institution The AcademicInstitution object containing details to add
     * @throws SQLException If there is an error accessing the database
     */
    private void addInstitutionDetails(Connection connection, AcademicInstitution institution) throws SQLException {
        String query = "INSERT INTO AcademicInstitutions (userId, institutionName, address, contactEmail) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, institution.getUserId());
            stmt.setString(2, institution.getInstitutionName());
            stmt.setString(3, institution.getAddress());
            stmt.setString(4, institution.getContactEmail());
            stmt.executeUpdate();
        }
    }

    /**
     * Retrieves a user by their email address.
     * @param email The email address of the user to retrieve
     * @return The User object if found, null otherwise
     */
    @Override
    public User getUserByEmail(String email) {
        System.out.println("Fetching user with email: " + email);
        String query = "SELECT u.*, u.status, u.createdAt, u.lastLogin, " +
                      "ap.firstName, ap.lastName, ap.currentInstitution, ap.position, " +
                      "ap.educationBackground, ap.expertise, ap.isProfileComplete, " +
                      "ai.institutionName, ai.address " +
                      "FROM Users u " +
                      "LEFT JOIN AcademicProfessionals ap ON u.userId = ap.userId " +
                      "LEFT JOIN AcademicInstitutions ai ON u.userId = ai.userId " +
                      "WHERE u.email = ?";

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            System.out.println("Query executed");

            if (rs.next()) {
                System.out.println("User found in database");
                System.out.println("UserType: " + rs.getString("userType"));
                System.out.println("Status: " + rs.getString("status"));
                UserType userType = UserType.fromValue(rs.getString("userType"));
                User user;

                if (userType == UserType.PROFESSIONAL) {
                    AcademicProfessional professional = new AcademicProfessional();
                    professional.setFirstName(rs.getString("firstName"));
                    professional.setLastName(rs.getString("lastName"));
                    professional.setCurrentInstitution(rs.getString("currentInstitution"));
                    professional.setPosition(rs.getString("position"));
                    professional.setEducationBackground(rs.getString("educationBackground"));
                    professional.setProfileComplete(rs.getBoolean("isProfileComplete"));
                    
                    // Load expertise
                    String expertiseStr = rs.getString("expertise");
                    if (expertiseStr != null && !expertiseStr.isEmpty()) {
                        professional.setExpertise(Arrays.asList(expertiseStr.split(",")));
                    }
                    
                    user = professional;
                } else {
                    AcademicInstitution institution = new AcademicInstitution();
                    institution.setInstitutionName(rs.getString("institutionName"));
                    institution.setAddress(rs.getString("address"));
                    user = institution;
                }

                user.setUserId(rs.getInt("userId"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setUserType(userType);
                user.setStatus(rs.getString("status"));
                
                System.out.println("DEBUG: Loaded profile complete status: " + 
                    (user instanceof AcademicProfessional ? 
                    ((AcademicProfessional)user).isProfileComplete() : "N/A"));
                    
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Updates an existing user in the database.
     * @param user The User object containing updated details
     * @return true if update was successful, false otherwise
     */
    @Override
    public boolean updateUser(User user) {
        Connection connection = null;
        try {
            connection = DatabaseConnection.getInstance().getConnection();
            connection.setAutoCommit(false);

            // Update Users table
            String userQuery = "UPDATE Users SET email = ?, password = ?, status = ?, lastLogin = ? WHERE userId = ?";
            try (PreparedStatement stmt = connection.prepareStatement(userQuery)) {
                stmt.setString(1, user.getEmail());
                stmt.setString(2, user.getPassword());
                stmt.setString(3, user.getStatus());
                stmt.setTimestamp(4, user.getLastLogin() != null ? new Timestamp(user.getLastLogin().getTime()) : null);
                stmt.setInt(5, user.getUserId());
                stmt.executeUpdate();
            }

            // Update type-specific details
            if (user instanceof AcademicProfessional) {
                updateProfessionalDetails(connection, (AcademicProfessional) user);
            } else if (user instanceof AcademicInstitution) {
                updateInstitutionDetails(connection, (AcademicInstitution) user);
            }

            connection.commit();
            return true;
        } catch (SQLException e) {
            try {
                if (connection != null) {
                    connection.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (connection != null) {
                    connection.setAutoCommit(true);
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Updates professional details in the database.
     * @param connection The database connection
     * @param professional The AcademicProfessional object containing updated details
     * @throws SQLException If there is an error accessing the database
     */
    private void updateProfessionalDetails(Connection connection, AcademicProfessional professional) throws SQLException {
        String query = "UPDATE AcademicProfessionals SET firstName = ?, lastName = ?, " +
                      "currentInstitution = ?, position = ?, educationBackground = ?, " +
                      "expertise = ?, isProfileComplete = ? " +
                      "WHERE userId = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, professional.getFirstName());
            stmt.setString(2, professional.getLastName());
            stmt.setString(3, professional.getCurrentInstitution());
            stmt.setString(4, professional.getPosition());
            stmt.setString(5, professional.getEducationBackground());
            stmt.setString(6, professional.getExpertise() != null ? String.join(",", professional.getExpertise()) : "");
            stmt.setBoolean(7, professional.isProfileComplete());
            stmt.setInt(8, professional.getUserId());
            stmt.executeUpdate();
        }
    }

    /**
     * Updates institution details in the database.
     * @param connection The database connection
     * @param institution The AcademicInstitution object containing updated details
     * @throws SQLException If there is an error accessing the database
     */
    private void updateInstitutionDetails(Connection connection, AcademicInstitution institution) throws SQLException {
        String query = "UPDATE AcademicInstitutions SET institutionName = ?, address = ? WHERE userId = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, institution.getInstitutionName());
            stmt.setString(2, institution.getAddress());
            stmt.setInt(3, institution.getUserId());
            stmt.executeUpdate();
        }
    }

    /**
     * Retrieves all institutions from the database.
     * @return List of AcademicInstitution objects
     */
    @Override
    public List<AcademicInstitution> getAllInstitutions() {
        List<AcademicInstitution> institutions = new ArrayList<>();
        String query = "SELECT u.*, ai.institutionName, ai.address FROM Users u " +
                      "JOIN AcademicInstitutions ai ON u.userId = ai.userId " +
                      "WHERE u.userType = ?";

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            
            stmt.setString(1, UserType.INSTITUTION.getValue());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                AcademicInstitution institution = new AcademicInstitution();
                institution.setUserId(rs.getInt("userId"));
                institution.setEmail(rs.getString("email"));
                institution.setInstitutionName(rs.getString("institutionName"));
                institution.setAddress(rs.getString("address"));
                institution.setUserType(UserType.INSTITUTION);
                institutions.add(institution);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return institutions;
    }

    /**
     * Retrieves a user by their ID.
     * @param userId The ID of the user to retrieve
     * @return The User object if found, null otherwise
     */
    @Override
    public User getUserById(int userId) {
        String sql = "SELECT u.*, ap.* FROM users u " +
                     "LEFT JOIN academicprofessionals ap ON u.userId = ap.userId " +
                     "WHERE u.userId = ?";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return loadUserFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Loads a User object from a ResultSet.
     * @param rs The ResultSet containing the user data
     * @return The User object
     * @throws SQLException If there is an error accessing the ResultSet
     */
    private User loadUserFromResultSet(ResultSet rs) throws SQLException {
        String userType = rs.getString("userType");
        
        if (userType.equals("PROFESSIONAL")) {
            AcademicProfessional professional = new AcademicProfessional();
            // Load base user fields
            professional.setUserId(rs.getInt("userId"));
            professional.setEmail(rs.getString("email"));
            professional.setUserType(UserType.valueOf(rs.getString("userType")));
            professional.setStatus(rs.getString("status"));
            
            // Load professional specific fields
            professional.setFirstName(rs.getString("firstName"));
            professional.setLastName(rs.getString("lastName"));
            professional.setCurrentInstitution(rs.getString("currentInstitution"));
            professional.setPosition(rs.getString("position"));
            professional.setEducationBackground(rs.getString("educationBackground"));
            professional.setProfileComplete(rs.getBoolean("isProfileComplete"));
            
            // Load expertise
            String expertiseStr = rs.getString("expertise");
            if (expertiseStr != null && !expertiseStr.isEmpty()) {
                professional.setExpertise(Arrays.asList(expertiseStr.split(",")));
            }
            
            return professional;
        }
      
        return null;
    }

    /**
     * Retrieves all professionals from the database.
     * @return List of AcademicProfessional objects
     */
    @Override
    public List<AcademicProfessional> getAllProfessionals() {
        List<AcademicProfessional> professionals = new ArrayList<>();
        String sql = "SELECT u.*, ap.* FROM Users u " +
                     "JOIN AcademicProfessionals ap ON u.userId = ap.userId " +
                     "WHERE u.userType = 'PROFESSIONAL' AND u.status = 'ACTIVE'";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                AcademicProfessional professional = (AcademicProfessional) loadUserFromResultSet(rs);
                if (professional != null) {
                    professionals.add(professional);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return professionals;
    }
}

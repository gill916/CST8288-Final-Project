package AcademicExchangePlatform.dao;


import AcademicExchangePlatform.dbenum.UserType;
import AcademicExchangePlatform.model.DatabaseConnection;
import AcademicExchangePlatform.model.User;
import AcademicExchangePlatform.model.UserFactory;

import java.sql.*;

public class UserDAOImpl implements UserDAO {

    @Override
    public boolean addUser(User user) {
        String query = "INSERT INTO Users (email, password, userType) VALUES (?, ?, ?)";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, user.getEmail());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getUserType().getValue());
            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet keys = statement.getGeneratedKeys();
                if (keys.next()) {
                    user.setUserId(keys.getInt(1));
                }
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public User getUserByEmail(String email) {
        String query = "SELECT * FROM Users WHERE email = ?";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                UserType userType = UserType.fromValue(resultSet.getString("userType"));
                User user = UserFactory.createUser(userType.getValue());
                user.setUserId(resultSet.getInt("userId"));
                user.setEmail(resultSet.getString("email"));
                user.setPassword(resultSet.getString("password"));
                user.setUserType(userType);
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public User getUserById(int userId) {
        String query = "SELECT * FROM Users WHERE userId = ?";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                UserType userType = UserType.fromValue(resultSet.getString("userType"));
                User user = UserFactory.createUser(userType.getValue());
                user.setUserId(resultSet.getInt("userId"));
                user.setEmail(resultSet.getString("email"));
                user.setPassword(resultSet.getString("password"));
                user.setUserType(userType);
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean updateUser(User user) {
        String query = "UPDATE Users SET email = ?, password = ?, userType = ? WHERE userId = ?";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, user.getEmail());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getUserType().getValue());
            statement.setInt(4, user.getUserId());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deleteUser(int userId) {
        String query = "DELETE FROM Users WHERE userId = ?";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, userId);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}

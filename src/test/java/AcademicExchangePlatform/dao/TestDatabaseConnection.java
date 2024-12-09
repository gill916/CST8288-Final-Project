/**
 * Author: Jiajun Cai
 * Student Number: 041127296
 */
package AcademicExchangePlatform.dao;

import AcademicExchangePlatform.model.DatabaseConnection;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TestDatabaseConnection {
    private static Connection mockConnection;
    private static PreparedStatement mockPreparedStatement;
    private static ResultSet mockResultSet;

    public static void setupMockConnection() throws SQLException {
        mockConnection = Mockito.mock(Connection.class);
        mockPreparedStatement = Mockito.mock(PreparedStatement.class);
        mockResultSet = Mockito.mock(ResultSet.class);

        Mockito.when(mockConnection.prepareStatement(Mockito.anyString()))
               .thenReturn(mockPreparedStatement);
        Mockito.when(mockPreparedStatement.executeQuery())
               .thenReturn(mockResultSet);

        DatabaseConnection mockDbConnection = Mockito.mock(DatabaseConnection.class);
        Mockito.when(mockDbConnection.getConnection()).thenReturn(mockConnection);
        
        // Use reflection to set the mock instance
        try {
            java.lang.reflect.Field instance = DatabaseConnection.class.getDeclaredField("instance");
            instance.setAccessible(true);
            instance.set(null, mockDbConnection);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Connection getConnection() {
        return mockConnection;
    }

    public static PreparedStatement getPreparedStatement() {
        return mockPreparedStatement;
    }

    public static ResultSet getResultSet() {
        return mockResultSet;
    }
} 
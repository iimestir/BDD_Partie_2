package database.access;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Singleton class DBManager
 */
public class DBManager {
    private static final DBManager instance = new DBManager();

    private static final String DB_NAME = "BDD2";
    private static final String DB_URL = "jdbc:postgresql://localhost/" + DB_NAME;
    private static final String DB_USER = "bdd_application";
    private static final String DB_PWD = "password";
    private Connection connection;

    /**
     * Prevents the DBManager class instantiation
     */
    private DBManager() {}

    /**
     * Returns the singleton instance
     * @return instance
     */
    public static DBManager getInstance() {
        return instance;
    }

    /**
     * Get the current DB connection, create it if the current connection is null
     * @return the DB connection
     * @throws SQLException exception thrown if the connection failed
     */
    public Connection getDBConnection() throws SQLException {
        if(connection == null)
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PWD);

        return connection;
    }

    /**
     * Used to start a transaction without auto-committing
     * @throws SQLException exception if the connection is null
     */
    public void initialize() throws SQLException {
        getDBConnection().setAutoCommit(false);
    }

    /**
     * Used to commit the changes to the database
     * @throws SQLException if the connection is null
     */
    public void commit() throws SQLException {
        if(getDBConnection().getAutoCommit())
            return;

        getDBConnection().commit();
        getDBConnection().setAutoCommit(true);
    }

    /**
     * Used to rollback the current changes
     * @throws SQLException if the connection is null
     */
    public void rollback() throws SQLException {
        if(getDBConnection().getAutoCommit())
            return;

        getDBConnection().rollback();
        getDBConnection().setAutoCommit(true);
    }
}

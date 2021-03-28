package database.business;

import database.DAO.DBManager;
import database.DAO.UserDAO;
import database.DTO.UserDTO;

import java.sql.SQLException;

public class UserBusinessLogic {
    private static final UserBusinessLogic instance = new UserBusinessLogic();

    /**
     * Singleton
     */
    protected UserBusinessLogic() {}

    /**
     * Singleton instance getter
     * @return the singleton
     */
    public static UserBusinessLogic getInstance() {
        return instance;
    }

    /**
     * Used to log in a user by his username and password
     * @param username username
     * @param password password
     * @return the user account
     * @throws SQLException if an error occurred
     */
    public UserDTO login(String username, String password) throws SQLException {
        try {
            DBManager.getInstance().initialize();
            UserDTO user = UserDAO.select(username, password);
            DBManager.getInstance().commit();

            return user;
        } catch(SQLException ex) {
            DBManager.getInstance().rollback();

            throw ex;
        }
    }

    /**
     * Used to register a user in the database
     * @param user the account informations
     * @param username the username
     * @param password the password
     * @throws SQLException if an error occurred
     */
    public void register(UserDTO user, String username, String password) throws SQLException {
        try {
            DBManager.getInstance().initialize();
            UserDAO.insert(user, username, password);
            DBManager.getInstance().commit();
        } catch(SQLException ex) {
            DBManager.getInstance().rollback();

            throw ex;
        }
    }

    /**
     * Used to edit a user profile
     * @param user the account informations (updated)
     * @param password the password (null if not updated)
     * @throws SQLException if an error occurred
     */
    public void updateUser(UserDTO user, String password) throws SQLException {
        try {
            DBManager.getInstance().initialize();
            UserDAO.update(user, password);
            DBManager.getInstance().commit();
        } catch(SQLException ex) {
            DBManager.getInstance().rollback();

            throw ex;
        }
    }

    /**
     * Used to deleted a user from the database
     * @param user the account informations
     * @throws SQLException if an error occured
     */
    public void deleteUser(UserDTO user) throws SQLException {
        try {
            DBManager.getInstance().initialize();
            UserDAO.delete(user);
            DBManager.getInstance().commit();
        } catch(SQLException ex) {
            DBManager.getInstance().rollback();

            throw ex;
        }
    }
}

package database.business;

import database.DAO.DBManager;
import database.DAO.UserDAO;
import database.DTO.UserDTO;

import java.sql.SQLException;

public class UserBusinessLogic implements UserInterface {
    @Override
    public UserDTO getUserByCredentials(String username, String password) throws SQLException {
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

    @Override
    public void insertUser(UserDTO user, String username, String password) throws SQLException {
        try {
            DBManager.getInstance().initialize();
            UserDAO.insert(user, username, password);
            DBManager.getInstance().commit();
        } catch(SQLException ex) {
            DBManager.getInstance().rollback();

            throw ex;
        }
    }

    @Override
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

    @Override
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

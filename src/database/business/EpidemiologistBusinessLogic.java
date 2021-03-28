package database.business;

import database.DTO.UserDTO;

import java.sql.SQLException;

public class EpidemiologistBusinessLogic implements EpidemiologistInterface {

    @Override
    public UserDTO getUserByCredentials(String username, String password) throws SQLException {
        UserInterface business = new UserBusinessLogic();
        return business.getUserByCredentials(username, password);
    }

    @Override
    public void insertUser(UserDTO user, String username, String password) throws SQLException {
        UserInterface business = new UserBusinessLogic();
        business.insertUser(user, username, password);
    }

    @Override
    public void updateUser(UserDTO user, String password) throws SQLException {
        UserInterface business = new UserBusinessLogic();
        business.updateUser(user, password);
    }

    @Override
    public void deleteUser(UserDTO user) throws SQLException {
        UserInterface business = new UserBusinessLogic();
        business.deleteUser(user);
    }
}

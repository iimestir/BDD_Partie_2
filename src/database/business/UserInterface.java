package database.business;

import database.DTO.UserDTO;

import java.sql.SQLException;

public interface UserInterface {
    UserDTO getUserByCredentials(String username, String password) throws SQLException;
    void insertUser(UserDTO user, String username, String password) throws SQLException;
    void updateUser(UserDTO user, String password) throws SQLException;
    void deleteUser(UserDTO user) throws SQLException;
}

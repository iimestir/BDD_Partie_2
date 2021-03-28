package database.business;

import database.DTO.UserDTO;

import java.sql.SQLException;

/**
 * Singleton class used for the epidemiologist database requests
 */
public class EpidemiologistBusinessLogic extends UserBusinessLogic {
    private static final EpidemiologistBusinessLogic instance = new EpidemiologistBusinessLogic();

    /**
     * Singleton
     */
    private EpidemiologistBusinessLogic() {
        super();
    }

    /**
     * Singleton instance getter
     * @return the singleton
     */
    public static EpidemiologistBusinessLogic getInstance() {
        return instance;
    }

    public UserDTO login(String username, String password) throws SQLException {
        return UserBusinessLogic.getInstance().login(username, password);
    }

    public void register(UserDTO user, String username, String password) throws SQLException {
        UserBusinessLogic.getInstance().register(user, username, password);
    }

    public void updateUser(UserDTO user, String password) throws SQLException {
        UserBusinessLogic.getInstance().updateUser(user, password);
    }

    public void deleteUser(UserDTO user) throws SQLException {
        UserBusinessLogic.getInstance().deleteUser(user);
    }
}

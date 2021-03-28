package database.business;

import database.DTO.UserDTO;

import java.sql.SQLException;

/**
 * Singleton class used for the epidemiologist database requests
 */
public class EpidemiologistLogic extends UserLogic {
    private static final EpidemiologistLogic instance = new EpidemiologistLogic();

    /**
     * Singleton
     */
    private EpidemiologistLogic() {
        super();
    }

    /**
     * Singleton instance getter
     * @return the singleton
     */
    public static EpidemiologistLogic getInstance() {
        return instance;
    }

    public UserDTO login(String username, String password) throws SQLException {
        return UserLogic.getInstance().login(username, password);
    }

    public void register(UserDTO user, String username, String password) throws SQLException {
        UserLogic.getInstance().register(user, username, password);
    }

    public void updateUser(UserDTO user, String password) throws SQLException {
        UserLogic.getInstance().updateUser(user, password);
    }

    public void deleteUser(UserDTO user) throws SQLException {
        UserLogic.getInstance().deleteUser(user);
    }
}

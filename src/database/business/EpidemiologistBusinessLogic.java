package database.business;

import database.access.DBManager;
import database.transfer.EpidemiologistDTO;
import database.transfer.UserDTO;

import java.sql.SQLException;
import java.util.UUID;

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

    @Override
    public void register(UserDTO user, String username, String password) throws SQLException {
        try {
            EpidemiologistDTO epidemiologist = ((EpidemiologistDTO) user);

            DBManager.getInstance().initialize();
            userDao.insert(epidemiologist, username, password);
            DBManager.getInstance().commit();

            DBManager.getInstance().initialize();
            UUID id = userDao.getUserId(username, password);
            userDao.defineEpidemiologist(epidemiologist, id);
            DBManager.getInstance().commit();
        } catch(SQLException ex) {
            DBManager.getInstance().rollback();

            throw ex;
        }
    }
}

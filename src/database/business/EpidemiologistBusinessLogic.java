package database.business;

import database.access.DBManager;
import database.transfer.ClimateDTO;
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

    /**
     * Used to insert a climate
     *
     * @param climate the climate
     * @throws SQLException if an error occurred
     */
    public void insertClimate(ClimateDTO climate) throws SQLException {
        try {
            DBManager.getInstance().initialize();
            climateDao.insert(climate);
            DBManager.getInstance().commit();
        } catch(SQLException ex) {
            DBManager.getInstance().rollback();

            throw ex;
        }
    }

    /**
     * Updates a climate from the DB
     *
     * @param climate the climate
     * @throws SQLException if an error occurred
     */
    public void updateClimate(ClimateDTO climate) throws SQLException {
        try {
            DBManager.getInstance().initialize();
            climateDao.update(climate);
            DBManager.getInstance().commit();
        } catch(SQLException ex) {
            DBManager.getInstance().rollback();

            throw ex;
        }
    }

    /**
     * Deletes a climate from the DB
     *
     * @param climate the climate
     * @throws SQLException if an error occurred
     */
    public void deleteClimate(ClimateDTO climate) throws SQLException {
        try {
            DBManager.getInstance().initialize();
            climateDao.delete(climate);
            DBManager.getInstance().commit();
        } catch(SQLException ex) {
            DBManager.getInstance().rollback();

            throw ex;
        }
    }
}

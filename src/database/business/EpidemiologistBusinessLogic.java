package database.business;

import common.Utils;
import database.access.DBManager;
import database.transfer.*;
import view.UITools;

import java.sql.SQLException;
import java.util.List;
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
     * Used to edit a user profile
     *
     * @param user the account information (updated)
     * @param username the current account username
     * @param password the current account password
     * @param newPassword the new password
     * @throws SQLException if an error occurred
     */
    public void updateUser(UserDTO user, String username, String password, String newPassword) throws SQLException {
        try {
            DBManager.getInstance().initialize();
            userDao.update(user, username, password, newPassword);
            DBManager.getInstance().commit();
        } catch(SQLException ex) {
            DBManager.getInstance().rollback();

            throw ex;
        }

        UITools.showDialog(Utils.getTranslatedString("message_record_updated"));
    }

    /**
     * Used to deleted a user from the database
     *
     * @param user the account information
     * @throws SQLException if an error occurred
     */
    public void deleteUser(UserDTO user) throws SQLException {
        try {
            DBManager.getInstance().initialize();
            userDao.delete(user);
            DBManager.getInstance().commit();
        } catch(SQLException ex) {
            DBManager.getInstance().rollback();

            throw ex;
        }

        UITools.showDialog(Utils.getTranslatedString("message_record_deleted"));
    }

    /**
     * Used to insert a climate
     *
     * @param climate the climate
     * @throws SQLException if an error occurred
     */
    public void insert(ClimateDTO climate) throws SQLException {
        try {
            DBManager.getInstance().initialize();
            climateDao.insert(climate);
            DBManager.getInstance().commit();
        } catch(SQLException ex) {
            DBManager.getInstance().rollback();

            throw ex;
        }

        UITools.showDialog(Utils.getTranslatedString("message_record_inserted"));
    }

    /**
     * Updates a climate from the DB
     *
     * @param oldRecord the old climate
     * @param newRecord the updated climate
     * @throws SQLException if an error occurred
     */
    public void update(ClimateDTO oldRecord, ClimateDTO newRecord) throws SQLException {
        try {
            DBManager.getInstance().initialize();
            climateDao.update(oldRecord, newRecord);
            DBManager.getInstance().commit();
        } catch(SQLException ex) {
            DBManager.getInstance().rollback();

            throw ex;
        }

        UITools.showDialog(Utils.getTranslatedString("message_record_updated"));
    }

    /**
     * Deletes a climate from the DB
     *
     * @param climate the climate
     * @throws SQLException if an error occurred
     */
    public void delete(ClimateDTO climate) throws SQLException {
        try {
            DBManager.getInstance().initialize();
            climateDao.delete(climate);
            DBManager.getInstance().commit();
        } catch(SQLException ex) {
            DBManager.getInstance().rollback();

            throw ex;
        }

        UITools.showDialog(Utils.getTranslatedString("message_record_deleted"));
    }

    /**
     * Used to insert a country
     *
     * @param ISO the country ISO code
     * @param country the country
     * @throws SQLException if an error occurred
     */
    public void insert(String ISO, CountryDTO country) throws SQLException {
        try {
            DBManager.getInstance().initialize();
            countryDao.insert(ISO, country);
            DBManager.getInstance().commit();
        } catch(SQLException ex) {
            DBManager.getInstance().rollback();

            throw ex;
        }

        UITools.showDialog(Utils.getTranslatedString("message_record_inserted"));
    }

    /**
     * Updates a country from the DB
     *
     * @param oldRecord the old country
     * @param  newRecord the new country
     * @throws SQLException if an error occurred
     */
    public void update(CountryDTO oldRecord, CountryDTO newRecord) throws SQLException {
        try {
            DBManager.getInstance().initialize();
            countryDao.update(oldRecord, newRecord);
            DBManager.getInstance().commit();
        } catch(SQLException ex) {
            DBManager.getInstance().rollback();

            throw ex;
        }

        UITools.showDialog(Utils.getTranslatedString("message_record_updated"));
    }

    /**
     * Deletes a country from the DB
     *
     * @param country the country
     * @throws SQLException if an error occurred
     */
    public void delete(CountryDTO country) throws SQLException {
        try {
            DBManager.getInstance().initialize();
            countryDao.delete(country);
            DBManager.getInstance().commit();
        } catch(SQLException ex) {
            DBManager.getInstance().rollback();

            throw ex;
        }

        UITools.showDialog(Utils.getTranslatedString("message_record_deleted"));
    }

    /**
     * Used to insert a hospitals record
     *
     * @param hospitals the hospitals record
     * @throws SQLException if an error occurred
     */
    public void insert(HospitalsDTO hospitals) throws SQLException {
        try {
            DBManager.getInstance().initialize();
            hospitalsDao.insert(hospitals);
            DBManager.getInstance().commit();
        } catch(SQLException ex) {
            DBManager.getInstance().rollback();

            throw ex;
        }

        UITools.showDialog(Utils.getTranslatedString("message_record_inserted"));
    }

    /**
     * Updates hospitals records
     *
     * @param oldRecord the update criteria
     * @param newRecord the updated record
     * @throws SQLException if an error occurred
     */
    public void update(HospitalsDTO oldRecord, HospitalsDTO newRecord) throws SQLException {
        try {
            DBManager.getInstance().initialize();
            hospitalsDao.update(oldRecord, newRecord);
            DBManager.getInstance().commit();
        } catch(SQLException ex) {
            DBManager.getInstance().rollback();

            throw ex;
        }

        UITools.showDialog(Utils.getTranslatedString("message_record_deleted"));
    }

    /**
     * Used to delete a hospitals record
     *
     * @param hospitals the hospitals record
     * @throws SQLException if an error occurred
     */
    public void delete(HospitalsDTO hospitals) throws SQLException {
        try {
            DBManager.getInstance().initialize();
            hospitalsDao.delete(hospitals);
            DBManager.getInstance().commit();
        } catch(SQLException ex) {
            DBManager.getInstance().rollback();

            throw ex;
        }

        UITools.showDialog(Utils.getTranslatedString("message_record_deleted"));
    }

    /**
     * Used to insert a vaccinations record
     *
     * @param vaccinations the vaccinations record
     * @throws SQLException if an error occurred
     */
    public void insert(VaccinationsDTO vaccinations) throws SQLException {
        try {
            DBManager.getInstance().initialize();
            vaccinationsDao.insert(vaccinations);
            DBManager.getInstance().commit();
        } catch(SQLException ex) {
            DBManager.getInstance().rollback();

            throw ex;
        }

        UITools.showDialog(Utils.getTranslatedString("message_record_inserted"));
    }

    /**
     * Updates vaccinations records
     *
     * @param oldRecord the update criteria
     * @param newRecord the updated record
     * @throws SQLException if an error occurred
     */
    public void update(VaccinationsDTO oldRecord, VaccinationsDTO newRecord) throws SQLException {
        try {
            DBManager.getInstance().initialize();
            vaccinationsDao.update(oldRecord, newRecord);
            DBManager.getInstance().commit();
        } catch(SQLException ex) {
            DBManager.getInstance().rollback();

            throw ex;
        }

        UITools.showDialog(Utils.getTranslatedString("message_record_deleted"));
    }

    /**
     * Used to delete a hospitals record
     *
     * @param vaccinations the hospitals record
     * @throws SQLException if an error occurred
     */
    public void delete(VaccinationsDTO vaccinations) throws SQLException {
        try {
            DBManager.getInstance().initialize();
            vaccinationsDao.delete(vaccinations);
            DBManager.getInstance().commit();
        } catch(SQLException ex) {
            DBManager.getInstance().rollback();

            throw ex;
        }

        UITools.showDialog(Utils.getTranslatedString("message_record_deleted"));
    }

    /**
     * Used to insert a producers record
     *
     * @param producers the producers record
     * @throws SQLException if an error occurred
     */
    public void insert(ProducersDTO producers) throws SQLException {
        try {
            DBManager.getInstance().initialize();
            producersDao.insert(producers);
            DBManager.getInstance().commit();
        } catch(SQLException ex) {
            DBManager.getInstance().rollback();

            throw ex;
        }

        UITools.showDialog(Utils.getTranslatedString("message_record_inserted"));
    }

    /**
     * Updates a producers record from the DB
     *
     * @param oldRecord the producers record
     * @param newRecord the updated producers record
     * @throws SQLException if an error occurred
     */
    public void update(ProducersDTO oldRecord, ProducersDTO newRecord) throws SQLException {
        try {
            DBManager.getInstance().initialize();
            producersDao.update(oldRecord, newRecord);
            DBManager.getInstance().commit();
        } catch(SQLException ex) {
            DBManager.getInstance().rollback();

            throw ex;
        }

        UITools.showDialog(Utils.getTranslatedString("message_record_updated"));
    }

    /**
     * Deletes a producers record from the DB
     *
     * @param producers the producers record
     * @throws SQLException if an error occurred
     */
    public void delete(ProducersDTO producers) throws SQLException {
        try {
            DBManager.getInstance().initialize();
            producersDao.delete(producers);
            DBManager.getInstance().commit();
        } catch(SQLException ex) {
            DBManager.getInstance().rollback();

            throw ex;
        }

        UITools.showDialog(Utils.getTranslatedString("message_record_deleted"));
    }

    /**
     * Selects a record from the user table in the DB
     *
     * @param record the user informations
     * @throws SQLException if an error occurred
     */
    public List<UserDTO> select(UserDTO record) throws SQLException {
        try {
            DBManager.getInstance().initialize();
            List<UserDTO> result = userDao.select(record);
            DBManager.getInstance().commit();

            return result;
        } catch(SQLException ex) {
            DBManager.getInstance().rollback();

            throw ex;
        }
    }

    /**
     * Updates a user record from the DB
     *
     * @param oldRecord the user record
     * @param newRecord the updated user record
     * @throws SQLException if an error occurred
     */
    public void update(UserDTO oldRecord, UserDTO newRecord) throws SQLException {
        try {
            DBManager.getInstance().initialize();
            userDao.update(oldRecord, newRecord);
            DBManager.getInstance().commit();
        } catch(SQLException ex) {
            DBManager.getInstance().rollback();

            throw ex;
        }

        UITools.showDialog(Utils.getTranslatedString("message_record_updated"));
    }

    /**
     * Deletes a user record from the DB
     *
     * @param record the user record
     * @throws SQLException if an error occurred
     */
    public void delete(UserDTO record) throws SQLException {
        try {
            DBManager.getInstance().initialize();
            userDao.delete(record);
            DBManager.getInstance().commit();
        } catch(SQLException ex) {
            DBManager.getInstance().rollback();

            throw ex;
        }

        UITools.showDialog(Utils.getTranslatedString("message_record_deleted"));
    }
}

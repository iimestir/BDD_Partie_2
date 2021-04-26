package database.business;

import database.access.*;
import database.transfer.*;

import java.sql.SQLException;
import java.util.List;

public class UserBusinessLogic {
    private static final UserBusinessLogic instance = new UserBusinessLogic();
    protected static final UserDAO userDao = UserDAO.getInstance();
    protected static final ClimateDAO climateDao = ClimateDAO.getInstance();
    protected static final CountryDAO countryDao = CountryDAO.getInstance();
    protected static final HospitalsDAO hospitalsDao = HospitalsDAO.getInstance();
    protected static final VaccinationsDAO vaccinationsDao = VaccinationsDAO.getInstance();
    protected static final ProducersDAO producersDao = ProducersDAO.getInstance();

    /**
     * Singleton
     */
    protected UserBusinessLogic() {}

    /**
     * Singleton instance getter
     *
     * @return the singleton
     */
    public static UserBusinessLogic getInstance() {
        return instance;
    }

    /**
     * Used to log in a user by his username and password
     *
     * @param username username
     * @param password password
     * @return the user account
     * @throws SQLException if an error occurred
     */
    public UserDTO login(String username, String password) throws SQLException {
        try {
            DBManager.getInstance().initialize();
            UserDTO user = userDao.select(username, password);
            DBManager.getInstance().commit();

            return user;
        } catch(SQLException ex) {
            DBManager.getInstance().rollback();

            throw ex;
        }
    }

    /**
     * Used to register a user in the database
     *
     * @param user the account information
     * @param username the username
     * @param password the password
     * @throws SQLException if an error occurred
     */
    public void register(UserDTO user, String username, String password) throws SQLException {
        try {
            DBManager.getInstance().initialize();
            userDao.insert(user, username, password);
            DBManager.getInstance().commit();
        } catch(SQLException ex) {
            DBManager.getInstance().rollback();

            throw ex;
        }
    }

    /**
     * Selects a climate from the DB
     *
     * @param climate the climate
     * @throws SQLException if an error occurred
     */
    public List<ClimateDTO> selectClimate(ClimateDTO climate) throws SQLException {
        try {
            DBManager.getInstance().initialize();
            List<ClimateDTO> result = climateDao.select(climate);
            DBManager.getInstance().commit();

            return result;
        } catch(SQLException ex) {
            DBManager.getInstance().rollback();

            throw ex;
        }
    }

    /**
     * Selects all climates from the DB
     *
     * @throws SQLException if an error occurred
     */
    public List<ClimateDTO> selectAllClimate() throws SQLException {
        try {
            DBManager.getInstance().initialize();
            List<ClimateDTO> result = climateDao.selectAll();
            DBManager.getInstance().commit();

            return result;
        } catch(SQLException ex) {
            DBManager.getInstance().rollback();

            throw ex;
        }
    }

    /**
     * Selects a country from the DB
     *
     * @param country the country
     * @throws SQLException if an error occurred
     */
    public List<CountryDTO> selectCountry(CountryDTO country) throws SQLException {
        try {
            DBManager.getInstance().initialize();
            List<CountryDTO> result = countryDao.select(country);
            DBManager.getInstance().commit();

            return result;
        } catch(SQLException ex) {
            DBManager.getInstance().rollback();

            throw ex;
        }
    }

    /**
     * Selects all climates from the DB
     *
     * @throws SQLException if an error occurred
     */
    public List<CountryDTO> selectAllCountries() throws SQLException {
        try {
            DBManager.getInstance().initialize();
            List<CountryDTO> result = countryDao.selectAll();
            DBManager.getInstance().commit();

            return result;
        } catch(SQLException ex) {
            DBManager.getInstance().rollback();

            throw ex;
        }
    }

    /**
     * Selects a vaccinations report from the DB
     *
     * @param hospitals the vaccinations report
     * @throws SQLException if an error occurred
     */
    public List<HospitalsDTO> selectHospitals(HospitalsDTO hospitals) throws SQLException {
        try {
            DBManager.getInstance().initialize();
            List<HospitalsDTO> result = hospitalsDao.select(hospitals);
            DBManager.getInstance().commit();

            return result;
        } catch(SQLException ex) {
            DBManager.getInstance().rollback();

            throw ex;
        }
    }

    /**
     * Selects all hospitals reports from the DB
     *
     * @throws SQLException if an error occurred
     */
    public List<HospitalsDTO> selectAllHospitals() throws SQLException {
        try {
            DBManager.getInstance().initialize();
            List<HospitalsDTO> result = hospitalsDao.selectAll();
            DBManager.getInstance().commit();

            return result;
        } catch(SQLException ex) {
            DBManager.getInstance().rollback();

            throw ex;
        }
    }

    /**
     * Selects a vaccinations report from the DB
     *
     * @param vaccinations the vaccinations report
     * @throws SQLException if an error occurred
     */
    public List<VaccinationsDTO> selectVaccinations(VaccinationsDTO vaccinations) throws SQLException {
        try {
            DBManager.getInstance().initialize();
            List<VaccinationsDTO> result = vaccinationsDao.select(vaccinations);
            DBManager.getInstance().commit();

            return result;
        } catch(SQLException ex) {
            DBManager.getInstance().rollback();

            throw ex;
        }
    }

    /**
     * Selects all vaccinations reports from the DB
     *
     * @throws SQLException if an error occurred
     */
    public List<VaccinationsDTO> selectAllVaccinations() throws SQLException {
        try {
            DBManager.getInstance().initialize();
            List<VaccinationsDTO> result = vaccinationsDao.selectAll();
            DBManager.getInstance().commit();

            return result;
        } catch(SQLException ex) {
            DBManager.getInstance().rollback();

            throw ex;
        }
    }

    /**
     * Selects a producers from the DB
     *
     * @param producers the producers
     * @throws SQLException if an error occurred
     */
    public List<ProducersDTO> selectProducers(ProducersDTO producers) throws SQLException {
        try {
            DBManager.getInstance().initialize();
            List<ProducersDTO> result = producersDao.select(producers);
            DBManager.getInstance().commit();

            return result;
        } catch(SQLException ex) {
            DBManager.getInstance().rollback();

            throw ex;
        }
    }

    /**
     * Selects all producers from the DB
     *
     * @throws SQLException if an error occurred
     */
    public List<ProducersDTO> selectAllProducers() throws SQLException {
        try {
            DBManager.getInstance().initialize();
            List<ProducersDTO> result = producersDao.selectAll();
            DBManager.getInstance().commit();

            return result;
        } catch(SQLException ex) {
            DBManager.getInstance().rollback();

            throw ex;
        }
    }

}

package database.access;

import common.Utils;
import database.transfer.CountryDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CountryDAO {
    private static final CountryDAO instance = new CountryDAO();

    /**
     * Get the singleton instance
     */
    public static CountryDAO getInstance() {
        return instance;
    }

    /**
     * Singleton
     */
    private CountryDAO() {}

    /**
     * Inserts a new record in the DB
     *
     * @param ISO record ISO code
     * @param record the record to insert
     * @throws SQLException if an error occurs
     */
    public void insert(String ISO, CountryDTO record) throws SQLException {
        if(!select(record).isEmpty())
            throw new SQLException("The specified CountryDTO is already persistent");

        Connection conn = DBManager.getInstance().getDBConnection();
        String request = "INSERT INTO Public.\"Country\"(\"ISO\",\"Continent\",\"Region\",\"Country\",\"HDI\"," +
                "\"Population\",\"area_sq_ml\",\"Climate\") VALUES (?,?,?,?,?,?,?,?)";

        PreparedStatement stmt = conn.prepareStatement(request);
        stmt.setString(1, ISO);
        stmt.setString(2, record.getContinent());
        stmt.setString(3, record.getRegion());
        stmt.setString(4, record.getName());
        stmt.setDouble(5, record.getHdi());
        stmt.setInt(6, record.getPopulation());
        stmt.setDouble(7, record.getArea_sq_ml());
        stmt.setInt(8, record.getClimateId());

        stmt.executeUpdate();
    }

    /**
     * Selects countries from the DB
     *
     * @param record countries information
     * @return countries list
     * @throws SQLException if an error occurs
     */
    public List<CountryDTO> select(CountryDTO record) throws SQLException {
        Connection conn = DBManager.getInstance().getDBConnection();
        StringBuilder request = new StringBuilder("SELECT * FROM Public.\"Country\"");

        prepareStringBuilderStatement(false, record, conn, request);
        PreparedStatement stmt = getPreparedStatement(record, conn, request);

        return retrieveCountries(stmt);
    }

    /**
     * Selects all countries
     *
     * @return countries list
     * @throws SQLException if an error occurs
     */
    public List<CountryDTO> selectAll() throws SQLException {
        Connection conn = DBManager.getInstance().getDBConnection();
        String request = "SELECT * FROM Public.\"Country\"";

        PreparedStatement stmt = conn.prepareStatement(request);

        return retrieveCountries(stmt);
    }

    /**
     * Deletes a record from the DB
     *
     * @param record the record
     * @throws SQLException if an error occurs
     */
    public void delete(CountryDTO record) throws SQLException {
        if(select(record).isEmpty())
            throw new SQLException("The specified CountryDTO is not persistent");

        Connection conn = DBManager.getInstance().getDBConnection();
        StringBuilder request = new StringBuilder("DELETE FROM Public.\"Country\"");

        prepareStringBuilderStatement(false, record, conn, request);
        PreparedStatement stmt = getPreparedStatement(record, conn, request);

        stmt.executeUpdate();
    }

    /**
     * Used on select methods
     *
     * @param stmt prepared statement
     * @return the list of records from the DB
     * @throws SQLException if an error occurred
     */
    private List<CountryDTO> retrieveCountries(PreparedStatement stmt) throws SQLException {
        ResultSet rs = stmt.executeQuery();
        List<CountryDTO> results = new ArrayList<>();
        while (rs.next()) {
            String id = rs.getString("ISO");
            String continent = rs.getString("Continent");
            String region = rs.getString("Region");
            String name = rs.getString("Country");
            double hdi = rs.getDouble("HDI");
            int population = rs.getInt("Population");
            double area_sq_ml = rs.getDouble("area_sq_ml");
            int climateId = rs.getInt("Climate");

            CountryDTO selected = new CountryDTO(id, continent, region, name, hdi, population, area_sq_ml, climateId);

            results.add(selected);
        }
        return results;
    }

    /**
     * Updates a record from the DB
     *
     * @param oldRecord the criteria
     * @param newRecord the updated information
     * @throws SQLException if an error occurs
     */
    public void update(CountryDTO oldRecord, CountryDTO newRecord) throws SQLException {
        if(select(oldRecord).isEmpty())
            throw new SQLException("The specified CountryDTO is not persistent");

        Connection conn = DBManager.getInstance().getDBConnection();
        StringBuilder request = new StringBuilder("UPDATE Public.\"Country\"");

        prepareStringBuilderStatement(true, newRecord, conn, request);
        prepareStringBuilderStatement(false, oldRecord, conn, request);

        PreparedStatement stmt = getPreparedStatement(oldRecord, newRecord, conn, request);

        stmt.executeUpdate();
    }

    /**
     * Returns a prepared statement (including all "ADD" and "WHERE" clauses)
     *
     * @param type request type
     * @param record the record
     * @param conn connection
     * @param request the current request string builder
     * @throws SQLException if an error occurred
     */
    private void prepareStringBuilderStatement(boolean type, CountryDTO record, Connection conn, StringBuilder request) throws SQLException {
        List<String> subRequest = new ArrayList<>();
        if(record.getId() != null)
            subRequest.add("\"ISO\" = ?");
        if(record.getContinent() != null)
            subRequest.add("\"Continent\" = ?");
        if(record.getRegion() != null)
            subRequest.add("\"Region\" = ?");
        if(record.getName() != null)
            subRequest.add("\"Country\" = ?");
        if(record.getHdi() != null)
            subRequest.add("\"HDI\" = ?");
        if(record.getPopulation() != null)
            subRequest.add("\"Population\" = ?");
        if(record.getArea_sq_ml() != null)
            subRequest.add("\"area_sq_ml\" = ?");
        if(record.getClimateId() != null)
            subRequest.add("\"Climate\" = ?");

        if(type)
            Utils.fillSQLUpdate(request, subRequest);
        else
            Utils.fillSQLSelect(request, subRequest);
    }

    /**
     * Returns the prepared statement of a selection request
     *
     * @param record the record
     * @param conn the DB connection
     * @param request the request string builder
     * @return the prepared statement
     * @throws SQLException if an error occurs
     */
    private PreparedStatement getPreparedStatement(CountryDTO record, Connection conn, StringBuilder request) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(request.toString());
        int i = 1;

        // WHERE ...
        prepareStatement(record, stmt, i);

        return stmt;
    }

    /**
     * Returns the prepared statement of an update request
     *
     * @param oldRecord the old record
     * @param newRecord the new record
     * @param conn the DB connection
     * @param request the request string builder
     * @return the prepared statement
     * @throws SQLException if an error occurs
     */
    private PreparedStatement getPreparedStatement(CountryDTO oldRecord, CountryDTO newRecord
            , Connection conn, StringBuilder request) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(request.toString());
        int i = 1;

        // SET ...
        i = prepareStatement(newRecord, stmt, i);
        // WHERE ...
        prepareStatement(oldRecord, stmt, i);

        return stmt;
    }

    /**
     * Prepare a statement for the sql request
     *
     * @param record the current record
     * @param stmt the current non prepared statement
     * @param i statement preparation index
     * @return the final index
     * @throws SQLException if an error occurred
     */
    private int prepareStatement(CountryDTO record, PreparedStatement stmt, int i) throws SQLException {
        if(record.getId() != null)
            stmt.setString(i++, record.getId());
        if(record.getContinent() != null)
            stmt.setString(i++, record.getContinent());
        if(record.getRegion() != null)
            stmt.setString(i++, record.getRegion());
        if(record.getName() != null)
            stmt.setString(i++, record.getName());
        if(record.getHdi() != null)
            stmt.setDouble(i++, record.getHdi());
        if(record.getPopulation() != null)
            stmt.setInt(i++, record.getPopulation());
        if(record.getArea_sq_ml() != null)
            stmt.setDouble(i++, record.getArea_sq_ml());
        if(record.getClimateId() != null)
            stmt.setInt(i++, record.getClimateId());
        return i;
    }
}

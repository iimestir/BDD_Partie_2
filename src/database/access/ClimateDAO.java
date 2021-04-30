package database.access;

import common.Utils;
import database.transfer.ClimateDTO;
import database.transfer.CountryDTO;
import database.transfer.HospitalsDTO;
import database.transfer.UserDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClimateDAO {
    private static final ClimateDAO instance = new ClimateDAO();

    /**
     * Get the singleton instance
     */
    public static ClimateDAO getInstance() {
        return instance;
    }

    /**
     * Singleton
     */
    private ClimateDAO() {}

    /**
     * Selects a climate
     *
     * @param climate the climate information
     * @return a climateDTO
     * @throws SQLException if an error occurred
     */
    public List<ClimateDTO> select(ClimateDTO climate) throws SQLException {
        Connection conn = DBManager.getInstance().getDBConnection();
        StringBuilder request = new StringBuilder("SELECT * FROM Public.\"Climate\"");

        PreparedStatement stmt = getStatement(climate, conn, request);

        return retrieveClimates(stmt);
    }

    /**
     * Selects all climates in the DB
     *
     * @return climates
     * @throws SQLException if an error occurred
     */
    public List<ClimateDTO> selectAll() throws SQLException {
        Connection conn = DBManager.getInstance().getDBConnection();
        String request = "SELECT * FROM Public.\"Climate\"";

        PreparedStatement stmt = conn.prepareStatement(request);

        return retrieveClimates(stmt);
    }

    /**
     * Deletes a climate from the DB
     *
     * @param climate the climate
     * @throws SQLException if an error occurs
     */
    public void delete(ClimateDTO climate) throws SQLException {
        if(select(climate).isEmpty())
            throw new SQLException("The specified ClimateDTO is not persistent");

        Connection conn = DBManager.getInstance().getDBConnection();
        StringBuilder request = new StringBuilder("DELETE FROM Public.\"Climate\"");

        PreparedStatement stmt = getStatement(climate, conn, request);
        stmt.executeUpdate();
    }

    /**
     * Used on select methods
     *
     * @param stmt prepared statement
     * @return the list of records from the DB
     * @throws SQLException if an error occurred
     */
    private List<ClimateDTO> retrieveClimates(PreparedStatement stmt) throws SQLException {
        ResultSet rs = stmt.executeQuery();
        List<ClimateDTO> results = new ArrayList<>();
        while (rs.next()) {
            int id = rs.getInt("Id");
            String description = rs.getString("Description");

            ClimateDTO selected = new ClimateDTO(id, description);

            results.add(selected);
        }
        return results;
    }

    /**
     * Inserts a new climate in the DB
     *
     * @param climate the climate
     * @throws SQLException if an error occurs
     */
    public void insert(ClimateDTO climate) throws SQLException {
        if(climate.isStored())
            throw new SQLException("The specified ClimateDTO is already persistent");

        Connection conn = DBManager.getInstance().getDBConnection();
        String request = "INSERT INTO Public.\"Climate\"(\"Description\") VALUES (?)";

        PreparedStatement stmt = conn.prepareStatement(request);
        stmt.setString(1, climate.getDescription());

        stmt.executeUpdate();
    }

    /**
     * Updates a climate stored in the DB
     *
     * @param oldRecord the old climate informations
     * @param newRecord the updated climate informations
     * @throws SQLException if an error occurs
     */
    public void update(ClimateDTO oldRecord, ClimateDTO newRecord) throws SQLException {
        if(!oldRecord.isStored())
            throw new SQLException("The specified ClimateDTO is not persistent");

        Connection conn = DBManager.getInstance().getDBConnection();
        StringBuilder request = new StringBuilder("UPDATE Public.\"Climate\" SET \"Description\" = ?");

        PreparedStatement stmt = getStatement(oldRecord, newRecord, conn, request);

        stmt.executeUpdate();
    }

    /**
     * Returns a prepared statement (including all "ADD" and "WHERE" clauses)
     *
     * @param record the record
     * @param conn connection
     * @param request the current request string builder
     * @return the prepared statement
     * @throws SQLException if an error occurred
     */
    private PreparedStatement getStatement(ClimateDTO record, Connection conn, StringBuilder request) throws SQLException {
        return getPreparedStatement(record, record, conn, request);
    }

    /**
     * Returns a prepared statement (including all "ADD" and "WHERE" clauses)
     * Used for "UPDATE" requests
     *
     * @param oldRecord the old record
     * @param newRecord the new record
     * @param conn connection
     * @param request the current request string builder
     * @return the prepared statement
     * @throws SQLException if an error occurred
     */
    private PreparedStatement getStatement(ClimateDTO oldRecord, ClimateDTO newRecord, Connection conn, StringBuilder request) throws SQLException {
        return getPreparedStatement(oldRecord, newRecord, conn, request);
    }

    private PreparedStatement getPreparedStatement(ClimateDTO oldRecord, ClimateDTO newRecord, Connection conn, StringBuilder request) throws SQLException {
        List<String> subRequest = new ArrayList<>();
        if(oldRecord.getId() != null)
            subRequest.add("\"Id\" = ?");
        if(oldRecord.getDescription() != null)
            subRequest.add("\"Description\" = ?");

        Utils.fillSQL(request, subRequest);
        PreparedStatement stmt = conn.prepareStatement(request.toString());

        int i = 1;
        if(newRecord.getId() != null)
            stmt.setInt(i++, newRecord.getId());
        if(newRecord.getDescription() != null)
            stmt.setString(i, newRecord.getDescription());

        return stmt;
    }
}

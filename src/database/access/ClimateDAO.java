package database.access;

import common.Utils;
import database.transfer.ClimateDTO;

import java.sql.*;
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

        prepareStringBuilderStatement(false, climate, conn, request);
        PreparedStatement stmt = getPreparedStatement(climate, conn, request);

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

        prepareStringBuilderStatement(false, climate, conn, request);
        PreparedStatement stmt = getPreparedStatement(climate, conn, request);

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
     * @param oldRecord the old climate information
     * @param newRecord the updated climate information
     * @throws SQLException if an error occurs
     */
    public void update(ClimateDTO oldRecord, ClimateDTO newRecord) throws SQLException {
        if(select(oldRecord).isEmpty())
            throw new SQLException("The specified ClimateDTO is not persistent");

        Connection conn = DBManager.getInstance().getDBConnection();
        StringBuilder request = new StringBuilder("UPDATE Public.\"Climate\"");

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
    private void prepareStringBuilderStatement(boolean type, ClimateDTO record, Connection conn, StringBuilder request) throws SQLException {
        List<String> subRequest = new ArrayList<>();
        if(record.getId() != null)
            subRequest.add("\"Id\" = ?");
        if(record.getDescription() != null)
            subRequest.add("\"Description\" = ?");

        if(type)
            Utils.fillSQLUpdate(request, subRequest);
        else
            Utils.fillSQLSelect(request, subRequest);
    }

    /**
     * Returns the preparedStatement of a selection request
     *
     * @param record
     * @param conn
     * @param request
     * @return
     * @throws SQLException
     */
    private PreparedStatement getPreparedStatement(ClimateDTO record, Connection conn, StringBuilder request) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(request.toString());
        int i = 1;

        // WHERE ...
        prepareStatement(record, stmt, i);

        return stmt;
    }

    /**
     * Returns the prepared statement of an update request
     *
     * @param oldRecord
     * @param newRecord
     * @param conn
     * @param request
     * @return
     * @throws SQLException
     */
    private PreparedStatement getPreparedStatement(ClimateDTO oldRecord, ClimateDTO newRecord
            , Connection conn, StringBuilder request) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(request.toString());
        int i = 1;

        // SET ...
        i = prepareStatement(newRecord,stmt,i);
        // WHERE ...
        prepareStatement(oldRecord, stmt, i);

        return stmt;
    }

    private int prepareStatement(ClimateDTO record, PreparedStatement stmt, int i) throws SQLException {
        if (record.getId() != null)
            stmt.setInt(i++, record.getId());
        if (record.getDescription() != null)
            stmt.setString(i++, record.getDescription());
        return i;
    }
}

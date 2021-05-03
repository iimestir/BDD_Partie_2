package database.access;

import common.Utils;
import database.transfer.ProducersDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProducersDAO {
    private static final ProducersDAO instance = new ProducersDAO();

    /**
     * Get the singleton instance
     */
    public static ProducersDAO getInstance() {
        return instance;
    }

    /**
     * Singleton
     */
    private ProducersDAO() {}

    public void insert(ProducersDTO record) throws SQLException {
        if(!select(record).isEmpty())
            throw new SQLException("The specified ProducersDTO is already persistent");

        Connection conn = DBManager.getInstance().getDBConnection();
        String request = "INSERT INTO Public.\"Producers\"(\"ISO\",\"Date\",\"Vaccines\")" +
                " VALUES (?,?,?)";

        PreparedStatement stmt = conn.prepareStatement(request);
        stmt.setString(1, record.getId());
        stmt.setDate(2, record.getDate());
        stmt.setArray(3, conn.createArrayOf("text",record.getVaccines()));

        stmt.executeUpdate();
    }

    /**
     * Selects a producers row in the DB
     *
     * @param record the producer row
     * @return list of corresponding records
     * @throws SQLException if an error occurs
     */
    public List<ProducersDTO> select(ProducersDTO record) throws SQLException {
        Connection conn = DBManager.getInstance().getDBConnection();
        StringBuilder request = new StringBuilder("SELECT * FROM Public.\"Producers\"");

        prepareStringBuilderStatement(false, record, conn, request);
        PreparedStatement stmt = getPreparedStatement(record, conn, request);

        return retrieveProducers(stmt);
    }

    /**
     * Selects all records from the DB
     *
     * @return producers records
     * @throws SQLException if an error occurs
     */
    public List<ProducersDTO> selectAll() throws SQLException {
        Connection conn = DBManager.getInstance().getDBConnection();
        String request = "SELECT * FROM Public.\"Producers\"";

        PreparedStatement stmt = conn.prepareStatement(request);

        return retrieveProducers(stmt);
    }

    /**
     * Deletes a record from the DB
     *
     * @param record the record
     * @throws SQLException if an error occurs
     */
    public void delete(ProducersDTO record) throws SQLException {
        if(select(record).isEmpty())
            throw new SQLException("The specified ProducersDTO is not persistent");

        Connection conn = DBManager.getInstance().getDBConnection();
        StringBuilder request = new StringBuilder("DELETE FROM Public.\"Producers\"");

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
    private List<ProducersDTO> retrieveProducers(PreparedStatement stmt) throws SQLException {
        ResultSet rs = stmt.executeQuery();
        List<ProducersDTO> results = new ArrayList<>();
        while (rs.next()) {
            String id = rs.getString("ISO");
            Date name = rs.getDate("Date");
            String[] vaccines = ((String[]) rs.getArray("Vaccines").getArray());

            ProducersDTO selected = new ProducersDTO(id, name, vaccines);

            results.add(selected);
        }
        return results;
    }

    /**
     * Updates a record from the DB
     *
     * @param oldRecord the record
     * @throws SQLException if an error occurs
     */
    public void update(ProducersDTO oldRecord, ProducersDTO newRecord) throws SQLException {
        if(select(oldRecord).isEmpty())
            throw new SQLException("The specified ProducersDTO is not persistent");

        Connection conn = DBManager.getInstance().getDBConnection();
        StringBuilder request = new StringBuilder("UPDATE Public.\"Producers\"");

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
    private void prepareStringBuilderStatement(boolean type, ProducersDTO record, Connection conn, StringBuilder request) throws SQLException {
        List<String> subRequest = new ArrayList<>();
        if(record.getId() != null)
            subRequest.add("\"ISO\" = ?");
        if(record.getDate() != null)
            subRequest.add("\"Date\" = ?");
        if(record.getVaccines() != null)
            subRequest.add("\"Vaccines\" = ?");

        if(type)
            Utils.fillSQLSet(request, subRequest);
        else
            Utils.fillSQLWhere(request, subRequest);
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
    private PreparedStatement getPreparedStatement(ProducersDTO record, Connection conn, StringBuilder request) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(request.toString());
        int i = 1;

        // WHERE ...
        prepareStatement(record, conn, stmt, i);

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
    private PreparedStatement getPreparedStatement(ProducersDTO oldRecord, ProducersDTO newRecord
            , Connection conn, StringBuilder request) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(request.toString());
        int i = 1;

        // SET ...
        i = prepareStatement(newRecord, conn, stmt, i);
        // WHERE ...
        prepareStatement(oldRecord, conn, stmt, i);

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
    private int prepareStatement(ProducersDTO record, Connection conn, PreparedStatement stmt, int i) throws SQLException {
        if(record.getId() != null)
            stmt.setString(i++, record.getId());
        if(record.getDate() != null)
            stmt.setDate(i++, record.getDate());
        if(record.getVaccines() != null)
            stmt.setArray(i++, conn.createArrayOf("text",record.getVaccines()));
        return i;
    }
}

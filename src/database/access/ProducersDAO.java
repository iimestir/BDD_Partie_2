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

        PreparedStatement stmt = getStatement(record, conn, request);

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

        PreparedStatement stmt = getStatement(record, conn, request);
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
        if(!oldRecord.isStored())
            throw new SQLException("The specified ProducersDTO is not persistent");

        Connection conn = DBManager.getInstance().getDBConnection();
        StringBuilder request = new StringBuilder("UPDATE Public.\"Producers\" SET " +
                "\"Date\" = ?,\"Vaccines\" = ?");

        PreparedStatement stmt = getStatement(3, oldRecord, conn, request);
        updateStatement(conn, stmt, newRecord);
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
    private PreparedStatement getStatement(ProducersDTO record, Connection conn, StringBuilder request) throws SQLException {
        return getPreparedStatement(1, record, conn, request);
    }

    /**
     * Returns a prepared statement (including all "ADD" and "WHERE" clauses)
     * Used for "UPDATE" requests
     *
     * @param d number of parameters before those that will be declared
     * @param oldRecord the old record
     * @param conn connection
     * @param request the current request string builder
     * @return the prepared statement
     * @throws SQLException if an error occurred
     */
    private PreparedStatement getStatement(int d, ProducersDTO oldRecord, Connection conn, StringBuilder request) throws SQLException {
        return getPreparedStatement(d, oldRecord, conn, request);
    }

    private void updateStatement(Connection conn, PreparedStatement stmt, ProducersDTO newRecord) throws SQLException {
        int i = 1;

        if(newRecord.getId() != null)
            stmt.setString(i++, newRecord.getId());
        if(newRecord.getDate() != null)
            stmt.setDate(i++, newRecord.getDate());
        if(newRecord.getVaccines() != null)
            stmt.setArray(i, conn.createArrayOf("text",newRecord.getVaccines()));

        stmt.executeUpdate();
    }

    private PreparedStatement getPreparedStatement(int d, ProducersDTO oldRecord, Connection conn, StringBuilder request) throws SQLException {
        List<String> subRequest = new ArrayList<>();
        if(oldRecord.getId() != null)
            subRequest.add("\"ISO\" = ?");
        if(oldRecord.getDate() != null)
            subRequest.add("\"Date\" = ?");
        if(oldRecord.getVaccines() != null)
            subRequest.add("\"Vaccines\" = ?");

        Utils.fillSQLSelect(request, subRequest);
        PreparedStatement stmt = conn.prepareStatement(request.toString());

        int i = d;
        if(oldRecord.getId() != null)
            stmt.setString(i++, oldRecord.getId());
        if(oldRecord.getDate() != null)
            stmt.setDate(i++, oldRecord.getDate());
        if(oldRecord.getVaccines() != null)
            stmt.setArray(i, conn.createArrayOf("text",oldRecord.getVaccines()));

        return stmt;
    }
}

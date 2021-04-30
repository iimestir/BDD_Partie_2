package database.access;

import common.Utils;
import database.transfer.VaccinationsDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VaccinationsDAO {
    private static final VaccinationsDAO instance = new VaccinationsDAO();

    /**
     * Get the singleton instance
     */
    public static VaccinationsDAO getInstance() {
        return instance;
    }

    /**
     * Singleton
     */
    private VaccinationsDAO() {}

    /**
     * Inserts a new record in the DB
     *
     * @param record the record to insert
     * @throws SQLException if an error occurs
     */
    public void insert(VaccinationsDTO record) throws SQLException {
        if(!select(record).isEmpty())
            throw new SQLException("The specified VaccinationsDTO is already persistent");

        Connection conn = DBManager.getInstance().getDBConnection();
        String request = "INSERT INTO Public.\"Vaccinations\"(\"ISO\",\"Date\",\"Tests\",\"Vaccinations\") " +
                "VALUES (?,?,?,?)";

        PreparedStatement stmt = conn.prepareStatement(request);
        stmt.setString(1, record.getISO());
        stmt.setDate(2, record.getDate());
        stmt.setInt(3, record.getTests());
        stmt.setInt(4, record.getVaccinations());

        stmt.executeUpdate();
    }

    /**
     * Selects records from the DB
     *
     * @param record records information
     * @return records list
     * @throws SQLException if an error occurs
     */
    public List<VaccinationsDTO> select(VaccinationsDTO record) throws SQLException {
        Connection conn = DBManager.getInstance().getDBConnection();
        StringBuilder request = new StringBuilder("SELECT * FROM Public.\"Vaccinations\"");

        PreparedStatement stmt = getStatement(record, conn, request);

        return retrieveCountries(stmt);
    }

    /**
     * Selects all records
     *
     * @return records list
     * @throws SQLException if an error occurs
     */
    public List<VaccinationsDTO> selectAll() throws SQLException {
        Connection conn = DBManager.getInstance().getDBConnection();
        String request = "SELECT * FROM Public.\"Vaccinations\"";

        PreparedStatement stmt = conn.prepareStatement(request);

        return retrieveCountries(stmt);
    }

    /**
     * Deletes records from the DB
     *
     * @param record records information
     * @throws SQLException if an error occurs
     */
    public void delete(VaccinationsDTO record) throws SQLException {
        if(select(record).isEmpty())
            throw new SQLException("The specified VaccinationsDTO is not persistent");

        Connection conn = DBManager.getInstance().getDBConnection();
        StringBuilder request = new StringBuilder("DELETE FROM Public.\"Vaccinations\"");

        PreparedStatement stmt = getStatement(record, conn, request);

        stmt.executeUpdate();
    }

    /**
     * Updates a record from the DB
     *
     * @param oldRecord the criteria
     * @param newRecord the updated information
     * @throws SQLException if an error occurs
     */
    public void update(VaccinationsDTO oldRecord, VaccinationsDTO newRecord) throws SQLException {
        Connection conn = DBManager.getInstance().getDBConnection();
        StringBuilder request = new StringBuilder("UPDATE Public.\"Vaccinations\" SET " +
                "\"ISO\" = ?,\"Date\" = ?,\"Tests\" = ?,\"Vaccinations\" = ?");

        PreparedStatement stmt = getStatement(5, oldRecord, conn, request);
        updateStatement(stmt, newRecord);
    }

    /**
     * Used on select methods
     *
     * @param stmt prepared statement
     * @return the list of records from the DB
     * @throws SQLException if an error occurred
     */
    private List<VaccinationsDTO> retrieveCountries(PreparedStatement stmt) throws SQLException {
        ResultSet rs = stmt.executeQuery();
        List<VaccinationsDTO> results = new ArrayList<>();
        while(rs.next()) {
            String ISO = rs.getString("ISO");
            Date date = rs.getDate("Date");
            Integer tests = rs.getInt("Tests");
            Integer vaccinations = rs.getInt("Vaccinations");

            VaccinationsDTO selected = new VaccinationsDTO(ISO, date, tests, vaccinations);

            results.add(selected);
        }

        return results;
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
    private PreparedStatement getStatement(VaccinationsDTO record, Connection conn, StringBuilder request) throws SQLException {
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
    private PreparedStatement getStatement(int d, VaccinationsDTO oldRecord,  Connection conn, StringBuilder request) throws SQLException {
        return getPreparedStatement(d, oldRecord, conn, request);
    }

    private void updateStatement(PreparedStatement stmt, VaccinationsDTO newRecord) throws SQLException {
        int i = 1;

        if(newRecord.getISO() != null)
            stmt.setString(i++, newRecord.getISO());
        if(newRecord.getDate() != null)
            stmt.setDate(i++, newRecord.getDate());
        if(newRecord.getTests() != null)
            stmt.setInt(i++, newRecord.getTests());
        if(newRecord.getVaccinations() != null)
            stmt.setInt(i, newRecord.getVaccinations());

        stmt.executeUpdate();
    }

    private PreparedStatement getPreparedStatement(int d, VaccinationsDTO oldRecord, Connection conn, StringBuilder request) throws SQLException {
        List<String> subRequest = new ArrayList<>();
        if(oldRecord.getISO() != null)
            subRequest.add("\"ISO\" = ?");
        if(oldRecord.getDate() != null)
            subRequest.add("\"Date\" = ?");
        if(oldRecord.getTests() != null)
            subRequest.add("\"Tests\" = ?");
        if(oldRecord.getVaccinations() != null)
            subRequest.add("\"Vaccinations\" = ?");

        Utils.fillSQLSelect(request, subRequest);
        PreparedStatement stmt = conn.prepareStatement(request.toString());

        int i = d;
        if(oldRecord.getISO() != null)
            stmt.setString(i++, oldRecord.getISO());
        if(oldRecord.getDate() != null)
            stmt.setDate(i++, oldRecord.getDate());
        if(oldRecord.getTests() != null)
            stmt.setInt(i++, oldRecord.getTests());
        if(oldRecord.getVaccinations() != null)
            stmt.setInt(i, oldRecord.getVaccinations());

        return stmt;
    }

}

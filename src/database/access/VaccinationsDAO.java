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

        prepareStringBuilderStatement(false, record, conn, request);
        PreparedStatement stmt = getPreparedStatement(record, conn, request);

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

        prepareStringBuilderStatement(false, record, conn, request);
        PreparedStatement stmt = getPreparedStatement(record, conn, request);

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
        StringBuilder request = new StringBuilder("UPDATE Public.\"Vaccinations\"");

        prepareStringBuilderStatement(true, newRecord, conn, request);
        prepareStringBuilderStatement(false, oldRecord, conn, request);

        PreparedStatement stmt = getPreparedStatement(oldRecord, newRecord, conn, request);

        stmt.executeUpdate();
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
     * @param type request type
     * @param record the record
     * @param conn connection
     * @param request the current request string builder
     * @throws SQLException if an error occurred
     */
    private void prepareStringBuilderStatement(boolean type, VaccinationsDTO record, Connection conn, StringBuilder request) throws SQLException {
        List<String> subRequest = new ArrayList<>();
        if(record.getISO() != null)
            subRequest.add("\"ISO\" = ?");
        if(record.getDate() != null)
            subRequest.add("\"Date\" = ?");
        if(record.getTests() != null)
            subRequest.add("\"Tests\" = ?");
        if(record.getVaccinations() != null)
            subRequest.add("\"Vaccinations\" = ?");

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
    private PreparedStatement getPreparedStatement(VaccinationsDTO record, Connection conn, StringBuilder request) throws SQLException {
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
    private PreparedStatement getPreparedStatement(VaccinationsDTO oldRecord, VaccinationsDTO newRecord
            , Connection conn, StringBuilder request) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(request.toString());
        int i = 1;

        // SET ...
        i = prepareStatement(newRecord, stmt, i);
        // WHERE ...
        prepareStatement(oldRecord, stmt, i);

        return stmt;
    }

    private int prepareStatement(VaccinationsDTO record, PreparedStatement stmt, int i) throws SQLException {
        if(record.getISO() != null)
            stmt.setString(i++, record.getISO());
        if(record.getDate() != null)
            stmt.setDate(i++, record.getDate());
        if(record.getTests() != null)
            stmt.setInt(i++, record.getTests());
        if(record.getVaccinations() != null)
            stmt.setInt(i++, record.getVaccinations());
        return i;
    }
}

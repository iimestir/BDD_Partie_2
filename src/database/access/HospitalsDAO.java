package database.access;

import common.Utils;
import database.transfer.HospitalsDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class HospitalsDAO {
    private static final HospitalsDAO instance = new HospitalsDAO();

    /**
     * Get the singleton instance
     */
    public static HospitalsDAO getInstance() {
        return instance;
    }

    /**
     * Singleton
     */
    private HospitalsDAO() {}

    /**
     * Selects records from the DB
     *
     * @param record records information
     * @return records list
     * @throws SQLException if an error occurs
     */
    public List<HospitalsDTO> select(HospitalsDTO record) throws SQLException {
        Connection conn = DBManager.getInstance().getDBConnection();
        StringBuilder request = new StringBuilder("SELECT * FROM Public.\"Hospitals\"");

        prepareStringBuilderStatement(false, record, conn, request);
        PreparedStatement stmt = getPreparedStatement(record, conn, request);

        return retrieveHospitals(stmt);
    }

    /**
     * Selects all records
     *
     * @return records list
     * @throws SQLException if an error occurs
     */
    public List<HospitalsDTO> selectAll() throws SQLException {
        Connection conn = DBManager.getInstance().getDBConnection();
        String request = "SELECT * FROM Public.\"Hospitals\"";

        PreparedStatement stmt = conn.prepareStatement(request);

        return retrieveHospitals(stmt);
    }

    /**
     * Deletes a record from the DB
     *
     * @param record the record
     * @throws SQLException if an error occurs
     */
    public void delete(HospitalsDTO record) throws SQLException {
        if(select(record).isEmpty())
            throw new SQLException("The specified CountryDTO is not persistent");

        Connection conn = DBManager.getInstance().getDBConnection();
        StringBuilder request = new StringBuilder("DELETE FROM Public.\"Hospitals\"");

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
    public void update(HospitalsDTO oldRecord, HospitalsDTO newRecord) throws SQLException {
        if(select(oldRecord).isEmpty())
            throw new SQLException("The specified CountryDTO is not persistent");

        Connection conn = DBManager.getInstance().getDBConnection();
        StringBuilder request = new StringBuilder("UPDATE Public.\"Hospitals\"");

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
    private List<HospitalsDTO> retrieveHospitals(PreparedStatement stmt) throws SQLException {
        ResultSet rs = stmt.executeQuery();
        List<HospitalsDTO> results = new ArrayList<>();
        while(rs.next()) {
            String ISO = rs.getString("ISO");
            Date date = rs.getDate("Date");
            Integer icu_patients = rs.getInt("icu_patients");
            Integer hosp_patients = rs.getInt("hosp_patients");
            UUID epidemiologistUUID = UUID.fromString(rs.getString("epidemiologist"));

            HospitalsDTO selected = new HospitalsDTO(ISO, date, icu_patients, hosp_patients, epidemiologistUUID);

            results.add(selected);
        }

        return results;
    }

    /**
     * Inserts a new record in the DB
     *
     * @param record the record to insert
     * @throws SQLException if an error occurs
     */
    public void insert(HospitalsDTO record) throws SQLException {
        if(!select(record).isEmpty())
            throw new SQLException("The specified HospitalsDTO is already persistent");

        Connection conn = DBManager.getInstance().getDBConnection();
        String request = "INSERT INTO Public.\"Hospitals\"(\"ISO\",\"Date\",\"icu_patients\",\"hosp_patients\",\"epidemiologist\") " +
                "VALUES (?,?,?,?,?)";

        PreparedStatement stmt = conn.prepareStatement(request);
        stmt.setString(1, record.getISO());
        stmt.setDate(2, record.getDate());
        stmt.setInt(3, record.getIcu_patients());
        stmt.setInt(4, record.getHosp_patients());
        stmt.setObject(5, record.getEpidemiologistUUID());

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
    private void prepareStringBuilderStatement(boolean type, HospitalsDTO record, Connection conn, StringBuilder request) throws SQLException {
        List<String> subRequest = new ArrayList<>();
        if(record.getISO() != null)
            subRequest.add("\"ISO\" = ?");
        if(record.getDate() != null)
            subRequest.add("\"Date\" = ?");
        if(record.getIcu_patients() != null)
            subRequest.add("\"icu_patients\" = ?");
        if(record.getHosp_patients() != null)
            subRequest.add("\"hosp_patients\" = ?");
        if(record.getEpidemiologistUUID() != null)
            subRequest.add("\"epidemiologist\" = ?");

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
    private PreparedStatement getPreparedStatement(HospitalsDTO record, Connection conn, StringBuilder request) throws SQLException {
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
    private PreparedStatement getPreparedStatement(HospitalsDTO oldRecord, HospitalsDTO newRecord
            , Connection conn, StringBuilder request) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(request.toString());
        int i = 1;

        // SET ...
        i = prepareStatement(newRecord,stmt,i);
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
    private int prepareStatement(HospitalsDTO record, PreparedStatement stmt, int i) throws SQLException {
        if(record.getISO() != null)
            stmt.setString(i++, record.getISO());
        if(record.getDate() != null)
            stmt.setDate(i++, record.getDate());
        if(record.getIcu_patients() != null)
            stmt.setInt(i++, record.getIcu_patients());
        if(record.getHosp_patients() != null)
            stmt.setInt(i++, record.getHosp_patients());
        if(record.getEpidemiologistUUID() != null)
            stmt.setObject(i++, record.getEpidemiologistUUID());
        return i;
    }
}

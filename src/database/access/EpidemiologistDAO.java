package database.access;

import common.Utils;
import database.transfer.EpidemiologistDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EpidemiologistDAO {
    private static final EpidemiologistDAO instance = new EpidemiologistDAO();

    /**
     * Get the singleton instance
     */
    public static EpidemiologistDAO getInstance() {
        return instance;
    }

    /**
     * Singleton
     */
    private EpidemiologistDAO() {}

    /**
     * Used to get an epidemiologist according to a given record
     *
     * @param record the given record
     * @return the corresponding epidemiologist
     * @throws SQLException if an error occurred
     */
    public List<EpidemiologistDTO> select(EpidemiologistDTO record) throws SQLException {
        Connection conn = DBManager.getInstance().getDBConnection();
        StringBuilder request = new StringBuilder("SELECT * FROM Public.\"User\"");

        request.append(" NATURAL JOIN Public.\"Epidemiologist\"");
        prepareStringBuilderStatement(false, record, conn, request);

        PreparedStatement stmt = getPreparedStatement(record, conn, request);

        return retrieveEpidemiologist(stmt);
    }

    /**
     * Updates a user stored in the DB
     *
     * @param oldRecord the criteria
     * @param newRecord the updated information
     * @throws SQLException if an error occurs
     */
    public void update(EpidemiologistDTO oldRecord, EpidemiologistDTO newRecord) throws SQLException {
        // Updates user table
        UserDAO.getInstance().update(oldRecord, newRecord);

        Connection conn = DBManager.getInstance().getDBConnection();

        // Updates epidemiologist table
        StringBuilder request = new StringBuilder("UPDATE Public.\"Epidemiologist\"");
        List<String> subRequestSet = new ArrayList<>();
        List<String> subRequestWhere = new ArrayList<>();
        if(newRecord.getCenter() != null)
            subRequestSet.add("\"Center\" = ?");
        if(newRecord.getServiceNumber() != null)
            subRequestSet.add("\"Service Phone\" = ?");
        if(oldRecord.getId() != null)
            subRequestWhere.add("\"UUID\" = ?");

        Utils.fillSQLSet(request, subRequestSet);
        Utils.fillSQLWhere(request, subRequestWhere);

        PreparedStatement stmt = conn.prepareStatement(request.toString());

        int i = 1;
        if(newRecord.getCenter() != null)
            stmt.setString(i++, newRecord.getCenter());
        if(newRecord.getServiceNumber() != null)
            stmt.setString(i++, newRecord.getServiceNumber());
        if(oldRecord.getId() != null)
            stmt.setObject(i++, oldRecord.getId());

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
    private void prepareStringBuilderStatement(boolean type, EpidemiologistDTO record, Connection conn, StringBuilder request) throws SQLException {
        List<String> subRequest = new ArrayList<>();
        if(record.getId() != null)
            subRequest.add("\"UUID\" = ?");
        if(record.getFirstName() != null)
            subRequest.add("\"Firstname\" = ?");
        if(record.getLastName() != null)
            subRequest.add("\"Lastname\" = ?");
        if(record.getStreet() != null)
            subRequest.add("\"Street\" = ?");
        if(record.getDoorNumber() != null)
            subRequest.add("\"Doornumber\" = ?");
        if(record.getCity() != null)
            subRequest.add("\"City\" = ?");
        if(record.getZipCode() != null)
            subRequest.add("\"ZIP\" = ?");
        if(record.getCenter() != null)
            subRequest.add("\"Center\" = ?");
        if(record.getServiceNumber() != null)
            subRequest.add("\"Service Phone\" = ?");

        if(type)
            Utils.fillSQLSet(request, subRequest);
        else
            Utils.fillSQLWhere(request, subRequest);
    }

    /**
     * Returns the prepared statement of an update request
     *
     * @param record the record
     * @param conn the DB connection
     * @param request the request string builder
     * @return the prepared statement
     * @throws SQLException if an error occurs
     */
    private PreparedStatement getPreparedStatement(EpidemiologistDTO record, Connection conn, StringBuilder request) throws SQLException {
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
    private PreparedStatement getPreparedStatement(EpidemiologistDTO oldRecord, EpidemiologistDTO newRecord
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
    private int prepareStatement(EpidemiologistDTO record, PreparedStatement stmt, int i) throws SQLException {
        if(record.getId() != null)
            stmt.setObject(i++, record.getId());
        if(record.getFirstName() != null)
            stmt.setString(i++, record.getFirstName());
        if(record.getLastName() != null)
            stmt.setString(i++, record.getLastName());
        if(record.getStreet() != null)
            stmt.setString(i++, record.getStreet());
        if(record.getDoorNumber() != null)
            stmt.setInt(i++, record.getDoorNumber());
        if(record.getCity() != null)
            stmt.setString(i++, record.getCity());
        if(record.getZipCode() != null)
            stmt.setString(i++, record.getZipCode());
        if(record.getCenter() != null)
            stmt.setString(i++, record.getCenter());
        if(record.getServiceNumber() != null)
            stmt.setString(i++, record.getServiceNumber());
        return i;
    }

    /**
     * Used on select methods
     *
     * @param stmt prepared statement
     * @return the list of records from the DB
     * @throws SQLException if an error occurred
     */
    private List<EpidemiologistDTO> retrieveEpidemiologist(PreparedStatement stmt) throws SQLException {
        ResultSet rs = stmt.executeQuery();
        List<EpidemiologistDTO> results = new ArrayList<>();

        while (rs.next()) {
            EpidemiologistDTO epidemiologist;
            UUID uuid = UUID.fromString(rs.getString("UUID"));

            // Epidemiologist
            epidemiologist = new EpidemiologistDTO(
                    uuid,
                    rs.getString("Firstname"),
                    rs.getString("Lastname"),
                    rs.getString("Street"),
                    rs.getInt("Doornumber"),
                    rs.getString("City"),
                    rs.getString("ZIP"),
                    rs.getString("Center"),
                    rs.getString("Service Phone")
            );

            results.add(epidemiologist);
        }

        return results;
    }
}

package database.access;

import common.LoginToken;
import common.Utils;
import database.transfer.EpidemiologistDTO;
import database.transfer.UserDTO;
import javafx.util.Pair;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Singleton class that handles the DB communication around the User table
 */
public class UserDAO {
    private static final UserDAO instance = new UserDAO();

    /**
     * Get the singleton instance
     */
    public static UserDAO getInstance() {
        return instance;
    }

    /**
     * Singleton
     */
    private UserDAO() {}

    /**
     * Insert a new user into the database
     *
     * @param user userDTO
     * @param username username
     * @param password password
     * @throws SQLException if an error occurs
     */
    public void insert(UserDTO user, String username, String password) throws SQLException {
        if(user.isStored())
            throw new SQLException("The specified UserDTO is already persistent");

        Connection conn = DBManager.getInstance().getDBConnection();

        String request = "INSERT INTO Public.\"User\"(\"Firstname\",\"Lastname\",\"Username\","
                + "\"Password\",\"Street\",\"Doornumber\",\"City\",\"ZIP\")"
                + " VALUES (?,?,crypt(?, gen_salt('bf')),crypt(?, gen_salt('bf')),?,?,?,?)";

        PreparedStatement stmt = conn.prepareStatement(request);
        stmt.setString(1,user.getFirstName());
        stmt.setString(2,user.getLastName());
        stmt.setString(3,username);
        stmt.setString(4,password);
        stmt.setString(5,user.getStreet());
        stmt.setInt(6,user.getDoorNumber());
        stmt.setString(7,user.getCity());
        stmt.setString(8,user.getZipCode());

        stmt.executeUpdate();
    }

    /**
     * Defines the user as an epidemiologist
     */
    public void defineEpidemiologist(EpidemiologistDTO user, UUID id)
            throws SQLException {
        Connection conn = DBManager.getInstance().getDBConnection();

        String request = "INSERT INTO Public.\"Epidemiologist\"(\"UUID\", \"Center\", \"Service Phone\""
                + ") VALUES (?,?,?)";

        PreparedStatement stmt = conn.prepareStatement(request);
        stmt.setObject(1, id);
        stmt.setString(2, user.getCenter());
        stmt.setString(3, user.getServiceNumber());

        stmt.executeUpdate();
    }

    /**
     * Returns the user ID using its username and password
     *
     * @param username username
     * @param password password
     * @return the user ID
     * @throws SQLException if an error occurred
     */
    public UUID getUserId(String username, String password) throws SQLException {
        return select(username, password).getId();
    }

    /**
     * Deletes the user account
     *
     * @param user User
     */
    public void delete(UserDTO user) throws SQLException {
        if(select(user).isEmpty())
            throw new SQLException("The specified ClimateDTO is not persistent");

        Connection conn = DBManager.getInstance().getDBConnection();
        StringBuilder request = new StringBuilder("DELETE FROM Public.\"User\"");

        prepareStringBuilderStatement(false, user, conn, request);
        PreparedStatement stmt = getPreparedStatement(user, conn, request);

        stmt.executeUpdate();
    }

    /**
     * Used to get a user according to the given username and password
     *
     * @param username the username
     * @param password the password
     * @return the corresponding user
     * @throws SQLException if an error occurred
     */
    public UserDTO select(String username, String password) throws SQLException {
        Connection conn = DBManager.getInstance().getDBConnection();
        String request = "SELECT * FROM Public.\"User\"" +
                " WHERE \"Username\" = crypt(?, \"Username\") AND \"Password\" = crypt(?, \"Password\")";

        PreparedStatement stmt = conn.prepareStatement(request);
        stmt.setString(1, username);
        stmt.setString(2, password);

        ResultSet rs = stmt.executeQuery();

        if(rs.next()) {
            UserDTO user;
            UUID uuid = UUID.fromString(rs.getString("UUID"));

            Pair<String,String> epidemiologistInfos = getEpidemiologistInfos(uuid);

            if(epidemiologistInfos != null) {
                // Epidemiologist
                user = new EpidemiologistDTO(
                        uuid,
                        rs.getString("Firstname"),
                        rs.getString("Lastname"),
                        rs.getString("Street"),
                        rs.getInt("Doornumber"),
                        rs.getString("City"),
                        rs.getString("ZIP"),
                        epidemiologistInfos.getKey(),
                        epidemiologistInfos.getValue()
                );
            } else {
                // User
                user = new UserDTO(
                        uuid,
                        rs.getString("Firstname"),
                        rs.getString("Lastname"),
                        rs.getString("Street"),
                        rs.getInt("Doornumber"),
                        rs.getString("City"),
                        rs.getString("ZIP")
                );
            }

            return user;
        }

        throw new SQLException(Utils.getTranslatedString("login_error"));
    }

    /**
     * Checks if the UUID is linked to an epidemiologist account
     *
     * @param uuid the ID
     * @return epidemiologist infos
     * @throws SQLException if an error occurred
     */
    private Pair<String, String> getEpidemiologistInfos(UUID uuid) throws SQLException {
        Connection conn = DBManager.getInstance().getDBConnection();

        String request = "SELECT * FROM Public.\"Epidemiologist\"" +
                " WHERE \"UUID\" = ?";

        PreparedStatement stmt = conn.prepareStatement(request);
        stmt.setObject(1, uuid);

        ResultSet rs = stmt.executeQuery();

        if(rs.next()) {
            String center = rs.getString("Center");
            String number = rs.getString("Service Phone");

            return new Pair<>(center,number);
        }

        return null;
    }


    ///////

    /**
     * Used to get a user according to a given record
     *
     * @param record the given record
     * @return the corresponding user
     * @throws SQLException if an error occurred
     */
    public List<UserDTO> select(UserDTO record) throws SQLException {
        Connection conn = DBManager.getInstance().getDBConnection();
        StringBuilder request = new StringBuilder("SELECT * FROM Public.\"User\"");

        prepareStringBuilderStatement(false, record, conn, request);
        PreparedStatement stmt = getPreparedStatement(record, conn, request);

        return retrieveUsers(stmt);
    }

    /**
     * Updates a user stored in the DB
     *
     * @param oldRecord the criteria
     * @param newRecord the updated information
     * @throws SQLException if an error occurs
     */
    public void update(UserDTO oldRecord, UserDTO newRecord) throws SQLException {
        if(select(oldRecord).isEmpty())
            throw new SQLException("The specified UserDTO is not persistent");

        Connection conn = DBManager.getInstance().getDBConnection();

        StringBuilder request = new StringBuilder("UPDATE Public.\"User\"");

        prepareStringBuilderStatement(true, newRecord, conn, request);
        prepareStringBuilderStatement(false, oldRecord, conn, request);

        PreparedStatement stmt = getPreparedStatement(oldRecord, newRecord, conn, request);

        stmt.executeUpdate();
    }

    /**
     * Updates the password of the current user
     *
     * @param newPassword the new password
     */
    public void updateCurrentUserPassword(String newPassword) throws SQLException {
        UserDTO user = LoginToken.CURRENT_LOGIN.get();

        if(select(user).isEmpty())
            throw new SQLException("The specified UserDTO is not persistent");

        Connection conn = DBManager.getInstance().getDBConnection();
        StringBuilder request = new StringBuilder("UPDATE Public.\"User\" SET Password = crypt(?, gen_salt('bf'))");

        prepareStringBuilderStatement(false, user, conn, request);

        PreparedStatement stmt = conn.prepareStatement(request.toString());

        stmt.setString(1, newPassword);
        prepareStatement(user, stmt, 2);

        stmt.executeUpdate();
    }

    /**
     * Used on select methods
     *
     * @param stmt prepared statement
     * @return the list of records from the DB
     * @throws SQLException if an error occurred
     */
    private List<UserDTO> retrieveUsers(PreparedStatement stmt) throws SQLException {
        ResultSet rs = stmt.executeQuery();
        List<UserDTO> results = new ArrayList<>();

        while (rs.next()) {
            UserDTO user;
            UUID uuid = UUID.fromString(rs.getString("UUID"));
            // User
            user = new UserDTO(
                    uuid,
                    rs.getString("Firstname"),
                    rs.getString("Lastname"),
                    rs.getString("Street"),
                    rs.getInt("Doornumber"),
                    rs.getString("City"),
                    rs.getString("ZIP")
            );

            results.add(user);
        }
        return results;
    }

    /**
     * Returns a prepared statement (including all "ADD" and "WHERE" clauses)
     *
     * @param type request type (true = SET, false = WHERE)
     * @param record the record
     * @param conn connection
     * @param request the current request string builder
     * @throws SQLException if an error occurred
     */
    private void prepareStringBuilderStatement(boolean type, UserDTO record, Connection conn, StringBuilder request) throws SQLException {
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
    private PreparedStatement getPreparedStatement(UserDTO record, Connection conn, StringBuilder request) throws SQLException {
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
    private PreparedStatement getPreparedStatement(UserDTO oldRecord, UserDTO newRecord
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
    private int prepareStatement(UserDTO record, PreparedStatement stmt, int i) throws SQLException {
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
        return i;
    }
}

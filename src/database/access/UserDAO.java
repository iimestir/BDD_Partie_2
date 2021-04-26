package database.access;

import common.Utils;
import database.transfer.EpidemiologistDTO;
import database.transfer.UserDTO;
import javafx.util.Pair;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
     * Updates a user in the database
     *
     * @param user the user account information
     * @param username the current account username
     * @param password the current account password
     * @param newPassword a password if the user changed his password
     * @throws SQLException if an error occurred
     */
    public void update(UserDTO user, String username, String password, String newPassword) throws SQLException {
        if(!user.isStored())
            throw new SQLException("The specified UserDTO is not persistent");

        Connection conn = DBManager.getInstance().getDBConnection();

        boolean isEpidemiologist = user instanceof EpidemiologistDTO;

        String request = "UPDATE Public.\"User\" SET \"Fistname\" = ?,\"Lastname\" = ?,";
        if(newPassword != null) request += "\"Password\" = crypt(?, \"Password\"),";
        request += "\"Street\" = ?,\"Doornumber\" = ?,\"City\" = ?,\"ZIP\" = ?";
        request += " WHERE \"Username\" = crypt(?, \"Username\") AND \"Password\" = crypt(?, \"Password\")";

        PreparedStatement stmt = conn.prepareStatement(request);
        stmt.setString(1,user.getFirstName());
        stmt.setString(2,user.getLastName());

        int i = 3;
        if(newPassword != null) {
            stmt.setString(3, newPassword);
            i = 4;
        }

        stmt.setString(i++, user.getStreet());
        stmt.setInt(i++, user.getDoorNumber());
        stmt.setString(i++, user.getCity());
        stmt.setString(i++, user.getZipCode());
        stmt.setString(i++, username);
        stmt.setString(i, password);

        stmt.executeUpdate();

        if(isEpidemiologist)
            updateEpidemiologist(((EpidemiologistDTO) user));
    }

    /**
     * Updates the epidemiologist table
     *
     * @param epidemiologist epidemiologist
     */
    private void updateEpidemiologist(EpidemiologistDTO epidemiologist) throws SQLException {
        Connection conn = DBManager.getInstance().getDBConnection();

        String request = "UPDATE Public.\"Epidemiologist\" SET \"Center\" = ?,\"Service Phone\" = ?";

        PreparedStatement stmt = conn.prepareStatement(request);
        stmt.setString(1,epidemiologist.getCenter());
        stmt.setString(2,epidemiologist.getServiceNumber());

        stmt.executeUpdate();
    }

    /**
     * Deletes the user account
     *
     * @param user User
     */
    public void delete(UserDTO user) throws SQLException {
        if(!user.isStored())
            throw new SQLException("The specified ClimateDTO is not persistent");

        Connection conn = DBManager.getInstance().getDBConnection();
        String request = "DELETE FROM Public.\"User\" WHERE Id = ?";

        PreparedStatement stmt = conn.prepareStatement(request);
        stmt.setString(1, user.getId().toString());

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
}

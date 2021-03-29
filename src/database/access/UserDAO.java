package database.access;

import database.transfer.EpidemiologistDTO;
import database.transfer.UserDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Handles the DB communication around the User table
 */
public class UserDAO {

    /**
     * Insert a new user into the database
     * @param user userDTO
     * @param username username
     * @param password password
     * @throws SQLException if an error occurs
     */
    public static void insert(UserDTO user, String username, String password) throws SQLException {
        if(user.isStored())
            throw new SQLException("The specified UserDTO is already persistent");

        Connection conn = DBManager.getInstance().getDBConnection();

        boolean isEpidemiologist = user instanceof EpidemiologistDTO;

        String request;
        if(!isEpidemiologist)
            request = "INSERT INTO Public.\"User\"(\"Firstname\",\"Lastname\",\"Username\","
                    + "\"Password\",\"Street\",\"Doornumber\",\"City\",\"ZIP\")"
                    + " VALUES (?,?,?,crypt(?, gen_salt('bf')),?,?,?,?)";
        else
            request = "INSERT INTO Public.\"User\"(\"Firstname\",\"Lastname\",\"Username\","
                    + "\"Password\",\"Street\",\"Doornumber\",\"City\",\"ZIP\",\"Center\",\"Service Phone\",\"Type\")"
                    + " VALUES (?,?,?,crypt(?, gen_salt('bf')),?,?,?,?,?,?,?)";

        PreparedStatement stmt = conn.prepareStatement(request);
        stmt.setString(1,"'"+user.getFirstName()+"'");
        stmt.setString(2,"'"+user.getLastName()+"'");
        stmt.setString(3,"'"+username+"'");
        stmt.setString(4,"'"+password+"'");
        stmt.setString(5,"'"+user.getStreet()+"'");
        stmt.setInt(6,user.getDoorNumber());
        stmt.setString(7,"'"+user.getCity()+"'");
        stmt.setString(8,"'"+user.getZipCode()+"'");

        if(isEpidemiologist) {
            stmt.setString(9,"'"+((EpidemiologistDTO) user).getCenter()+"'");
            stmt.setString(10,"'"+((EpidemiologistDTO) user).getServiceNumber()+"'");
            stmt.setInt(11,1);
        }

        stmt.executeUpdate();
    }

    /**
     * Updates a user in the database
     * @param user the userDTO
     * @param password a password if he changed his password
     * @throws SQLException if an error occurred
     */
    public static void update(UserDTO user, String password) throws SQLException {
        if(!user.isStored())
            throw new SQLException("The specified UserDTO is not persistent");

        Connection conn = DBManager.getInstance().getDBConnection();
        boolean isEpidemiologist = user instanceof EpidemiologistDTO;

        String request;
        if(!isEpidemiologist) {
            // User
            if(password != null) request =
                    "UPDATE Public.\"User\" SET Fistname = ?,Lastname = ?,Password = ?" +
                            ",Street = ?,Doornumber = ?,City = ?,ZIP = ?";
            else request =
                    "UPDATE Public.\"User\" SET Fistname = ?,Lastname = ?" +
                            ",Street = ?,Doornumber = ?,City = ?,ZIP = ?";
        } else {
            // Epidemiologist
            if(password != null) request =
                    "UPDATE Public.\"User\" SET Fistname = ?,Lastname = ?,Password = ?" +
                            ",Street = ?,Doornumber = ?,City = ?,ZIP = ?, Center = ?, Service Phone = ?";
            else request =
                    "UPDATE Public.\"User\" SET Fistname = ?,Lastname = ?" +
                            ",Street = ?,Doornumber = ?,City = ?,ZIP = ?, Center = ?, Service Phone = ?";
        }


        PreparedStatement stmt = conn.prepareStatement(request);
        stmt.setString(1,"'"+user.getFirstName()+"'");
        stmt.setString(2,"'"+user.getLastName()+"'");

        int i = 3;
        if(password != null) {
            stmt.setString(3, "crypt('" + password + "', gen_salt('bf'))");
            i = 4;
        }

        stmt.setString(i++, "'"+user.getStreet()+"'");
        stmt.setInt(i++, user.getDoorNumber());
        stmt.setString(i++, "'"+user.getCity()+"'");
        stmt.setString(i++, "'"+user.getZipCode()+"'");

        if(isEpidemiologist) {
            stmt.setString(i++,"'"+((EpidemiologistDTO) user).getCenter()+"'");
            stmt.setString(i,"'"+((EpidemiologistDTO) user).getServiceNumber()+"'");
        }

        stmt.executeUpdate();
    }

    public static void delete(UserDTO user) {
        // TODO
    }

    /**
     * Used to get a user according to the given username and password
     * @param username the username
     * @param password the password
     * @return the corresponding user
     * @throws SQLException if an error occurred
     */
    public static UserDTO select(String username, String password) throws SQLException {
        Connection conn = DBManager.getInstance().getDBConnection();
        String request = "SELECT * FROM public.\"User\"" +
                " WHERE \"Username\" = ? AND \"Password\" = crypt(?, \"Password\")";

        PreparedStatement stmt = conn.prepareStatement(request);
        stmt.setString(1, "'"+username+"'");
        stmt.setString(2, "'"+password+"'");

        ResultSet rs = stmt.executeQuery();
        rs.next();

        UserDTO user;
        int accountType = rs.getInt("Type");
        if(accountType == 0)
            // User
            user = new UserDTO(
                    rs.getInt("UUID"),
                    rs.getString("Firstname"),
                    rs.getString("Lastname"),
                    rs.getString("Street"),
                    rs.getInt("Doornumber"),
                    rs.getString("City"),
                    rs.getString("ZIP")
            );
        else
            // Epidemiologist
            user = new EpidemiologistDTO(
                    rs.getInt("UUID"),
                    rs.getString("Firstname"),
                    rs.getString("Lastname"),
                    rs.getString("Street"),
                    rs.getInt("Doornumber"),
                    rs.getString("City"),
                    rs.getString("ZIP"),
                    rs.getString("Center"),
                    rs.getString("Service Phone")
            );

        return user;
    }
}

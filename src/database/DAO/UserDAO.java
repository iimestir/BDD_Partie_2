package database.DAO;

import database.DTO.UserDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    /**
     * Insert a new user into the database
     * @param user userDTO
     * @param username username
     * @param password password
     * @return the new user ID
     * @throws SQLException if an error occurs
     */
    public static int insert(UserDTO user, String username, String password) throws SQLException {
        Connection conn = DBManager.getInstance().getDBConnection();

        String request =
                "INSERT INTO Public.\"User\"(\"Firstname\",\"Lastname\",\"Username\"," +
                        "\"Password\",\"Street\",\"Doornumber\",\"City\",\"ZIP\")" +
                        " VALUES (?,?,?,crypt(?, gen_salt('bf')),?,?,?,?)";

        PreparedStatement stmt = conn.prepareStatement(request);
        stmt.setString(1,"'"+user.getFirstName()+"'");
        stmt.setString(2,"'"+user.getLastName()+"'");
        stmt.setString(3,"'"+username+"'");
        stmt.setString(4,"'"+password+"'");
        stmt.setString(5,"'"+user.getStreet()+"'");
        stmt.setInt(6,user.getDoorNumber());
        stmt.setString(7,"'"+user.getCity()+"'");
        stmt.setString(8,"'"+user.getZipCode()+"'");

        stmt.executeUpdate();

        // TODO : return the id
        return -1;
    }

    /**
     * Updates a user in the database
     * @param user the userDTO
     * @param password a password if he changed his password
     * @throws SQLException if an error occurred
     */
    public static void update(UserDTO user, String password) throws SQLException {
        Connection conn = DBManager.getInstance().getDBConnection();
        String request = "";

        if(password != null) request =
                    "UPDATE Public.\"User\" SET Fistname = ?,Lastname = ?,Password = ?,Street = ?,Doornumber = ?,City = ?,ZIP = ?";
        else request =
                    "UPDATE Public.\"User\" SET Fistname = ?,Lastname = ?,Street = ?,Doornumber = ?,City = ?,ZIP = ?";

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
        stmt.setString(i, "'"+user.getZipCode()+"'");

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

        return new UserDTO(
                rs.getInt("UUID"),
                rs.getString("Firstname"),
                rs.getString("Lastname"),
                rs.getString("Street"),
                rs.getInt("Doornumber"),
                rs.getString("City"),
                rs.getString("ZIP")
        );
    }
}

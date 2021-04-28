package database.access;

import database.transfer.ClimateDTO;
import database.transfer.CountryDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClimateDAO {
    private static final ClimateDAO instance = new ClimateDAO();

    /**
     * Get the singleton instance
     */
    public static ClimateDAO getInstance() {
        return instance;
    }

    /**
     * Singleton
     */
    private ClimateDAO() {}

    /**
     * Selects a climate
     *
     * @param climate the climate information
     * @return a climateDTO
     * @throws SQLException if an error occurred
     */
    public List<ClimateDTO> select(ClimateDTO climate) throws SQLException {
        Connection conn = DBManager.getInstance().getDBConnection();
        String request = "SELECT * FROM Public.\"Climate\"";

        boolean and = false;
        if(climate.getId() != null) {
            request += " WHERE \"Id\" = ?";
            and = true;
        }
        if(climate.getDescription() != null) {
            if(and)
                request += " AND \"Description\" = ?";
            else
                request += " WHERE \"Description\" = ?";
        }

        PreparedStatement stmt = conn.prepareStatement(request);
        int i = 1;
        if(climate.getId() != null)
            stmt.setInt(i++, climate.getId());
        if(climate.getDescription() != null)
            stmt.setString(i++, climate.getDescription());

        return retrieveClimates(stmt);
    }

    /**
     * Selects all climates in the DB
     *
     * @return climates
     * @throws SQLException if an error occurred
     */
    public List<ClimateDTO> selectAll() throws SQLException {
        Connection conn = DBManager.getInstance().getDBConnection();
        String request = "SELECT * FROM Public.\"Climate\"";

        PreparedStatement stmt = conn.prepareStatement(request);

        return retrieveClimates(stmt);
    }

    private List<ClimateDTO> retrieveClimates(PreparedStatement stmt) throws SQLException {
        ResultSet rs = stmt.executeQuery();
        List<ClimateDTO> results = new ArrayList<>();
        while (rs.next()) {
            int id = rs.getInt("Id");
            String description = rs.getString("Description");

            ClimateDTO selected = new ClimateDTO(id, description);

            results.add(selected);
        }
        return results;
    }

    /**
     * Inserts a new climate in the DB
     *
     * @param climate the climate
     * @throws SQLException if an error occurs
     */
    public void insert(ClimateDTO climate) throws SQLException {
        if(climate.isStored())
            throw new SQLException("The specified ClimateDTO is already persistent");

        Connection conn = DBManager.getInstance().getDBConnection();
        String request = "INSERT INTO Public.\"Climate\"(\"Description\") VALUES (?)";

        PreparedStatement stmt = conn.prepareStatement(request);
        stmt.setString(1, climate.getDescription());

        stmt.executeUpdate();
    }

    /**
     * Updates a climate stored in the DB
     *
     * @param climate the climate
     * @throws SQLException if an error occurs
     */
    public void update(ClimateDTO climate) throws SQLException {
        if(!climate.isStored())
            throw new SQLException("The specified ClimateDTO is not persistent");

        Connection conn = DBManager.getInstance().getDBConnection();
        String request = "UPDATE Public.\"Climate\" SET \"Description\" = ? WHERE Id = ?";

        PreparedStatement stmt = conn.prepareStatement(request);
        stmt.setString(1, climate.getDescription());
        stmt.setInt(2, climate.getId());

        stmt.executeUpdate();
    }

    /**
     * Deletes a climate from the DB
     *
     * @param climate the climate
     * @throws SQLException if an error occurs
     */
    public void delete(ClimateDTO climate) throws SQLException {
        if(!climate.isStored())
            throw new SQLException("The specified ClimateDTO is not persistent");

        Connection conn = DBManager.getInstance().getDBConnection();
        String request = "DELETE FROM Public.\"Climate\" WHERE Id = ?";

        PreparedStatement stmt = conn.prepareStatement(request);
        stmt.setInt(1, climate.getId());

        stmt.executeUpdate();
    }
}

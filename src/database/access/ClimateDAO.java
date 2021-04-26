package database.access;

import database.transfer.ClimateDTO;

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
        String request = "SELECT * FROM Public.\"Climate\" WHERE";

        if(climate.getId() != null)
            request += " \"Id\" = ? AND";
        if(climate.getDescription() != null)
            request += " \"Description\" = ? AND";
        request = request.substring(0, request.length() - 3);

        PreparedStatement stmt = conn.prepareStatement(request);
        int i = 1;
        if(climate.getId() != null)
            stmt.setInt(i++, climate.getId());
        if(climate.getDescription() != null)
            stmt.setString(i++, climate.getDescription());

        ResultSet rs = stmt.executeQuery();

        List<ClimateDTO> results = new ArrayList<>();
        while(rs.next()) {
            int id = rs.getInt("Id");
            String descripton = rs.getString("Description");

            ClimateDTO selected = new ClimateDTO(id, descripton);

            results.add(selected);
        }

        return results;
    }

    public void update(ClimateDTO climate) throws SQLException {
        // TODO
    }

    public void delete(ClimateDTO climate) throws SQLException {
        // TODO
    }
}

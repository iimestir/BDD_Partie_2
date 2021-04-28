package database.access;

import common.Utils;
import database.transfer.ClimateDTO;
import database.transfer.CountryDTO;
import database.transfer.HospitalsDTO;

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
        StringBuilder request = new StringBuilder("SELECT * FROM Public.\"Climate\"");

        PreparedStatement stmt = getStatement(climate, conn, request);

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

    /**
     * Deletes a climate from the DB
     *
     * @param climate the climate
     * @throws SQLException if an error occurs
     */
    public void delete(ClimateDTO climate) throws SQLException {
        if(select(climate).isEmpty())
            throw new SQLException("The specified ClimateDTO is not persistent");

        Connection conn = DBManager.getInstance().getDBConnection();
        StringBuilder request = new StringBuilder("DELETE FROM Public.\"Climate\"");

        PreparedStatement stmt = getStatement(climate, conn, request);
        stmt.executeUpdate();
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
        String request = "UPDATE Public.\"Climate\" SET \"Description\" = ? WHERE \"Id\" = ?";

        PreparedStatement stmt = conn.prepareStatement(request);
        stmt.setString(1, climate.getDescription());
        stmt.setInt(2, climate.getId());

        stmt.executeUpdate();
    }

    private PreparedStatement getStatement(ClimateDTO record, Connection conn, StringBuilder request) throws SQLException {
        List<String> subRequest = new ArrayList<>();
        if(record.getId() != null)
            subRequest.add("\"Id\" = ?");
        if(record.getDescription() != null)
            subRequest.add("\"Description\" = ?");

        Utils.fillSQL(request, subRequest);
        PreparedStatement stmt = conn.prepareStatement(request.toString());

        int i = 1;
        if(record.getId() != null)
            stmt.setInt(i++, record.getId());
        if(record.getDescription() != null)
            stmt.setString(i, record.getDescription());

        return stmt;
    }
}

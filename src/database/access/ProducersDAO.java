package database.access;

import common.Utils;
import database.transfer.ProducersDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProducersDAO {
    private static final ProducersDAO instance = new ProducersDAO();

    /**
     * Get the singleton instance
     */
    public static ProducersDAO getInstance() {
        return instance;
    }

    /**
     * Singleton
     */
    private ProducersDAO() {}

    public void insert(ProducersDTO record) throws SQLException {
        if(record.isStored())
            throw new SQLException("The specified ProducersDTO is already persistent");

        Connection conn = DBManager.getInstance().getDBConnection();
        String request = "INSERT INTO Public.\"Climate\"(\"ISO\",\"Date\",\"Vaccines\")" +
                " VALUES (?,?,?)";

        PreparedStatement stmt = conn.prepareStatement(request);
        stmt.setString(1, record.getId());
        stmt.setDate(2, record.getDate());
        stmt.setArray(3, conn.createArrayOf("text",record.getVaccines()));

        stmt.executeUpdate();
    }

    /**
     * Selects a producers row in the DB
     *
     * @param record the producer row
     * @return list of corresponding records
     * @throws SQLException if an error occurs
     */
    public List<ProducersDTO> select(ProducersDTO record) throws SQLException {
        Connection conn = DBManager.getInstance().getDBConnection();
        StringBuilder request = new StringBuilder("SELECT * FROM Public.\"Producers\"");

        List<String> subRequest = new ArrayList<>();
        if(record.getId() != null)
            subRequest.add("\"ISO\" = ?");
        if(record.getDate() != null)
            subRequest.add("\"Date\" = ?");
        if(record.getVaccines() != null)
            subRequest.add("\"Vaccines\" = ?");

        Utils.fillSQL(request, subRequest);

        PreparedStatement stmt = conn.prepareStatement(request.toString());
        int i = 1;
        if(record.getId() != null)
            stmt.setString(i++, record.getId());
        if(record.getDate() != null)
            stmt.setDate(i++, record.getDate());
        if(record.getVaccines() != null)
            stmt.setArray(i, conn.createArrayOf("text",record.getVaccines()));

        return retrieveProducers(stmt);
    }

    /**
     * Selects all records from the DB
     *
     * @return producers records
     * @throws SQLException if an error occurs
     */
    public List<ProducersDTO> selectAll() throws SQLException {
        Connection conn = DBManager.getInstance().getDBConnection();
        String request = "SELECT * FROM Public.\"Producers\"";

        PreparedStatement stmt = conn.prepareStatement(request);

        return retrieveProducers(stmt);
    }

    private List<ProducersDTO> retrieveProducers(PreparedStatement stmt) throws SQLException {
        ResultSet rs = stmt.executeQuery();
        List<ProducersDTO> results = new ArrayList<>();
        while (rs.next()) {
            String id = rs.getString("ISO");
            Date name = rs.getDate("Date");
            String[] vaccines = ((String[]) rs.getArray("Vaccines").getArray());

            ProducersDTO selected = new ProducersDTO(id, name, vaccines);

            results.add(selected);
        }
        return results;
    }

    /**
     * Updates a record from the DB
     *
     * @param record the record
     * @throws SQLException if an error occurs
     */
    public void update(ProducersDTO record) throws SQLException {
        if(!record.isStored())
            throw new SQLException("The specified ProducersDTO is not persistent");

        Connection conn = DBManager.getInstance().getDBConnection();
        String request = "UPDATE Public.\"Producers\" SET " +
                "\"Date\" = ?,\"Vaccines\" = ?" +
                " WHERE \"Id\" = ?";

        PreparedStatement stmt = conn.prepareStatement(request);
        stmt.setDate(1, record.getDate());
        stmt.setArray(2, conn.createArrayOf("text",record.getVaccines()));
        stmt.setString(3, record.getId());

        stmt.executeUpdate();
    }

    /**
     * Deletes a record from the DB
     *
     * @param record the record
     * @throws SQLException if an error occurs
     */
    public void delete(ProducersDTO record) throws SQLException {
        if(!record.isStored())
            throw new SQLException("The specified ProducersDTO is not persistent");

        Connection conn = DBManager.getInstance().getDBConnection();
        String request = "DELETE FROM Public.\"Producers\" WHERE \"ISO\" = ?";

        PreparedStatement stmt = conn.prepareStatement(request);
        stmt.setString(1, record.getId());

        stmt.executeUpdate();
    }
}

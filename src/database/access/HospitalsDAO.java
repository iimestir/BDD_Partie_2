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

    public List<HospitalsDTO> select(HospitalsDTO report) throws SQLException {
        Connection conn = DBManager.getInstance().getDBConnection();
        StringBuilder request = new StringBuilder("SELECT * FROM Public.\"Hospitals\"");

        List<String> subRequest = new ArrayList<>();
        if(report.getISO() != null)
            subRequest.add("\"ISO\" = ?");
        if(report.getDate() != null)
            subRequest.add("\"Date\" = ?");
        if(report.getIcu_patients() != null)
            subRequest.add("\"icu_patients\" = ?");
        if(report.getHosp_patients() != null)
            subRequest.add("\"hosp_patients\" = ?");
        if(report.getEpidemiologistUUID() != null)
            subRequest.add("\"epidemiologist\" = ?");

        Utils.fillSQL(request, subRequest);

        PreparedStatement stmt = conn.prepareStatement(request.toString());
        int i = 1;
        if(report.getISO() != null)
            stmt.setString(i++, report.getISO());
        if(report.getDate() != null)
            stmt.setDate(i++, report.getDate());
        if(report.getIcu_patients() != null)
            stmt.setInt(i++, report.getIcu_patients());
        if(report.getHosp_patients() != null)
            stmt.setInt(i++, report.getHosp_patients());
        if(report.getEpidemiologistUUID() != null)
            stmt.setString(i, report.getEpidemiologistUUID().toString());

        return retrieveHospitals(stmt);
    }

    public List<HospitalsDTO> selectAll() throws SQLException {
        Connection conn = DBManager.getInstance().getDBConnection();
        String request = "SELECT * FROM Public.\"Hospitals\"";

        PreparedStatement stmt = conn.prepareStatement(request);

        return retrieveHospitals(stmt);
    }

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

    public void insert(HospitalsDTO report) throws SQLException {
        Connection conn = DBManager.getInstance().getDBConnection();
        String request = "INSERT INTO Public.\"Hospitals\"(\"ISO\",\"Date\",\"icu_patients\",\"hosp_patients\",\"epidemiologist\") " +
                "VALUES (?,?,?,?,?)";

        PreparedStatement stmt = conn.prepareStatement(request);
        stmt.setString(1, report.getISO());
        stmt.setDate(2, report.getDate());
        stmt.setInt(3, report.getIcu_patients());
        stmt.setInt(4, report.getHosp_patients());
        stmt.setString(5, report.getEpidemiologistUUID().toString());

        stmt.executeUpdate();
    }
}

package database.access;

import common.Utils;
import database.transfer.CountryDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CountryDAO {
    private static final CountryDAO instance = new CountryDAO();

    /**
     * Get the singleton instance
     */
    public static CountryDAO getInstance() {
        return instance;
    }

    /**
     * Singleton
     */
    private CountryDAO() {}

    /**
     * Inserts a new country in the DB
     *
     * @param ISO country ISO code
     * @param country the country to insert
     * @throws SQLException if an error occurs
     */
    public void insert(String ISO, CountryDTO country) throws SQLException {
        if(country.isStored())
            throw new SQLException("The specified ClimateDTO is already persistent");

        Connection conn = DBManager.getInstance().getDBConnection();
        String request = "INSERT INTO Public.\"Climate\"(\"ISO\",\"Continent\",\"Region\",\"Country\",\"HDI\"," +
                "\"Population\",\"area_sq_ml\",\"Climate\") VALUES (?,?,?,?,?,?,?,?)";

        PreparedStatement stmt = conn.prepareStatement(request);
        stmt.setString(1, ISO);
        stmt.setString(2, country.getContinent());
        stmt.setString(3, country.getRegion());
        stmt.setString(4, country.getName());
        stmt.setDouble(5, country.getHdi());
        stmt.setInt(6, country.getPopulation());
        stmt.setDouble(7, country.getArea_sq_ml());
        stmt.setInt(8, country.getClimateId());

        stmt.executeUpdate();
    }

    /**
     * Selects countries from the DB
     *
     * @param country countries information
     * @return countries list
     * @throws SQLException if an error occurs
     */
    public List<CountryDTO> select(CountryDTO country) throws SQLException {
        Connection conn = DBManager.getInstance().getDBConnection();
        StringBuilder request = new StringBuilder("SELECT * FROM Public.\"Country\"");

        List<String> subRequest = new ArrayList<>();
        if(country.getId() != null)
            subRequest.add("\"ISO\" = ?");
        if(country.getContinent() != null)
            subRequest.add("\"Continent\" = ?");
        if(country.getRegion() != null)
            subRequest.add("\"Region\" = ?");
        if(country.getName() != null)
            subRequest.add("\"Country\" = ?");
        if(country.getHdi() != null)
            subRequest.add("\"HDI\" = ?");
        if(country.getPopulation() != null)
            subRequest.add("\"Population\" = ?");
        if(country.getArea_sq_ml() != null)
            subRequest.add("\"area_sq_ml\" = ?");
        if(country.getClimateId() != null)
            subRequest.add("\"Climate\" = ?");

        Utils.fillSQL(request, subRequest);

        PreparedStatement stmt = conn.prepareStatement(request.toString());
        int i = 1;
        if(country.getId() != null)
            stmt.setString(i++, country.getId());
        if(country.getContinent() != null)
            stmt.setString(i++, country.getContinent());
        if(country.getRegion() != null)
            stmt.setString(i++, country.getRegion());
        if(country.getName() != null)
            stmt.setString(i++, country.getName());
        if(country.getHdi() != null)
            stmt.setDouble(i++, country.getHdi());
        if(country.getPopulation() != null)
            stmt.setInt(i++, country.getPopulation());
        if(country.getArea_sq_ml() != null)
            stmt.setDouble(i++, country.getArea_sq_ml());
        if(country.getClimateId() != null)
            stmt.setInt(i, country.getClimateId());

        return retrieveCountries(stmt);
    }

    /**
     * Selects all countries
     *
     * @return countries list
     * @throws SQLException if an error occurs
     */
    public List<CountryDTO> selectAll() throws SQLException {
        Connection conn = DBManager.getInstance().getDBConnection();
        String request = "SELECT * FROM Public.\"Country\"";

        PreparedStatement stmt = conn.prepareStatement(request);

        return retrieveCountries(stmt);
    }


    private List<CountryDTO> retrieveCountries(PreparedStatement stmt) throws SQLException {
        ResultSet rs = stmt.executeQuery();
        List<CountryDTO> results = new ArrayList<>();
        while (rs.next()) {
            String id = rs.getString("ISO");
            String name = rs.getString("Country");
            String continent = rs.getString("Continent");;
            String region = rs.getString("Region");;
            double hdi = rs.getDouble("HDI");;
            int population = rs.getInt("Population");;
            double area_sq_ml = rs.getDouble("area_sq_ml");;
            int climateId = rs.getInt("Climate");;

            CountryDTO selected = new CountryDTO(id, name, continent, region, hdi, population, area_sq_ml, climateId);

            results.add(selected);
        }
        return results;
    }

    /**
     * Updates a country from the DB
     *
     * @param country the country to update
     * @throws SQLException if an error occurs
     */
    public void update(CountryDTO country) throws SQLException {
        if(!country.isStored())
            throw new SQLException("The specified CountryDTO is not persistent");

        Connection conn = DBManager.getInstance().getDBConnection();
        String request = "UPDATE Public.\"Country\" SET " +
                "\"Continent\" = ?,\"Region\" = ?,\"Country\" = ?,\"HDI\" = ?,\"Population\" = ?,\"area_sq_ml\" = ?,\"Climate\" = ?" +
                " WHERE Id = ?";

        PreparedStatement stmt = conn.prepareStatement(request);
        stmt.setString(1, country.getContinent());
        stmt.setString(2, country.getRegion());
        stmt.setString(3, country.getName());
        stmt.setDouble(4, country.getHdi());
        stmt.setInt(5, country.getPopulation());
        stmt.setDouble(6, country.getArea_sq_ml());
        stmt.setInt(7, country.getClimateId());
        stmt.setString(8, country.getId());

        stmt.executeUpdate();
    }

    /**
     * Deletes a country from the DB
     *
     * @param country the country
     * @throws SQLException if an error occurs
     */
    public void delete(CountryDTO country) throws SQLException {
        if(!country.isStored())
            throw new SQLException("The specified CountryDTO is not persistent");

        Connection conn = DBManager.getInstance().getDBConnection();
        String request = "DELETE FROM Public.\"Country\" WHERE ISO = ?";

        PreparedStatement stmt = conn.prepareStatement(request);
        stmt.setString(1, country.getId());

        stmt.executeUpdate();
    }
}

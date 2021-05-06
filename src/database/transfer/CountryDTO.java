package database.transfer;

public class CountryDTO extends DTO<String> {
    private String continent;
    private String region;
    private String name;
    private Double hdi;
    private Integer population;
    private Double area_sq_ml;
    private Integer climateId;

    public CountryDTO(String id, String continent, String region, String name, Double hdi, Integer population, Double area_sq_ml, Integer climateId) {
        this(continent, region, name, hdi, population, area_sq_ml, climateId);
        this.id = id;
    }

    public CountryDTO(String continent, String region, String name, Double hdi, Integer population, Double area_sq_ml, Integer climateId) {
        this.continent = continent;
        this.region = region;
        this.name = name;
        this.hdi = hdi;
        this.population = population;
        this.area_sq_ml = area_sq_ml;
        this.climateId = climateId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContinent() {
        return continent;
    }

    public void setContinent(String continent) {
        this.continent = continent;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public Double getHdi() {
        return hdi;
    }

    public void setHdi(double hdi) {
        this.hdi = hdi;
    }

    public Integer getPopulation() {
        return population;
    }

    public void setPopulation(int population) {
        this.population = population;
    }

    public Double getArea_sq_ml() {
        return area_sq_ml;
    }

    public void setArea_sq_ml(double area_sq_ml) {
        this.area_sq_ml = area_sq_ml;
    }

    public Integer getClimateId() {
        return climateId;
    }

    public void setClimateId(int climateId) {
        this.climateId = climateId;
    }

    @Override
    public String toString() {
        return this.name;
    }
}

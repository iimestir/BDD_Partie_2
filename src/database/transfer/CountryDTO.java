package database.transfer;

public class CountryDTO extends DTO<String> {
    private String name;
    private String continent;
    private String region;
    private Double hdi;
    private Integer population;
    private Double area_sq_ml;
    private Integer climateId;

    public CountryDTO(String id, String name, String continent, String region, Double hdi, Integer population, Double area_sq_ml, Integer climateId) {
        this(name, continent, region, hdi, population, area_sq_ml, climateId);
        this.id = id;
    }

    public CountryDTO(String name, String continent, String region, Double hdi, Integer population, Double area_sq_ml, Integer climateId) {
        this.name = name;
        this.continent = continent;
        this.region = region;
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
}

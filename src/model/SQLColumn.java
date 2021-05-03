package model;

public enum SQLColumn {
    ID("Id"),
    UUID("UUID"),
    ISO("ISO"),
    DATE("Date"),

    // Climate
    DESCRIPTION("Description"),

    // Country
    CONTINENT("Continent"),
    REGION("Region"),
    COUNTRY("Country"),
    HDI("HDI"),
    POPULATION("Population"),
    AREA_SQ_ML("area_sq_ml"),
    CLIMATE("Climate"),

    // Hospitals
    ICU_PATIENTS("icu_patients"),
    HOSP_PATIENTS("hosp_patients"),
    EPIDEMIOLOGIST("epidemiologist"),

    // Producers
    VACCINES("Vaccines"),

    // Vaccinations
    TESTS("Tests"),
    VACCINATIONS("Vaccinations"),

    // USER
    FIRSTNAME("Firstname"),
    LASTNAME("Lastname"),
    STREET("Street"),
    DOOR_NUMBER("Doornumber"),
    CITY("City"),
    ZIP("ZIP"),

    // EPIDEMIOLOGIST
    CENTER("Center"),
    SERVICE_PHONE("Service Phone");

    private final String tableName;

    SQLColumn(String tableName) {
        this.tableName = tableName;
    }

    @Override
    public String toString() {
        return tableName;
    }
}

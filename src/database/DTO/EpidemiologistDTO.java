package database.DTO;

/**
 * Represents an epidemiologist
 */
public class EpidemiologistDTO extends UserDTO {
    private String center;
    private String serviceNumber;

    /**
     * Epidemiologist constructor with ID
     * @param id id
     * @param firstName first name
     * @param lastName last name
     * @param street street
     * @param doorNumber door number
     * @param city city
     * @param zipCode zip code
     */
    public EpidemiologistDTO(Integer id, String firstName, String lastName, String street, Integer doorNumber, String city,
                             String zipCode, String center, String serviceNumber) {
        super(id,firstName, lastName, street, doorNumber, city, zipCode);

        this.center = center;
        this.serviceNumber = serviceNumber;
    }

    /**
     * Epidemiologist constructor
     * @param firstName first name
     * @param lastName last name
     * @param street street
     * @param doorNumber door number
     * @param city city
     * @param zipCode zip code
     */
    public EpidemiologistDTO(String firstName, String lastName, String street, Integer doorNumber, String city, 
                             String zipCode, String center, String serviceNumber) {
        super(firstName, lastName, street, doorNumber, city, zipCode);

        this.center = center;
        this.serviceNumber = serviceNumber;
    }

    public String getCenter() {
        return center;
    }

    public String getServiceNumber() {
        return serviceNumber;
    }
}

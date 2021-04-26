package database.transfer;

import java.util.UUID;

/**
 * Object storing a User information
 *
 * We purposely don't store the username nor the password
 */
public class UserDTO extends DTO<UUID> {
    private String firstName;
    private String lastName;

    // Address
    private String street;
    private Integer doorNumber;
    private String city;
    private String zipCode;

    /**
     * UserDTO Constructor
     *
     * @param id uuid
     * @param firstName string
     * @param lastName string
     * @param street string
     * @param doorNumber integer
     * @param city string
     * @param zipCode string
     */
    public UserDTO(UUID id, String firstName, String lastName, String street,
                   Integer doorNumber, String city, String zipCode) {
        this(firstName, lastName, street, doorNumber, city, zipCode);
        this.id = id;
    }

    /**
     * UserDTO Constructor without the ID
     *
     * @param firstName string
     * @param lastName string
     * @param street string
     * @param doorNumber integer
     * @param city string
     * @param zipCode string
     */
    public UserDTO(String firstName, String lastName, String street, Integer doorNumber, String city, String zipCode) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.street = street;
        this.doorNumber = doorNumber;
        this.city = city;
        this.zipCode = zipCode;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getStreet() {
        return street;
    }

    public Integer getDoorNumber() {
        return doorNumber;
    }

    public String getCity() {
        return city;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setFirstName(String firstName) { this.firstName = firstName; }

    public void setLastName(String lastName) { this.lastName = lastName; }

    public void setStreet(String street) { this.street = street; }

    public void setDoorNumber(Integer doorNumber) { this.doorNumber = doorNumber; }

    public void setCity(String city) { this.city = city; }

    public void setZipCode(String zipCode) { this.zipCode = zipCode; }
}

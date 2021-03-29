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
}

package database.DTO;

/**
 * Represents an epidemiologist
 */
public class EpidemiologistDTO extends UserDTO {
    
    public EpidemiologistDTO(String firstName, String lastName, String street, Integer doorNumber, String city, 
                             String zipCode) {
        super(firstName, lastName, street, doorNumber, city, zipCode);
        
    }
}

package database.transfer;

public class ClimateDTO extends DTO<Integer> {
    private String description;

    public ClimateDTO(Integer id, String description) {
        this(description);
        this.id = id;
    }

    public ClimateDTO(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

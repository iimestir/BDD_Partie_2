package database.transfer;

import java.sql.Date;

public class ProducersDTO extends DTO<String> {
    private Date date;
    private String[] vaccines;

    public ProducersDTO(String id, Date date, String[] vaccines) {
        this(date, vaccines);
        this.id = id;
    }

    public ProducersDTO(Date date, String[] vaccines) {
        this.date = date;
        this.vaccines = vaccines;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String[] getVaccines() {
        return vaccines;
    }

    public void setVaccines(String[] vaccines) {
        this.vaccines = vaccines;
    }
}

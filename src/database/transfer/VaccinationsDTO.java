package database.transfer;

import java.sql.Date;

public class VaccinationsDTO extends DTO {
    private String ISO;
    private Date date;
    private Integer tests;
    private Integer vaccinations;

    public VaccinationsDTO(String ISO, Date date, Integer tests, Integer vaccinations) {
        this.ISO = ISO;
        this.date = date;
        this.tests = tests;
        this.vaccinations = vaccinations;
    }

    public String getISO() {
        return ISO;
    }

    public void setISO(String ISO) {
        this.ISO = ISO;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getTests() {
        return tests;
    }

    public void setTests(Integer tests) {
        this.tests = tests;
    }

    public Integer getVaccinations() {
        return vaccinations;
    }

    public void setVaccinations(Integer vaccinations) {
        this.vaccinations = vaccinations;
    }
}

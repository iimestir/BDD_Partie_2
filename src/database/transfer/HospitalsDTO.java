package database.transfer;

import java.sql.Date;
import java.util.UUID;

public class HospitalsDTO extends DTO {
    private String ISO;
    private Date date;
    private Integer icu_patients;
    private Integer hosp_patients;
    private UUID epidemiologistUUID;

    public HospitalsDTO(String ISO, Date date, Integer icu_patients, Integer hosp_patients, UUID epidemiologistUUID) {
        this.ISO = ISO;
        this.date = date;
        this.icu_patients = icu_patients;
        this.hosp_patients = hosp_patients;
        this.epidemiologistUUID = epidemiologistUUID;
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

    public Integer getIcu_patients() {
        return icu_patients;
    }

    public void setIcu_patients(Integer icu_patients) {
        this.icu_patients = icu_patients;
    }

    public Integer getHosp_patients() {
        return hosp_patients;
    }

    public void setHosp_patients(Integer hosp_patients) {
        this.hosp_patients = hosp_patients;
    }

    public UUID getEpidemiologistUUID() {
        return epidemiologistUUID;
    }

    public void setEpidemiologistUUID(UUID epidemiologistUUID) {
        this.epidemiologistUUID = epidemiologistUUID;
    }
}

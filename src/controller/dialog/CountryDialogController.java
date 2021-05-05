package controller.dialog;

import common.Utils;
import database.business.UserBusinessLogic;
import database.transfer.CountryDTO;
import database.transfer.HospitalsDTO;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

public class CountryDialogController implements Initializable {
    @FXML private Label countryLabel;
    @FXML private Label isoLabel;
    @FXML private Label hdiLabel;
    @FXML private Label populationLabel;
    @FXML private Label areaLabel;
    @FXML private Label icuLabel;
    @FXML private Label hospLabel;
    @FXML private Label reportLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void setData(String ISO) throws SQLException {
        List<CountryDTO> countries = UserBusinessLogic.getInstance().select(
                new CountryDTO(ISO,null,null,null,null,null,null,null)
        );

        if(countries.isEmpty())
            throw new SQLException(Utils.getTranslatedString("country_not_persistent") + " (" + ISO + ")");

        List<HospitalsDTO> hospitals = UserBusinessLogic.getInstance().select(
                new HospitalsDTO(ISO,null,null,null,null)
        );

        if(hospitals.isEmpty())
            throw new SQLException(Utils.getTranslatedString("country_no_report"));

        hospitals.sort(Comparator.comparing(HospitalsDTO::getDate));

        CountryDTO country = countries.get(0);
        HospitalsDTO report = hospitals.get(hospitals.size()-1);

        countryLabel.setText(country.getName());
        isoLabel.setText(country.getId());
        hdiLabel.setText(country.getHdi().toString());
        populationLabel.setText(country.getPopulation().toString());
        areaLabel.setText(country.getArea_sq_ml().toString());
        icuLabel.setText(report.getIcu_patients().toString());
        hospLabel.setText(report.getHosp_patients().toString());
        reportLabel.setText(Utils.getTranslatedString("report_date") + " " + report.getDate());
    }
}

package controller.subpanel;

import database.business.UserBusinessLogic;
import database.transfer.CountryDTO;
import database.transfer.HospitalsDTO;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.paint.Color;
import org.controlsfx.control.WorldMapView;
import view.UITools;
import view.dialog.CountryDialog;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;

public class WorldMapController implements Initializable {
    @FXML private WorldMapView worldMapView;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        worldMapView.selectedCountriesProperty().addListener((observableValue, countries, t1) -> {
            if(!t1.isEmpty()) {
                try {
                    CountryDialog dialog = CountryDialog.promptDialog(t1.get(0).getLocale().getISO3Country());
                    dialog.showAndWait();
                } catch (SQLException | IOException ex) {
                    UITools.showErrorDialog(ex.getLocalizedMessage());
                }
            }
        });
    }

    public void updateMap() {
        HospitalsDTO mostInfectedReport;

        try {
            List<HospitalsDTO> hospitals = UserBusinessLogic.getInstance().selectAllHospitals();
            hospitals.sort(Comparator.comparing(HospitalsDTO::getHosp_patients));
            mostInfectedReport = hospitals.get(hospitals.size()-1);
        } catch(SQLException ex) {
            UITools.showErrorDialog(ex.getLocalizedMessage());
            return;
        }

        colorMap(mostInfectedReport);
    }

    private void colorMap(HospitalsDTO mostInfectedReport) {
        worldMapView.countryViewFactoryProperty().setValue(country -> {
            try {
                List<CountryDTO> countries = UserBusinessLogic.getInstance().selectAllCountries();

                String iso = country.getLocale().getISO3Country();
                Optional<CountryDTO> find = countries.stream().filter(p -> p.getId().equals(iso)).findFirst();

                WorldMapView.CountryView view = new WorldMapView.CountryView(country);

                if(find.isEmpty()) {
                    view.setFill(Color.GRAY);
                    return view;
                }

                CountryDTO value = find.get();

                HospitalsDTO criteria =
                        new HospitalsDTO(value.getId(),null,null,null,null);

                List<HospitalsDTO> hospitals = UserBusinessLogic.getInstance().select(criteria);
                if(hospitals.isEmpty()) {
                    view.setFill(Color.GRAY);
                    return view;
                }
                hospitals.sort(Comparator.comparing(HospitalsDTO::getDate));
                HospitalsDTO lastReport = hospitals.get(hospitals.size()-1);

                float p = ((float) lastReport.getHosp_patients()) / ((float) mostInfectedReport.getHosp_patients());
                view.setFill(new Color(p,1.0-p,0.3,1.0));

                return view;
            } catch(MissingResourceException ex) {
                // Do nothing
            } catch(SQLException ex) {
                UITools.showErrorDialog(ex.getLocalizedMessage());
            }

            return null;
        });
    }
}

package controller.subpanel;

import database.business.UserBusinessLogic;
import database.transfer.CountryDTO;
import database.transfer.HospitalsDTO;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.paint.Color;
import org.controlsfx.control.WorldMapView;
import view.UITools;

import java.net.URL;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class WorldMapController implements Initializable {
    @FXML private WorldMapView worldMapView;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            colorMap();
        } catch (SQLException ex) {
            UITools.showErrorDialog(ex.getLocalizedMessage());

            ex.printStackTrace();
        }
    }

    private void colorMap() throws SQLException {
        List<CountryDTO> countries = UserBusinessLogic.getInstance().selectAllCountries();

        worldMapView.setCountryViewFactory(country -> {
            String iso = country.getLocale().getISO3Country();
            Optional<CountryDTO> find = countries.stream().filter(p -> p.getId().equals(iso)).findFirst();

            WorldMapView.CountryView view = new WorldMapView.CountryView(country);

            if(!find.isPresent()) {
                view.setFill(Color.GRAY);
                return view;
            }

            CountryDTO value = find.get();

            HospitalsDTO criteria = new HospitalsDTO(value.getId(),null,null,null,null);
            try {
                List<HospitalsDTO> hospitals = UserBusinessLogic.getInstance().select(criteria);
                hospitals.sort(Comparator.comparing(HospitalsDTO::getDate));

                HospitalsDTO lastReport = hospitals.get(hospitals.size()-1);

                hospitals.sort(Comparator.comparing(HospitalsDTO::getIcu_patients));

                HospitalsDTO mostInfectedReport = hospitals.get(hospitals.size()-1);

                float opacity = ((float) lastReport.getIcu_patients()) / ((float) mostInfectedReport.getIcu_patients());
                
                view.setFill(new Color(1.0,1.0,1.0,opacity));
            } catch (SQLException ex) {
                UITools.showErrorDialog(ex.getLocalizedMessage());
            }

            return view;
        });
    }
}

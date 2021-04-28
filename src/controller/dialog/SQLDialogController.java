package controller.dialog;

import database.transfer.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import model.DTOType;
import model.SQLRequest;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.UUID;
import java.util.stream.Collectors;

public class SQLDialogController implements Initializable {
    @FXML private GridPane formGridPane;

    private int rowIndex = 0;
    private DTOType dto;
    private SQLRequest sqlType;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    private List<String> getDTOArguments() {
        return formGridPane.getChildren().stream()
                .filter(p -> p instanceof TextField)
                .map(p -> ((TextField) p).getText())
                .collect(Collectors.toList());
    }

    private void fillForm(Label... labels) {
        for(Label l : labels) {
            formGridPane.add(l,0,rowIndex);
            formGridPane.add(new TextField(),1,rowIndex++);
        }
    }

    public void setData(SQLRequest sqlType, DTOType dto) {
        this.dto = dto;
        this.sqlType = sqlType;

        switch(dto) {
            case CLIMATE -> {
                if(sqlType != SQLRequest.INSERT)
                    fillForm(new Label("Id"));

                fillForm(new Label("Description"));
            }
            case COUNTRY -> {
                if(sqlType != SQLRequest.INSERT)
                    fillForm(new Label("ISO"));

                fillForm(
                        new Label("Continent"),
                        new Label("Region"),
                        new Label("Country"),
                        new Label("HDI"),
                        new Label("Population"),
                        new Label("area_sq_ml"),
                        new Label("Climate")
                );
            }
            case HOSPITALS -> {
                fillForm(
                        new Label("ISO"),
                        new Label("Date"),
                        new Label("icu_patients"),
                        new Label("hosp_patients"),
                        new Label("epidemiologist")
                );
            }
            case PRODUCERS -> {
                if(sqlType != SQLRequest.INSERT)
                    fillForm(new Label("ISO"));

                fillForm(
                        new Label("Date"),
                        new Label("Vaccines")
                );
            }
            case USER -> {
                // TODO
            }
            case VACCINATIONS -> {
                fillForm(
                        new Label("ISO"),
                        new Label("Date"),
                        new Label("Tests"),
                        new Label("Vaccinations")
                );
            }
        }
    }

    public DTO getDTO() throws IllegalArgumentException, NumberFormatException {
        List<String> args = getDTOArguments();
        int i = 0;

        switch(dto) {
            case CLIMATE -> {
                if(sqlType != SQLRequest.INSERT)
                    return new ClimateDTO(
                            Integer.parseInt(args.get(i++)),
                            args.get(i)
                    );
                else
                    return new ClimateDTO(args.get(i));
            }
            case COUNTRY -> {
                if(sqlType != SQLRequest.INSERT)
                    return new CountryDTO(
                            args.get(i++),
                            args.get(i++),
                            args.get(i++),
                            args.get(i++),
                            Double.parseDouble(args.get(i++)),
                            Integer.parseInt(args.get(i++)),
                            Double.parseDouble(args.get(i++)),
                            Integer.parseInt(args.get(i))
                    );
                else
                    return new CountryDTO(
                            args.get(i++),
                            args.get(i++),
                            args.get(i++),
                            Double.parseDouble(args.get(i++)),
                            Integer.parseInt(args.get(i++)),
                            Double.parseDouble(args.get(i++)),
                            Integer.parseInt(args.get(i))
                    );
            }
            case HOSPITALS -> {
                return new HospitalsDTO(
                        args.get(i++),
                        java.sql.Date.valueOf(args.get(i++)),
                        Integer.parseInt(args.get(i)),
                        Integer.parseInt(args.get(i)),
                        UUID.fromString(args.get(i))
                );
            }
            case PRODUCERS -> {
                if(sqlType != SQLRequest.INSERT)
                    return new ProducersDTO(
                            args.get(i++),
                            java.sql.Date.valueOf(args.get(i++)),
                            args.get(i).split(",")
                    );
                else
                    return new ProducersDTO(
                            java.sql.Date.valueOf(args.get(i++)),
                            args.get(i).split(",")
                    );
            }
            case USER -> {
                // TODO
            }
            case VACCINATIONS -> {
                return new VaccinationsDTO(
                        args.get(i++), java.sql.Date.valueOf(args.get(i++)),
                        Integer.parseInt(args.get(i)),
                        Integer.parseInt(args.get(i))
                );
            }
        }

        throw new IllegalArgumentException("Unknown DTO: " + dto);
    }
}

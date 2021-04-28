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

    /**
     * Returns the user inputs
     *
     * @return the user inputs
     */
    private List<String> getDTOArguments() {
        return formGridPane.getChildren().stream()
                .filter(p -> p instanceof TextField)
                .map(p -> ((TextField) p).getText())
                .collect(Collectors.toList());
    }

    /**
     * Fills the gridpane with the criteria information
     *
     * @param labels nodes
     */
    private void fillForm(Label... labels) {
        for(Label l : labels) {
            formGridPane.add(l,0,rowIndex);
            formGridPane.add(new TextField(),1,rowIndex++);
        }
    }

    /**
     * Saves the criteria in the form
     *
     * @param sqlType Request type
     * @param dto criteria
     */
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
                fillForm(
                        new Label("ISO"),
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
                fillForm(
                        new Label("ISO"),
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

    /**
     * Returns the dialog final information
     *
     * @return DTO
     * @throws IllegalArgumentException if an input is not correct
     * @throws NumberFormatException if an input is not correct
     */
    public DTO getDTO() throws IllegalArgumentException, NumberFormatException {
        List<String> args = getDTOArguments();
        int i = -1;

        switch(dto) {
            case CLIMATE -> {
                if(sqlType != SQLRequest.INSERT)
                    return new ClimateDTO(
                            args.get(++i).equals("") ? null : Integer.parseInt(args.get(i)),
                            args.get(++i).equals("") ? null : args.get(i)
                    );
                else
                    return new ClimateDTO(args.get(++i));
            }
            case COUNTRY -> {
                return new CountryDTO(
                        args.get(++i).equals("") ? null : args.get(i),
                        args.get(++i).equals("") ? null : args.get(i),
                        args.get(++i).equals("") ? null : args.get(i),
                        args.get(++i).equals("") ? null : args.get(i),
                        args.get(++i).equals("") ? null : Double.parseDouble(args.get(i)),
                        args.get(++i).equals("") ? null : Integer.parseInt(args.get(i)),
                        args.get(++i).equals("") ? null : Double.parseDouble(args.get(i)),
                        args.get(++i).equals("") ? null : Integer.parseInt(args.get(i))
                );
            }
            case HOSPITALS -> {
                return new HospitalsDTO(
                        args.get(++i).equals("") ? null : args.get(i),
                        args.get(++i).equals("") ? null : java.sql.Date.valueOf(args.get(i)),
                        args.get(++i).equals("") ? null : Integer.parseInt(args.get(i)),
                        args.get(++i).equals("") ? null : Integer.parseInt(args.get(i)),
                        args.get(++i).equals("") ? null : UUID.fromString(args.get(i))
                );
            }
            case PRODUCERS -> {
                return new ProducersDTO(
                        args.get(++i).equals("") ? null : args.get(i),
                        args.get(++i).equals("") ? null : java.sql.Date.valueOf(args.get(i)),
                        args.get(++i).equals("") ? null : args.get(i).split(",")
                );
            }
            case USER -> {
                // TODO
            }
            case VACCINATIONS -> {
                return new VaccinationsDTO(
                        args.get(++i).equals("") ? null : args.get(i),
                        args.get(++i).equals("") ? null : java.sql.Date.valueOf(args.get(i)),
                        args.get(++i).equals("") ? null : Integer.parseInt(args.get(i)),
                        args.get(++i).equals("") ? null : Integer.parseInt(args.get(i))
                );
            }
        }

        throw new IllegalArgumentException("Unknown DTO: " + dto);
    }
}

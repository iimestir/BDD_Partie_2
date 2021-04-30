package controller.dialog;

import common.Utils;
import database.transfer.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;
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
     * Fills the grid pane with the criteria information
     *
     * @param type request type
     * @param id if the text field is an ID text field
     * @param labels nodes
     */
    private void fillForm(SQLRequest type, boolean id, Label... labels) {
        for(Label l : labels) {
            formGridPane.add(l,0,rowIndex);

            TextField criteriaTextField = new TextField();
            criteriaTextField.setPromptText(l.getText());

            formGridPane.add(criteriaTextField,1,rowIndex);

            if(type == SQLRequest.UPDATE) {
                TextField updateTextField = new TextField();
                updateTextField.setPromptText(l.getText() + " " + Utils.getTranslatedString("new_value"));
                updateTextField.setVisible(!id);

                formGridPane.add(updateTextField,2,rowIndex);
            }

            rowIndex++;
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
                    fillForm(sqlType, true, new Label("Id"));

                fillForm(sqlType, false, new Label("Description"));
            }
            case COUNTRY -> {
                fillForm(
                        sqlType, false,
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
                        sqlType, false,
                        new Label("ISO"),
                        new Label("Date"),
                        new Label("icu_patients"),
                        new Label("hosp_patients"),
                        new Label("epidemiologist")
                );
            }
            case PRODUCERS -> {
                fillForm(
                        sqlType, false,
                        new Label("ISO"),
                        new Label("Date"),
                        new Label("Vaccines")
                );
            }
            case USER -> {
                if(sqlType != SQLRequest.INSERT)
                    fillForm(sqlType, true, new Label("UUID"));

                fillForm(
                        sqlType, false,
                        new Label("Firstname"),
                        new Label("Lastname"),
                        new Label("Doornumber"),
                        new Label("City"),
                        new Label("ZIP")
                );
            }
            case VACCINATIONS -> {
                fillForm(
                        sqlType, false,
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
    public Object getDTO() throws IllegalArgumentException, NumberFormatException {
        List<String> args = getDTOArguments();

        return sqlType == SQLRequest.UPDATE ? getPairDTO(args) : getSingleDTO(args);
    }

    private DTO getSingleDTO(List<String> args) {
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
                if(sqlType != SQLRequest.INSERT)
                    return new UserDTO(
                            args.get(++i).equals("") ? null : args.get(i),
                            args.get(++i).equals("") ? null : args.get(i),
                            args.get(++i).equals("") ? null : args.get(i),
                            args.get(++i).equals("") ? null : Integer.parseInt(args.get(i)),
                            args.get(++i).equals("") ? null : args.get(i),
                            args.get(++i).equals("") ? null : args.get(i)
                    );
                else
                    return new UserDTO(
                            args.get(++i).equals("") ? null : UUID.fromString(args.get(i)),
                            args.get(++i).equals("") ? null : args.get(i),
                            args.get(++i).equals("") ? null : args.get(i),
                            args.get(++i).equals("") ? null : args.get(i),
                            args.get(++i).equals("") ? null : Integer.parseInt(args.get(i)),
                            args.get(++i).equals("") ? null : args.get(i),
                            args.get(++i).equals("") ? null : args.get(i)
                    );
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

    private Pair<DTO,DTO> getPairDTO(List<String> args) {
        int o = -2;

        switch(dto) {
            case CLIMATE -> {
                int n = 1;

                return new Pair<>(
                        new ClimateDTO(
                                args.get(o+=2).equals("") ? null : Integer.parseInt(args.get(o)),
                                args.get(o+=2).equals("") ? null : args.get(o)
                        ),
                        new ClimateDTO(
                                args.get(n+=2).equals("") ? null : args.get(n)
                        )
                );
            }
            case COUNTRY -> {
                int n = 1;

                return new Pair<>(
                        new CountryDTO(
                                args.get(o+=2).equals("") ? null : args.get(o),
                                args.get(o+=2).equals("") ? null : args.get(o),
                                args.get(o+=2).equals("") ? null : args.get(o),
                                args.get(o+=2).equals("") ? null : args.get(o),
                                args.get(o+=2).equals("") ? null : Double.parseDouble(args.get(o)),
                                args.get(o+=2).equals("") ? null : Integer.parseInt(args.get(o)),
                                args.get(o+=2).equals("") ? null : Double.parseDouble(args.get(o)),
                                args.get(o+=2).equals("") ? null : Integer.parseInt(args.get(o))
                        ),
                        new CountryDTO(
                                args.get(n+=2).equals("") ? null : args.get(n),
                                args.get(n+=2).equals("") ? null : args.get(n),
                                args.get(n+=2).equals("") ? null : args.get(n),
                                args.get(n+=2).equals("") ? null : Double.parseDouble(args.get(n)),
                                args.get(n+=2).equals("") ? null : Integer.parseInt(args.get(n)),
                                args.get(n+=2).equals("") ? null : Double.parseDouble(args.get(n)),
                                args.get(n+=2).equals("") ? null : Integer.parseInt(args.get(n))
                        )
                );
            }
            case HOSPITALS -> {
                int n = -1;

                return new Pair<>(
                        new HospitalsDTO(
                                args.get(o+=2).equals("") ? null : args.get(o),
                                args.get(o+=2).equals("") ? null : java.sql.Date.valueOf(args.get(o)),
                                args.get(o+=2).equals("") ? null : Integer.parseInt(args.get(o)),
                                args.get(o+=2).equals("") ? null : Integer.parseInt(args.get(o)),
                                args.get(o+=2).equals("") ? null : UUID.fromString(args.get(o))
                        ),
                        new HospitalsDTO(
                                args.get(n+=2).equals("") ? null : args.get(n),
                                args.get(n+=2).equals("") ? null : java.sql.Date.valueOf(args.get(n)),
                                args.get(n+=2).equals("") ? null : Integer.parseInt(args.get(n)),
                                args.get(n+=2).equals("") ? null : Integer.parseInt(args.get(n)),
                                args.get(n+=2).equals("") ? null : UUID.fromString(args.get(n))
                        )
                );
            }
            case PRODUCERS -> {
                int n = 1;

                return new Pair<>(
                        new ProducersDTO(
                                args.get(o+=2).equals("") ? null : args.get(o),
                                args.get(o+=2).equals("") ? null : java.sql.Date.valueOf(args.get(o)),
                                args.get(o+=2).equals("") ? null : args.get(o).split(",")
                        ),
                        new ProducersDTO(
                                args.get(n+=2).equals("") ? null : java.sql.Date.valueOf(args.get(n)),
                                args.get(n+=2).equals("") ? null : args.get(n).split(",")
                        )
                );
            }
            case VACCINATIONS -> {
                int n = -1;

                return new Pair<>(
                        new VaccinationsDTO(
                                args.get(o+=2).equals("") ? null : args.get(o),
                                args.get(o+=2).equals("") ? null : java.sql.Date.valueOf(args.get(o)),
                                args.get(o+=2).equals("") ? null : Integer.parseInt(args.get(o)),
                                args.get(o+=2).equals("") ? null : Integer.parseInt(args.get(o))
                        ),
                        new VaccinationsDTO(
                                args.get(n+=2).equals("") ? null : args.get(n),
                                args.get(n+=2).equals("") ? null : java.sql.Date.valueOf(args.get(n)),
                                args.get(n+=2).equals("") ? null : Integer.parseInt(args.get(n)),
                                args.get(n+=2).equals("") ? null : Integer.parseInt(args.get(n))
                        )
                );
            }
            case USER -> {
                int n = 1;

                return new Pair<>(
                        new UserDTO(
                                args.get(o+=2).equals("") ? null : UUID.fromString(args.get(o)),
                                args.get(o+=2).equals("") ? null : args.get(o),
                                args.get(o+=2).equals("") ? null : args.get(o),
                                args.get(o+=2).equals("") ? null : args.get(o),
                                args.get(o+=2).equals("") ? null : Integer.parseInt(args.get(o)),
                                args.get(o+=2).equals("") ? null : args.get(o),
                                args.get(o+=2).equals("") ? null : args.get(o)
                        ),
                        new UserDTO(
                                args.get(n+=2).equals("") ? null : args.get(n),
                                args.get(n+=2).equals("") ? null : args.get(n),
                                args.get(n+=2).equals("") ? null : args.get(n),
                                args.get(n+=2).equals("") ? null : Integer.parseInt(args.get(n)),
                                args.get(n+=2).equals("") ? null : args.get(n),
                                args.get(n+=2).equals("") ? null : args.get(n)
                        )
                );
            }
            default -> {
                return null;
            }
        }
    }
}

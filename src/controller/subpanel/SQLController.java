package controller.subpanel;

import common.LoginToken;
import common.Utils;
import database.business.EpidemiologistBusinessLogic;
import database.business.UserBusinessLogic;
import database.transfer.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import model.DTOType;
import model.SQLRequest;
import view.UITools;
import view.dialog.SQLDialog;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class SQLController implements Initializable {
    @FXML private ComboBox<SQLRequest> requestComboBox;
    @FXML private ComboBox<DTOType> tableComboBox;
    @FXML private TableView<DTO> tableView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if(LoginToken.isEpidemiologist())
            requestComboBox.getItems().addAll(SQLRequest.class.getEnumConstants());
        else
            requestComboBox.getItems().add(SQLRequest.INSERT);

        tableComboBox.getItems().addAll(DTOType.class.getEnumConstants());

        tableView.setEditable(false);
    }

    private void fillClimates(List<ClimateDTO> dto) {
        tableView.getItems().clear();

        // Columns
        TableColumn id = new TableColumn("Id");
        TableColumn description = new TableColumn("Description");
        tableView.getColumns().addAll(id, description);

        tableView.getItems().addAll(dto);
    }

    private void fillCountries(List<CountryDTO> dto) {
        tableView.getItems().clear();

        // Columns
        TableColumn iso = new TableColumn("ISO");
        TableColumn continent = new TableColumn("Continent");
        TableColumn region = new TableColumn("Region");
        TableColumn country = new TableColumn("Country");
        TableColumn hdi = new TableColumn("HDI");
        TableColumn population = new TableColumn("Population");
        TableColumn area_sq_ml = new TableColumn("area_sq_ml");
        TableColumn climate = new TableColumn("Climate");
        tableView.getColumns().addAll(iso, continent, region, country, hdi, population, area_sq_ml, climate);

        tableView.getItems().addAll(dto);
    }

    private void fillHospitals(List<HospitalsDTO> dto) {
        tableView.getItems().clear();

        // Columns
        TableColumn iso = new TableColumn("ISO");
        TableColumn date = new TableColumn("Date");
        TableColumn icu_patients = new TableColumn("icu_patients");
        TableColumn hosp_patients = new TableColumn("hosp_patients");
        TableColumn epidemiologist = new TableColumn("epidemiologist");
        tableView.getColumns().addAll(iso, date, icu_patients, hosp_patients, epidemiologist);

        tableView.getItems().addAll(dto);
    }

    private void fillProducers(List<ProducersDTO> dto) {
        tableView.getItems().clear();

        // Columns
        TableColumn iso = new TableColumn("ISO");
        TableColumn date = new TableColumn("Date");
        TableColumn vaccines = new TableColumn("Vaccines");
        tableView.getColumns().addAll(iso, date, vaccines);

        tableView.getItems().addAll(dto);
    }

    private void fillVaccinations(List<VaccinationsDTO> dto) {
        tableView.getItems().clear();

        // Columns
        TableColumn iso = new TableColumn("ISO");
        TableColumn date = new TableColumn("Date");
        TableColumn tests = new TableColumn("Tests");
        TableColumn vaccinations = new TableColumn("Vaccinations");
        tableView.getColumns().addAll(iso, date, tests, vaccinations);

        tableView.getItems().addAll(dto);
    }

    private void selectOnTable(DTOType table, DTO dto) throws SQLException {
        switch(table) {
            case CLIMATE -> fillClimates(UserBusinessLogic.getInstance().select(((ClimateDTO) dto)));
            case COUNTRY -> fillCountries(UserBusinessLogic.getInstance().select(((CountryDTO) dto)));
            case HOSPITALS -> fillHospitals(UserBusinessLogic.getInstance().select(((HospitalsDTO) dto)));
            case PRODUCERS -> fillProducers(UserBusinessLogic.getInstance().select(((ProducersDTO) dto)));
            case VACCINATIONS -> fillVaccinations(UserBusinessLogic.getInstance().select(((VaccinationsDTO) dto)));
            case USER -> {
                // TODO
            }
        }
    }

    private void insertOnTable(DTOType table, DTO dto) throws IllegalAccessException, SQLException {
        if(!LoginToken.isEpidemiologist())
            throw new IllegalAccessException(Utils.getTranslatedString("error_message_only_epidemiologist"));

        switch(table) {
            case CLIMATE -> EpidemiologistBusinessLogic.getInstance().insert(((ClimateDTO) dto));
            case COUNTRY -> EpidemiologistBusinessLogic.getInstance().insert(((CountryDTO) dto).getId(), ((CountryDTO) dto));
            case HOSPITALS -> EpidemiologistBusinessLogic.getInstance().insert(((HospitalsDTO) dto));
            case PRODUCERS -> EpidemiologistBusinessLogic.getInstance().insert(((ProducersDTO) dto));
            case VACCINATIONS -> EpidemiologistBusinessLogic.getInstance().insert(((VaccinationsDTO) dto));
            case USER -> throw new IllegalArgumentException(Utils.getTranslatedString("error_message_insert_user"));
        }
    }

    private void updateOnTable(DTOType table, DTO dto) throws IllegalAccessException, SQLException {
        if(!LoginToken.isEpidemiologist())
            throw new IllegalAccessException(Utils.getTranslatedString("error_message_only_epidemiologist"));

        switch(table) {
            case CLIMATE -> EpidemiologistBusinessLogic.getInstance().update(((ClimateDTO) dto));
            case COUNTRY -> EpidemiologistBusinessLogic.getInstance().update(((CountryDTO) dto));
            case PRODUCERS -> EpidemiologistBusinessLogic.getInstance().update(((ProducersDTO) dto));
            case USER -> {
                // TODO
            }
            default -> throw new IllegalArgumentException(Utils.getTranslatedString("error_message_table_update"));
        }
    }

    private void deleteOnTable(DTOType table, DTO dto) throws IllegalAccessException, SQLException {
        if(!LoginToken.isEpidemiologist())
            throw new IllegalAccessException(Utils.getTranslatedString("error_message_only_epidemiologist"));

        switch(table) {
            case CLIMATE -> EpidemiologistBusinessLogic.getInstance().delete(((ClimateDTO) dto));
            case COUNTRY -> EpidemiologistBusinessLogic.getInstance().delete(((CountryDTO) dto));
            case PRODUCERS -> EpidemiologistBusinessLogic.getInstance().delete(((ProducersDTO) dto));
            case USER -> {
                // TODO
            }
        }
    }

    private void request(SQLRequest request, DTOType table, DTO dto) throws SQLException, IllegalAccessException {
        switch(request) {
            case SELECT -> selectOnTable(table, dto);
            case INSERT -> insertOnTable(table, dto);
            case UPDATE -> updateOnTable(table, dto);
            case DELETE -> deleteOnTable(table, dto);
        }
    }

    public void requestButtonAction(ActionEvent actionEvent) {
        try {
            final SQLRequest request = requestComboBox.getValue();
            final DTOType table = tableComboBox.getValue();

            final SQLDialog dialog = SQLDialog.promptDialog(request, table);

            final Optional<DTO> optional = dialog.showAndWait();
            if(optional.isEmpty())
                return;

            final DTO dto = dialog.getResult();

            request(request, table, dto);
        } catch(Exception ex) {
            UITools.showErrorDialog(ex.getLocalizedMessage());
        }
    }
}

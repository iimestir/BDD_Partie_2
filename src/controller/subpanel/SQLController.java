package controller.subpanel;

import common.LoginToken;
import common.Utils;
import database.business.EpidemiologistBusinessLogic;
import database.business.UserBusinessLogic;
import database.transfer.*;
import javafx.beans.property.ReadOnlyStringWrapper;
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
import java.util.Arrays;
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
            requestComboBox.getItems().add(SQLRequest.SELECT);

        tableComboBox.getItems().addAll(DTOType.class.getEnumConstants());

        tableView.setEditable(false);
    }

    private void fillClimates(List<ClimateDTO> dto) {
        tableView.getItems().clear();
        tableView.getColumns().clear();

        // Columns
        TableColumn<DTO, String> id = new TableColumn<>("Id");
        TableColumn<DTO, String> description = new TableColumn<>("Description");
        id.setCellValueFactory(data ->
                new ReadOnlyStringWrapper(data.getValue().getId().toString()));
        description.setCellValueFactory(data ->
                new ReadOnlyStringWrapper(((ClimateDTO) data.getValue()).getDescription()));

        tableView.getColumns().addAll(id, description);

        tableView.getItems().addAll(dto);
    }

    private void fillCountries(List<CountryDTO> dto) {
        tableView.getItems().clear();
        tableView.getColumns().clear();

        // Columns
        TableColumn<DTO, String> iso = new TableColumn<>("ISO");
        TableColumn<DTO, String> continent = new TableColumn("Continent");
        TableColumn<DTO, String> region = new TableColumn<>("Region");
        TableColumn<DTO, String> country = new TableColumn<>("Country");
        TableColumn<DTO, String> hdi = new TableColumn<>("HDI");
        TableColumn<DTO, String> population = new TableColumn<>("Population");
        TableColumn<DTO, String> area_sq_ml = new TableColumn<>("area_sq_ml");
        TableColumn<DTO, String> climate = new TableColumn<>("Climate");
        iso.setCellValueFactory(data ->
                new ReadOnlyStringWrapper(((CountryDTO) data.getValue()).getId()));
        continent.setCellValueFactory(data ->
                new ReadOnlyStringWrapper(((CountryDTO) data.getValue()).getContinent()));
        region.setCellValueFactory(data ->
                new ReadOnlyStringWrapper(((CountryDTO) data.getValue()).getRegion()));
        country.setCellValueFactory(data ->
                new ReadOnlyStringWrapper(((CountryDTO) data.getValue()).getName()));
        hdi.setCellValueFactory(data ->
                new ReadOnlyStringWrapper(((CountryDTO) data.getValue()).getHdi().toString()));
        population.setCellValueFactory(data ->
                new ReadOnlyStringWrapper(((CountryDTO) data.getValue()).getPopulation().toString()));
        area_sq_ml.setCellValueFactory(data ->
                new ReadOnlyStringWrapper(((CountryDTO) data.getValue()).getArea_sq_ml().toString()));
        climate.setCellValueFactory(data ->
                new ReadOnlyStringWrapper(((CountryDTO) data.getValue()).getClimateId().toString()));

        tableView.getColumns().addAll(iso, continent, region, country, hdi, population, area_sq_ml, climate);

        tableView.getItems().addAll(dto);
    }

    private void fillHospitals(List<HospitalsDTO> dto) {
        tableView.getItems().clear();
        tableView.getColumns().clear();

        // Columns
        TableColumn<DTO, String> iso = new TableColumn<>("ISO");
        TableColumn<DTO, String> date = new TableColumn<>("Date");
        TableColumn<DTO, String> icu_patients = new TableColumn<>("icu_patients");
        TableColumn<DTO, String> hosp_patients = new TableColumn<>("hosp_patients");
        TableColumn<DTO, String> epidemiologist = new TableColumn<>("epidemiologist");
        iso.setCellValueFactory(data ->
                new ReadOnlyStringWrapper(((HospitalsDTO) data.getValue()).getISO()));
        date.setCellValueFactory(data ->
                new ReadOnlyStringWrapper(((HospitalsDTO) data.getValue()).getDate().toString()));
        icu_patients.setCellValueFactory(data ->
                new ReadOnlyStringWrapper(((HospitalsDTO) data.getValue()).getIcu_patients().toString()));
        hosp_patients.setCellValueFactory(data ->
                new ReadOnlyStringWrapper(((HospitalsDTO) data.getValue()).getHosp_patients().toString()));
        epidemiologist.setCellValueFactory(data ->
                new ReadOnlyStringWrapper(((HospitalsDTO) data.getValue()).getEpidemiologistUUID().toString()));

        tableView.getColumns().addAll(iso, date, icu_patients, hosp_patients, epidemiologist);

        tableView.getItems().addAll(dto);
    }

    private void fillProducers(List<ProducersDTO> dto) {
        tableView.getItems().clear();
        tableView.getColumns().clear();

        // Columns
        TableColumn<DTO, String> iso = new TableColumn<>("ISO");
        TableColumn<DTO, String> date = new TableColumn<>("Date");
        TableColumn<DTO, String> vaccines = new TableColumn<>("Vaccines");
        iso.setCellValueFactory(data ->
                new ReadOnlyStringWrapper(((ProducersDTO) data.getValue()).getId()));
        date.setCellValueFactory(data ->
                new ReadOnlyStringWrapper(((ProducersDTO) data.getValue()).getDate().toString()));
        vaccines.setCellValueFactory(data ->
                new ReadOnlyStringWrapper(Arrays.toString(((ProducersDTO) data.getValue()).getVaccines())));

        tableView.getColumns().addAll(iso, date, vaccines);

        tableView.getItems().addAll(dto);
    }

    private void fillVaccinations(List<VaccinationsDTO> dto) {
        tableView.getItems().clear();
        tableView.getColumns().clear();

        // Columns
        TableColumn<DTO, String> iso = new TableColumn<>("ISO");
        TableColumn<DTO, String> date = new TableColumn<>("Date");
        TableColumn<DTO, String> tests = new TableColumn<>("Tests");
        TableColumn<DTO, String> vaccinations = new TableColumn<>("Vaccinations");
        iso.setCellValueFactory(data ->
                new ReadOnlyStringWrapper(((VaccinationsDTO) data.getValue()).getISO()));
        date.setCellValueFactory(data ->
                new ReadOnlyStringWrapper(((VaccinationsDTO) data.getValue()).getDate().toString()));
        tests.setCellValueFactory(data ->
                new ReadOnlyStringWrapper(((VaccinationsDTO) data.getValue()).getTests().toString()));
        vaccinations.setCellValueFactory(data ->
                new ReadOnlyStringWrapper(((VaccinationsDTO) data.getValue()).getVaccinations().toString()));

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
            case HOSPITALS -> EpidemiologistBusinessLogic.getInstance().delete(((HospitalsDTO) dto));
            case PRODUCERS -> EpidemiologistBusinessLogic.getInstance().delete(((ProducersDTO) dto));
            case VACCINATIONS -> EpidemiologistBusinessLogic.getInstance().delete(((VaccinationsDTO) dto));
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

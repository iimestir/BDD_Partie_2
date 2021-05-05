package controller.subpanels;

import common.LoginToken;
import common.Utils;
import database.business.EpidemiologistBusinessLogic;
import database.business.UserBusinessLogic;
import database.transfer.*;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Pair;
import model.DTOType;
import model.SQLColumn;
import model.SQLRequest;
import org.controlsfx.control.CheckComboBox;
import view.UITools;
import view.dialog.SQLDialog;

import java.net.URL;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

@SuppressWarnings("rawtypes")
public class SQLController implements Initializable {
    @FXML private Button sqlButton;
    @FXML private CheckComboBox<SQLColumn> columnCheckBox;
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

        tableComboBox.valueProperty().addListener((observableValue, dtoType, t1) -> fillColumnBox(t1));

        columnCheckBox.disableProperty().bind(requestComboBox.valueProperty().isNotEqualTo(SQLRequest.SELECT));
        sqlButton.disableProperty().bind(
                requestComboBox.valueProperty().isNull()
                        .or(tableComboBox.valueProperty().isNull())
        );
    }

    /**
     * Updates the column combobox when the value of the table combobox is changed
     *
     * @param table new value
     */
    private void fillColumnBox(DTOType table) {
        switch(table) {
            case CLIMATE ->
                    columnCheckBox.getItems().setAll(SQLColumn.ID, SQLColumn.DESCRIPTION);
            case COUNTRY ->
                    columnCheckBox.getItems().setAll(
                            SQLColumn.ISO, SQLColumn.CONTINENT, SQLColumn.REGION, SQLColumn.COUNTRY,
                            SQLColumn.HDI, SQLColumn.POPULATION, SQLColumn.AREA_SQ_ML, SQLColumn.CLIMATE
                    );
            case HOSPITALS ->
                    columnCheckBox.getItems().setAll(
                            SQLColumn.ISO, SQLColumn.DATE, SQLColumn.ICU_PATIENTS, SQLColumn.HOSP_PATIENTS,
                            SQLColumn.EPIDEMIOLOGIST
                    );
            case PRODUCERS ->
                    columnCheckBox.getItems().setAll(
                            SQLColumn.ISO, SQLColumn.DATE, SQLColumn.VACCINES
                    );
            case VACCINATIONS ->
                    columnCheckBox.getItems().setAll(
                            SQLColumn.ISO, SQLColumn.DATE, SQLColumn.TESTS, SQLColumn.VACCINATIONS
                    );
            case USER -> columnCheckBox.getItems().setAll(
                    SQLColumn.UUID, SQLColumn.FIRSTNAME, SQLColumn.LASTNAME, SQLColumn.STREET, SQLColumn.DOOR_NUMBER,
                    SQLColumn.CITY, SQLColumn.ZIP
            );
            case EPIDEMIOLOGIST -> columnCheckBox.getItems().setAll(
                    SQLColumn.UUID, SQLColumn.FIRSTNAME, SQLColumn.LASTNAME, SQLColumn.STREET, SQLColumn.DOOR_NUMBER,
                    SQLColumn.CITY, SQLColumn.ZIP, SQLColumn.CENTER, SQLColumn.SERVICE_PHONE
            );
        }

        columnCheckBox.getCheckModel().checkAll();
    }

    /**
     * Fill the table with a list of records
     *
     * @param dto list of records
     */
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

        if(columnCheckBox.getCheckModel().isChecked(SQLColumn.ID))
            tableView.getColumns().add(id);
        if(columnCheckBox.getCheckModel().isChecked(SQLColumn.DESCRIPTION))
            tableView.getColumns().add(description);

        tableView.getItems().addAll(dto);
    }

    /**
     * Fill the table with a list of records
     *
     * @param dto list of records
     */
    private void fillCountries(List<CountryDTO> dto) {
        tableView.getItems().clear();
        tableView.getColumns().clear();

        // Columns
        TableColumn<DTO, String> iso = new TableColumn<>("ISO");
        TableColumn<DTO, String> continent = new TableColumn<>("Continent");
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

        if(columnCheckBox.getCheckModel().isChecked(SQLColumn.ISO))
            tableView.getColumns().add(iso);
        if(columnCheckBox.getCheckModel().isChecked(SQLColumn.CONTINENT))
            tableView.getColumns().add(continent);
        if(columnCheckBox.getCheckModel().isChecked(SQLColumn.REGION))
            tableView.getColumns().add(region);
        if(columnCheckBox.getCheckModel().isChecked(SQLColumn.COUNTRY))
            tableView.getColumns().add(country);
        if(columnCheckBox.getCheckModel().isChecked(SQLColumn.HDI))
            tableView.getColumns().add(hdi);
        if(columnCheckBox.getCheckModel().isChecked(SQLColumn.POPULATION))
            tableView.getColumns().add(population);
        if(columnCheckBox.getCheckModel().isChecked(SQLColumn.AREA_SQ_ML))
            tableView.getColumns().add(area_sq_ml);
        if(columnCheckBox.getCheckModel().isChecked(SQLColumn.CLIMATE))
            tableView.getColumns().add(climate);

        tableView.getItems().addAll(dto);
    }

    /**
     * Fill the table with a list of records
     *
     * @param dto list of records
     */
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

        if(columnCheckBox.getCheckModel().isChecked(SQLColumn.ISO))
            tableView.getColumns().add(iso);
        if(columnCheckBox.getCheckModel().isChecked(SQLColumn.DATE))
            tableView.getColumns().add(date);
        if(columnCheckBox.getCheckModel().isChecked(SQLColumn.ICU_PATIENTS))
            tableView.getColumns().add(icu_patients);
        if(columnCheckBox.getCheckModel().isChecked(SQLColumn.HOSP_PATIENTS))
            tableView.getColumns().add(hosp_patients);
        if(columnCheckBox.getCheckModel().isChecked(SQLColumn.EPIDEMIOLOGIST))
            tableView.getColumns().add(epidemiologist);

        tableView.getItems().addAll(dto);
    }

    /**
     * Fill the table with a list of records
     *
     * @param dto list of records
     */
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

        if(columnCheckBox.getCheckModel().isChecked(SQLColumn.ISO))
            tableView.getColumns().add(iso);
        if(columnCheckBox.getCheckModel().isChecked(SQLColumn.DATE))
            tableView.getColumns().add(date);
        if(columnCheckBox.getCheckModel().isChecked(SQLColumn.VACCINES))
            tableView.getColumns().add(vaccines);

        tableView.getItems().addAll(dto);
    }

    /**
     * Fill the table with a list of records
     *
     * @param dto list of records
     */
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

        if(columnCheckBox.getCheckModel().isChecked(SQLColumn.ISO))
            tableView.getColumns().add(iso);
        if(columnCheckBox.getCheckModel().isChecked(SQLColumn.DATE))
            tableView.getColumns().add(date);
        if(columnCheckBox.getCheckModel().isChecked(SQLColumn.TESTS))
            tableView.getColumns().add(tests);
        if(columnCheckBox.getCheckModel().isChecked(SQLColumn.VACCINATIONS))
            tableView.getColumns().add(vaccinations);

        tableView.getItems().addAll(dto);
    }

    /**
     * Fill the table with a list of records
     *
     * @param dto list of records
     */
    private void fillUsers(List<UserDTO> dto) {
        tableView.getItems().clear();
        tableView.getColumns().clear();

        // Columns
        TableColumn<DTO, String> uuid = new TableColumn<>("UUID");
        TableColumn<DTO, String> firstname = new TableColumn<>("Firstname");
        TableColumn<DTO, String> lastname = new TableColumn<>("Lastname");
        TableColumn<DTO, String> street = new TableColumn<>("Street");
        TableColumn<DTO, String> door = new TableColumn<>("Doornumber");
        TableColumn<DTO, String> city = new TableColumn<>("City");
        TableColumn<DTO, String> zip = new TableColumn<>("ZIP");
        TableColumn<DTO, String> center = new TableColumn<>("Center");
        TableColumn<DTO, String> phone = new TableColumn<>("Service Phone");

        uuid.setCellValueFactory(data ->
                new ReadOnlyStringWrapper(((UserDTO) data.getValue()).getId().toString()));
        firstname.setCellValueFactory(data ->
                new ReadOnlyStringWrapper(((UserDTO) data.getValue()).getFirstName()));
        lastname.setCellValueFactory(data ->
                new ReadOnlyStringWrapper(((UserDTO) data.getValue()).getLastName()));
        street.setCellValueFactory(data ->
                new ReadOnlyStringWrapper(((UserDTO) data.getValue()).getStreet()));
        door.setCellValueFactory(data ->
                new ReadOnlyStringWrapper(((UserDTO) data.getValue()).getDoorNumber().toString()));
        city.setCellValueFactory(data ->
                new ReadOnlyStringWrapper(((UserDTO) data.getValue()).getCity()));
        zip.setCellValueFactory(data ->
                new ReadOnlyStringWrapper(((UserDTO) data.getValue()).getZipCode()));
        if(!dto.isEmpty() && dto.get(0) instanceof EpidemiologistDTO) {
            center.setCellValueFactory(data ->
                    new ReadOnlyStringWrapper(((EpidemiologistDTO) data.getValue()).getCenter()));
            phone.setCellValueFactory(data ->
                    new ReadOnlyStringWrapper(((EpidemiologistDTO) data.getValue()).getServiceNumber()));
        }

        if(columnCheckBox.getCheckModel().isChecked(SQLColumn.UUID))
            tableView.getColumns().add(uuid);
        if(columnCheckBox.getCheckModel().isChecked(SQLColumn.FIRSTNAME))
            tableView.getColumns().add(firstname);
        if(columnCheckBox.getCheckModel().isChecked(SQLColumn.LASTNAME))
            tableView.getColumns().add(lastname);
        if(columnCheckBox.getCheckModel().isChecked(SQLColumn.STREET))
            tableView.getColumns().add(street);
        if(columnCheckBox.getCheckModel().isChecked(SQLColumn.DOOR_NUMBER))
            tableView.getColumns().add(door);
        if(columnCheckBox.getCheckModel().isChecked(SQLColumn.CITY))
            tableView.getColumns().add(city);
        if(columnCheckBox.getCheckModel().isChecked(SQLColumn.ZIP))
            tableView.getColumns().add(zip);
        if(!dto.isEmpty() && dto.get(0) instanceof EpidemiologistDTO) {
            if(columnCheckBox.getCheckModel().getCheckedIndices().isEmpty()
                    || columnCheckBox.getCheckModel().isChecked(SQLColumn.CENTER))
                tableView.getColumns().add(center);
            if(columnCheckBox.getCheckModel().getCheckedIndices().isEmpty()
                    || columnCheckBox.getCheckModel().isChecked(SQLColumn.SERVICE_PHONE))
                tableView.getColumns().add(phone);
        }

        tableView.getItems().addAll(dto);
    }

    /**
     * Fills the table view with the epidemiologists records
     *
     * @param dto the records
     */
    private void fillEpidemiologists(List<EpidemiologistDTO> dto) {
        fillUsers(dto.stream().map(p -> (UserDTO)p).collect(Collectors.toList()));
    }

    /**
     * Fill the table with the returned value from the DB
     *
     * @param table the table type
     * @param dto the selection criteria
     * @throws SQLException if an error occurred
     */
    private void selectOnTable(DTOType table, DTO dto) throws SQLException, IllegalAccessException {
        switch(table) {
            case CLIMATE -> fillClimates(UserBusinessLogic.getInstance().select(((ClimateDTO) dto)));
            case COUNTRY -> fillCountries(UserBusinessLogic.getInstance().select(((CountryDTO) dto)));
            case HOSPITALS -> fillHospitals(UserBusinessLogic.getInstance().select(((HospitalsDTO) dto)));
            case PRODUCERS -> fillProducers(UserBusinessLogic.getInstance().select(((ProducersDTO) dto)));
            case VACCINATIONS -> fillVaccinations(UserBusinessLogic.getInstance().select(((VaccinationsDTO) dto)));
            case USER -> {
                if(!LoginToken.isEpidemiologist())
                    throw new IllegalAccessException(Utils.getTranslatedString("error_message_only_epidemiologist"));

                fillUsers(EpidemiologistBusinessLogic.getInstance().select(((UserDTO) dto)));
            }
            case EPIDEMIOLOGIST -> {
                if(!LoginToken.isEpidemiologist())
                    throw new IllegalAccessException(Utils.getTranslatedString("error_message_only_epidemiologist"));

                fillEpidemiologists(EpidemiologistBusinessLogic.getInstance().select(((EpidemiologistDTO) dto)));
            }
        }
    }

    /**
     * Inserts a record in the table
     *
     * @param table table type
     * @param dto insertion values
     * @throws IllegalAccessException if the user is not an epidemiologist
     * @throws SQLException if an error occurred
     */
    private void insertOnTable(DTOType table, DTO dto) throws IllegalAccessException, SQLException {
        if(!LoginToken.isEpidemiologist())
            throw new IllegalAccessException(Utils.getTranslatedString("error_message_only_epidemiologist"));

        switch(table) {
            case CLIMATE -> EpidemiologistBusinessLogic.getInstance().insert(((ClimateDTO) dto));
            case COUNTRY -> EpidemiologistBusinessLogic.getInstance().insert(((CountryDTO) dto).getId(), ((CountryDTO) dto));
            case HOSPITALS -> EpidemiologistBusinessLogic.getInstance().insert(((HospitalsDTO) dto));
            case PRODUCERS -> EpidemiologistBusinessLogic.getInstance().insert(((ProducersDTO) dto));
            case VACCINATIONS -> EpidemiologistBusinessLogic.getInstance().insert(((VaccinationsDTO) dto));
            case USER, EPIDEMIOLOGIST -> throw new IllegalArgumentException(Utils.getTranslatedString("error_message_insert_user"));
        }
    }

    /**
     * Updates a record in the DB
     *
     * @param table table type
     * @param dto update criteria
     * @param updated the updated information
     * @throws IllegalAccessException if the user is not an epidemiologist
     * @throws SQLException if an error occurred
     */
    private void updateOnTable(DTOType table, DTO dto, DTO updated) throws IllegalAccessException, SQLException {
        if(!LoginToken.isEpidemiologist())
            throw new IllegalAccessException(Utils.getTranslatedString("error_message_only_epidemiologist"));

        switch(table) {
            case CLIMATE -> EpidemiologistBusinessLogic.getInstance().update(((ClimateDTO) dto), ((ClimateDTO) updated));
            case COUNTRY -> EpidemiologistBusinessLogic.getInstance().update(((CountryDTO) dto), ((CountryDTO) updated));
            case HOSPITALS -> EpidemiologistBusinessLogic.getInstance().update(((HospitalsDTO) dto), ((HospitalsDTO) updated));
            case PRODUCERS -> EpidemiologistBusinessLogic.getInstance().update(((ProducersDTO) dto), ((ProducersDTO) updated));
            case VACCINATIONS -> EpidemiologistBusinessLogic.getInstance().update(((VaccinationsDTO) dto), ((VaccinationsDTO) updated));
            case USER -> EpidemiologistBusinessLogic.getInstance().update(((UserDTO) dto), ((UserDTO) updated));
            case EPIDEMIOLOGIST -> EpidemiologistBusinessLogic.getInstance().update(((EpidemiologistDTO) dto), ((EpidemiologistDTO) updated));
        }
    }

    /**
     * Deletes a record in the DB
     *
     * @param table table type
     * @param dto deletion criteria
     * @throws IllegalAccessException if the user is not an epidemiologist
     * @throws SQLException if an error occurred
     */
    private void deleteOnTable(DTOType table, DTO dto) throws IllegalAccessException, SQLException {
        if(!LoginToken.isEpidemiologist())
            throw new IllegalAccessException(Utils.getTranslatedString("error_message_only_epidemiologist"));

        switch(table) {
            case CLIMATE -> EpidemiologistBusinessLogic.getInstance().delete(((ClimateDTO) dto));
            case COUNTRY -> EpidemiologistBusinessLogic.getInstance().delete(((CountryDTO) dto));
            case HOSPITALS -> EpidemiologistBusinessLogic.getInstance().delete(((HospitalsDTO) dto));
            case PRODUCERS -> EpidemiologistBusinessLogic.getInstance().delete(((ProducersDTO) dto));
            case VACCINATIONS -> EpidemiologistBusinessLogic.getInstance().delete(((VaccinationsDTO) dto));
            case USER -> EpidemiologistBusinessLogic.getInstance().delete(((UserDTO) dto));
            case EPIDEMIOLOGIST -> EpidemiologistBusinessLogic.getInstance().delete(((EpidemiologistDTO) dto));
        }
    }

    /**
     * Create a request in the business layer
     *
     * @param request the request type
     * @param table the table type
     * @param dto the request criteria (will be a pair of dto if the request is "update")
     * @throws SQLException if an error occurred
     * @throws IllegalAccessException if the user is not an epidemiologist
     */
    private void request(SQLRequest request, DTOType table, Object dto) throws SQLException, IllegalAccessException {
        switch(request) {
            case SELECT -> selectOnTable(table, ((DTO) dto));
            case INSERT -> insertOnTable(table, ((DTO) dto));
            case UPDATE -> {
                @SuppressWarnings("unchecked")
                Pair<DTO, DTO> pairDto = ((Pair<DTO,DTO>) dto);
                DTO oldRecord = pairDto.getKey();
                DTO newRecord = pairDto.getValue();

                updateOnTable(table, oldRecord, newRecord);
            }
            case DELETE -> deleteOnTable(table, ((DTO) dto));
        }
    }

    /**
     * When the user pushes the "SQL" button
     */
    public void requestButtonAction() {
        try {
            final SQLRequest request = requestComboBox.getValue();
            final DTOType table = tableComboBox.getValue();

            final SQLDialog dialog = SQLDialog.promptDialog(request, table);

            final Optional<Object> optional = Optional.ofNullable(dialog.showAndWait());
            if(optional.isEmpty())
                return;

            final Object dto = dialog.getResult();
            if(dto == null)
                return;

            request(request, table, dto);
        } catch(Exception ex) {
            UITools.showErrorDialog(ex.getLocalizedMessage());
        }
    }
}

package controller.subpanels;

import database.business.UserBusinessLogic;
import database.transfer.CountryDTO;
import database.transfer.HospitalsDTO;
import database.transfer.ProducersDTO;
import database.transfer.VaccinationsDTO;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import org.controlsfx.control.CheckComboBox;
import view.UITools;

import java.net.URL;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings("DuplicatedCode")
public class DashboardController implements Initializable {
    @FXML private CheckComboBox<CountryDTO> countryCheckComboBox;
    @FXML private PieChart vaccinesPieChart;
    @FXML private PieChart vaccinationsPieChart;
    @FXML private PieChart hospPieChart;
    @FXML private LineChart<String, Integer> hospLineChart;
    @FXML private LineChart<String, Integer> icuLineChart;
    @FXML private Label tooltip;

    private final Map<String, String[]> producers = new HashMap<>();
    private final Map<String, List<VaccinationsDTO>> vaccinations = new HashMap<>();
    private final Map<String, List<HospitalsDTO>> hospitals = new HashMap<>();

    private final ListChangeListener<CountryDTO> listener = change -> {
        change.next();
        boolean update = false;

        if(change.getRemovedSize() != 0) {
            final List<String> removed = change.getRemoved().stream().map(CountryDTO::getId).collect(Collectors.toList());

            vaccinesPieChart.getData().removeIf(data -> removed.contains(data.getName()));
            vaccinationsPieChart.getData().removeIf(data -> removed.contains(data.getName()));
            hospPieChart.getData().removeIf(data -> removed.contains(data.getName()));
            hospLineChart.getData().removeIf(data -> removed.contains(data.getName()));
            icuLineChart.getData().removeIf(data -> removed.contains(data.getName()));

            update = true;
        }

        if(change.getAddedSize() != 0) {
            fillVaccinesPieData(change.getAddedSubList());
            fillVaccinationPieData(change.getAddedSubList());
            fillHospPieData(change.getAddedSubList());
            fillHospLineChart(change.getAddedSubList());
            fillIcuLineChart(change.getAddedSubList());

            update = true;
        }

        if(update) {
            updatePieChartMouseEvent(vaccinesPieChart);
            updatePieChartMouseEvent(vaccinationsPieChart);
            updatePieChartMouseEvent(hospPieChart);
            updateLineChartMouseEvent(hospLineChart);
            updateLineChartMouseEvent(icuLineChart);
        }
    };

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tooltip.toFront();
        tooltip.setBackground(new Background(new BackgroundFill(
                Color.BLACK, new CornerRadii(5.0), new Insets(-5.0)
        )));

        vaccinesPieChart.setLegendVisible(false);
        vaccinationsPieChart.setLegendVisible(false);
        hospPieChart.setLegendVisible(false);
        hospLineChart.setLegendVisible(false);
        icuLineChart.setLegendVisible(false);

        countryCheckComboBox.getCheckModel().getCheckedItems().addListener(listener);
    }

    /**
     * Loads the datas from the database
     */
    public void loadData() {
        try {
            loadHospitalsData();
            loadVaccinationsData();
            loadProducersData();
            loadCountriesData();
        } catch (SQLException ex) {
            UITools.showErrorDialog(ex.getLocalizedMessage());
        }
    }

    /**
     * Loads the countries data from the database
     *
     * @throws SQLException if an SQL error occurs
     */
    private void loadCountriesData() throws SQLException {
        List<CountryDTO> records = UserBusinessLogic.getInstance().selectAllCountries();
        records.sort(Comparator.comparing(CountryDTO::getName));

        countryCheckComboBox.getItems().setAll(records);
    }

    /**
     * Loads the vaccinations data from the database
     *
     * @throws SQLException if an SQL error occurs
     */
    private void loadVaccinationsData() throws SQLException {
        List<VaccinationsDTO> records = UserBusinessLogic.getInstance().selectAllVaccinations();
        records.sort(Comparator.comparing(VaccinationsDTO::getDate));

        records.forEach(p -> {
            String iso = p.getISO();

            if (!vaccinations.containsKey(iso))
                vaccinations.put(
                        iso,
                        records.stream()
                                .filter(q -> q.getISO().equals(iso))
                                .collect(Collectors.toList())
                );
        });
    }

    /**
     * Loads the hospitals data from the database
     *
     * @throws SQLException if an SQL error occurs
     */
    private void loadHospitalsData() throws SQLException {
        List<HospitalsDTO> records = UserBusinessLogic.getInstance().selectAllHospitalsLimited(6);
        records.sort(Comparator.comparing(HospitalsDTO::getDate));

        records.forEach(p -> {
            String iso = p.getISO();

            if (!hospitals.containsKey(iso))
                hospitals.put(
                        iso,
                        records.stream()
                                .filter(q -> q.getISO().equals(iso))
                                .collect(Collectors.toList())
                );
        });
    }

    /**
     * Loads the producers data from the database
     *
     * @throws SQLException if an SQL error occurs
     */
    private void loadProducersData() throws SQLException {
        List<ProducersDTO> records = UserBusinessLogic.getInstance().selectAllProducers();

        records.forEach(p -> {
            String iso = p.getId();

            if (!producers.containsKey(iso))
                producers.put(
                        iso,
                        p.getVaccines()
                );
        });
    }

    /**
     * Used to link a tooltip in a given pie chart graphs
     *
     * @param chart the pie chart
     */
    private void updatePieChartMouseEvent(PieChart chart) {
        for (final PieChart.Data data : chart.getData()) {
            data.getNode().addEventHandler(MouseEvent.MOUSE_MOVED,
                    e -> {
                        tooltip.relocate(e.getSceneX(), e.getSceneY());

                        tooltip.setText(data.getName() + " : " + data.getPieValue());
                        tooltip.setVisible(true);
                    });

            data.getNode().addEventHandler(MouseEvent.MOUSE_EXITED,
                    e -> tooltip.setVisible(false));
        }
    }

    /**
     * Used to link a tooltip in a given line chart graphs
     *
     * @param chart the line chart
     */
    private void updateLineChartMouseEvent(LineChart<String, Integer> chart) {
        for (XYChart.Series<String, Integer> data : chart.getData()) {
            data.getNode().addEventHandler(MouseEvent.MOUSE_MOVED,
                    e -> {
                        tooltip.relocate(e.getSceneX(), e.getSceneY());

                        tooltip.setText(data.getName());
                        tooltip.setVisible(true);
                    });

            data.getNode().addEventHandler(MouseEvent.MOUSE_EXITED,
                    e -> tooltip.setVisible(false));
        }
    }

    /**
     * Checks if the pie chart contains a given data
     *
     * @param chart chart
     * @param iso the data
     * @return boolean
     */
    private boolean containsData(PieChart chart, String iso) {
        return chart.getData().stream().anyMatch(p -> p.getName().equals(iso));
    }

    /**
     * Fills the vaccines amount pie chart with data
     *
     * @param addedSubList given added data
     */
    private void fillVaccinesPieData(List<? extends CountryDTO> addedSubList) {
        addedSubList.forEach(p -> {
            if(producers.get(p.getId()) == null || producers.get(p.getId()).length == 0
                    || containsData(vaccinesPieChart, p.getId()))
                return;

            PieChart.Data data = new PieChart.Data(p.getId(), producers.get(p.getId()).length);

            vaccinesPieChart.getData().add(data);
        });
    }

    /**
     * Fills the vaccinations amount pie chart with data
     *
     * @param addedSubList given added data
     */
    private void fillVaccinationPieData(List<? extends CountryDTO> addedSubList) {
        addedSubList.forEach(p -> {
            if(vaccinations.get(p.getId()) == null || vaccinations.get(p.getId()).isEmpty()
                    || containsData(vaccinationsPieChart, p.getId()))
                return;

            VaccinationsDTO report = vaccinations.get(p.getId()).get(vaccinations.get(p.getId()).size()-1);
            PieChart.Data data = new PieChart.Data(p.getId(), report.getVaccinations());

            vaccinationsPieChart.getData().add(data);
        });
    }

    /**
     * Fills the hospitalisations amount pie chart with data
     *
     * @param addedSubList given added data
     */
    private void fillHospPieData(List<? extends CountryDTO> addedSubList) {
        addedSubList.forEach(p -> {
            if(hospitals.get(p.getId()) == null || hospitals.get(p.getId()).isEmpty()
                    || containsData(hospPieChart, p.getId()))
                return;

            HospitalsDTO report = hospitals.get(p.getId()).get(hospitals.get(p.getId()).size()-1);
            PieChart.Data data = new PieChart.Data(p.getId(), report.getHosp_patients());

            hospPieChart.getData().add(data);
        });
    }

    /**
     * Fills the hospitalisations pie chart with datas
     *
     * @param addedSubList given added data
     */
    private void fillHospLineChart(List<? extends CountryDTO> addedSubList) {
        addedSubList.forEach(p -> {
            if(hospitals.get(p.getId()) == null || hospitals.get(p.getId()).isEmpty())
                return;

            List<XYChart.Data<String, Integer>> datas =
                    hospitals.get(p.getId()).stream()
                            .map(r -> new XYChart.Data<>(r.getDate().toString(), r.getHosp_patients()))
                            .collect(Collectors.toList());

            fillLineChart(p.getId(), hospLineChart, datas);
        });
    }

    /**
     * Fills the ICU pie chart with datas
     *
     * @param addedSubList given added data
     */
    private void fillIcuLineChart(List<? extends CountryDTO> addedSubList) {
        addedSubList.forEach(p -> {
            if(hospitals.get(p.getId()) == null || hospitals.get(p.getId()).isEmpty())
                return;

            List<XYChart.Data<String, Integer>> datas =
                    hospitals.get(p.getId()).stream()
                            .map(r -> new XYChart.Data<>(r.getDate().toString(), r.getIcu_patients()))
                            .collect(Collectors.toList());

            fillLineChart(p.getId(), icuLineChart, datas);
        });
    }

    /**
     * Fills a pie chart with datas
     */
    private void fillLineChart(String iso, LineChart<String, Integer> chart, List<XYChart.Data<String, Integer>> datas) {
        XYChart.Series<String, Integer> series = new XYChart.Series<>();
        series.setName(iso);
        series.getData().addAll(datas);

        chart.getData().add(series);
        chart.setCreateSymbols(false);
    }

    @FXML
    private void selectAllAction() {
        countryCheckComboBox.getCheckModel().checkAll();
    }

    @FXML
    private void deselectAllAction() {
        countryCheckComboBox.getCheckModel().clearChecks();
    }
}

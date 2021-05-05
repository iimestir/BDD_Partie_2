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
    @FXML private CheckComboBox<String> countryCheckComboBox;
    @FXML private PieChart vaccinesPieChart;
    @FXML private PieChart vaccinationsPieChart;
    @FXML private PieChart hospPieChart;
    @FXML private LineChart<String, Integer> hospLineChart;
    @FXML private LineChart<String, Integer> icuLineChart;
    @FXML private Label tooltip;

    private final Map<String, List<HospitalsDTO>> hospitals = new HashMap<>();
    private final Map<String, List<VaccinationsDTO>> vaccinations = new HashMap<>();
    private final Map<String, String[]> producers = new HashMap<>();

    private final ListChangeListener<String> listener = change -> updateCharts();

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

        enableListener();
    }

    /**
     * Loads the datas from the database
     */
    public void loadDatas() {
        try {
            loadCountriesData();
            loadHospitalsData();
            loadVaccinationsData();
            loadProducersData();
        } catch (SQLException ex) {
            UITools.showErrorDialog(ex.getLocalizedMessage());
        }
    }

    /**
     * Updates the dashboard
     */
    public void updateCharts() {
        if(countryCheckComboBox.getCheckModel().getCheckedItems().size() == 0) {
            vaccinesPieChart.getData().clear();
            vaccinationsPieChart.getData().clear();
            hospPieChart.getData().clear();
            hospLineChart.getData().clear();
            icuLineChart.getData().clear();
            return;
        }

        updateVaccinesPieChart();
        updateVaccinationsPieChart();
        updateHospPieChart();
        fillHospLineChart();
        fillIcuLineChart();
    }

    /**
     * Loads the countries data from the database
     *
     * @throws SQLException if an SQL error occurs
     */
    private void loadCountriesData() throws SQLException {
        List<CountryDTO> records = UserBusinessLogic.getInstance().selectAllCountries();
        records.sort(Comparator.comparing(CountryDTO::getId));

        countryCheckComboBox.getItems().setAll(
                records.stream().map(CountryDTO::getId).collect(Collectors.toList())
        );
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
        List<HospitalsDTO> records = UserBusinessLogic.getInstance().selectAllHospitals();
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
     * Updates the vaccines pie chart
     */
    private void updateVaccinesPieChart() {
        vaccinesPieChart.getData().clear();

        producers.forEach((p, q) -> {
            if(!countryCheckComboBox.getCheckModel().getCheckedItems().contains(p))
                return;

            PieChart.Data data = new PieChart.Data(p, q.length);

            vaccinesPieChart.getData().add(data);
        });

        updatePieChartMouseEvent(vaccinesPieChart);
    }

    /**
     * Updates the vaccinations pie chart
     */
    private void updateVaccinationsPieChart() {
        vaccinationsPieChart.getData().clear();

        vaccinations.forEach((p, q) -> {
            if(!countryCheckComboBox.getCheckModel().getCheckedItems().contains(p))
                return;

            VaccinationsDTO report = q.get(q.size() - 1);
            PieChart.Data data = new PieChart.Data(report.getISO(), report.getVaccinations());

            vaccinationsPieChart.getData().add(data);
        });

        updatePieChartMouseEvent(vaccinationsPieChart);
    }

    /**
     * Updates the hospitals pie chart
     */
    private void updateHospPieChart() {
        hospPieChart.getData().clear();

        hospitals.forEach((p, q) -> {
            if(!countryCheckComboBox.getCheckModel().getCheckedItems().contains(p))
                return;

            HospitalsDTO report = q.get(q.size() - 1);
            PieChart.Data data = new PieChart.Data(report.getISO(), report.getHosp_patients());

            hospPieChart.getData().add(data);
        });

        updatePieChartMouseEvent(hospPieChart);
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

                        tooltip.setText(String.valueOf(data.getPieValue()));
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
     * Fills the hospitalisations pie chart with datas
     */
    private void fillHospLineChart() {
        hospLineChart.getData().clear();

        hospitals.forEach((p, q) -> {
            if(!countryCheckComboBox.getCheckModel().getCheckedItems().contains(p))
                return;

            List<XYChart.Data<String, Integer>> datas =
                    q.stream()
                            .map(r -> new XYChart.Data<>(r.getDate().toString(), r.getHosp_patients()))
                            .collect(Collectors.toList());

            fillLineChart(p, hospLineChart, datas);
        });
    }

    /**
     * Fills the ICU pie chart with datas
     */
    private void fillIcuLineChart() {
        icuLineChart.getData().clear();

        hospitals.forEach((p, q) -> {
            if(!countryCheckComboBox.getCheckModel().getCheckedItems().contains(p))
                return;

            List<XYChart.Data<String, Integer>> datas =
                    q.stream()
                            .map(r -> new XYChart.Data<>(r.getDate().toString(), r.getIcu_patients()))
                            .collect(Collectors.toList());

            fillLineChart(p, icuLineChart, datas);
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
        updateLineChartMouseEvent(chart);
    }

    /**
     * Initializes listeners
     */
    private void enableListener() {
        countryCheckComboBox.getCheckModel().getCheckedItems().addListener(listener);
    }

    /**
     * Disables listeners
     */
    private void disableListener() {
        countryCheckComboBox.getCheckModel().getCheckedItems().removeListener(listener);
    }

    @FXML
    private void selectAllAction() {
        disableListener();
        countryCheckComboBox.getCheckModel().checkAll();
        enableListener();

        updateCharts();
    }

    @FXML
    private void deselectAllAction() {
        disableListener();
        countryCheckComboBox.getCheckModel().clearChecks();
        enableListener();

        updateCharts();
    }
}

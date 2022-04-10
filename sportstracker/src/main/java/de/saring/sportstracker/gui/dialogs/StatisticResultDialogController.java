package de.saring.sportstracker.gui.dialogs;

import jakarta.inject.Inject;

import de.saring.sportstracker.data.statistic.StatisticCalculator;
import de.saring.sportstracker.gui.STContext;
import de.saring.util.unitcalc.FormatUtils;
import de.saring.util.unitcalc.SpeedMode;
import de.saring.util.unitcalc.TimeUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Window;

/**
 * Controller (MVC) class of the dialog for displaying the results for the calculated exercise statistics.
 *
 * @author Stefan Saring
 */
public class StatisticResultDialogController extends AbstractDialogController {

    @FXML
    private Label laTotalExercisesValue;
    @FXML
    private Label laTotalDistanceValue;
    @FXML
    private Label laTotalDurationValue;
    @FXML
    private Label laTotalAscentValue;
    @FXML
    private Label laTotalDescentValue;
    @FXML
    private Label laTotalEnergyValue;

    @FXML
    private Label laAvgDistanceValue;
    @FXML
    private Label laAvgAvgSpeedValue;
    @FXML
    private Label laAvgDurationValue;
    @FXML
    private Label laAvgAscentValue;
    @FXML
    private Label laAvgDescentValue;
    @FXML
    private Label laAvgAvgHeartrateValue;
    @FXML
    private Label laAvgEnergyValue;

    @FXML
    private Label laMinDistanceValue;
    @FXML
    private Label laMinAvgSpeedValue;
    @FXML
    private Label laMinDurationValue;
    @FXML
    private Label laMinAscentValue;
    @FXML
    private Label laMinDescentValue;
    @FXML
    private Label laMinAvgHeartrateValue;
    @FXML
    private Label laMinEnergyValue;

    @FXML
    private Label laMaxDistanceValue;
    @FXML
    private Label laMaxAvgSpeedValue;
    @FXML
    private Label laMaxDurationValue;
    @FXML
    private Label laMaxAscentValue;
    @FXML
    private Label laMaxDescentValue;
    @FXML
    private Label laMaxAvgHeartrateValue;
    @FXML
    private Label laMaxEnergyValue;

    private StatisticCalculator statisticResult;
    private SpeedMode speedMode;


    /**
     * Standard c'tor for dependency injection.
     *
     * @param context the SportsTracker UI context
     */
    @Inject
    public StatisticResultDialogController(final STContext context) {
        super(context);
    }

    /**
     * Displays the Statistic dialog for the current Exercise filter.
     *
     * @param parent parent window of the dialog
     * @param statisticResult statistic results to display
     * @param speedMode speed mode for showing the results
     */
    public void show(final Window parent, final StatisticCalculator statisticResult, SpeedMode speedMode) {
        this.statisticResult = statisticResult;
        this.speedMode = speedMode;

        showInfoDialog("/fxml/dialogs/StatisticResultDialog.fxml", parent,
                context.getResources().getString("st.dlg.statistic_results.title"));
    }

    @Override
    protected void setupDialogControls() {
        // the controls are read only, so binding and view model is not needed here
        displayStatisticResultValues();
    }

    private void displayStatisticResultValues() {
        final FormatUtils formatUtils = context.getFormatUtils();
        final String empty = "";

        // display total values
        laTotalExercisesValue.setText(String.valueOf(statisticResult.getExerciseCount()));
        laTotalDistanceValue.setText(formatUtils.distanceToString(statisticResult.getTotalDistance(), 2));
        laTotalDurationValue.setText(TimeUtils.seconds2TimeString(statisticResult.getTotalDuration()) + " (hh:mm:ss)");
        laTotalAscentValue.setText(formatUtils.heightToString(statisticResult.getTotalAscent()));
        laTotalDescentValue.setText(formatUtils.heightToString(statisticResult.getTotalDescent()));
        laTotalEnergyValue.setText(statisticResult.getTotalCalories() > 0 ?
                formatUtils.caloriesToString(statisticResult.getTotalCalories()) : empty);

        // display average values
        laAvgDistanceValue.setText(formatUtils.distanceToString(statisticResult.getAvgDistance(), 2));
        laAvgAvgSpeedValue.setText(formatUtils.speedToString(statisticResult.getAvgSpeed(), 2, speedMode));
        laAvgDurationValue.setText(TimeUtils.seconds2TimeString(statisticResult.getAvgDuration()) + " (hh:mm:ss)");
        laAvgAscentValue.setText(formatUtils.heightToString(statisticResult.getAvgAscent()));
        laAvgDescentValue.setText(formatUtils.heightToString(statisticResult.getAvgDescent()));
        laAvgAvgHeartrateValue.setText(statisticResult.getAvgHeartRate() > 0 ?
                formatUtils.heartRateToString(statisticResult.getAvgHeartRate()) : empty);
        laAvgEnergyValue.setText(statisticResult.getAvgCalories() > 0 ?
                formatUtils.caloriesToString(statisticResult.getAvgCalories()) : empty);

        // display minimum values
        laMinDistanceValue.setText(formatUtils.distanceToString(statisticResult.getMinDistance(), 2));
        laMinAvgSpeedValue.setText(formatUtils.speedToString(statisticResult.getMinAvgSpeed(), 2, speedMode));
        laMinDurationValue.setText(TimeUtils.seconds2TimeString(statisticResult.getMinDuration()) + " (hh:mm:ss)");
        laMinAscentValue.setText(formatUtils.heightToString(statisticResult.getMinAscent()));
        laMinDescentValue.setText(formatUtils.heightToString(statisticResult.getMinDescent()));
        laMinAvgHeartrateValue.setText(statisticResult.getMinAvgHeartRate() > 0 ?
                formatUtils.heartRateToString(statisticResult.getMinAvgHeartRate()) : empty);
        laMinEnergyValue.setText(statisticResult.getMinCalories() > 0 ?
                formatUtils.caloriesToString(statisticResult.getMinCalories()) : empty);

        // display maximum values
        laMaxDistanceValue.setText(formatUtils.distanceToString(statisticResult.getMaxDistance(), 2));
        laMaxAvgSpeedValue.setText(formatUtils.speedToString(statisticResult.getMaxAvgSpeed(), 2, speedMode));
        laMaxDurationValue.setText(TimeUtils.seconds2TimeString(statisticResult.getMaxDuration()) + " (hh:mm:ss)");
        laMaxAscentValue.setText(formatUtils.heightToString(statisticResult.getMaxAscent()));
        laMaxDescentValue.setText(formatUtils.heightToString(statisticResult.getMaxDescent()));
        laMaxAvgHeartrateValue.setText(statisticResult.getMaxAvgHeartRate() > 0 ?
                formatUtils.heartRateToString(statisticResult.getMaxAvgHeartRate()) : empty);
        laMaxEnergyValue.setText(statisticResult.getMaxCalories() > 0 ?
                formatUtils.caloriesToString(statisticResult.getMaxCalories()) : empty);
    }
}

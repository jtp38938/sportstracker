package de.saring.sportstracker.gui.dialogs;

import java.util.stream.Stream;

import de.saring.util.unitcalc.SpeedMode;
import de.saring.util.unitcalc.UnitSystem;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Window;
import javafx.util.StringConverter;

import javax.inject.Inject;

import de.saring.sportstracker.core.STOptions;
import de.saring.sportstracker.gui.STContext;
import de.saring.sportstracker.gui.STDocument;
import de.saring.util.gui.javafx.BindingUtils;

/**
 * Controller (MVC) class of the Preferences dialog for editing the application preferences / options.
 *
 * @author Stefan Saring
 */
public class PreferencesDialogController extends AbstractDialogController {

    private final STDocument document;

    // tab pane "Main"
    @FXML
    private RadioButton rbInitialViewCalendar;
    @FXML
    private RadioButton rbInitialViewExerciseList;
    @FXML
    private ToggleGroup tgInitialView;
    @FXML
    private ChoiceBox<STOptions.AutoCalculation> cbAutomaticCalculation;
    @FXML
    private CheckBox cbSaveOnExit;

    // tab pane "Units"
    @FXML
    private RadioButton rbUnitsMetric;
    @FXML
    private RadioButton rbUnitsEnglish;
    @FXML
    private ToggleGroup tgUnitSystem;
    @FXML
    private RadioButton rbSpeedModeSpeed;
    @FXML
    private RadioButton rbSpeedModePace;
    @FXML
    private ToggleGroup tgSpeedMode;
    @FXML
    private RadioButton rbWeekStartMonday;
    @FXML
    private RadioButton rbWeekStartSunday;
    @FXML
    private ToggleGroup tgWeekStart;

    // tab pane "List View"
    @FXML
    private CheckBox cbOptionalAvgHeartrate;
    @FXML
    private CheckBox cbOptionalAscent;
    @FXML
    private CheckBox cbOptionalDescent;
    @FXML
    private CheckBox cbOptionalEnergy;
    @FXML
    private CheckBox cbOptionalEquipment;
    @FXML
    private CheckBox cbOptionalComment;

    // tab pane "ExerciseViewer"
    @FXML
    private CheckBox cbDiagramTwoGraphs;
    @FXML
    private CheckBox cbDiagramSmoothedCharts;

    /** ViewModel of the edited options. */
    private PreferencesViewModel preferencesViewModel;


    /**
     * Standard c'tor for dependency injection.
     *
     * @param context the SportsTracker UI context
     * @param document the SportsTracker model/document
     */
    @Inject
    public PreferencesDialogController(final STContext context, final STDocument document) {
        super(context);
        this.document = document;
    }

    /**
     * Displays the Preferences dialog.
     *
     * @param parent parent window of the dialog
     */
    public void show(final Window parent) {
        this.preferencesViewModel = new PreferencesViewModel(document.getOptions());

        showEditDialog("/fxml/dialogs/PreferencesDialog.fxml", parent,
                context.getResources().getString("st.dlg.options.title"));
    }

    @Override
    protected void setupDialogControls() {
        setupSelectionControls();

        // setup binding between view model and the UI controls
        // (validation is not needed here)
        BindingUtils.bindToggleGroupToProperty(tgInitialView, preferencesViewModel.initialView);
        cbAutomaticCalculation.valueProperty().bindBidirectional(preferencesViewModel.defaultAutoCalculation);
        cbSaveOnExit.selectedProperty().bindBidirectional(preferencesViewModel.saveOnExit);

        BindingUtils.bindToggleGroupToProperty(tgUnitSystem, preferencesViewModel.unitSystem);
        BindingUtils.bindToggleGroupToProperty(tgSpeedMode, preferencesViewModel.preferredSpeedMode);
        BindingUtils.bindToggleGroupToProperty(tgWeekStart, preferencesViewModel.weekStart);

        cbOptionalAvgHeartrate.selectedProperty().bindBidirectional(preferencesViewModel.listViewShowAvgHeartrate);
        cbOptionalAscent.selectedProperty().bindBidirectional(preferencesViewModel.listViewShowAscent);
        cbOptionalDescent.selectedProperty().bindBidirectional(preferencesViewModel.listViewShowDescent);
        cbOptionalEnergy.selectedProperty().bindBidirectional(preferencesViewModel.listViewShowEnergy);
        cbOptionalEquipment.selectedProperty().bindBidirectional(preferencesViewModel.listViewShowEquipment);
        cbOptionalComment.selectedProperty().bindBidirectional(preferencesViewModel.listViewShowComment);

        cbDiagramTwoGraphs.selectedProperty().bindBidirectional(preferencesViewModel.evDisplaySecondChart);
        cbDiagramSmoothedCharts.selectedProperty().bindBidirectional(preferencesViewModel.evDisplaySmoothedCharts);
    }

    @Override
    protected boolean validateAndStore() {

        // store the new preferences, no further validation needed
        preferencesViewModel.storeInOptions(document.getOptions());
        document.storeOptions();
        return true;
    }

    private void setupSelectionControls() {

        // store selection objects as user data in radio buttons
        rbInitialViewCalendar.setUserData(STOptions.View.Calendar);
        rbInitialViewExerciseList.setUserData(STOptions.View.List);

        rbUnitsMetric.setUserData(UnitSystem.METRIC);
        rbUnitsEnglish.setUserData(UnitSystem.ENGLISH);

        rbSpeedModeSpeed.setUserData(SpeedMode.SPEED);
        rbSpeedModePace.setUserData(SpeedMode.PACE);

        rbWeekStartMonday.setUserData(PreferencesViewModel.WeekStart.MONDAY);
        rbWeekStartSunday.setUserData(PreferencesViewModel.WeekStart.SUNDAY);

        // fill autocalculation choice box, display localized names the enum items
        Stream.of(STOptions.AutoCalculation.values()).forEach((autoCalculation) ->
                cbAutomaticCalculation.getItems().add(autoCalculation));

        cbAutomaticCalculation.setConverter(new StringConverter<STOptions.AutoCalculation>() {

            @Override
            public String toString(final STOptions.AutoCalculation autoCalculation) {
                return switch (autoCalculation) {
                    case Distance -> context.getResources().getString("st.dlg.options.distance.text");
                    case AvgSpeed -> context.getResources().getString("st.dlg.options.avg_speed.text");
                    case Duration -> context.getResources().getString("st.dlg.options.duration.text");
                    default -> "";
                };
            }

            @Override
            public STOptions.AutoCalculation fromString(final String string) {
                return null;
            }
        });
    }
}

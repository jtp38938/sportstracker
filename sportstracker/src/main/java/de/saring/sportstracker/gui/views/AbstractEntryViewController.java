package de.saring.sportstracker.gui.views;

import java.io.IOException;

import javafx.scene.Parent;

import de.saring.sportstracker.gui.STContext;
import de.saring.sportstracker.gui.STDocument;
import de.saring.util.gui.javafx.FxmlLoader;

/**
 * Abstract Controller (MVC) base class of for all SportsTracker content Views.
 *
 * @author Stefan Saring
 */
public abstract class AbstractEntryViewController implements EntryViewController {

    private static final long[] EMPTY_ID_ARRAY = new long[0];

    private final STContext context;
    private final STDocument document;
    private final ViewPrinter viewPrinter;
    private EntryViewEventHandler eventHandler;

    private Parent rootNode;

    /**
     * Standard c'tor for dependency injection.
     *
     * @param context the SportsTracker UI context
     * @param document the SportsTracker document / model
     * @param viewPrinter the printer of the SportsTracker views
     */
    public AbstractEntryViewController(final STContext context, final STDocument document, final ViewPrinter viewPrinter) {
        this.context = context;
        this.document = document;
        this.viewPrinter = viewPrinter;
    }

    @Override
    public void initAndSetupViewContent(EntryViewEventHandler eventHandler) {
        this.eventHandler = eventHandler;

        final String fxmlFilename = getFxmlFilename();
        try {
            rootNode = FxmlLoader.load(this.getClass().getResource(fxmlFilename), //
                    context.getResources().getResourceBundle(), this);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load the FXML resource '" + fxmlFilename + "'!", e);
        }

        setupView();
    }

    @Override
    public Parent getRootNode() {
        return rootNode;
    }

    @Override
    public int getSelectedExerciseCount() {
        return 0;
    }

    @Override
    public long[] getSelectedExerciseIDs() {
        return EMPTY_ID_ARRAY;
    }

    @Override
    public int getSelectedNoteCount() {
        return 0;
    }

    @Override
    public long[] getSelectedNoteIDs() {
        return EMPTY_ID_ARRAY;
    }

    @Override
    public int getSelectedWeightCount() {
        return 0;
    }

    @Override
    public long[] getSelectedWeightIDs() {
        return EMPTY_ID_ARRAY;
    }

    @Override
    public void print() {
        viewPrinter.printView(rootNode);
    }

    /**
     * Returns the event handler for this entry view.
     *
     * @return EntryViewEventHandler
     */
    protected EntryViewEventHandler getEventHandler() {
        return eventHandler;
    }
    /**
     * Returns the name of the FXML file which contains the UI definition of the view.
     *
     * @return FXML filename
     */
    protected abstract String getFxmlFilename();

    /**
     * Sets up all the view controls. Will be called after the UI has been loaded from FXML.
     */
    protected abstract void setupView();

    /**
     * Returns the SportsTracker UI context.
     *
     * @return STContext
     */
    protected STContext getContext() {
        return context;
    }

    /**
     * Returns the SportsTracker model/document.
     *
     * @return STDocument
     */
    protected STDocument getDocument() {
        return document;
    }
}

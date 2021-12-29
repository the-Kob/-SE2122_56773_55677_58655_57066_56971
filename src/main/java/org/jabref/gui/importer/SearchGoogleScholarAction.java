package org.jabref.gui.importer;

import org.jabref.gui.DialogService;
import org.jabref.gui.JabRefFrame;
import org.jabref.gui.StateManager;
import org.jabref.gui.actions.SimpleCommand;
import org.jabref.preferences.PreferencesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import static org.jabref.gui.actions.ActionHelper.needsDatabase;


public class SearchGoogleScholarAction extends SimpleCommand{
    private static final Logger LOGGER = LoggerFactory.getLogger(NewEntryAction.class);

    private final JabRefFrame jabRefFrame;
    /**
     * The type of the entry to create.
     */
    private final DialogService dialogService;
    private final PreferencesService preferences;

    public SearchGoogleScholarAction(JabRefFrame jabRefFrame, DialogService dialogService, PreferencesService preferences, StateManager stateManager) {
        this.jabRefFrame = jabRefFrame;
        this.dialogService = dialogService;
        this.preferences = preferences;

        this.executable.bind(needsDatabase(stateManager));
    }

    @Override
    public void execute() {
        if (jabRefFrame.getBasePanelCount() <= 0) {
            LOGGER.error("Action 'Search in Google Scholar' must be disabled when no database is open.");
            return;
        }

        String toSearch = dialogService.showInputDialogAndWait("Search in Google Scholar", "Search...").get();
        toSearch = toSearch.toLowerCase().replace(' ', '+');

        if (toSearch.equals("")) {
            return;
        } else {
            Desktop desktop = java.awt.Desktop.getDesktop();

            try {
                desktop.browse(new URI("https://scholar.google.com/scholar?q="+toSearch+"&btnG=)"));
            } catch (URISyntaxException | IOException e) {
                e.printStackTrace();
            }
        }

    }
}

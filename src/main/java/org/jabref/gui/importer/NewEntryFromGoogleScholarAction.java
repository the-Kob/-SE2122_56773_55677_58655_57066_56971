package org.jabref.gui.importer;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.jabref.gui.*;

import org.jabref.gui.actions.SimpleCommand;
import org.jabref.gui.importer.serpapi.SerpApiSearchException;
import org.jabref.model.entry.BibEntry;
import org.jabref.model.entry.field.StandardField;
import org.jabref.model.entry.types.EntryType;
import org.jabref.model.entry.types.StandardEntryType;
import org.jabref.preferences.PreferencesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.jabref.gui.importer.serpapi.GoogleSearch;

import java.util.*;

public class NewEntryFromGoogleScholarAction extends SimpleCommand {

    private static final Logger LOGGER = LoggerFactory.getLogger(NewEntryAction.class);

    private final JabRefFrame jabRefFrame;
    private final DialogService dialogService;
    private final PreferencesService preferences;


    public NewEntryFromGoogleScholarAction(JabRefFrame jabRefFrame, DialogService dialogService, PreferencesService preferences, StateManager stateManager) {
        this.jabRefFrame = jabRefFrame;
        this.dialogService = dialogService;
        this.preferences = preferences;
    }

    @Override
    public void execute() {
        try {
            String name = dialogService.showInputDialogAndWait("Insert Google Scholar article name", "Name").get();

            Map<String, String> info = parseJson(getArticleJson(name));

            BibEntry bibEntry = new BibEntry(StandardEntryType.Article);
            bibEntry.setField(StandardField.TITLE, info.get("title"));
            bibEntry.setField(StandardField.AUTHOR, info.get("author"));
            bibEntry.setField(StandardField.JOURNAL, info.get("journal"));

            jabRefFrame.getCurrentLibraryTab().insertEntry(bibEntry);
        }
        catch (SerpApiSearchException e) {
            e.printStackTrace();
        }
    }

    private void trackNewEntry(EntryType type) {
        Map<String, String> properties = new HashMap<>();
        properties.put("EntryType", type.getName());

        Globals.getTelemetryClient().ifPresent(client -> client.trackEvent("NewEntry", properties, new HashMap<>()));
    }

    private JsonObject getArticleJson(String name) throws SerpApiSearchException {
        Map<String, String> parameters = new HashMap<>();

        parameters.put("engine", "google_scholar");
        parameters.put("q", name);
        parameters.put("api_key", "11dee2df7785c52c4778d3d19d4983f2da681c90beb8fc35ec8dc41e2c422a25");

        JsonObject json = new GoogleSearch(parameters).getJson();

        JsonArray array = json.getAsJsonArray("organic_results");

        for (int i = 0; i < array.size(); i++) {
            if (array.get(i).getAsJsonObject().get("title").getAsString().equals(name)) {
                return array.get(i).getAsJsonObject();
            }
        }

        return null;
    }

    private Map<String, String> parseJson(JsonObject json) {
        if (json == null) {
            return null;
        }

        Map<String, String> info = new HashMap<>();

        info.put("title", json.get("title").getAsString());

        JsonArray authorsArray = json.getAsJsonObject("publication_info").getAsJsonArray("authors");

        String authors = "";

        for (int i = 0; i < authorsArray.size() - 1; i++) {
            authors += authorsArray.get(i).getAsJsonObject().get("name").getAsString();
            authors += " and ";
        }

        authors += authorsArray.get(authorsArray.size() - 1).getAsJsonObject().get("name").getAsString();

        info.put("author", authors);

        String journal = json.getAsJsonObject("publication_info").get("summary").getAsString().split("-")[1];

        info.put("journal", journal);

        return info;
    }
}

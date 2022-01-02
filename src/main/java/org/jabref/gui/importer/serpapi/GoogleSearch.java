package org.jabref.gui.importer.serpapi;

import java.util.Map;

/***
 * Google Search Results using SerpApi
 *
 * Usage
 * ---
 * ```java
 * Map<String, String> parameter = new HashMap<>();
 * parameter.put("q", "Coffee");
 * GoogleSearch google = new GoogleSearch(parameter, "secret api key");
 * JsonObject data = google.getJson();
 * JsonArray organic_results = data.get("organic_results").getAsJsonArray();
 * ```
 */
public class GoogleSearch extends SerpApiSearch {

  public GoogleSearch(Map<String, String> parameter, String apiKey) {
    super(parameter, apiKey, "google");
  }

  public GoogleSearch() {
    super("google");
  }

  public GoogleSearch(Map<String, String> parameter) {
    super(parameter, "google_scholar");
  }

// end
}

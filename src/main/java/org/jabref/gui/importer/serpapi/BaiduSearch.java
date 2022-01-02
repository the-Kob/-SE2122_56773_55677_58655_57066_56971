package org.jabref.gui.importer.serpapi;

import com.google.gson.JsonArray;

import java.util.Map;

/***
 * Baidu Search Results using SerpApi
 *
 * Usage
 * ---
 * ```java
 * Map<String, String> parameter = new HashMap<>();
 * parameter.put("q", "Coffee");
 * BaiduSearch baidu = new BaiduSearch(parameter, "secret api key");
 * JsonObject data = baidu.getJson();
 * JsonArray organic_results = data.get("organic_results").getAsJsonArray();
 * ```
 */
public class BaiduSearch extends SerpApiSearch {

  public BaiduSearch(Map<String, String> parameter, String apiKey) {
    super(parameter, apiKey, "baidu");
  }

  public BaiduSearch() {
    super("baidu");
  }

  public BaiduSearch(Map<String, String> parameter) {
    super(parameter, "baidu");
  }

  @Override
   public JsonArray getLocation(String q, Integer limit) throws SerpApiSearchException {
     throw new SerpApiSearchException("location is not supported for Baidu");
   }
}

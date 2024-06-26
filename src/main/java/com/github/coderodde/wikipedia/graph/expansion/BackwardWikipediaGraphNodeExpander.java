package com.github.coderodde.wikipedia.graph.expansion;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * This class implements a backward node expander in the Wikipedia article 
 * graph. If article <tt>A</tt> has a link to <tt>B</tt>, this expander will generate
 * <tt>A</tt> whenever asked to process <tt>B</tt>. We can say that this 
 * expander traverses each directed arc from head to tail.
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.0.1 (Mar 24, 2024)
 */
public class BackwardWikipediaGraphNodeExpander 
extends AbstractWikipediaGraphNodeExpander {
    
    public BackwardWikipediaGraphNodeExpander(final String languageLocaleName) {
        super(languageLocaleName);
    }
    
    @Override
    public List<String> getNeighbors(final String articleTitle) {
        try {
            final List<String> linkNameList = new ArrayList<>();
            String continueArticle = null;
            boolean exitRequested = false;
            
            while (true) {
                final String jsonText = downloadJson(articleTitle,
                                                     continueArticle,
                                                     false);
                JsonObject root = GSON.fromJson(jsonText, JsonObject.class);
                JsonElement continueJsonElement = root.get("continue");
                
                if (continueJsonElement == null) {
                    exitRequested = true;
                } else {
                    
                    final JsonElement blcontinueJsonElement = 
                            continueJsonElement
                                    .getAsJsonObject()
                                    .get("blcontinue");
                    
                    continueArticle = blcontinueJsonElement.getAsString();
                }
                
                JsonElement queryElement = root.get("query");
                JsonElement backlinksElement = 
                        queryElement.getAsJsonObject().get("backlinks");

                JsonArray pagesArray = backlinksElement.getAsJsonArray();

                for (JsonElement element : pagesArray) {
                    int namespace = element.getAsJsonObject().get("ns").getAsInt();

                    if (namespace == 0) {
                        String title = element.getAsJsonObject().get("title")
                                .getAsString();

                        title = URLEncoder.encode(
                                title,
                                StandardCharsets.UTF_8.toString())
                                .replace("+", "_");

                        linkNameList.add(constructFullWikipediaLink(title));
                    }
                }

                if (exitRequested) {
                    return linkNameList;
                }
            }
        } catch (final Exception ex) {
            throw new RuntimeException(
                    String.format(
                            "Backward article \"%s\" failed to expand.", 
                            articleTitle), 
                    ex);
        }
    }
}

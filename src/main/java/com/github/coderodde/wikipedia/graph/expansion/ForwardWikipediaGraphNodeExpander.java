package com.github.coderodde.wikipedia.graph.expansion;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This class implements a forward node expander in the Wikipedia article graph.
 * If article <tt>A</tt> has a link to <tt>B</tt>, this expander will generate
 * <tt>B</tt> whenever asked to process <tt>A</tt>. We can say that this 
 * expander traverses each directed arc from tail to head.
 * 
 * @version 1.0.1 (Mar 24, 2024)
 */
public class ForwardWikipediaGraphNodeExpander 
extends AbstractWikipediaGraphNodeExpander {
    
    private static final Gson GSON = new Gson();

    public ForwardWikipediaGraphNodeExpander(final String languageLocaleName) {
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
                                                     true);
                
                JsonObject root = GSON.fromJson(jsonText, JsonObject.class);
                JsonObject nextContinueArticleJsonObject = 
                        root.getAsJsonObject("continue");
                                
                if (nextContinueArticleJsonObject == null) {
                    exitRequested = true;
                } else {
                    continueArticle =
                            nextContinueArticleJsonObject
                                    .get("plcontinue")
                                    .getAsString();
                }
                
                JsonElement queryElement = root.get("query");
                JsonObject queryObject = queryElement.getAsJsonObject();
                JsonObject pagesObject = queryObject.getAsJsonObject("pages");

                Set<Map.Entry<String, JsonElement>> set = pagesObject.entrySet();

                JsonObject idObject = 
                        set
                            .iterator()
                            .next()
                            .getValue()
                            .getAsJsonObject();

                JsonArray linkArray = idObject.getAsJsonArray("links");

                for (JsonElement titleElement : linkArray) {
                    int namespace = 
                            titleElement.getAsJsonObject().get("ns").getAsInt();

                    if (namespace != 0) {
                        continue;
                    }

                    String title = 
                            titleElement
                                    .getAsJsonObject()
                                    .get("title")
                                    .getAsString();

                    title = URLEncoder.encode(
                            title,
                            StandardCharsets.UTF_8.toString())
                            .replace("+", "_");

                    linkNameList.add(constructFullWikipediaLink(title));
                }
                
                if (exitRequested) {
                    return linkNameList;
                }
            }
        } catch (final Exception ex) {
            throw new RuntimeException(
                    String.format(
                            "Forward article \"%s\" failed to expand.", 
                            articleTitle), 
                    ex);
        }
    }
}

package com.github.coderodde.wikipedia.graph.expansion;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.net.URLEncoder;
import java.util.ArrayList;
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

    public ForwardWikipediaGraphNodeExpander(final String languageLocaleName) {
        super(languageLocaleName);
    }
    
    @Override
    public List<String> getNeighbors(final String articleTitle) throws Exception {
        String continuationCode = null;
        final Gson gson = new Gson();
        final List<String> linkNameList = new ArrayList<>();
        boolean exitRequested = false;
        
        try {
            while (true) {
                
                final String jsonText = downloadJson(articleTitle, 
                                                     true, 
                                                     continuationCode);

                JsonObject root = gson.fromJson(jsonText, JsonObject.class);
                JsonElement queryElement = root.get("query");
                JsonElement continueElement = root.get("continue");
                
                JsonElement batchCompleteElement = root.get("batchcomplete");
                
                if (batchCompleteElement != null) {
                    exitRequested = true;
                }
                
                if (continueElement != null) {
                    JsonObject continueJsonObject = 
                            continueElement.getAsJsonObject();
                    
                    continuationCode = 
                            continueJsonObject
                                    .get("plcontinue")
                                    .getAsString();
                    
                    System.out.println(">>> Continueing on " + continuationCode);
                }

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
                            "UTF-8")
                            .replace("+", "_");

                    linkNameList.add(constructFullWikipediaLink(title));
                }
                
                if (exitRequested) {
                    return linkNameList;
                }
            }
        } catch (final Exception ex) {
            throw new Exception(
                    String.format(
                            "Forward article \"%s\" failed to expand.", 
                            articleTitle), 
                    ex);
        }
    }
}

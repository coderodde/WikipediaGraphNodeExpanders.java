package net.coderodde.wikipedia.graph.expansion;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * This class implements a backward node expander in the Wikipedia article 
 * graph. If article <tt>A</tt> has a link to <tt>B</tt>, this expander will generate
 * <tt>A</tt> whenever asked to process <tt>B</tt>. We can say that this 
 * expander traverses each directed arc from head to tail.
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.0.0 (Mar 19, 2024)
 */
public class BackwardWikipediaGraphNodeExpander 
extends AbstractWikipediaGraphNodeExpander {
    
    public BackwardWikipediaGraphNodeExpander(final String languageLocaleName) {
        super(languageLocaleName);
    }
    
    @Override
    public List<String> generateSuccessors(final String articleTitle) {
        List<String> linkNameList = new ArrayList<>();
        String jsonText = downloadJson(articleTitle, false);

        try {
            Gson gson = new Gson();
            JsonObject root = gson.fromJson(jsonText, JsonObject.class);
            JsonElement queryElement = root.get("query");
            JsonElement backlinksElement = 
                    queryElement.getAsJsonObject().get("backlinks");
            
            JsonArray pagesArray = backlinksElement.getAsJsonArray();
            
            for (JsonElement element : pagesArray) {
                int namespace = element.getAsJsonObject().get("ns").getAsInt();
                
                if (namespace == 0) {
                    String title = element.getAsJsonObject().get("title")
                            .getAsString();
                    try {
                        title = URLEncoder.encode(
                                title,
                                StandardCharsets.UTF_8.toString())
                                .replace("+", "_");
                        
                    } catch (UnsupportedEncodingException ex) {
                        System.err.printf(
                                "Could not URL encode \"%s\". Omitting.\n", 
                                title);

                        return Collections.<String>emptyList();
                    }
                    
                    linkNameList.add(constructFullWikipediaLink(title));
                }
            }
        } catch (NullPointerException ex) {
            return linkNameList;
        }

        return linkNameList;
    }
}

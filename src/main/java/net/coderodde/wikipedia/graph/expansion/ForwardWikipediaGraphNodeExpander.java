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
import java.util.Map;
import java.util.Set;

/**
 * This class implements a forward node expander in the Wikipedia article graph.
 * If article <tt>A</tt> has a link to <tt>B</tt>, this expander will generate
 * <tt>B</tt> whenever asked to process <tt>A</tt>. We can say that this 
 * expander traverses each directed arc from tail to head.
 * 
 * @version 1.0.0 (Mar 19, 2024)
 */
public class ForwardWikipediaGraphNodeExpander 
extends AbstractWikipediaGraphNodeExpander {

    public ForwardWikipediaGraphNodeExpander(final String languageLocaleName) {
        super(languageLocaleName);
    }
    
    @Override
    public List<String> generateSuccessors(final String articleTitle) {
        List<String> linkNameList = new ArrayList<>();
        String jsonText = downloadJson(articleTitle, true);
        
        try {
            Gson gson = new Gson();
            
            JsonObject root = gson.fromJson(jsonText, JsonObject.class);
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
        } catch (NullPointerException ex) {
            return linkNameList;
        }
        
        return linkNameList;
    }
}

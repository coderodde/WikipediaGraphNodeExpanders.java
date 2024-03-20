package net.coderodde.wikipedia.graph.expansion;

import com.github.coderodde.graph.pathfinding.delayed.AbstractNodeExpander;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Pattern;
import org.apache.commons.io.IOUtils;

/**
 * This abstract class specifies the facilities shared by both forward and 
 * backward node expanders.
 * 
 * @version 1.0.0 (Mar 20, 2024)
 */
public abstract class AbstractWikipediaGraphNodeExpander
extends AbstractNodeExpander<String> {
   
    /**
     * The script URL template for expanding forward.
     */
    private static final String FORWARD_REQUEST_API_URL_SUFFIX = 
            "?action=query" +
            "&titles=%s" + 
            "&prop=links" + 
            "&pllimit=max" + 
            "&format=json"; 
    
    /**
     * The script URL template for expanding backwards.
     */
    private static final String BACKWARD_REQUEST_API_URL_SUFFIX = 
            "?action=query" +
            "&list=backlinks" +
            "&bltitle=%s" + 
            "&bllimit=max" + 
            "&format=json";
    
    private final String languageLocaleName;
    
    private static final String API_URL_FORMAT = 
            "https://%s.wikipedia.org/w/api.php";
    
    /**
     * Caches the textual representation of the URL pointing to the 
     * <a href="https://www.mediawiki.org/wiki/API:Main_page">Wikipedia API</a>.
     */
    private final String apiUrl;

    /**
     * Constructs a graph node expander for the language subgraph specified in
     * the input URL.
     */
    protected AbstractWikipediaGraphNodeExpander(String languageLocaleName) {
        checkLanguageLocaleName(languageLocaleName);
        this.languageLocaleName = languageLocaleName;
        this.apiUrl = constructAPIURL(languageLocaleName);
    }
    
    protected String getLanguageLocaleName() {
        return languageLocaleName;
    }
    
    /**
     * {@inheritDoc }
     */
    @Override
    public boolean isValidNode(final String node) {
        return !generateSuccessors(node).isEmpty();
    }
    
    /**
     * Constructs a Wikipedia API URL from the raw {@code wikipediaUrl}. The
     * input {@code wikipediaUrl} is of the form <tt>en.wikipedia.org</tt>. The
     * idea here is that the search may be applied to article subgraphs with 
     * different languages.
     * 
     * @param wikipediaUrl the Wikipedia URL.
     * @return full URL to Wikipedia API.
     */
    private String constructAPIURL(final String languageLocaleName) {
        return String.format(API_URL_FORMAT, languageLocaleName);
    }
    
    /**
     * The actual implementation of the method producing the neighbors of a 
     * graph node.
     * 
     * @param articleName    the node to expand.
     * @param languageLocaleName the locale name of the language.
     * @param forward specifies the direction of the node expansion operation.
     *                if {@code forward} is {@code true}, generates the child
     *                nodes of {@code node}. Otherwise, generates the parent 
     *                nodes of {@code node}.
     * @return 
     */
    protected List<String> baseGetNeighbors(final String articleName,
                                            final String languageLocaleName,
                                            final boolean forward) {
        String jsonDataUrl;
        
        try {
            jsonDataUrl =
                    apiUrl + String.format(forward ?
                                                FORWARD_REQUEST_API_URL_SUFFIX :
                                                BACKWARD_REQUEST_API_URL_SUFFIX,
                                           URLEncoder.encode(articleName,
                                                             "UTF-8"));
            
        } catch (final UnsupportedEncodingException ex) {
            throw new IllegalStateException(ex.getMessage(), ex);
        }
        
        String jsonText;
        
        try {
            System.out.println("url: " + jsonDataUrl);
            jsonText = IOUtils.toString(new URL(jsonDataUrl),
                                        Charset.forName("UTF-8"));
        } catch (final IOException ex) {
            throw new IllegalStateException(
                    "[I/O ERROR] Failed loading the JSON data from the " +
                    "Wikipedia API: " + ex.getMessage(), ex);
        }
        
        return forward ?
                extractForwardLinkTitles(jsonText) :
                extractBackwardLinkTitles(jsonText);
    }
    
    private void checkLanguageLocaleName(String languageLocaleName) {
        if (!Arrays.asList(Locale.getISOLanguages())
                   .contains(languageLocaleName)) {
            
            throw new IllegalArgumentException(
                    String.format("Unknown language locale name: %s.", 
                                  languageLocaleName));
        }
    }
    
    /**
     * Returns all the Wikipedia article titles that the current article links 
     * to.
     * 
     * @param jsonText the data in JSON format.
     * @return a list of Wikipedia article titles parsed from {@code jsonText}.
     */
    private static List<String> extractForwardLinkTitles(String jsonText) {
        List<String> linkNameList = new ArrayList<>();
        JsonArray linkNameArray;

        try {
            Gson gson = new Gson();
            JsonObject root = gson.fromJson(jsonText, JsonObject.class);
            JsonElement queryElement = root.get("query");
            JsonObject queryObject = queryElement.getAsJsonObject();
            JsonObject pagesObject = queryObject.getAsJsonObject("pages");
            JsonArray linksArray = pagesObject.getAsJsonArray("links");
            
            Set<Entry<String, JsonElement>> set = pagesObject.entrySet();
            
            JsonObject idObject = 
                    set
                        .iterator()
                        .next()
                        .getValue()
                        .getAsJsonObject();
            
            JsonArray linkArray = idObject.getAsJsonArray("links");
            
            Iterator<JsonElement> iterator = linkArray.iterator();
            
            while (iterator.hasNext()) {
                JsonElement titleElement = iterator.next();
                
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
                
                linkNameList.add(title);
            }
        } catch (NullPointerException ex) {
            return linkNameList;
        }

//        linkNameArray.forEach((element) -> {
//            int namespace = element.getAsJsonObject().get("ns").getAsInt();
//
//            if (namespace == 0) {
//                String title = element.getAsJsonObject()
//                                      .get("title")
//                                      .getAsString();
//                
//                try {
//                    title = URLEncoder.encode(
//                                title,
//                                StandardCharsets.UTF_8.toString())
//                            .replace("+", "%20");
//                    
//                    linkNameList.add(title);
//                    
//                } catch (UnsupportedEncodingException ex) {
//                    System.err.printf("Could not URL encode \"%s\". Omitting.\n", title);
//                }
//            }
//        });

        return linkNameList;
    }
    
    /**
     * Returns all the Wikipedia article titles that link to the current
     * article.
     * 
     * @param jsonText the data in JSON format.
     * @return a list of Wikipedia article titles parsed from {@code jsonText}.
     */
    private List<String> extractBackwardLinkTitles(String jsonText) {
        List<String> linkNameList = new ArrayList<>();

        try {
            Gson gson = new Gson();
            JsonObject root = gson.fromJson(jsonText, JsonObject.class);
            JsonElement queryElement = root.get("query");
            JsonElement backlinksElement = 
                    queryElement.getAsJsonObject().get("backlinks");
            
            JsonArray pagesArray = backlinksElement.getAsJsonArray();
            Iterator<JsonElement> iterator = pagesArray.iterator();
            
            while (iterator.hasNext()) {
                JsonElement element = iterator.next();
                int namespace = element.getAsJsonObject().get("ns").getAsInt();
                
                if (namespace == 0) {
                    String title = element.getAsJsonObject().get("title")
                                                            .getAsString();
                    
                    title = constructFullWikipediaLink(title);
                    
                    linkNameList.add(title);
                }
            }
        } catch (NullPointerException ex) {
            return linkNameList;
        }
        
        System.out.println(linkNameList.toString());

        return linkNameList;
    }
    
    private String constructFullWikipediaLink(String title) {
        return "https://" + languageLocaleName + ".wikipedia.org/wiki/" + title;
    }
}

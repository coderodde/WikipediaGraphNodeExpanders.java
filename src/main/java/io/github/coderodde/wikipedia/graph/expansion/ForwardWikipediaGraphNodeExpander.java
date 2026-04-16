package io.github.coderodde.wikipedia.graph.expansion;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.github.coderodde.wikipedia.json.downloader.WikipediaArticleJsonDownloader;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
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
    
    public ForwardWikipediaGraphNodeExpander(
            final String languageCodeName,
            final WikipediaArticleJsonDownloader downloader) throws Exception {
        
        super(languageCodeName, downloader);
    }
    
    @Override
    public List<String> getNeighbors(final String articleTitle) {
        try {
            final List<String> linkNameList = new ArrayList<>();
            String continueArticle = null;
            boolean exitRequested = false;
            
            while (true) {
                final String jsonText = 
                        downloader.downloadJson(articleTitle, 
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
                
                if (linkArray == null) {
                    return List.of();
                }

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
                    
//                    title = URLEncoder.encode(
//                            title,
//                            StandardCharsets.UTF_8.toString())
//                            .replace("+", "_");
                    
                    linkNameList.add(
                            downloader.constructFullWikipediaLink(title, 
                                    languageISOCode));
                }
                
                if (exitRequested) {
                    return linkNameList;
                }
            }
        } catch (final Exception ex) {
            return null;
//            throw new RuntimeException(
//                    String.format(
//                            "Forward article \"%s\" failed to expand.", 
//                            articleTitle), 
//                    ex);
        }
    }
}

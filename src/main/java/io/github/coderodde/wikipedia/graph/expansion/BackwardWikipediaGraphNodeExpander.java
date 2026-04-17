package io.github.coderodde.wikipedia.graph.expansion;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.github.coderodde.wikipedia.json.downloader.WikipediaArticleJsonDownloader;
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
    
    public BackwardWikipediaGraphNodeExpander(
            final String languageCodeName,
            final WikipediaArticleJsonDownloader downloader) 
            throws Exception {
        
        super(languageCodeName, downloader);
    }
    
    @Override
    public List<String> getNeighbors(String articleTitle) {
        try {
            final List<String> linkNameList = new ArrayList<>();
            String continueArticle = null;
            boolean exitRequested = false;
            articleTitle = articleTitle.replaceAll("[\\+ ]", "_");
            
            while (true) {
//                final String normalizedTitle = articleTitle
////                    URLEncoder.encode(
////                        articleTitle, 
////                        StandardCharsets.UTF_8)
//                    .replaceAll("\\+ ", "_");
                
                final String jsonText = downloader.downloadJson(
                    articleTitle, 
                    continueArticle, 
                    false);
                
//                final String jsonText = 
//                        downloader.downloadJson(
//                                URLEncoder.encode(
//                                        articleTitle, 
//                                        StandardCharsets.UTF_8)
//                                        .replace("+", "_"),
//                                continueArticle,
//                                false);
//                
//                        downloader.downloadJson(articleTitle,
//                                                continueArticle,
//                                                false);
                
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

//                        title = URLEncoder.encode(
//                                title,
//                                StandardCharsets.UTF_8.toString())
//                                .replace("+", "_");

                        linkNameList.add(
                                downloader.constructFullWikipediaLink(title,
                                        languageISOCode));
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

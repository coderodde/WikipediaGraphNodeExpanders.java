package io.github.coderodde.wikipedia.graph.expansion;

import com.google.gson.Gson;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This abstract class specifies the facilities shared by both forward and 
 * backward node expanders.
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.0.1 (Oct 24, 2025)
 * @since 1.0.0 (Mar 20, 2024)
 */
public abstract class AbstractWikipediaGraphNodeExpander {
    
    protected static final Gson GSON = new Gson();
    private static final Set<String> languages = ConcurrentHashMap.newKeySet();
    
    static {
        languages.addAll(Arrays.asList(Locale.getISOLanguages()));
    }
    
    protected final String languageISOCode;
    
    protected AbstractWikipediaGraphNodeExpander(String languageCode) throws Exception {
        
        Objects.requireNonNull(languageCode);
        
        if (!languages.contains(languageCode)) {
            throw new Exception(
                    String.format("Language ISO code \"%s\" is unknown",
                                  languageCode));
        }
        
        this.languageISOCode = languageCode.toLowerCase();
        
    }
    
    /**
     * Returns {@code true} if the article node is valid. {@code false} 
     * otherwise.
     * 
     * @param node the name of the article
     * 
     * @return {@code true} only if the input node is valid.
     */
    public boolean isValidNode(final String node) {
        return !getNeighbors(node).isEmpty();
    }
    
    /**
     * Retrieves the neighbouring links targets.
     * 
     * @param node the starting node.
     * 
     * @return the neighbouring links.
     */
    public abstract List<String> getNeighbors(final String node);
}

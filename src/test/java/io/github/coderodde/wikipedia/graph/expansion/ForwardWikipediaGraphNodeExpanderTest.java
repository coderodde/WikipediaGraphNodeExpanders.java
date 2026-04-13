package io.github.coderodde.wikipedia.graph.expansion;

import io.github.coderodde.wikipedia.json.downloader.WikipediaArticleJsonDownloader;
import org.junit.Test;
import static org.junit.Assert.*;

// Tests in this class require that there is connection to the Wikipedia API!
public class ForwardWikipediaGraphNodeExpanderTest {
    
    private final WikipediaArticleJsonDownloader downloader = 
              new WikipediaArticleJsonDownloader("en");
    
    private final ForwardWikipediaGraphNodeExpander nodeExpander;
    
    public ForwardWikipediaGraphNodeExpanderTest() throws Exception {
        this.nodeExpander = 
                new ForwardWikipediaGraphNodeExpander("en", downloader);
    }
    
    //@Test
    public void getNeighbors() throws Exception {
        assertTrue(nodeExpander.isValidNode("Life"));
        assertTrue(!nodeExpander.getNeighbors("Life").isEmpty());
        assertFalse(nodeExpander.isValidNode("Disc_jfdsfsockey"));
        assertTrue(nodeExpander.getNeighbors("Disc_jfdsfsockey").isEmpty());
    }
    
    //@Test
    public void debug1() throws Exception {
        assertTrue(nodeExpander.isValidNode("Bringin'_On_the_Heartbreak"));
        assertTrue(
                !nodeExpander.getNeighbors("Bringin'_On_the_Heartbreak")
                             .isEmpty());
    }
    
    //@Test
    public void debug2() throws Exception {
        assertTrue(nodeExpander.isValidNode("Flossie_Wong-Staal"));
        assertTrue(
                !nodeExpander.getNeighbors("Flossie_Wong-Staal")
                             .isEmpty());
    }
    
    //@Test
    public void debug3() throws Exception {
        assertTrue(nodeExpander.isValidNode("L4_(spacecraft)"));
        assertTrue(
                !nodeExpander.getNeighbors("L4_(spacecraft)")
                             .isEmpty());
    }
    
    //@Test
    public void debug4() throws Exception {
        assertTrue(nodeExpander.isValidNode("L4_%28spacecraft%29"));
        assertTrue(
                !nodeExpander.getNeighbors("L4_%28spacecraft%29")
                             .isEmpty());
    }
    
    
    //@Test
    public void testOnBadWikipediaCountryCode() {
        try {
            new ForwardWikipediaGraphNodeExpander(
                    "bad-country-code",
                    downloader);
            
            fail("Should have thrown Exception on bad-country-code");
        } catch (final Exception ex) {
            
        }
    }
}

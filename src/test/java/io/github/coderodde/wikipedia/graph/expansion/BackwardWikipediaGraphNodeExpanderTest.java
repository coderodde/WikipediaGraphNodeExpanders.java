package io.github.coderodde.wikipedia.graph.expansion;

import io.github.coderodde.wikipedia.json.downloader.WikipediaArticleJsonDownloader;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

// Tests in this class require that there is connection to the Wikipedia API!
public class BackwardWikipediaGraphNodeExpanderTest {

    private final WikipediaArticleJsonDownloader downloader = 
              new WikipediaArticleJsonDownloader("en");
    
    private final BackwardWikipediaGraphNodeExpander nodeExpander;
    
    public BackwardWikipediaGraphNodeExpanderTest() throws Exception {
        this.nodeExpander = 
                new BackwardWikipediaGraphNodeExpander("en", downloader);
    }
    
    @Test
    public void getNeighbors() throws Exception {
        assertTrue(nodeExpander.isValidNode("Life"));
        assertTrue(!nodeExpander.getNeighbors("Life").isEmpty());
        assertFalse(nodeExpander.isValidNode("Disc_jfdsfsockey"));
        assertTrue(nodeExpander.getNeighbors("Disc_jfdsfsockey").isEmpty());
    }
    
    @Test
    public void debug1() throws Exception {
        assertTrue(nodeExpander.isValidNode("Bringin'_On_the_Heartbreak"));
        List<String> lst =
                nodeExpander.getNeighbors("Bringin'_On_the_Heartbreak");
        
        assertTrue(lst.size() > 1);
//        assertTrue(
//                !nodeExpander.getNeighbors("Bringin'_On_the_Heartbreak")
//                             .isEmpty());
    }
    
    //@Test
    public void debug2() throws Exception {
        assertTrue(nodeExpander.isValidNode("Flossie_Wong-Staal"));
        assertTrue(
                !nodeExpander.getNeighbors("Flossie_Wong-Staal")
                             .isEmpty());
    }
    
    @Test
    public void debug3() throws Exception {
//        List<String> links = nodeExpander.getNeighbors("L4_(spacecraft)");
//        
//        System.out.println(links);
//        
//        assertTrue(nodeExpander.isValidNode("1967–68_Luxembourg_National_Division"));
        assertTrue(nodeExpander.isValidNode("L4_(spacecraft)"));
        assertTrue(
                !nodeExpander.getNeighbors("L4_(spacecraft)")
                             .isEmpty());
    }
    
    @Test
    public void debug4() throws Exception {
        assertTrue(nodeExpander.isValidNode("L4_%28spacecraft%29"));
//        assertTrue(
//                !nodeExpander.getNeighbors("L4_%28spacecraft%29")
//                             .isEmpty());
    }
    
    @Test
    public void testOnBadWikipediaCountryCode() {
        try {
            new BackwardWikipediaGraphNodeExpander(
                    "bad-country-code",
                    downloader);
            
            fail("Should have thrown Exception on bad-country-code");
        } catch (final Exception ex) {
            
        }
    }
}

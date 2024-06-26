package com.github.coderodde.wikipedia.graph.expansion;

import org.junit.Test;
import static org.junit.Assert.*;

// Tests in this class require that there is connection to the Wikipedia API!
public class BackwardWikipediaGraphNodeExpanderTest {
    
    private BackwardWikipediaGraphNodeExpander nodeExpander;
    
    @Test
    public void getNeighbors() throws Exception {
        nodeExpander = 
                new BackwardWikipediaGraphNodeExpander("en");
        
        assertTrue(nodeExpander.isValidNode("Disc_jockey"));
        assertTrue(!nodeExpander.getNeighbors("Disc_jockey").isEmpty());
        
        assertFalse(nodeExpander.isValidNode("Disc_jfdsfsockey"));
        assertTrue(nodeExpander.getNeighbors("Disc_jockeyfdsfsd").isEmpty());
    }
    
    @Test(expected = Exception.class)
    public void testOnBadWikipediaCountryCode() {
        new BackwardWikipediaGraphNodeExpander("shit");
    }
}

package com.github.coderodde.wikipedia.graph.expansion;

import java.io.IOException;
import org.junit.Test;
import static org.junit.Assert.*;

// Tests in this class require that there is connection to the Wikipedia API!
public class ForwardWikipediaGraphNodeExpanderTest {
    
    private ForwardWikipediaGraphNodeExpander nodeExpander;
    
    @Test
    public void getNeighbors() throws IOException {
        nodeExpander = 
                new ForwardWikipediaGraphNodeExpander("en");
        
        assertTrue(nodeExpander.isValidNode("Disc_jockey"));
        assertTrue(!nodeExpander.getNeighbors("Disc_jockey").isEmpty());
        
        try {
            assertFalse(nodeExpander.isValidNode("Disc_jfdsfsockey"));
            fail();
        } catch (final Exception ex) {
            System.out.println("ex = " + ex);
        }
        
        try {
            assertTrue(nodeExpander.getNeighbors("Disc_jockeyfdsfsd")
                                   .isEmpty());
            
            fail();
        } catch (final Exception ex) {
            
        }
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testOnBadWikipediaCountryCode() {
        new ForwardWikipediaGraphNodeExpander("shit");
    }
}

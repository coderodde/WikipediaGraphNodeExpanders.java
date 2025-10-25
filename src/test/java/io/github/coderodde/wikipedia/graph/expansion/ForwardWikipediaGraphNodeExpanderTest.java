package io.github.coderodde.wikipedia.graph.expansion;

import io.github.coderodde.wikipedia.graph.expansion.ForwardWikipediaGraphNodeExpander;
import org.junit.Test;
import static org.junit.Assert.*;

// Tests in this class require that there is connection to the Wikipedia API!
public class ForwardWikipediaGraphNodeExpanderTest {
    
    private ForwardWikipediaGraphNodeExpander nodeExpander;
    
    @Test
    public void getNeighbors() throws Exception {
        nodeExpander = 
                new ForwardWikipediaGraphNodeExpander("en");
        
        assertTrue(nodeExpander.isValidNode("Life"));
        assertTrue(!nodeExpander.getNeighbors("Life").isEmpty());
        
        try {
            assertFalse(nodeExpander.isValidNode("Disc_jfdsfsockey"));
            fail();
        } catch (final Exception ex) {
            
        }
        
        try {
            assertTrue(nodeExpander.getNeighbors("Disc_jockeyfdsfsd")
                                   .isEmpty());
            fail();
        } catch (final Exception ex) {
            
        }
    }
    
    @Test
    public void testOnBadWikipediaCountryCode() {
        try {
            new ForwardWikipediaGraphNodeExpander("bad-country-code");
            fail("Should have thrown Exception on bad-country-code");
        } catch (final Exception ex) {
            
        }
    }
}

package net.coderodde.wikipedia.graph.expansion;

import org.junit.Test;
import static org.junit.Assert.*;

// Tests in this class require that there is connection to the Wikipedia API!
public class BackwardWikipediaGraphNodeExpanderTest {
    
    private BackwardWikipediaGraphNodeExpander expander;
    
    @Test
    public void testExpand1() {
        expander = 
                new BackwardWikipediaGraphNodeExpander(
                        "https://en.wikipedia.org/wiki/Disc_jockey");
        
        assertEquals("en.wikipedia.org", expander.getBasicUrl());
        assertTrue(expander.isValidNode("Disc_jockey"));
        assertTrue(!expander.expand("Disc_jockey").isEmpty());
        
        assertFalse(expander.isValidNode("Disc_jfdsfsockey"));
        assertTrue(expander.expand("Disc_jockeyfdsfsd").isEmpty());
    }
    
    @Test
    public void testExpand2() {    
        expander = 
                new BackwardWikipediaGraphNodeExpander(
                        "https://fi.wikipedia.org/wiki/DJ");
        
        assertEquals("fi.wikipedia.org", expander.getBasicUrl());
        assertTrue(expander.isValidNode("DJ"));
        assertTrue(!expander.expand("DJ").isEmpty());
        
        assertFalse(expander.isValidNode("DJJJfdsfJJ"));
        assertTrue(expander.expand("DJJJfdsafd").isEmpty());
    }
    
    @Test
    public void testExpandWithNonSecureHTTPPrefix() {
        expander = 
                new BackwardWikipediaGraphNodeExpander(
                        "http://en.wikipedia.org/wiki/Disc_jockey");
        
        assertEquals("en.wikipedia.org", expander.getBasicUrl());
        assertTrue(expander.isValidNode("Disc_jockey"));
        assertTrue(!expander.expand("Disc_jockey").isEmpty());
        
        assertFalse(expander.isValidNode("Disc_jfdsfsockey"));
        assertTrue(expander.expand("Disc_jockeyfdsfsd").isEmpty());
    }
    
    @Test
    public void testExpandWithoutProtocolPrefix() {
        expander = 
                new BackwardWikipediaGraphNodeExpander(
                        "en.wikipedia.org/wiki/Disc_jockey");
        
        assertEquals("en.wikipedia.org", expander.getBasicUrl());
        assertTrue(expander.isValidNode("Disc_jockey"));
        assertTrue(!expander.expand("Disc_jockey").isEmpty());
        
        assertFalse(expander.isValidNode("Disc_jfdsfsockey"));
        assertTrue(expander.expand("Disc_jockeyfdsfsd").isEmpty());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testOnBadWikipediaURL1() {
        new BackwardWikipediaGraphNodeExpander("en.wikipedia.org");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testOnBadWikipediaURL2() {
        new BackwardWikipediaGraphNodeExpander("en.wikipedia.org/");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testOnBadWikipediaURL3() {
        new BackwardWikipediaGraphNodeExpander("en.wikipedia.org/wki");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testOnBadWikipediaURL4() {
        new BackwardWikipediaGraphNodeExpander(
                "en.wikipedia.org/wki/Disc_jockey");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testOnBadWikipediaURL5() {
        new BackwardWikipediaGraphNodeExpander(
                "htp://en.wikipedia.org/wiki/Disc_jockey");
    }
}

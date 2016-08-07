package net.coderodde.wikipedia.graph.expansion;

import org.junit.Test;
import static org.junit.Assert.*;

// Tests in this class require that there is connection to the Wikipedia API!
public class ForwardWikipediaGraphNodeExpanderTest {
    
    private ForwardWikipediaGraphNodeExpander expander;
    
    @Test
    public void testExpand1() {
        expander = 
                new ForwardWikipediaGraphNodeExpander(
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
                new ForwardWikipediaGraphNodeExpander(
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
                new ForwardWikipediaGraphNodeExpander(
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
                new ForwardWikipediaGraphNodeExpander(
                        "en.wikipedia.org/wiki/Disc_jockey");
        
        assertEquals("en.wikipedia.org", expander.getBasicUrl());
        assertTrue(expander.isValidNode("Disc_jockey"));
        assertTrue(!expander.expand("Disc_jockey").isEmpty());
        
        assertFalse(expander.isValidNode("Disc_jfdsfsockey"));
        assertTrue(expander.expand("Disc_jockeyfdsfsd").isEmpty());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testOnBadWikipediaURL1() {
        new ForwardWikipediaGraphNodeExpander("en.wikipedia.org");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testOnBadWikipediaURL2() {
        new ForwardWikipediaGraphNodeExpander("en.wikipedia.org/");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testOnBadWikipediaURL3() {
        new ForwardWikipediaGraphNodeExpander("en.wikipedia.org/wki");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testOnBadWikipediaURL4() {
        new ForwardWikipediaGraphNodeExpander(
                "en.wikipedia.org/wki/Disc_jockey");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testOnBadWikipediaURL5() {
        new ForwardWikipediaGraphNodeExpander(
                "htp://en.wikipedia.org/wiki/Disc_jockey");
    }
}
